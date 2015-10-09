package gov.gtas.services;

import gov.gtas.model.HitDetail;
import gov.gtas.model.HitsSummary;
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
	
	@Override
	public List<HitDetail> findByPassengerId(Long id) {
		return hitsSummaryRepository.findRuleHitsByPassengerId(id);
	}

	@Override
	public Iterable<HitsSummary> findAll() {
		return hitsSummaryRepository.findAll();
	}
}
