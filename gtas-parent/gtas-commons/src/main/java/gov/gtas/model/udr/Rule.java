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
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Rule object corresponding to a Drools rule.<br>
 * (This is derived for a parent UDR rule.)
 */
@Entity
@Table(name = "rule")
public class Rule extends BaseEntity {

	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 6208917106485574650L;
	
	private static final  String RULE_CRITERIA_SEPARATOR = "\n";
		
	@Column(name="RULE_INDX")
	private int ruleIndex;
	
    @ManyToOne(cascade = CascadeType.MERGE, fetch=FetchType.EAGER)
    @JoinColumn(name="UDR_RULE_REF", nullable=false, referencedColumnName="id")     
    private UdrRule parent;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="KB_REF", nullable=true, referencedColumnName="id")     
    private KnowledgeBase knowledgeBase;
    
	@OneToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER, mappedBy="parent")
	@OrderColumn(name="COND_SEQ")
	private List<RuleCond> ruleConds;
	
	@Column(name="RULE_CRITERIA", nullable=true, length = 1024)
	private String combinedRuleCriteria;

	@Column(name="RULE_DRL", nullable=true, length = 4000)
	private String ruleDrl;

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
    	this.ruleConds.add(cond);
    }
    /**
     * Removes all conditions from this rule.
     */
    public void removeAllConditions(){
    	this.ruleConds = null;
    }

	/**
	 * @return the ruleConds
	 */
	public List<RuleCond> getRuleConds() {
		return ruleConds;
	}

	/**
	 * @return the ruleDrl
	 */
	public String getRuleDrl() {
		return ruleDrl;
	}

	/**
	 * @param ruleDrl the ruleDrl to set
	 */
	public void setRuleDrl(String ruleDrl) {
		this.ruleDrl = ruleDrl;
	}

	/**
	 * @return the ruleCriteria
	 */
	@Transient
	public String[] getRuleCriteria() {
		return combinedRuleCriteria == null? new String[0]
				:combinedRuleCriteria.split(RULE_CRITERIA_SEPARATOR);
	}

	/**
	 * @param ruleCriteria the ruleCriteria to set
	 */
	public void addRuleCriteria(String ruleCriteria) {
		if(StringUtils.isEmpty(this.combinedRuleCriteria)){
			this.combinedRuleCriteria = ruleCriteria;
		}else {
			this.combinedRuleCriteria += RULE_CRITERIA_SEPARATOR + ruleCriteria;
		}
	}

	/**
	 * @return the knowledgeBase
	 */
	public KnowledgeBase getKnowledgeBase() {
		return knowledgeBase;
	}

	/**
	 * @param knowledgeBase the knowledgeBase to set
	 */
	public void setKnowledgeBase(KnowledgeBase knowledgeBase) {
		this.knowledgeBase = knowledgeBase;
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
		
		return equalsBuilder.isEquals();
	}

}
