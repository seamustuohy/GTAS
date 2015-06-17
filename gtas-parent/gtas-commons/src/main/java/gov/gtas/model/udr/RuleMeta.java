package gov.gtas.model.udr;

import gov.gtas.model.BaseEntity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * RuleMeta
 */
@Entity
@Table(name = "rule_meta", catalog = "gtas")
public class RuleMeta extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 384462394390643572L;
	private String title;
	private String description;
	private Date startDt;
	private Date endDt;
	private Character enabled;
	private Character priorityHigh;
	private Character hitSharing;

	public RuleMeta() {
	}

	public RuleMeta(long id, Date startDt, Date endDt) {
		this.id = id;
		this.startDt = startDt;
		this.endDt = endDt;
	}

	public RuleMeta(long id, String title, String description, Date startDt,
			Date endDt, Character enabled, Character priorityHigh,
			Character hitSharing) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.startDt = startDt;
		this.endDt = endDt;
		this.enabled = enabled;
		this.priorityHigh = priorityHigh;
		this.hitSharing = hitSharing;
	}

	@Column(name = "TITLE", length = 64)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "DESCRIPTION", length = 1024)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DT", nullable = false, length = 19)
	public Date getStartDt() {
		return this.startDt;
	}

	public void setStartDt(Date startDt) {
		this.startDt = startDt;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_DT", nullable = false, length = 19)
	public Date getEndDt() {
		return this.endDt;
	}

	public void setEndDt(Date endDt) {
		this.endDt = endDt;
	}

	@Column(name = "ENABLED", length = 1)
	public Character getEnabled() {
		return this.enabled;
	}

	public void setEnabled(Character enabled) {
		this.enabled = enabled;
	}

	@Column(name = "PRIORITY_HIGH", length = 1)
	public Character getPriorityHigh() {
		return this.priorityHigh;
	}

	public void setPriorityHigh(Character priorityHigh) {
		this.priorityHigh = priorityHigh;
	}

	@Column(name = "HIT_SHARING", length = 1)
	public Character getHitSharing() {
		return this.hitSharing;
	}

	public void setHitSharing(Character hitSharing) {
		this.hitSharing = hitSharing;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(title);
		hashCodeBuilder.append(description);
		hashCodeBuilder.append(startDt);
		hashCodeBuilder.append(endDt);
		hashCodeBuilder.append(enabled);
		hashCodeBuilder.append(priorityHigh);
		hashCodeBuilder.append(hitSharing);
		return hashCodeBuilder.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof RuleMeta)) {
			return false;
		}
		RuleMeta other = (RuleMeta) obj;
		EqualsBuilder equalsBuilder = new EqualsBuilder();
		equalsBuilder.append(title, other.title);
		equalsBuilder.append(description, other.description);
		equalsBuilder.append(startDt, other.startDt);
		equalsBuilder.append(endDt, other.endDt);
		equalsBuilder.append(enabled, other.enabled);
		equalsBuilder.append(priorityHigh, other.priorityHigh);
		equalsBuilder.append(hitSharing, other.hitSharing);
		return equalsBuilder.isEquals();
	}

}
