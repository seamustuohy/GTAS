package gov.gtas.repository;

import org.springframework.data.repository.CrudRepository;

import gov.gtas.model.Disposition;

public interface DispositionRepository extends CrudRepository<Disposition, Long> {
}
