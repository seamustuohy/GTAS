package gov.gtas.bo;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class CompositeRuleServiceResult implements RuleServiceResult, Serializable {
    private static final long serialVersionUID = 6373119898883595702L;
    
    private List<RuleHitDetail> resultList;
    private RuleExecutionStatistics executionStatistics;
    private RuleExecutionStatistics wlExecutionStatistics;

    public CompositeRuleServiceResult(RuleServiceResult udrResults,
            RuleServiceResult watchlistResults) {
        if(udrResults != null){
            this.resultList = udrResults.getResultList();
            this.executionStatistics = udrResults.getExecutionStatistics();
        }else{
            this.resultList = new LinkedList<RuleHitDetail>();
        }
        if(watchlistResults != null){
            this.resultList.addAll(watchlistResults.getResultList());
            this.wlExecutionStatistics = watchlistResults.getExecutionStatistics();
            if(this.executionStatistics ==null){
                this.executionStatistics = this.wlExecutionStatistics;
            }
        }
    }

    @Override
    public List<RuleHitDetail> getResultList() {
        return this.resultList;
    }

    @Override
    public RuleExecutionStatistics getExecutionStatistics() {
        return this.executionStatistics;
    }

    /**
     * @return the wlExecutionStatistics
     */
    public RuleExecutionStatistics getWlExecutionStatistics() {
        return wlExecutionStatistics;
    }

}
