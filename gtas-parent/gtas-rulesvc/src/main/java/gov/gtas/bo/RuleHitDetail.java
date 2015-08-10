package gov.gtas.bo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import gov.gtas.model.Passenger;

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
	private long passengerId;
	private String passengerType;
	private String passengerName;
	private String[] hitReasons;
	
	public RuleHitDetail(final long udrRuleId, final int engineRuleIndex, final String ruleTitle, final Passenger passenger, final String cause){
		this.udrRuleId = udrRuleId;
		this.engineRuleIndex = engineRuleIndex;
		this.hitRule = ruleTitle+"("+udrRuleId+"/"+engineRuleIndex+")";
		this.passengerId = passenger.getId();
		this.passengerType = passenger.getPassengerType();
		this.passengerName = passenger.getFirstName()+" "+passenger.getLastName();
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
	 * @return the passengerId
	 */
	public long getPassengerId() {
		return passengerId;
	}

	/**
	 * @return the passengerType
	 */
	public String getPassengerType() {
		return passengerType;
	}

	/**
	 * @return the passengerName
	 */
	public String getPassengerName() {
		return passengerName;
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
	 * @param passenger
	 * @return
	 */
	public static RuleHitDetail createRuleHitDetail(Long udrRuleId, int engineRuleIndex, String title, Passenger passenger, String cause){
		RuleHitDetail ret = new RuleHitDetail(udrRuleId, engineRuleIndex, title, passenger, cause);
		return ret;
	}
}
