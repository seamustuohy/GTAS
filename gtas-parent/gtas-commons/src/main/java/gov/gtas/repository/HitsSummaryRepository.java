package gov.gtas.repository;

import gov.gtas.model.HitDetail;
import gov.gtas.model.HitsSummary;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface HitsSummaryRepository extends
		CrudRepository<HitsSummary, Long> {

	List<HitDetail> findByPassengerId(Long id);

	Iterable<HitsSummary> findAll();
}
