package gov.gtas.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.gtas.model.Flight;
import gov.gtas.model.HitsSummary;
import gov.gtas.model.Passenger;
import gov.gtas.services.dto.PassengersRequestDto;
import gov.gtas.services.dto.SortOptionsDto;

public class PassengerRepositoryImpl implements PassengerRepositoryCustom {
    private static final Logger logger = LoggerFactory.getLogger(PassengerRepositoryImpl.class);

    @PersistenceContext
    private EntityManager em;
    
    public List<Object[]> getPassengersByCriteria_(Long flightId, PassengersRequestDto dto) {
        String q = "select p, f, h"
                + " from Passenger p"
                + " join p.flights f"
                + " left join p.hits h"
                + " with h.flight.id = :flightId"
                + " where 1=1";
        
        if (flightId != null) {
            q += " and f.id = :flightId";            
        }
        
        TypedQuery<Object[]> typedQuery = em.createQuery(q, Object[].class);
        typedQuery.setParameter("flightId", flightId);
        int offset = (dto.getPageNumber() - 1) * dto.getPageSize();
        typedQuery.setFirstResult(offset);
        typedQuery.setMaxResults(dto.getPageSize());
        System.out.println(typedQuery.unwrap(org.hibernate.Query.class).getQueryString());
        List<Object[]> results = typedQuery.getResultList();
        System.out.println(results.size());
        return results;
    }
    
    /**
     * This was an especially difficult query to construct mainly because of a
     * bug in hibernate. See https://hibernate.atlassian.net/browse/HHH-7321.
     * The problem is that the left join on hits requires a dual 'on' condition
     * (hits.pax_id = pax.id and hits.flight_id = flight.id). Constructing this
     * in JPA seems perfectly valid, and Hibernate converts this into a 'with'
     * statement with multiple conditions. I get the exception
     * "org.hibernate.hql.internal.ast.QuerySyntaxException: with-clause
     * referenced two different from-clause elements."
     */
    public Pair<Long, List<Object[]>> findByCriteria(Long flightId, PassengersRequestDto dto) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> q = cb.createQuery(Object[].class);
        Root<Passenger> pax = q.from(Passenger.class);
        Join<Passenger, Flight> flight = pax.join("flights"); 
        Join<Passenger, HitsSummary> hits = pax.join("hits", JoinType.LEFT);
        List<Predicate> predicates = new ArrayList<Predicate>();

        if (flightId != null) {
            hits.on(cb.equal(hits.get("flight").get("id"), cb.parameter(Long.class, "flightId")));
            predicates.add(cb.equal(flight.<Long>get("id"), flightId));            
        }

        // sorting
        if (dto.getSort() != null) {
            List<Order> orders = new ArrayList<>();
            for (SortOptionsDto sort : dto.getSort()) {
                String column = sort.getColumn();
                Expression<?> e = null;
                if (isFlightColumn(column)) {
                    e = flight.get(column); 
                } else if (column.equals("onRuleHitList")) { 
                    e = hits.get("ruleHitCount");
                } else if (column.equals("onWatchList")) {
                    e = hits.get("watchListHitCount");
                } else {
                    e = pax.get(column);
                }
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

        if (flightId == null) {
            // dates
            Predicate etaCondition = null;
            if (dto.getEtaStart() != null && dto.getEtaEnd() != null) {
                Path<Date> eta = flight.<Date>get("eta");
                Predicate startPredicate = cb.or(cb.isNull(eta), cb.greaterThanOrEqualTo(flight.<Date>get("eta"), dto.getEtaStart()));
                Predicate endPredicate = cb.or(cb.isNull(eta), cb.lessThanOrEqualTo(eta, dto.getEtaEnd())); 
                etaCondition = cb.and(startPredicate, endPredicate);
                predicates.add(etaCondition);
            }
    
            // filters
            if (StringUtils.isNotBlank(dto.getLastName())) {
                String likeString = String.format("%%%s%%", dto.getLastName().toUpperCase());
                predicates.add(cb.like(pax.<String>get("lastName"), likeString));
            }
            if (StringUtils.isNotBlank(dto.getOrigin())) {
                predicates.add(cb.equal(flight.<String>get("origin"), dto.getOrigin()));
            }
            if (StringUtils.isNotBlank(dto.getDest())) {
                predicates.add(cb.equal(flight.<String>get("destination"), dto.getDest()));
            }
            if (StringUtils.isNotBlank(dto.getFlightNumber())) {
                String likeString = String.format("%%%s%%", dto.getFlightNumber().toUpperCase());
                predicates.add(cb.like(flight.<String>get("fullFlightNumber"), likeString));
            }
            /*
             * hack: javascript sends the empty string represented by the 'all' dropdown
             * value as '0', so we check for that here to mean 'any direction' 
             */
            if (StringUtils.isNotBlank(dto.getDirection()) && !"0".equals(dto.getDirection())) {
                predicates.add(cb.equal(flight.<String>get("direction"), dto.getDirection()));
            }
        }
        
        q.multiselect(pax, flight, hits).where(predicates.toArray(new Predicate[]{}));
        TypedQuery<Object[]> typedQuery = addPagination(q, dto.getPageNumber(), dto.getPageSize());
        
//        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
//        Join<Passenger, Flight> flight = pax.join("flights"); 
//        Join<Passenger, HitsSummary> hits = pax.join("hits", JoinType.LEFT);
//
//        countQuery.select(cb.count(countQuery.from(Passenger.class))).where(predicates.toArray(new Predicate[]{}));
//        Long count = em.createQuery(countQuery).getSingleResult();
        
        if (flightId != null) {
            typedQuery.setParameter("flightId", flightId);
        }
        
        logger.debug(typedQuery.unwrap(org.hibernate.Query.class).getQueryString());
//        System.out.println(typedQuery.unwrap(org.hibernate.Query.class).getQueryString());
        List<Object[]> results = typedQuery.getResultList();
        
        return new ImmutablePair<Long, List<Object[]>>(-1L, results);
    }
    
    public <T> TypedQuery<T> addPagination(CriteriaQuery<T> q, int pageNumber, int pageSize) {
        int offset = (pageNumber - 1) * pageSize;
        TypedQuery<T> typedQuery = em.createQuery(q);
        typedQuery.setFirstResult(offset);
        
        /*
         * complete hack: we're returning more results than the 
         * pagesize b/c the service will potentially throw some of
         * them away.  This is all b/c the left join on hitssummary
         * will not work correctly if we have to check both flight id
         * and passenger id.
         */
        typedQuery.setMaxResults(pageSize * 3);
        return typedQuery;
    }
    
    private Set<String> flightColumns = new HashSet<String>(Arrays.asList("fullFlightNumber", "eta", "etd"));
    private boolean isFlightColumn(String c) {
        return flightColumns.contains(c);
    }
}
