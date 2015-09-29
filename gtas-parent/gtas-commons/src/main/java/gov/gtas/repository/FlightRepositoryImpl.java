package gov.gtas.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import gov.gtas.model.Flight;

@Repository
public class FlightRepositoryImpl implements FlightRepositoryCustom {
    @PersistenceContext
    private EntityManager em;
    
    public FlightRepositoryImpl() { }
    
    public List<Flight> getAllSorted(Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Flight> q = cb.createQuery(Flight.class);
        Root<Flight> f = q.from(Flight.class);
        CriteriaQuery<Flight> select = q.select(f);
        q.orderBy(cb.desc(f.get("ruleHitCount")));
        
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int firstResultIndex = (pageNumber - 1) * pageSize;

        TypedQuery<Flight> typedQuery = em.createQuery(select);
        typedQuery.setFirstResult(firstResultIndex);
        typedQuery.setMaxResults(pageable.getPageSize());
        List<Flight> results = typedQuery.getResultList();
        
        return results;
    }
}