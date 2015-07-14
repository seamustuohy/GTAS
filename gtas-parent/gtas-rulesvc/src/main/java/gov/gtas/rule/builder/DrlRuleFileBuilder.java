package gov.gtas.rule.builder;

import static gov.gtas.rule.builder.RuleTemplateConstants.ACTION_PASSENGER_HIT;
import static gov.gtas.rule.builder.RuleTemplateConstants.GLOBAL_RESULT_DECLARATION;
import static gov.gtas.rule.builder.RuleTemplateConstants.IMPORT_PREFIX;
import static gov.gtas.rule.builder.RuleTemplateConstants.NEW_LINE;
import static gov.gtas.rule.builder.RuleTemplateConstants.RULE_PACKAGE_NAME;

import java.util.List;

import gov.gtas.bo.RuleHitDetail;
import gov.gtas.model.Document;
import gov.gtas.model.Flight;
import gov.gtas.model.Traveler;
import gov.gtas.model.udr.CondValue;
import gov.gtas.model.udr.Rule;
import gov.gtas.model.udr.RuleCond;
import gov.gtas.model.udr.UdrRule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class DrlRuleFileBuilder {
	/*
	 * The logger for the DrlRuleBuilder.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(DrlRuleFileBuilder.class);
	
	private StringBuilder stringBuilder;
	
	//Rule level state variables
	private StringBuilder passengerBuilder;
	private StringBuilder flightBuilder;
	private StringBuilder documentBuilder;

	public DrlRuleFileBuilder(){
		this.stringBuilder = new StringBuilder();
		addPackageAndImport();
		//add the global result declaration;
		this.stringBuilder.append(GLOBAL_RESULT_DECLARATION);
	}  
	public DrlRuleFileBuilder addRule(final UdrRule udrRule){
		for(Rule rule: udrRule.getEngineRules()){
			passengerBuilder = null;
			flightBuilder = null;
			documentBuilder = null;
			addRuleHeader(udrRule, rule);
			for(RuleCond cond:rule.getRuleConds()){
				addRuleCondition(cond);
			}
			if(passengerBuilder !=null){
				this.stringBuilder.append(passengerBuilder.append(")\n").toString());
			}
			if(documentBuilder !=null){
				this.stringBuilder.append(documentBuilder.append(")\n").toString());
			}
			if(flightBuilder !=null){
				this.stringBuilder.append(flightBuilder.append(")\n").toString());
			}
			addRuleAction(rule);
		}
		return this;
	}
	public String build(){
		
		return this.stringBuilder.toString();
	}
	private void addPackageAndImport(){
		this.stringBuilder.append(RULE_PACKAGE_NAME)
		.append(IMPORT_PREFIX).append(RuleHitDetail.class.getName()).append(";").append(NEW_LINE)
		.append(IMPORT_PREFIX).append(Traveler.class.getName()).append(";").append(NEW_LINE)
		.append(IMPORT_PREFIX).append(Flight.class.getName()).append(";").append(NEW_LINE)
		.append(IMPORT_PREFIX).append(Document.class.getName()).append(";").append(NEW_LINE)
		.append("\n")
        .append(GLOBAL_RESULT_DECLARATION);
	}
    private void addRuleHeader(UdrRule parent, Rule rule){
    	this.stringBuilder.append("rule \"")
    	.append(parent.getTitle()).append(":").append(rule.getRuleIndex()).append("\"").append(NEW_LINE)
    	.append("when\n");    	
    }
    private void addRuleCondition(RuleCond cond){
    	switch(cond.getEntityName()){
	    	case Pax:
	    		if(passengerBuilder == null){
	    			passengerBuilder = new StringBuilder("$p:Pax(");
	    		    addCondition(cond, passengerBuilder);
	    		}else{
	    			passengerBuilder.append(", ");
	    		    addCondition(cond, passengerBuilder);	    			
	    		}
	    		break;
	    	case Document:
	    		if(documentBuilder == null){
	    			documentBuilder = new StringBuilder("$d:Document(");
	    		    addCondition(cond, documentBuilder);
	    		}else{
	    			documentBuilder.append(", ");
	    		    addCondition(cond, documentBuilder);	    			
	    		}
	    		break;
	    	case Flight:
	    		if(flightBuilder == null){
	    		    flightBuilder = new StringBuilder("$f:Flight(");
	    		    addCondition(cond, flightBuilder);
	    		}else{
	    			flightBuilder.append(", ");
	    		    addCondition(cond, flightBuilder);	    			
	    		}
	    		break;
	    	case Airport:
	    		break;
    	}
    	
    }
    private void addCondition(final RuleCond cond, final StringBuilder bldr){
	    this.stringBuilder.append(cond.getAttrName());
	    switch(cond.getOpCode()){
		    case EQUAL:
			    bldr.append(cond.getAttrName())
		    	.append(" == ");	
			    addConditionValue(cond.getValues().get(0), bldr);
		    	break;
		    case NOT_EQUAL:
		    	bldr.append(cond.getAttrName()).append(" != ");
			    addConditionValue(cond.getValues().get(0), bldr);
		    	break;
		    case GREATER:
		    	bldr.append(cond.getAttrName()).append(" > ");
			    addConditionValue(cond.getValues().get(0), bldr);
		    	break;
		    case GREATER_OR_EQUAL:
		    	bldr.append(cond.getAttrName()).append(" >= ");
			    addConditionValue(cond.getValues().get(0), bldr);
		    	break;
		    case LESS:
		    	bldr.append(cond.getAttrName()).append(" < ");
			    addConditionValue(cond.getValues().get(0), bldr);
		    	break;
		    case LESS_OR_EQUAL:
		    	bldr.append(cond.getAttrName()).append(" <= ");
			    addConditionValue(cond.getValues().get(0), bldr);
		    	break;
		    case IN:
		    	bldr.append(cond.getAttrName()).append(" in ");
		    	addConditionValues(cond, bldr);
		    	break;
		    case NOT_IN:
		    	bldr.append(" not ").append(cond.getAttrName()).append(" in ");
		    	addConditionValues(cond, bldr);
		    	break;
		    case BETWEEN:
		    	break;
	    }
    }
    private void addConditionValue(final CondValue val, StringBuilder bldr){
     	switch(val.getValType()){
    	case BOOLEAN:
    		bldr.append(val.getCharVal().charAt(0)=='Y'?"true":"false");
    		break;
    	case STRING:
    		bldr.append("\"").append(val.getCharVal()).append("\"");
    		break;
   	    case DATE:
   	    	//TODO
   	    	break;
   	    case TIMESTAMP:
   	    	//TODO
   	    	break;
    	case INTEGER:
    	case LONG:
    	case DOUBLE:
    		bldr.append(val.getNumVal());
    		break;
    	}
    }
    private void addConditionValues(final RuleCond cond, StringBuilder bldr){
    	List<CondValue> values = cond.getValues();
    	bldr.append("[");
    	boolean firstTime = true;
    	for(CondValue val:values){
    		if(firstTime){
    			firstTime = false;
    		}else{
    			bldr.append(",");
    		}
    		addConditionValue(val,bldr);
    	}
    	bldr.append("]");
    }
    private void addRuleAction(Rule rule){
    	this.stringBuilder.append("then\n")
    	.append(String.format(ACTION_PASSENGER_HIT, rule.getId())).append("\n")
    	.append("end\n");
    }
}
