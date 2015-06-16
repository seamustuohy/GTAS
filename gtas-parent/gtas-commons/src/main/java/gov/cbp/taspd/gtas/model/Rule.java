package gov.cbp.taspd.gtas.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Rule
 */
@Entity
@Table(name = "rule", catalog = "gtas")
public class Rule extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6208917106485574650L;
	private int version;
	private Character deleted;
	private String kbRef;
	private String editedBy;
	private Date editDt;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "rule")
	private List<RuleCond> ruleConds;

	public Rule() {
	}

	public Rule(long id, Date editDt) {
		this.id = id;
		this.editDt = editDt;
	}

	public Rule(long id, Character deleted, String kbRef, String editedBy,
			Date editDt, int version) {
		this.id = id;
		this.deleted = deleted;
		this.kbRef = kbRef;
		this.editedBy = editedBy;
		this.editDt = editDt;
		this.version = version;
	}

	@Version
	@Column(name = "VERSION")
	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Column(name = "DELETED", length = 1)
	public Character getDeleted() {
		return this.deleted;
	}

	public void setDeleted(Character deleted) {
		this.deleted = deleted;
	}

	@Column(name = "KB_REF", length = 16777215)
	public String getKbRef() {
		return this.kbRef;
	}

	public void setKbRef(String kbRef) {
		this.kbRef = kbRef;
	}

	@Column(name = "EDITED_BY", length = 32)
	public String getEditedBy() {
		return this.editedBy;
	}

	public void setEditedBy(String editedBy) {
		this.editedBy = editedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EDIT_DT", nullable = false, length = 19)
	public Date getEditDt() {
		return this.editDt;
	}

	public void setEditDt(Date editDt) {
		this.editDt = editDt;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(version);
		hashCodeBuilder.append(deleted);
		hashCodeBuilder.append(kbRef);
		hashCodeBuilder.append(editedBy);
		hashCodeBuilder.append(editDt);
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
		if (!(obj instanceof Rule)) {
			return false;
		}
		Rule other = (Rule) obj;
		EqualsBuilder equalsBuilder = new EqualsBuilder();
		equalsBuilder.append(version, other.version);
		equalsBuilder.append(deleted, other.deleted);
		equalsBuilder.append(kbRef, other.kbRef);
		equalsBuilder.append(editedBy, other.editedBy);
		equalsBuilder.append(editDt, other.editDt);
		return equalsBuilder.isEquals();
	}

}
