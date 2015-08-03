package gov.gtas.repository;

import gov.gtas.model.HitsSummary;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface HitsSummaryRepository extends
		CrudRepository<HitsSummary, Long> {

//	@Query("SELECT hits.ruleId FROM HitsSummary hits WHERE hits.travelerId = (:id)")
//	List<Long> findByTravelerId(@Param("id") Long id);

}
