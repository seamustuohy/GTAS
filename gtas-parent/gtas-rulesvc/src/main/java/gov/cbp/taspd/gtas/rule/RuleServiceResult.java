package gov.cbp.taspd.gtas.rule;

import gov.cbp.taspd.gtas.bo.RuleExecutionStatistics;

import java.util.List;

public interface RuleServiceResult {
  List<?> getResultList();
  RuleExecutionStatistics getExecutionStatistics();
}
