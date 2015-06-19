package gov.gtas.model.udr;

import gov.gtas.model.BaseEntity;
import gov.gtas.model.User;
import gov.gtas.util.DateCalendarUtils;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
	 * serial version UID
	 */
	private static final long serialVersionUID = 6208917106485574650L;
	
	@Version
	private Long version;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "DEL_FLAG", nullable = false, length = 1)
	private YesNoEnum deleted;
	
	@Column(name = "EDIT_DT", nullable = false)
	private Date editDt;

	@OneToOne(cascade=CascadeType.ALL, mappedBy="parent")
	private RuleMeta metaData;
	
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="KB_REF", nullable=true, referencedColumnName="id")     
    private KnowledgeBase knowledgeBase;
    
	@OneToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
	private List<RuleCond> ruleConds;

    @ManyToOne
    @JoinColumn(name="EDITED_BY", referencedColumnName="user_id", nullable = false)     
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

	public Rule(long id, YesNoEnum deleted, KnowledgeBase kb, User editedBy,
			Date editDt) {
		this.id = id;
		this.deleted = deleted;
		this.knowledgeBase = kb;
		this.editedBy = editedBy;
		this.editDt = editDt;
	}

	/* (non-Javadoc)
	 * @see gov.gtas.model.BaseEntity#setId(java.lang.Long)
	 */
//	@Override
//	public void setId(Long id) {
//		super.setId(id);
//		//set the id for rule meta object
//		if(this.metaData != null){
//			this.metaData.setId(id);
//		}
//	}

    /**
	 * @return the metaData
	 */
	public RuleMeta getMetaData() {
		return metaData;
	}

	/**
	 * @param metaData the metaData to set
	 */
	public void setMetaData(RuleMeta metaData) {
		this.metaData = metaData;
		if(this.id != null){
		   metaData.setId(this.id);
		}
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EDIT_DT", nullable = false, length = 19)
	public Date getEditDt() {
		return this.editDt;
	}

	public void setEditDt(Date editDt) {
		this.editDt = editDt;
	}

	
	/**
	 * @return the editedBy
	 */
	public User getEditedBy() {
		return editedBy;
	}

	/**
	 * @param editedBy the editedBy to set
	 */
	public void setEditedBy(User editedBy) {
		this.editedBy = editedBy;
	}

	/**
	 * @return the deleted
	 */
	public YesNoEnum getDeleted() {
		return deleted;
	}

	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(YesNoEnum deleted) {
		this.deleted = deleted;
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
		equalsBuilder.append(getVersion(), other.getVersion());
		equalsBuilder.append(deleted, other.deleted);
//		equalsBuilder.append(kbRef, other.kbRef);
		equalsBuilder.append(editedBy, other.editedBy);
		
		//date equality up to seconds
		if(!DateCalendarUtils.dateRoundedEquals(editDt,  other.editDt)){
						return false;
		}
		
		return equalsBuilder.isEquals();
	}

    public Long getVersion() {
        return version;
    }
}
