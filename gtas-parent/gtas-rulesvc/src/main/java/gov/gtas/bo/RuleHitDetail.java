package gov.gtas.bo;

import gov.gtas.model.Pax;
import gov.gtas.model.Traveler;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RuleHitDetail implements Serializable {
    public static final String HIT_REASON_SEPARATOR = "///";
	/**
	 * serial version UID.
	 */
	private static final long serialVersionUID = 2946626283174855377L;
    
	@JsonIgnore
	private long udrRuleId;
	
	@JsonIgnore
	private int engineRuleIndex;
	
	private String hitRule;
	private long travelerId;
	private String travelerType;
	private String travelerName;
	private String[] hitReasons;
	
	public RuleHitDetail(final long udrRuleId, final int engineRuleIndex, final String ruleTitle, final Traveler traveler, final String cause){
		this.udrRuleId = udrRuleId;
		this.engineRuleIndex = engineRuleIndex;
		this.hitRule = ruleTitle+"("+udrRuleId+"/"+engineRuleIndex+")";
		this.travelerId = traveler.getId();
		this.travelerType = traveler instanceof Pax ? "Passenger":"Crew";
		this.travelerName = traveler.getFirstName()+" "+traveler.getLastName();
		this.hitReasons = cause.split(HIT_REASON_SEPARATOR);
	}

	/**
	 * @return the hitRule
	 */
	public String getHitRule() {
		return hitRule;
	}

	/**
	 * @return the udrRuleId
	 */
	public long getUdrRuleId() {
		return udrRuleId;
	}


	/**
	 * @return the engineRuleIndex
	 */
	public int getEngineRuleIndex() {
		return engineRuleIndex;
	}


	/**
	 * @return the travelerId
	 */
	public long getTravelerId() {
		return travelerId;
	}

	/**
	 * @return the travelerType
	 */
	public String getTravelerType() {
		return travelerType;
	}

	/**
	 * @return the travelerName
	 */
	public String getTravelerName() {
		return travelerName;
	}

	/**
	 * @return the hitReasons
	 */
	public String[] getHitReasons() {
		return hitReasons;
	}

	/**
	 * Factory method that creates a RuleHitDetailObject.
	 * @param udr the 
	 * @param rule
	 * @param traveler
	 * @return
	 */
	public static RuleHitDetail createRuleHitDetail(Long udrRuleId, int engineRuleIndex, String title, Traveler traveler, String cause){
		RuleHitDetail ret = new RuleHitDetail(udrRuleId, engineRuleIndex, title, traveler, cause);
		return ret;
	}
}
