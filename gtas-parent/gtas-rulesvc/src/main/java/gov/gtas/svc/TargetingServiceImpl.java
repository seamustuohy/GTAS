package gov.gtas.svc;

import gov.gtas.bo.RuleExecutionStatistics;
import gov.gtas.bo.RuleHitDetail;
import gov.gtas.bo.RuleServiceRequest;
import gov.gtas.constant.RuleServiceConstants;
import gov.gtas.error.CommonErrorConstants;
import gov.gtas.error.ErrorHandlerFactory;
import gov.gtas.model.ApisMessage;
import gov.gtas.model.HitDetail;
import gov.gtas.model.HitsSummary;
import gov.gtas.model.MessageStatus;
import gov.gtas.repository.ApisMessageRepository;
import gov.gtas.repository.HitsSummaryRepository;
import gov.gtas.rule.RuleService;
import gov.gtas.rule.RuleServiceResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

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
	/* The rule engine to be used. */
	private final RuleService ruleService;

	@Autowired
	private ApisMessageRepository apisMsgRepository;

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
		return res;
	}

	@Override
	public List<ApisMessage> retrieveApisMessage(MessageStatus messageStatus) {
		return apisMsgRepository.findByStatus(messageStatus);

	}

	@Override
	public void updateApisMessage(ApisMessage message,
			MessageStatus messageStatus) {
		ApisMessage apisMessage = apisMsgRepository.findOne(message.getId());
		if (apisMessage != null) {
			apisMessage.setStatus(messageStatus);
		}
	}

	// @Scheduled(fixedDelay = 4000)
	@Transactional
	public void runningRuleEngine() {
		System.out.println(new Date() + " a fixed delay running");
		List<ApisMessage> apisMessageList = retrieveApisMessage(MessageStatus.LOADED);
		System.out
				.println("retrieved message size-> " + apisMessageList.size());
		
		List<HitsSummary> hitsSummaryList = new ArrayList<HitsSummary>();
		if (apisMessageList.size() > 0) {
			for (ApisMessage apisMessage : apisMessageList) {
				RuleServiceResult ruleRunningResult = analyzeApisMessage(apisMessage);
				RuleExecutionStatistics ruleExeStatus = ruleRunningResult
						.getExecutionStatistics();
				System.out.println("\nTotal Rules fired. --> "
						+ ruleExeStatus.getTotalRulesFired());

				@SuppressWarnings("unchecked")
				List<RuleHitDetail> results = (List<RuleHitDetail>) ruleRunningResult
						.getResultList();
				Iterator<RuleHitDetail> iter = results.iterator();
				while (iter.hasNext()) {
					RuleHitDetail ruleDetail = iter.next();

					String[] hitReasons = ruleDetail.getHitReasons();

					StringBuilder sb = new StringBuilder();
					for (String hitReason : hitReasons) {
						sb.append(hitReason);
						sb.append("$$$");
					}

					HitsSummary hitsSummary = new HitsSummary();
					hitsSummary.setTravelerId(ruleDetail.getTravelerId());
					hitsSummary.setCreateDate(new Date());

					HitDetail hitDetail = new HitDetail();
					hitDetail.setRuleId(ruleDetail.getUdrRuleId());
					hitDetail.setRuleConditions(sb.toString());
					hitDetail.setCreateDate(new Date());
					hitDetail.setParent(hitsSummary);

					List<HitDetail> detailList = new ArrayList<HitDetail>();

					detailList.add(hitDetail);
					hitsSummary.setHitdetails(detailList);

					hitsSummaryList.add(hitsSummary);
					
				}

				// updateApisMessage(apisMessage, MessageStatus.ANALYZED);
			}
			hitsSummaryRepository.save(hitsSummaryList);
		}
	}
}
