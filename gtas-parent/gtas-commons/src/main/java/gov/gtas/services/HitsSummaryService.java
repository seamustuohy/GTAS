package gov.gtas.services;

import gov.gtas.model.HitDetail;

import java.util.List;

public interface HitsSummaryService {

	public List<HitDetail> findByTravelerId(Long id);

}
