package gov.gtas.repository;

import gov.gtas.model.HitDetail;
import gov.gtas.model.HitsSummary;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface HitsSummaryRepository extends
		CrudRepository<HitsSummary, Long> {

	@Query("SELECT hits.hitdetails FROM HitsSummary hits WHERE hits.passenger.id = (:id)")
	public List<HitDetail> findByPassengerId(@Param("id") Long id);

	@Query("SELECT s FROM HitsSummary s")
	public Iterable<HitsSummary> findAll();

	@Query("SELECT hits FROM HitsSummary hits WHERE hits.passenger.id = :pid and hits.flight.id = :fid")
	List<HitsSummary> findByFlightIdAndPassengerId(@Param("fid") Long flightId,
			@Param("pid") Long passengerId);
}
