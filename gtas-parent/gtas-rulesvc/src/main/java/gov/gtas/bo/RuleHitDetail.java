package gov.gtas.bo;

import java.io.Serializable;

public class RuleHitDetail implements Serializable {

	/**
	 * serial version UID.
	 */
	private static final long serialVersionUID = 2946626283174855377L;

	private long ruleId;
	private long passengerId;
	
	public RuleHitDetail(final long ruleId, final long passengerId){
		this.ruleId = ruleId;
		this.passengerId = passengerId;
	}

	/**
	 * @return the ruleId
	 */
	public long getRuleId() {
		return ruleId;
	}

	/**
	 * @return the passengerId
	 */
	public long getPassengerId() {
		return passengerId;
	}
	
}
