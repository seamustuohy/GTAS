package gov.gtas.model.udr;

import gov.gtas.model.BaseEntity;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Rule object corresponding to a Drools rule.<br>
 * (This is derived for a parent UDR rule.)
 */
@Entity
@Table(name = "rule", catalog = "gtas")
public class Rule extends BaseEntity {

	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 6208917106485574650L;
		
	@Column(name="RULE_INDX")
	private int ruleIndex;
	
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="UDR_RULE_REF", nullable=false, referencedColumnName="id")     
    private UdrRule parent;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="KB_REF", nullable=true, referencedColumnName="id")     
    private KnowledgeBase knowledgeBase;
    
	@OneToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
	@OrderColumn(name="COND_SEQ")
	private List<RuleCond> ruleConds;

	/**
     * Constructor to be used by JPA EntityManager.
     */
	public Rule() {
	}

	public Rule(UdrRule parent, int ruleIndex, KnowledgeBase kb) {
		this.parent = parent;
		this.ruleIndex = ruleIndex;
		this.knowledgeBase = kb;
	}

	/**
	 * @return the ruleIndex
	 */
	public int getRuleIndex() {
		return ruleIndex;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(UdrRule parent) {
		this.parent = parent;
	}

	/**
     * adds a condition to this rule.
     * @param cond the condition to add.
     */
	public void addConditionToRule(RuleCond cond){
    	if(ruleConds == null){
    		ruleConds = new LinkedList<RuleCond>();
    	}
    	//set up the child keys
    	cond.refreshParentRuleId(this.getId());
    	List<RuleCond> ruleConditions = this.ruleConds;
    	ruleConditions.add(cond);
    	this.ruleConds = ruleConditions;
    }
    /**
     * Removes all conditions from this rule.
     */
    public void removeAllConditions(){
    	this.ruleConds = null;
    }
//    /**
//	 * @return the metaData
//	 */
//	public RuleMeta getMetaData() {
//		return metaData;
//	}

//	/**
//	 * @param metaData the metaData to set
//	 */
//	public void setMetaData(RuleMeta metaData) {
//		this.metaData = metaData;
//		if(this.id != null){
//		   metaData.setId(this.id);
//		}
//	}

//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "EDIT_DT", nullable = false, length = 19)
//	public Date getEditDt() {
//		return this.editDt;
//	}
//
//	public void setEditDt(Date editDt) {
//		this.editDt = editDt;
//	}
//
//	
//	/**
//	 * @return the editedBy
//	 */
//	public User getEditedBy() {
//		return editedBy;
//	}
//
//	/**
//	 * @param editedBy the editedBy to set
//	 */
//	public void setEditedBy(User editedBy) {
//		this.editedBy = editedBy;
//	}
//
//	/**
//	 * @return the deleted
//	 */
//	public YesNoEnum getDeleted() {
//		return deleted;
//	}
//
//	/**
//	 * @param deleted the deleted to set
//	 */
//	public void setDeleted(YesNoEnum deleted) {
//		this.deleted = deleted;
//	}

	/**
	 * @return the ruleConds
	 */
	public List<RuleCond> getRuleConds() {
		return ruleConds;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(id);
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
//		equalsBuilder.append(getVersion(), other.getVersion());
//		equalsBuilder.append(kbRef, other.kbRef);
		
//		//date equality up to seconds
//		if(!DateCalendarUtils.dateRoundedEquals(editDt,  other.editDt)){
//						return false;
//		}
		
		return equalsBuilder.isEquals();
	}

}
