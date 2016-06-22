package gov.gtas.services;

import gov.gtas.model.InvalidObjectInfo;
import java.util.List;

public interface ErrorLoggingService {

    public InvalidObjectInfo create(InvalidObjectInfo invalidObjectInfo);
    public InvalidObjectInfo delete(Long id);
    public List<InvalidObjectInfo> findAll();
    public InvalidObjectInfo update(InvalidObjectInfo port) ;
    public InvalidObjectInfo findById(Long id);
}
