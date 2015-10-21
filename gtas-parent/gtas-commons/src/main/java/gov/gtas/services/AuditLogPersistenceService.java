package gov.gtas.services;

import gov.gtas.enumtype.AuditActionType;
import gov.gtas.model.AuditRecord;

import java.util.Date;
import java.util.List;

public interface AuditLogPersistenceService {
	public AuditRecord create(AuditRecord aRec);
    public AuditRecord findById(Long id);

    public List<AuditRecord> findByDateRange(Date dateFrom, Date dateTo);
    public List<AuditRecord> findByDateFrom(Date dateFrom);
    public List<AuditRecord> findByUser(String userId);
    public List<AuditRecord> findByActionType(AuditActionType type);
    public List<AuditRecord> findByUserAndActionType(AuditActionType type, String user);
    public List<AuditRecord> findByUserAndTarget(String user, String target);
    public List<AuditRecord> findByTarget(String target);
}
