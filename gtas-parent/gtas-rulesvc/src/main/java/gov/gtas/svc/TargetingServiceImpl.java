package gov.gtas.svc;

import java.util.ArrayList;
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

import gov.gtas.bo.CompositeRuleServiceResult;
import gov.gtas.bo.RuleExecutionStatistics;
import gov.gtas.bo.RuleHitDetail;
import gov.gtas.bo.RuleServiceRequest;
import gov.gtas.bo.RuleServiceResult;
import gov.gtas.constant.CommonErrorConstants;
import gov.gtas.constant.RuleConstants;
import gov.gtas.constant.RuleServiceConstants;
import gov.gtas.constant.WatchlistConstants;
import gov.gtas.error.ErrorHandlerFactory;
import gov.gtas.model.ApisMessage;
import gov.gtas.model.HitDetail;
import gov.gtas.model.HitsSummary;
import gov.gtas.model.Message;
import gov.gtas.model.MessageStatus;
import gov.gtas.model.Pnr;
import gov.gtas.repository.ApisMessageRepository;
import gov.gtas.repository.FlightRepository;
import gov.gtas.repository.HitsSummaryRepository;
import gov.gtas.repository.MessageRepository;
import gov.gtas.repository.PnrRepository;
import gov.gtas.rule.RuleService;
import gov.gtas.svc.request.builder.PassengerFlightTuple;
import gov.gtas.svc.util.RuleExecutionContext;
import gov.gtas.svc.util.TargetingServiceUtils;

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
		if (null == message) {
			throw ErrorHandlerFactory.getErrorHandler().createException(
					CommonErrorConstants.NULL_ARGUMENT_ERROR_CODE,
					"ApisMessage", "TargetingServiceImpl.analyzeApisMessage()");
		}
		RuleServiceRequest req = TargetingServiceUtils.createApisRequest(
				message).getRuleServiceRequest();
		RuleServiceResult res = ruleService.invokeRuleEngine(req);
		res = TargetingServiceUtils.ruleResultPostProcesssing(res);
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
		res = TargetingServiceUtils.ruleResultPostProcesssing(res);
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
		res = TargetingServiceUtils.ruleResultPostProcesssing(res);
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
			res = TargetingServiceUtils.ruleResultPostProcesssing(res);
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
			res = TargetingServiceUtils.ruleResultPostProcesssing(res);
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

		Iterator<Message> source = messageRepository.findAll().iterator();
		List<Message> target = new ArrayList<Message>();
		source.forEachRemaining(target::add);

		if (logger.isInfoEnabled()) {
			logger.info("TargetingServiceImpl.analyzeLoadedMessages() - retrieved  message list size-> "
					+ target.size());
		}
		RuleExecutionContext ctx = executeRules(target);

		if (updateProcesssedMessageStat) {
			for (Message message : target) {
				message.setStatus(statusAfterProcesssing);
			}
		}
		return ctx;
	}

	private RuleExecutionContext executeRules(List<Message> target) {
		RuleExecutionContext ctx = TargetingServiceUtils
				.createPnrApisRequestContext(target);
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
			udrResult = TargetingServiceUtils
					.ruleResultPostProcesssing(udrResult);
		}
		if (wlResult != null) {
			wlResult = TargetingServiceUtils
					.ruleResultPostProcesssing(wlResult);
		}

		ctx.setRuleServiceResult(new CompositeRuleServiceResult(udrResult,
				wlResult));
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

		RuleExecutionStatistics ruleExeStatus = ruleRunningResult
				.getRuleServiceResult().getExecutionStatistics();
		if (logger.isInfoEnabled()) {
			logger.info("\nTargetingServiceImpl.runningRuleEngine() - Total Rules fired. --> " + ruleExeStatus.getTotalRulesFired());
		}

		deleteExistingHitRecords(ruleRunningResult.getPaxFlightTuples());

		List<HitsSummary> hitsSummary = storeHitsInfo(ruleRunningResult);
		Set<Long> uniqueFlights = new HashSet<>();
        for (HitsSummary s : hitsSummary) {
            uniqueFlights.add(s.getFlightId());
        }
        
        return uniqueFlights;
    }
	
    @Transactional
	public void updateFlightHitCounts(Set<Long> flights) {
        for (Long flightId : flights) {
            flightRepository.updateRuleHitCountForFlight(flightId);
            flightRepository.updateListHitCountForFlight(flightId);
        }
	}

	private List<HitsSummary> storeHitsInfo(RuleExecutionContext ruleRunningResult) {
		List<HitsSummary> hitsSummaryList = new ArrayList<HitsSummary>();
		List<RuleHitDetail> results = (List<RuleHitDetail>) ruleRunningResult
				.getRuleServiceResult().getResultList();
		Iterator<RuleHitDetail> iter = results.iterator();
		while (iter.hasNext()) {
			RuleHitDetail ruleDetail = iter.next();
			HitsSummary hitsSummary = constructHitsInfo(ruleDetail);
			hitsSummaryList.add(hitsSummary);
		}
		hitsSummaryRepository.save(hitsSummaryList);
		
		return hitsSummaryList;
	}

	private void deleteExistingHitRecords(
			Set<PassengerFlightTuple> passengerFlightTuples) {
		List<PassengerFlightTuple> setList = new ArrayList<PassengerFlightTuple>(
				passengerFlightTuples);
		for (int i = 0; i < setList.size(); i++) {
			PassengerFlightTuple passengerFlightTuple = setList.get(i);
			List<HitsSummary> found = hitsSummaryRepository
					.findByFlightIdAndPassengerId(passengerFlightTuple
							.getFlight().getId(), passengerFlightTuple
							.getPassenger().getId());
			if (!CollectionUtils.isEmpty(found)) {
				found.forEach(obj -> {
					em.remove(obj);
				});
			}
			if (i % Integer.valueOf(batchSize) == 0) {
				em.flush();
				em.clear();
			}
		}
	}

	/**
	 * @param ruleHitDetail
	 * @return HitsSummary
	 */
	private HitsSummary constructHitsInfo(RuleHitDetail ruleHitDetail) {
		String[] hitReasons = ruleHitDetail.getHitReasons();

		StringBuilder sb = new StringBuilder();
		for (String hitReason : hitReasons) {
			sb.append(hitReason);
			sb.append(HITS_REASONS_SEPARATOR);
		}

		HitsSummary hitsSummary = new HitsSummary();
		hitsSummary.setPassengerId(ruleHitDetail.getPassengerId());
		hitsSummary.setDescription(ruleHitDetail.getDescription());
		hitsSummary.setTitle(ruleHitDetail.getTitle());
		hitsSummary.setFlightId(ruleHitDetail.getFlightId());
		hitsSummary.setCreatedDate(new Date());
		hitsSummary.setHitType(ruleHitDetail.getHitType());

		hitsSummary.setRuleHitCount(ruleHitDetail.getRuleHitCount());
		hitsSummary.setWatchListHitCount(ruleHitDetail.getHitCount()
				- ruleHitDetail.getRuleHitCount());
		HitDetail hitDetail = new HitDetail();
		String type = ruleHitDetail.getHitType();
		if (type.equalsIgnoreCase("R"))
			hitDetail.setRuleId(ruleHitDetail.getUdrRuleId());
		else {
			hitDetail.setRuleId(ruleHitDetail.getRuleId());
		}

		hitDetail.setRuleConditions(sb.toString());
		hitDetail.setCreatedDate(new Date());
		hitDetail.setParent(hitsSummary);

		List<HitDetail> detailList = new ArrayList<HitDetail>();

		detailList.add(hitDetail);
		hitsSummary.setHitdetails(detailList);
		return hitsSummary;
	}
}
