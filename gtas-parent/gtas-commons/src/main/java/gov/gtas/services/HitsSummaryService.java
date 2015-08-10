package gov.gtas.services;

import gov.gtas.model.HitDetail;
import gov.gtas.model.HitsSummary;

import java.util.List;

public interface HitsSummaryService {

	public List<HitDetail> findByPassengerId(Long id);
	public Iterable<HitsSummary> findAll();

}
