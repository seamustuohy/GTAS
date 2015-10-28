package gov.gtas.repository;

import org.springframework.data.repository.CrudRepository;

import gov.gtas.model.Agency;

public interface AgencyRepository extends CrudRepository<Agency, Long> {
	public Agency findByNameAndLocation(String name, String location);
}
