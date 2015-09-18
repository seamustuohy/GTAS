package gov.gtas.bo;

import gov.gtas.enumtype.HitTypeEnum;
import gov.gtas.model.Flight;
import gov.gtas.model.Passenger;
import gov.gtas.model.lookup.PassengerTypeCode;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RuleHitDetail implements Serializable, Cloneable {
	public static final String HIT_REASON_SEPARATOR = "///";
	/**
	 * serial version UID.
	 */
	private static final long serialVersionUID = 2946626283174855377L;

	private String hitRule;

	private Long udrRuleId;

	private Long ruleId;

	private Long flightId;

	private Long passengerId;

	private String passengerType;

	private String passengerName;

	private String hitType;

	private int hitCount;

	private int ruleHitCount;

	private String[] hitReasons;

	@JsonIgnore
	private String title;

	@JsonIgnore
	private String description;

	@JsonIgnore
	private Passenger passenger;

	/**
	 * This constructor is used when creating a hit detail object as a result of
	 * a UDR rule hit.
	 * 
	 * @param ruleId
	 *            a numeric rule Id (can be null)
	 * @param ruleTitle
	 *            the name of the DRL rule(Rule.getName()).
	 * @param passenger
	 *            the Passenger object that matched.
	 * @param flight
	 *            the flight object that matched.
	 * @param cause
	 *            the reason for the match.
	 */
	public RuleHitDetail(final Long udrId, final Long ruleId,
			final String ruleTitle, final Passenger passenger,
			final Flight flight, final String cause) {
		this.udrRuleId = udrId;
		this.ruleId = ruleId;
		this.title = ruleTitle;
		this.description = ruleTitle;
		this.hitRule = ruleTitle + "(" + udrId + ")";
		this.passengerId = passenger.getId();
		this.passengerType = decodePassengerTypeName(passenger
				.getPassengerType());
		this.passengerName = passenger.getFirstName() + " "
				+ passenger.getLastName();
		this.hitReasons = cause.split(HIT_REASON_SEPARATOR);
		if (flight != null) {
			this.flightId = flight.getId();
		}
		this.passenger = passenger;
		this.hitType = HitTypeEnum.R.toString();
		this.hitCount = 1;
		this.ruleHitCount = 1;
	}

	/**
	 * This constructor is used when creating a hit detail object as a result of
	 * a watch list hit.
	 * 
	 * @param ruleId
	 *            a numeric rule Id (can be null)
	 * @param ruleTitle
	 *            the name of the DRL rule(Rule.getName()).
	 * @param passenger
	 *            the Passenger object that matched.
	 * @param flight
	 *            the flight object that matched.
	 * @param cause
	 *            the reason for the match.
	 */
	public RuleHitDetail(final Long watchlistItemId, final String hitType,
			final Passenger passenger, final String cause) {
		this.udrRuleId = null;
		this.ruleId = watchlistItemId;
		switch (hitType) {
		case "D":
			this.title = "Document List Rule #" + watchlistItemId;
			this.hitType = HitTypeEnum.D.toString();
			break;
		case "P":
			this.title = "Passenger List Rule #" + watchlistItemId;
			this.hitType = HitTypeEnum.P.toString();
			break;
		default:
			break;
		}
		this.description = this.title;
		this.hitRule = this.title + "(" + watchlistItemId + ")";
		this.passengerId = passenger.getId();
		this.passengerType = decodePassengerTypeName(passenger
				.getPassengerType());
		this.passengerName = passenger.getFirstName() + " "
				+ passenger.getLastName();
		this.hitReasons = cause.split(HIT_REASON_SEPARATOR);
		this.passenger = passenger;
		this.hitCount = 1;
	}

	/**
	 * This constructor is used when the knowledge base is built from UDR.
	 * 
	 * @param ruleId
	 *            the id of the GTAS RULE DB object. An UDR can generate
	 *            multiple RULE objects.
	 * @param passenger
	 *            the Passenger object that matched.
	 * @param flight
	 *            the flight object that matched.
	 * 
	 */
	// public RuleHitDetail(final Long ruleId, final Passenger passenger,
	// final Flight flight) {
	// this.ruleId = ruleId;
	// this.passengerId = passenger.getId();
	// this.passengerType = decodePassengerTypeName(passenger
	// .getPassengerType());
	// this.passengerName = passenger.getFirstName() + " "
	// + passenger.getLastName();
	// if (flight != null) {
	// this.flightId = flight.getId();
	// }
	// }

	/**
	 * Converts the passenger type code to a friendly name.
	 * 
	 * @param typ
	 *            the type code.
	 * @return the decoded type name.
	 */
	private String decodePassengerTypeName(String typ) {
		String ret = typ;
		for (PassengerTypeCode typeEnum : PassengerTypeCode.values()) {
			if (typ.equalsIgnoreCase(typeEnum.name())) {
				ret = typeEnum.getPassengerTypeName();
			}
		}
		return ret;
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
	public Long getUdrRuleId() {
		return udrRuleId;
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
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the flightId
	 */
	public Long getFlightId() {
		return flightId;
	}

	/**
	 * @param flightId
	 *            the flightId to set
	 */
	public void setFlightId(Long flightId) {
		this.flightId = flightId;
	}

	/**
	 * @return the hitType
	 */
	public String getHitType() {
		return hitType;
	}

	/**
	 * @param hitType
	 *            the hitType to set
	 */
	public void setHitType(String hitType) {
		this.hitType = hitType;
	}

	/**
	 * @return the ruleId
	 */
	public Long getRuleId() {
		return ruleId;
	}

	/**
	 * @return the hitCount
	 */
	public int getHitCount() {
		return hitCount;
	}

	/**
	 * @param hitCount
	 *            the hitCount to set
	 */
	public void incrementHitCount() {
		this.hitCount++;
	}

	/**
	 * @return the ruleHitCount
	 */
	public int getRuleHitCount() {
		return ruleHitCount;
	}

	/**
	 * @param ruleHitCount
	 *            the ruleHitCount to set
	 */
	public void incrementRuleHitCount() {
		this.ruleHitCount++;
	}

	/**
	 * @return the passenger
	 */
	public Passenger getPassenger() {
		return passenger;
	}

	/**
	 * @param passenger
	 *            the passenger to set
	 */
	public void setPassenger(Passenger passenger) {
		this.passenger = passenger;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public RuleHitDetail clone() throws CloneNotSupportedException {
		return (RuleHitDetail) super.clone();
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.udrRuleId, this.ruleId, this.passengerId,
				this.flightId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		final RuleHitDetail other = (RuleHitDetail) obj;
		// return Objects.equals(this.udrRuleId, other.udrRuleId)
		// && Objects.equals(this.ruleId, other.ruleId)
		// && Objects.equals(this.passengerId, other.passengerId)
		// && Objects.equals(this.flightId, other.flightId);
		return Objects.equals(this.passengerId, other.passengerId)
				&& Objects.equals(this.flightId, other.flightId);
	}
}
