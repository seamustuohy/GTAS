package gov.gtas.rule;

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

public class RuleRunnerScheduler {

	private static final Logger logger = LoggerFactory
			.getLogger(RuleRunnerScheduler.class);

	@Autowired
	private TargetingService targetingService;

	@Autowired
	private ErrorPersistenceService errorPersistenceService;

	public RuleRunnerScheduler() {
	}

	@Scheduled(fixedDelayString = "${fixedDelay.in.milliseconds}", initialDelayString = "${initialDelay.in.milliseconds}")
	public void jobScheduling() {
		logger.info("entering jobScheduling()");
		try {
			targetingService.preProcessing();
			Set<Long> uniqueFlights = targetingService.runningRuleEngine();
			targetingService.updateFlightHitCounts(uniqueFlights);
		} catch (Exception exception) {
			exception.printStackTrace();
			ErrorDetailInfo errInfo = ErrorHandlerFactory.createErrorDetails(
					RuleServiceConstants.RULE_ENGINE_RUNNER_ERROR_CODE,
					exception);
			errorPersistenceService.create(errInfo);
		}
		logger.info("exiting jobScheduling()");
	}

}
