package gov.gtas.repository;

import org.springframework.data.repository.CrudRepository;

import gov.gtas.model.FrequentFlyer;

public interface FrequentFlyerRepository extends CrudRepository<FrequentFlyer, Long> {
	public FrequentFlyer findByCarrierAndNumber(String carrier, String number);
}
