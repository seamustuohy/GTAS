package gov.gtas.svc;

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
import gov.gtas.repository.HitsSummaryRepository;
import gov.gtas.repository.MessageRepository;
import gov.gtas.repository.PnrRepository;
import gov.gtas.rule.RuleService;
import gov.gtas.svc.request.builder.PassengerFlightTuple;
import gov.gtas.svc.util.TargetingServiceUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		RuleServiceRequest req = TargetingServiceUtils
				.createApisRequest(message);
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
			RuleServiceRequest req = TargetingServiceUtils
					.createApisRequest(msgs);
			RuleServiceResult res = ruleService.invokeRuleEngine(req);
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
			RuleServiceRequest req = TargetingServiceUtils
					.createPnrRequest(msgs);
			RuleServiceResult res = ruleService.invokeRuleEngine(req);
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
	public RuleServiceResult analyzeLoadedMessages(MessageStatus statusToLoad,
			MessageStatus statusAfterProcesssing,
			final boolean updateProcesssedMessageStat) {
		Iterator<Message> source = messageRepository.findAll().iterator();
		List<Message> target = new ArrayList<Message>();
		source.forEachRemaining(target::add);

		if (logger.isInfoEnabled()) {
			logger.info("TargetingServiceImpl.analyzeLoadedMessages() - retrieved  message list size-> "
					+ target.size());
		}

		RuleServiceRequest req = TargetingServiceUtils
				.createPnrApisRequest(target);
		// default knowledge Base is the UDR KB
		RuleServiceResult udrResult = ruleService.invokeRuleEngine(req);

		RuleServiceResult wlResult = ruleService.invokeRuleEngine(req,
				WatchlistConstants.WL_KNOWLEDGE_BASE_NAME);
		if (udrResult == null && wlResult == null) {
			throw ErrorHandlerFactory
					.getErrorHandler()
					.createException(
							RuleServiceConstants.KB_NOT_FOUND_ERROR_CODE,
							(RuleConstants.UDR_KNOWLEDGE_BASE_NAME + "/" + WatchlistConstants.WL_KNOWLEDGE_BASE_NAME));
		}
		if (updateProcesssedMessageStat) {
			for (Message message : target) {
				message.setStatus(statusAfterProcesssing);
			}
		}
		// eliminate duplicates
		if (udrResult != null) {
			udrResult = TargetingServiceUtils
					.ruleResultPostProcesssing(udrResult);
		}
		return new CompositeRuleServiceResult(udrResult, wlResult);
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

	// @Scheduled(fixedDelay = 4000)
	@Transactional
	public void runningRuleEngine() {
		// logger.info(new Date() + " a fixed delay running");
		List<HitsSummary> hitsSummaryList = new ArrayList<HitsSummary>();

		RuleServiceResult ruleRunningResult = analyzeLoadedMessages(
				MessageStatus.LOADED, MessageStatus.ANALYZED, true);

		RuleExecutionStatistics ruleExeStatus = ruleRunningResult
				.getExecutionStatistics();
		if (logger.isInfoEnabled()) {
			logger.info(("\nTargetingServiceImpl.runningRuleEngine() - Total Rules fired. --> " + ruleExeStatus
					.getTotalRulesFired()));
		}

		deleteExistingHitRecords();

		List<RuleHitDetail> results = (List<RuleHitDetail>) ruleRunningResult
				.getResultList();
		Iterator<RuleHitDetail> iter = results.iterator();
		while (iter.hasNext()) {
			RuleHitDetail ruleDetail = iter.next();
			HitsSummary hitsSummary = constructHitsInfo(ruleDetail);
			hitsSummaryList.add(hitsSummary);
		}
		hitsSummaryRepository.save(hitsSummaryList);
	}

	private void deleteExistingHitRecords() {
		Set<PassengerFlightTuple> passengerFlightTuples = TargetingServiceUtils
				.getPaxFlightTuples();
		passengerFlightTuples.forEach(passengerFlightTuple -> {
			HitsSummary found = hitsSummaryRepository
					.findByFlightIdAndPassengerId(passengerFlightTuple
							.getFlight().getId(), passengerFlightTuple
							.getPassenger().getId());
			if (found != null) {
				hitsSummaryRepository.delete(found);
			}
		});
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

		HitDetail hitDetail = new HitDetail();
		hitDetail.setRuleId(ruleHitDetail.getUdrRuleId());
		hitDetail.setRuleConditions(sb.toString());
		hitDetail.setCreatedDate(new Date());
		hitDetail.setParent(hitsSummary);

		List<HitDetail> detailList = new ArrayList<HitDetail>();

		detailList.add(hitDetail);
		hitsSummary.setHitdetails(detailList);
		return hitsSummary;
	}
}
