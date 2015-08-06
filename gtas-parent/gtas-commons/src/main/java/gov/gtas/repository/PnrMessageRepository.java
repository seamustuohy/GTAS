package gov.gtas.repository;

import org.springframework.data.repository.CrudRepository;

import gov.gtas.model.PnrMessage;

public interface PnrMessageRepository extends CrudRepository<PnrMessage, Long> {

}