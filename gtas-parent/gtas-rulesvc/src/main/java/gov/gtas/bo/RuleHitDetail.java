package gov.gtas.bo;

import java.io.Serializable;

public class RuleHitDetail implements Serializable {

	/**
	 * serial version UID.
	 */
	private static final long serialVersionUID = 2946626283174855377L;

	private long udrRuleId;
	private int engineRuleIndex;
	private long passengerId;
	
	public RuleHitDetail(final long udrRuleId, final int engineRuleIndex, final long passengerId){
		this.udrRuleId = udrRuleId;
		this.engineRuleIndex = engineRuleIndex;
		this.passengerId = passengerId;
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
	
}
