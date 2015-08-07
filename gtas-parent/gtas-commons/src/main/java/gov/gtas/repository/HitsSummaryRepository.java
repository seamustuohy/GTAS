package gov.gtas.repository;

import gov.gtas.model.HitDetail;
import gov.gtas.model.HitsSummary;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface HitsSummaryRepository extends
		CrudRepository<HitsSummary, Long> {

	@Query("SELECT hits.hitdetails FROM HitsSummary hits WHERE hits.travelerId = (:id)")
	List<HitDetail> findByTravelerId(@Param("id") Long id);
	
	@Query("SELECT s FROM HitsSummary s")
	Iterable<HitsSummary> findAll();
}
