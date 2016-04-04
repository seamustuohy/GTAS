package gov.gtas.job.scheduler;

import gov.gtas.error.ErrorDetailInfo;
import gov.gtas.error.ErrorHandlerFactory;
import gov.gtas.services.ErrorPersistenceService;

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

	@Autowired
	private ErrorPersistenceService errorPersistenceService;

	public DashboardUpdateScheduler() {
	}

	@Scheduled(fixedDelayString = "${dashboard.fixedDelay.in.milliseconds}")
	@Transactional
	public void jobScheduling() {
		logger.info("entering jobScheduling()");

		try {
			entityManager.createNativeQuery(apiDashboardUpdateSql)
					.executeUpdate();
			logger.info("Updated dashboard api.");
			entityManager.createNativeQuery(pnrDashboardUpdateSql)
					.executeUpdate();
			logger.info("Updated dashboard pnr.");
		} catch (Exception ex) {
			logger.error("SQLException:" + ex.getMessage(), ex);
			ErrorDetailInfo errInfo = ErrorHandlerFactory
					.createErrorDetails(ex);
			errorPersistenceService.create(errInfo);
		}

		logger.info("exiting jobScheduling()");

	}

}
