package gov.gtas.bo;

import java.util.List;
/**
 * Interface definition for rule engine execution result objects.
 * @author GTAS3 (AB)
 *
 */
public interface RuleServiceResult {
    /**
     * Gets the list of Passenger IDs "hit" by the rules.
     * @return the list of hits.
     */
  List<RuleHitDetail> getResultList();
  /**
   * Gets the statistics of the rule engine execution.
   * @return rule engine execution statistics.
   */
  RuleExecutionStatistics getExecutionStatistics();
}
