package gov.gtas.repository;

import gov.gtas.enumtype.AuditActionType;
import gov.gtas.model.AuditRecord;
import gov.gtas.model.User;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AuditRecordRepository extends CrudRepository<AuditRecord, Long>{
	public List<AuditRecord> findByActionType(AuditActionType actionType);
	public List<AuditRecord> findByUser(User user);
	public List<AuditRecord> findByUserAndActionType(User user, AuditActionType actionType);
	public List<AuditRecord> findByUserAndTarget(User user, String target);
	public List<AuditRecord> findByTarget(String target);
	
	@Query("SELECT ar FROM AuditRecord ar WHERE ar.timestamp >= :fromDate and  ar.timestamp <= :toDate")
    public List<AuditRecord> findByTimestampRange(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);  
	
	@Query("SELECT ar FROM AuditRecord ar WHERE ar.timestamp >= :fromDate")
    public List<AuditRecord> findByTimestampFrom(@Param("fromDate") Date fromDate);   
}
