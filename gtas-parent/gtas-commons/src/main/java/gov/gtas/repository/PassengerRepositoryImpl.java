package gov.gtas.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.gtas.model.Flight;
import gov.gtas.model.Passenger;
import gov.gtas.services.dto.PassengersRequestDto;
import gov.gtas.services.dto.SortOptionsDto;

public class PassengerRepositoryImpl implements PassengerRepositoryCustom {
    private static final Logger logger = LoggerFactory.getLogger(PassengerRepositoryImpl.class);

    @PersistenceContext
    private EntityManager em;
    
    public List<Object[]> getAllPassengersAndFlights(PassengersRequestDto dto) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = cb.createQuery(Object[].class);
        Root<Passenger> pax = q.from(Passenger.class);
        Join<Passenger, Flight> flights = pax.join("flights"); 
        List<Predicate> predicates = new ArrayList<Predicate>();

        // dates
        Predicate etaCondition = null;
        if (dto.getEtaStart() != null && dto.getEtaEnd() != null) {
            Path<Date> eta = flights.<Date>get("eta");
            Predicate startPredicate = cb.or(cb.isNull(eta), cb.greaterThanOrEqualTo(flights.<Date>get("eta"), dto.getEtaStart()));
            Predicate endPredicate = cb.or(cb.isNull(eta), cb.lessThanOrEqualTo(eta, dto.getEtaEnd())); 
            etaCondition = cb.and(startPredicate, endPredicate);
            predicates.add(etaCondition);
        }

        // sorting
        if (dto.getSort() != null) {
            List<Order> orders = new ArrayList<>();
            for (SortOptionsDto sort : dto.getSort()) {
                Expression<?> e = flights.get(sort.getColumn());
                Order order = null;
                if (sort.getDir().equals("desc")) {
                    order = cb.desc(e);
                } else {
                    order = cb.asc(e);
                }
                orders.add(order);
            }
            q.orderBy(orders);
        }
        
        // filters
        if (StringUtils.isNotBlank(dto.getOrigin())) {
            predicates.add(cb.equal(flights.<String>get("origin"), dto.getOrigin()));
        }
        if (StringUtils.isNotBlank(dto.getDest())) {
            predicates.add(cb.equal(flights.<String>get("destination"), dto.getDest()));
        }
        if (StringUtils.isNotBlank(dto.getFlightNumber())) {
            String likeString = String.format("%%%s%%", dto.getFlightNumber());
            predicates.add(cb.like(flights.<String>get("fullFlightNumber"), likeString));
        }
        /*
         * hack: javascript sends the empty string represented by the 'all' dropdown
         * value as '0', so we check for that here to mean 'any direction' 
         */
        if (StringUtils.isNotBlank(dto.getDirection()) && !"0".equals(dto.getDirection())) {
            predicates.add(cb.equal(flights.<String>get("direction"), dto.getDirection()));
        }
        
        q.multiselect(pax, flights).where(predicates.toArray(new Predicate[]{}));
        TypedQuery<Object[]> typedQuery = addPagination(q, dto.getPageNumber(), dto.getPageSize());
        logger.debug(typedQuery.unwrap(org.hibernate.Query.class).getQueryString());
        //System.out.println(typedQuery.unwrap(org.hibernate.Query.class).getQueryString());
        List<Object[]> results = typedQuery.getResultList();
        
        return results;
    }
    
    public List<Passenger> getPassengersByFlightId(Long flightId, PassengersRequestDto dto) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Passenger> q = cb.createQuery(Passenger.class);
        Root<Passenger> pax = q.from(Passenger.class);
        Join<Passenger, Flight> flights = pax.join("flights"); 
        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(cb.equal(flights.<Long>get("id"), flightId));

        // filters
        
        q.select(pax).where(predicates.toArray(new Predicate[]{}));
        TypedQuery<Passenger> typedQuery = addPagination(q, dto.getPageNumber(), dto.getPageSize());
        logger.debug(typedQuery.unwrap(org.hibernate.Query.class).getQueryString());
        //System.out.println(typedQuery.unwrap(org.hibernate.Query.class).getQueryString());
        List<Passenger> results = typedQuery.getResultList();
        
        return results;
    }
    
    public <T> TypedQuery<T> addPagination(CriteriaQuery<T> q, int pageNumber, int pageSize) {
        int offset = (pageNumber - 1) * pageSize;
        TypedQuery<T> typedQuery = em.createQuery(q);
        typedQuery.setFirstResult(offset);
        typedQuery.setMaxResults(pageSize);
        return typedQuery;
    }
}
