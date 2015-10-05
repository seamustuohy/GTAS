package gov.gtas.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.gtas.model.Flight;
import gov.gtas.model.Passenger;
import gov.gtas.services.dto.PassengersRequestDto;

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

        // pagination
        int pageNumber = dto.getPageNumber();
        int pageSize = dto.getPageSize();
        int firstResultIndex = (pageNumber - 1) * pageSize;
        
        q.multiselect(pax, flights).where(predicates.toArray(new Predicate[]{}));
        TypedQuery<Object[]> typedQuery = em.createQuery(q);
        typedQuery.setFirstResult(firstResultIndex);
        typedQuery.setMaxResults(dto.getPageSize());
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
        
        // pagination
        int pageNumber = dto.getPageNumber();
        int pageSize = dto.getPageSize();
        int firstResultIndex = (pageNumber - 1) * pageSize;
        
        q.select(pax).where(predicates.toArray(new Predicate[]{}));
        TypedQuery<Passenger> typedQuery = em.createQuery(q);
        typedQuery.setFirstResult(firstResultIndex);
        typedQuery.setMaxResults(dto.getPageSize());
        logger.debug(typedQuery.unwrap(org.hibernate.Query.class).getQueryString());
        //System.out.println(typedQuery.unwrap(org.hibernate.Query.class).getQueryString());
        List<Passenger> results = typedQuery.getResultList();
        
        return results;
    }
    
}
