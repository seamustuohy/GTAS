package gov.gtas.services;

import gov.gtas.model.HitDetail;
import gov.gtas.model.HitsSummary;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HitsSummaryService {

	public List<HitDetail> findByPassengerId(Long id);
	public Iterable<HitsSummary> findAll();
	public List<HitsSummary> findHitsByFlightId(@Param("fid") Long flightId);

}
