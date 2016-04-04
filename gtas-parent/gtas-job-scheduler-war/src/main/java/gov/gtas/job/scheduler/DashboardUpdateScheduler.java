package gov.gtas.job.scheduler;

import static gov.gtas.constant.GtasSecurityConstants.GTAS_APPLICATION_USERID;
import gov.gtas.enumtype.AuditActionType;
import gov.gtas.error.ErrorDetailInfo;
import gov.gtas.error.ErrorHandlerFactory;
import gov.gtas.json.AuditActionData;
import gov.gtas.json.AuditActionTarget;
import gov.gtas.services.AuditLogPersistenceService;
import gov.gtas.services.ErrorPersistenceService;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DashboardUpdateScheduler {

	private static final Logger logger = LoggerFactory
			.getLogger(DashboardUpdateScheduler.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Value("${dashboard.api.message.update}")
	private String apiDashboardUpdateSql;

	@Value("${dashboard.pnr.message.update}")
	private String pnrDashboardUpdateSql;

	private ErrorPersistenceService errorPersistenceService;

	private AuditLogPersistenceService auditLogPersistenceService;

	@Autowired
	public DashboardUpdateScheduler(
			ErrorPersistenceService errorPersistenceService,
			AuditLogPersistenceService auditLogPersistenceService) {
		this.errorPersistenceService = errorPersistenceService;
		this.auditLogPersistenceService = auditLogPersistenceService;
	}

	@Scheduled(fixedDelayString = "${dashboard.fixedDelay.in.milliseconds}")
	@Transactional
	public void jobScheduling() {
		logger.info("entering jobScheduling()");

		try {
			entityManager.createNativeQuery(apiDashboardUpdateSql)
					.executeUpdate();
			logger.info("Updated dashboard api.");
			int updatedRecords = entityManager.createNativeQuery(
					pnrDashboardUpdateSql).executeUpdate();
			logger.info("Updated dashboard pnr.");
			writeAuditLogForTargetingRun(updatedRecords);
		} catch (Exception ex) {
			logger.error("SQLException:" + ex.getMessage(), ex);
			ErrorDetailInfo errInfo = ErrorHandlerFactory
					.createErrorDetails(ex);
			errorPersistenceService.create(errInfo);
		}

		logger.info("exiting jobScheduling()");

	}

	private void writeAuditLogForTargetingRun(Integer updatedRecords) {
		try {
			AuditActionTarget target = new AuditActionTarget(
					AuditActionType.UPDATE_DASHBOARD,
					"GTAS Updating Dashboard", null);
			AuditActionData actionData = new AuditActionData();
			actionData.addProperty("updatedDashboardRecord", String.valueOf(updatedRecords));
			String message = "Updating Dashboard run on " + new Date();
			auditLogPersistenceService.create(AuditActionType.UPDATE_DASHBOARD,
					target.toString(), actionData.toString(), message,
					GTAS_APPLICATION_USERID);
		} catch (Exception ex) {
			logger.error("SQLException:" + ex.getMessage(), ex);
			ErrorDetailInfo errInfo = ErrorHandlerFactory
					.createErrorDetails(ex);
			errorPersistenceService.create(errInfo);
		}
	}

}
