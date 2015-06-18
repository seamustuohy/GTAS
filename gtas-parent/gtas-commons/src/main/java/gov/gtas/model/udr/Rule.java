package gov.gtas.model.udr;

import gov.gtas.model.BaseEntity;
import gov.gtas.model.User;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Rule
 */
@Entity
@Table(name = "rule", catalog = "gtas")
public class Rule extends BaseEntity {

	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 6208917106485574650L;
	
	private Character deleted;
	private Date editDt;

	@OneToOne
	@JoinColumn(name="ID", referencedColumnName="RULE_ID")
	private RuleMeta metaData;
	
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="KB_REF", referencedColumnName="id")     
    private KnowledgeBase knowledgeBase;
    
	@OneToMany(cascade = CascadeType.ALL)
	private List<RuleCond> ruleConds;

    @ManyToOne
    @JoinColumn(name="EDITED_BY", referencedColumnName="user_id")     
    private User editedBy;

    /**
     * Constructor to be used by JPA EntityManager.
     */
	public Rule() {
	}

	public Rule(long id, Date editDt) {
		this.id = id;
		this.editDt = editDt;
	}

	public Rule(long id, Character deleted, KnowledgeBase kb, User editedBy,
			Date editDt) {
		this.id = id;
		this.deleted = deleted;
		this.knowledgeBase = kb;
		this.editedBy = editedBy;
		this.editDt = editDt;
	}


	@Column(name = "DELETED", length = 1)
	public Character getDeleted() {
		return this.deleted;
	}

	public void setDeleted(Character deleted) {
		this.deleted = deleted;
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
		hashCodeBuilder.append(id);
//		hashCodeBuilder.append(version);
//		hashCodeBuilder.append(deleted);
//		hashCodeBuilder.append(kbRef);
//		hashCodeBuilder.append(editedBy);
//		hashCodeBuilder.append(editDt);
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
		equalsBuilder.append(id, other.id);
//		equalsBuilder.append(version, other.version);
//		equalsBuilder.append(deleted, other.deleted);
//		equalsBuilder.append(kbRef, other.kbRef);
//		equalsBuilder.append(editedBy, other.editedBy);
//		equalsBuilder.append(editDt, other.editDt);
		return equalsBuilder.isEquals();
	}

}
