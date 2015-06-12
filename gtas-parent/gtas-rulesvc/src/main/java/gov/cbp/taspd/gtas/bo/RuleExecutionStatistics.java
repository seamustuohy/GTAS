package gov.cbp.taspd.gtas.bo;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A Java bean class that keeps useful statistics of a rule engine run (i.e. session).
 * @author GTAS3 (AB)
 *
 */
public class RuleExecutionStatistics implements Serializable{
    /**
	 * serial version UID
	 */
	private static final long serialVersionUID = 1396889006615410141L;
	
	private int totalRulesFired;
    private int totalObjectsModified;
    final private List<String> ruleFiringSequence;
    final private List<String> modifiedObjectClassNameList;
    /**
     * Constructor to initialize the member lists.
     */
    public RuleExecutionStatistics(){
    	this.ruleFiringSequence = new LinkedList<String>();
    	this.modifiedObjectClassNameList = new LinkedList<String>();
    }
    /**
     * @return add one to the totalRulesFired and return the result.
     */
    public int incrementTotalRulesFired(){
    	return ++totalRulesFired;
    }
    /**
     * @return add one to the totalRulesFired and return the result.
     */
    public int incrementTotalObjectsModified(){
    	return ++totalObjectsModified;
    }
    /**
     * Adds the fired rule name to the sequence of fired rules.
     * @param ruleName the name of the rule
     */
    public void addRuleFired(final String ruleName){
    	ruleFiringSequence.add(ruleName);
    }
    /**
     * Adds the fired rule name to the sequence of fired rules.
     * @param ruleName the name of the rule
     */
    public void addModifiedObject(final Object modifiedObject){
    	modifiedObjectClassNameList.add(modifiedObject.getClass().getName());
    }
	/**
	 * @return the totalRulesFired
	 */
	public int getTotalRulesFired() {
		return totalRulesFired;
	}
	/**
	 * @param totalRulesFired the totalRulesFired to set
	 */
	public void setTotalRulesFired(int totalRulesFired) {
		this.totalRulesFired = totalRulesFired;
	}
	/**
	 * @return the totalObjectsModified
	 */
	public int getTotalObjectsModified() {
		return totalObjectsModified;
	}
	/**
	 * @param totalObjectsModified the totalObjectsModified to set
	 */
	public void setTotalObjectsModified(int totalObjectsModified) {
		this.totalObjectsModified = totalObjectsModified;
	}
	/**
	 * @return the ruleFiringSequence
	 */
	public List<String> getRuleFiringSequence() {
		return Collections.unmodifiableList(ruleFiringSequence);
	}
	/**
	 * @return the modifiedObjectClassNameList
	 */
	public List<String> getModifiedObjectClassNameList() {
		return Collections.unmodifiableList(modifiedObjectClassNameList);
	}    
}
