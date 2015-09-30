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
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import gov.gtas.model.Flight;
import gov.gtas.services.dto.FlightsRequestDto;
import gov.gtas.services.dto.SortOptionsDto;

@Repository
public class FlightRepositoryImpl implements FlightRepositoryCustom {
    @PersistenceContext
    private EntityManager em;
    
    public FlightRepositoryImpl() { }
    
    public List<Flight> getAllSorted(FlightsRequestDto dto) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Flight> q = cb.createQuery(Flight.class);
        Root<Flight> root = q.from(Flight.class);

        // dates
        Predicate etaCondition = null;
        if (dto.getEtaStart() != null && dto.getEtaEnd() != null) {
            Path<Date> eta = root.<Date>get("eta");
            Predicate startPredicate = cb.or(cb.isNull(eta), cb.greaterThanOrEqualTo(root.<Date>get("eta"), dto.getEtaStart()));
            Predicate endPredicate = cb.or(cb.isNull(eta), cb.lessThanOrEqualTo(eta, dto.getEtaEnd())); 
            etaCondition = cb.and(startPredicate, endPredicate);
        }

        // sorting
        if (dto.getSort() != null) {
            List<Order> orders = new ArrayList<>();
            for (SortOptionsDto sort : dto.getSort()) {
                Expression<?> e = root.get(sort.getColumn());
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
        
        CriteriaQuery<Flight> select = (etaCondition == null) ? q.select(root) : q.select(root).where(etaCondition);

        // pagination
        int pageNumber = dto.getPageNumber();
        int pageSize = dto.getPageSize();
        int firstResultIndex = (pageNumber - 1) * pageSize;
        TypedQuery<Flight> typedQuery = em.createQuery(select);
        typedQuery.setFirstResult(firstResultIndex);
        typedQuery.setMaxResults(dto.getPageSize());
//        System.out.println(typedQuery.unwrap(org.hibernate.Query.class).getQueryString());
        List<Flight> results = typedQuery.getResultList();
        
        return results;
    }
}