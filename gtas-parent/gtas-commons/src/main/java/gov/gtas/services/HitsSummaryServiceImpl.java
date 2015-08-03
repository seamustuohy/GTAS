package gov.gtas.services;

import gov.gtas.repository.HitsSummaryRepository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class HitsSummaryServiceImpl implements HitsSummaryService {

	@Autowired
	HitsSummaryRepository hitsSummaryRepository;

	/**
	 * return list of rule ids that matched the traveler
	 */
	@Override
	public List<Long> findByTravelerId(Long id) {
		return hitsSummaryRepository.findByTravelerId(id);
	}
}
