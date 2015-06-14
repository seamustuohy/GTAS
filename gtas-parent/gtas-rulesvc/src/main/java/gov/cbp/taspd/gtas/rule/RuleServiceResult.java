package gov.cbp.taspd.gtas.rule;

import gov.cbp.taspd.gtas.bo.RuleExecutionStatistics;

import java.util.List;
/**
 * Interface definition for rule engine execution result objects.
 * @author GTAS3 (AB)
 *
 */
public interface RuleServiceResult {
	/**
	 * Gets the list of objects "hit" by the rules.
	 * @return the list of hits.
	 */
  List<?> getResultList();
  /**
   * Gets the statistics of the rule engine execution.
   * @return rule engine execution statistics.
   */
  RuleExecutionStatistics getExecutionStatistics();
}
