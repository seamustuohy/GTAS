package gov.gtas.bo;

import java.io.Serializable;
import java.util.List;

public class BasicRuleServiceResult implements RuleServiceResult, Serializable {
	private static final long serialVersionUID = 6373119898883595702L;
	
	private List<RuleHitDetail> resultList;
	private RuleExecutionStatistics executionStatistics;

	public BasicRuleServiceResult(List<RuleHitDetail> resultList,
			RuleExecutionStatistics executionStatistics) {
		this.resultList = resultList;
		this.executionStatistics = executionStatistics;
	}

	@Override
	public List<RuleHitDetail> getResultList() {
		return this.resultList;
	}

	@Override
	public RuleExecutionStatistics getExecutionStatistics() {
		return this.executionStatistics;
	}

}
