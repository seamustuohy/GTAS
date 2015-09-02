package gov.gtas.repository;

import gov.gtas.model.InvalidObjectInfo;
import org.springframework.data.repository.CrudRepository;

public interface ErrorLoggingRepository extends CrudRepository<InvalidObjectInfo, Long>{

}
