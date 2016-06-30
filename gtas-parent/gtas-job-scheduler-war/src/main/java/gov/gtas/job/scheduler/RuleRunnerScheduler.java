package gov.gtas.job.scheduler;

import gov.gtas.constant.RuleServiceConstants;
import gov.gtas.error.ErrorDetailInfo;
import gov.gtas.error.ErrorHandlerFactory;
import gov.gtas.services.ErrorPersistenceService;
import gov.gtas.svc.TargetingService;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RuleRunnerScheduler {

	private static final Logger logger = LoggerFactory
			.getLogger(RuleRunnerScheduler.class);

	private TargetingService targetingService;

	private ErrorPersistenceService errorPersistenceService;

	@Autowired
	public RuleRunnerScheduler(TargetingService targetingService,
			ErrorPersistenceService errorPersistenceService) {
		this.targetingService = targetingService;
		this.errorPersistenceService = errorPersistenceService;
	}

	@Scheduled(fixedDelayString = "${ruleRunner.fixedDelay.in.milliseconds}", initialDelayString = "${ruleRunner.initialDelay.in.milliseconds}")
	public void jobScheduling() {
		logger.info("entering jobScheduling()");
		try {
			targetingService.preProcessing();
			Set<Long> uniqueFlights = targetingService.runningRuleEngine();
			targetingService.updateFlightHitCounts(uniqueFlights);
		} catch (Exception exception) {
			logger.error(exception.getCause().getMessage());
			ErrorDetailInfo errInfo = ErrorHandlerFactory.createErrorDetails(
					RuleServiceConstants.RULE_ENGINE_RUNNER_ERROR_CODE,
					exception);
			errorPersistenceService.create(errInfo);
		}
		logger.info("exiting jobScheduling()");
	}

}
