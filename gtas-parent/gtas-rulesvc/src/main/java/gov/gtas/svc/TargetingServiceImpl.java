package gov.gtas.svc;

import gov.gtas.bo.CompositeRuleServiceResult;
import gov.gtas.bo.RuleExecutionStatistics;
import gov.gtas.bo.RuleHitDetail;
import gov.gtas.bo.RuleServiceRequest;
import gov.gtas.bo.RuleServiceResult;
import gov.gtas.bo.TargetDetailVo;
import gov.gtas.bo.TargetSummaryVo;
import gov.gtas.constant.CommonErrorConstants;
import gov.gtas.constant.RuleConstants;
import gov.gtas.constant.RuleServiceConstants;
import gov.gtas.constant.WatchlistConstants;
import gov.gtas.error.ErrorHandlerFactory;
import gov.gtas.model.ApisMessage;
import gov.gtas.model.Flight;
import gov.gtas.model.HitDetail;
import gov.gtas.model.HitsSummary;
import gov.gtas.model.Message;
import gov.gtas.model.MessageStatus;
import gov.gtas.model.Passenger;
import gov.gtas.model.Pnr;
import gov.gtas.repository.ApisMessageRepository;
import gov.gtas.repository.FlightRepository;
import gov.gtas.repository.HitsSummaryRepository;
import gov.gtas.repository.MessageRepository;
import gov.gtas.repository.PassengerRepository;
import gov.gtas.repository.PnrRepository;
import gov.gtas.rule.RuleService;
import gov.gtas.svc.request.builder.PassengerFlightTuple;
import gov.gtas.svc.util.RuleExecutionContext;
import gov.gtas.svc.util.TargetingResultUtils;
import gov.gtas.svc.util.TargetingServiceUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * Implementation of the Targeting Service API.
 * 
 * @author GTAS3 (AB)
 *
 */
@Service
public class TargetingServiceImpl implements TargetingService {

	private static final Logger logger = LoggerFactory
			.getLogger(TargetingServiceImpl.class);

	private final String HITS_REASONS_SEPARATOR = "$$$";

	/* The rule engine to be used. */
	private final RuleService ruleService;

	@Autowired
	private MessageRepository<Message> messageRepository;

	@Autowired
	private ApisMessageRepository apisMsgRepository;

	@Autowired
	private PnrRepository PnrMsgRepository;

	@Autowired
	private HitsSummaryRepository hitsSummaryRepository;

	@Autowired
	private FlightRepository flightRepository;

	@Autowired
	private PassengerRepository passengerRepository;

	@Autowired
	private EntityManager em;

	@Value("${hibernate.jdbc.batch_size}")
	private String batchSize;

	/**
	 * Constructor obtained from the spring context by auto-wiring.
	 * 
	 * @param rulesvc
	 *            the auto-wired rule engine instance.
	 */
	@Autowired
	public TargetingServiceImpl(final RuleService rulesvc) {
		ruleService = rulesvc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gtas.svc.TargetingService#analyzeApisMessage(gov.gtas.model.ApisMessage
	 * )
	 */
	@Override
	@Transactional
	public RuleServiceResult analyzeApisMessage(ApisMessage message) {
		logger.info("Entering analyzeApisMessage().");

		if (null == message) {
			logger.error("message is null.");
			throw ErrorHandlerFactory.getErrorHandler().createException(
					CommonErrorConstants.NULL_ARGUMENT_ERROR_CODE,
					"ApisMessage", "TargetingServiceImpl.analyzeApisMessage()");
		}
		RuleServiceRequest req = TargetingServiceUtils.createApisRequest(
				message).getRuleServiceRequest();
		RuleServiceResult res = ruleService.invokeRuleEngine(req);
		res = TargetingResultUtils.ruleResultPostProcesssing(res);
		logger.info("Exiting analyzeApisMessage().");
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gtas.svc.TargetingService#applyRules(gov.gtas.bo.RuleServiceRequest,
	 * java.lang.String)
	 */
	@Override
	public RuleServiceResult applyRules(RuleServiceRequest request,
			String drlRules) {
		RuleServiceResult res = ruleService.invokeAdhocRulesFromString(
				drlRules, request);
		res = TargetingResultUtils.ruleResultPostProcesssing(res);
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.gtas.svc.TargetingService#analyzeApisMessage(long)
	 */
	@Override
	@Transactional
	public RuleServiceResult analyzeApisMessage(long messageId) {
		ApisMessage msg = apisMsgRepository.findOne(messageId);
		if (msg == null) {
			throw ErrorHandlerFactory.getErrorHandler().createException(
					RuleServiceConstants.MESSAGE_NOT_FOUND_ERROR_CODE,
					messageId);
		}
		RuleServiceResult res = this.analyzeApisMessage(msg);
		res = TargetingResultUtils.ruleResultPostProcesssing(res);
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.gtas.svc.TargetingService#analyzeLoadedApisMessage()
	 */
	@Override
	@Transactional
	public List<RuleHitDetail> analyzeLoadedApisMessage() {
		List<RuleHitDetail> ret = null;
		List<ApisMessage> msgs = this.retrieveApisMessage(MessageStatus.LOADED);
		if (msgs != null) {
			RuleExecutionContext ctx = TargetingServiceUtils
					.createApisRequestContext(msgs);
			RuleServiceResult res = ruleService.invokeRuleEngine(ctx
					.getRuleServiceRequest());
			res = TargetingResultUtils.ruleResultPostProcesssing(res);
			ret = res.getResultList();
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.gtas.svc.TargetingService#analyzeLoadedPnr()
	 */
	@Override
	@Transactional
	public List<RuleHitDetail> analyzeLoadedPnr() {
		List<RuleHitDetail> ret = null;
		List<Pnr> msgs = this.retrievePnr(MessageStatus.LOADED);
		if (msgs != null) {
			RuleExecutionContext ctx = TargetingServiceUtils
					.createPnrRequestContext(msgs);
			RuleServiceResult res = ruleService.invokeRuleEngine(ctx
					.getRuleServiceRequest());
			res = TargetingResultUtils.ruleResultPostProcesssing(res);
			ret = res.getResultList();
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.gtas.svc.TargetingService#analyzeLoadedMessages()
	 */
	@Override
	@Transactional
	public RuleExecutionContext analyzeLoadedMessages(
			MessageStatus statusToLoad, MessageStatus statusAfterProcesssing,
			final boolean updateProcesssedMessageStat) {
		logger.info("Entering analyzeLoadedMessages()");
		Iterator<Message> source = messageRepository.findByStatus(statusToLoad).iterator();
		List<Message> target = new ArrayList<Message>();
		source.forEachRemaining(target::add);

		if (logger.isInfoEnabled()) {
			logger.info("TargetingServiceImpl.analyzeLoadedMessages() - retrieved  message list size-> "
					+ target.size());
		}
		RuleExecutionContext ctx = executeRules(target);

		logger.info("updating loaded messages status.");
		if (updateProcesssedMessageStat) {
			for (Message message : target) {
				message.setStatus(statusAfterProcesssing);
			}
		}
		logger.info("Exiting analyzeLoadedMessages()");
		return ctx;
	}

	private RuleExecutionContext executeRules(List<Message> target) {
		logger.info("Entering executeRules().");

		RuleExecutionContext ctx = TargetingServiceUtils
				.createPnrApisRequestContext(target);

		logger.info("Running Rule set.");
		// default knowledge Base is the UDR KB
		RuleServiceResult udrResult = ruleService.invokeRuleEngine(ctx
				.getRuleServiceRequest());

		RuleServiceResult wlResult = ruleService.invokeRuleEngine(
				ctx.getRuleServiceRequest(),
				WatchlistConstants.WL_KNOWLEDGE_BASE_NAME);
		if (udrResult == null && wlResult == null) {
			throw ErrorHandlerFactory
					.getErrorHandler()
					.createException(
							RuleServiceConstants.KB_NOT_FOUND_ERROR_CODE,
							(RuleConstants.UDR_KNOWLEDGE_BASE_NAME + "/" + WatchlistConstants.WL_KNOWLEDGE_BASE_NAME));
		}

		// eliminate duplicates
		if (udrResult != null) {
			logger.info("Eliminate duplicates from UDR rule running.");
			udrResult = TargetingResultUtils
					.ruleResultPostProcesssing(udrResult);
		}
		if (wlResult != null) {
			logger.info("Eliminate duplicates from watchlist rule running.");
			wlResult = TargetingResultUtils.ruleResultPostProcesssing(wlResult);
		}

		TargetingResultUtils.updateRuleExecutionContext(ctx,
				new CompositeRuleServiceResult(udrResult, wlResult));

		logger.info("Exiting executeRules().");
		return ctx;
	}

	@Override
	@Transactional
	public List<ApisMessage> retrieveApisMessage(MessageStatus messageStatus) {
		return apisMsgRepository.findByStatus(messageStatus);
	}

	@Override
	@Transactional
	public List<Pnr> retrievePnr(MessageStatus messageStatus) {
		return PnrMsgRepository.findByStatus(messageStatus);
	}

	@Override
	@Transactional
	public void updateApisMessage(ApisMessage message,
			MessageStatus messageStatus) {
		ApisMessage apisMessage = apisMsgRepository.findOne(message.getId());
		if (apisMessage != null) {
			apisMessage.setStatus(messageStatus);
		}
	}

	@Override
	@Transactional
	public void updatePnr(Pnr message, MessageStatus messageStatus) {
		Pnr pnr = PnrMsgRepository.findOne(message.getId());
		if (pnr != null) {
			pnr.setStatus(messageStatus);
		}
	}

	@Transactional
	public Set<Long> runningRuleEngine() {
		RuleExecutionContext ruleRunningResult = analyzeLoadedMessages(
				MessageStatus.LOADED, MessageStatus.ANALYZED, true);
		logger.info("Entering runningRuleEngine().");
		RuleExecutionStatistics ruleExeStatus = ruleRunningResult
				.getRuleExecutionStatistics();
		if (logger.isInfoEnabled()) {
			logger.info("\nTargetingServiceImpl.runningRuleEngine() - Total Rules fired. --> "
					+ ruleExeStatus.getTotalRulesFired());
		}

		deleteExistingHitRecords(ruleRunningResult.getPaxFlightTuples());

		List<HitsSummary> hitsSummary = storeHitsInfo(ruleRunningResult);
		Set<Long> uniqueFlights = new HashSet<>();
		for (HitsSummary s : hitsSummary) {
			uniqueFlights.add(s.getFlight().getId());
		}
		logger.info("Exiting runningRuleEngine().");
		return uniqueFlights;
	}

	@Transactional
	public void updateFlightHitCounts(Set<Long> flights) {
		logger.info("Entering updateFlightHitCounts().");
		if (CollectionUtils.isEmpty(flights)) {
			logger.info("no flight");
			return;
		}
		logger.info("update rule hit count on flights.");
		for (Long flightId : flights) {
			flightRepository.updateRuleHitCountForFlight(flightId);
			flightRepository.updateListHitCountForFlight(flightId);
		}
	}

	private List<HitsSummary> storeHitsInfo(
			RuleExecutionContext ruleRunningResult) {
		logger.info("Entering storeHitsInfo().");

		List<HitsSummary> hitsSummaryList = new ArrayList<HitsSummary>();
		Collection<TargetSummaryVo> results = ruleRunningResult
				.getTargetingResult();

		Iterator<TargetSummaryVo> iter = results.iterator();
		while (iter.hasNext()) {
			TargetSummaryVo ruleDetail = iter.next();
			HitsSummary hitsSummary = constructHitsInfo(ruleDetail);
			hitsSummaryList.add(hitsSummary);
		}
		hitsSummaryRepository.save(hitsSummaryList);

		logger.info("Exiting storeHitsInfo().");
		return hitsSummaryList;
	}

	private void deleteExistingHitRecords(
			Set<PassengerFlightTuple> passengerFlightTuples) {
		logger.info("Entering deleteExistingHitRecords().");
		List<PassengerFlightTuple> setList = new ArrayList<PassengerFlightTuple>(
				passengerFlightTuples);
		for (int i = 0; i < setList.size(); i++) {
			PassengerFlightTuple passengerFlightTuple = setList.get(i);
			List<HitsSummary> found = hitsSummaryRepository
					.findByFlightIdAndPassengerId(passengerFlightTuple
							.getFlight().getId(), passengerFlightTuple
							.getPassenger().getId());

			if (!CollectionUtils.isEmpty(found)) {
				logger.info("Hits Summary record(s) found.");
				found.forEach(obj -> {
						em.remove(obj);
				});
			}
			if (i % Integer.valueOf(batchSize) == 0) {
				em.flush();
				em.clear();
			}
		}
		logger.info("Exiting deleteExistingHitRecords().");
	}

	/**
	 * @param hitSummmaryVo
	 * @return HitsSummary
	 */
	private HitsSummary constructHitsInfo(TargetSummaryVo hitSummmaryVo) {
		logger.info("Entering constructHitsInfo().");
		HitsSummary hitsSummary = new HitsSummary();
		Passenger foundPassenger = passengerRepository.findOne(hitSummmaryVo
				.getPassengerId());
		if (foundPassenger != null) {
			hitsSummary.setPassenger(foundPassenger);
		}

		Flight foundFlight = flightRepository.findOne(hitSummmaryVo
				.getFlightId());
		if (foundFlight != null) {
			hitsSummary.setFlight(foundFlight);
		}
		hitsSummary.setCreatedDate(new Date());
		hitsSummary.setHitType(hitSummmaryVo.getHitType().toString());

		hitsSummary.setRuleHitCount(hitSummmaryVo.getRuleHitCount());
		hitsSummary.setWatchListHitCount(hitSummmaryVo.getWatchlistHitCount());
		List<HitDetail> detailList = new ArrayList<HitDetail>();
		for (TargetDetailVo hdv : hitSummmaryVo.getHitDetails()) {
			detailList.add(createHitDetail(hitsSummary, hdv));
		}
		hitsSummary.setHitdetails(detailList);
		logger.info("Exiting constructHitsInfo().");
		return hitsSummary;
	}

	private HitDetail createHitDetail(HitsSummary hitsSummary,
			TargetDetailVo hitDetailVo) {
		logger.info("Entering createHitDetail().");
		HitDetail hitDetail = new HitDetail();
		if (hitDetailVo.getUdrRuleId() != null) {
			logger.info("Set UDR Rule Id.");
			hitDetail.setRuleId(hitDetailVo.getUdrRuleId());
		} else {
			logger.info("Set Rule Id.");
			hitDetail.setRuleId(hitDetailVo.getRuleId());
		}

		String[] hitReasons = hitDetailVo.getHitReasons();

		StringBuilder sb = new StringBuilder();
		for (String hitReason : hitReasons) {
			sb.append(hitReason);
			sb.append(HITS_REASONS_SEPARATOR);
		}

		hitDetail.setRuleConditions(sb.toString());
		hitDetail.setCreatedDate(new Date());
		hitDetail.setTitle(hitDetailVo.getTitle());
		hitDetail.setDescription(hitDetailVo.getDescription());
		hitDetail.setHitType(hitDetailVo.getHitType().toString());

		hitDetail.setParent(hitsSummary);
		logger.info("Exiting createHitDetail().");
		return hitDetail;
	}
}
