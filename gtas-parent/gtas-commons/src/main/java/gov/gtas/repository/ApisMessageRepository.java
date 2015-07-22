package gov.gtas.repository;

import gov.gtas.model.ApisMessage;
import gov.gtas.model.MessageStatus;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ApisMessageRepository extends
		CrudRepository<ApisMessage, Long> {

	List<ApisMessage> findByStatus(MessageStatus status);

}