package gov.gtas.rule.builder;

import gov.gtas.querybuilder.enums.EntityEnum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TravelerConditionBuilder extends EntityConditionBuilder {
	/*
	 * The logger for the TravelerConditionBuilder.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(TravelerConditionBuilder.class);
	
//    private boolean passenger;
//    private boolean crew;
	public TravelerConditionBuilder(final String drlVariableName){
		super(drlVariableName, EntityEnum.TRAVELER.getEntityName());
//		passenger = false;
//		crew = false;
	}
	
	/* (non-Javadoc)
	 * @see gov.gtas.rule.builder.EntityConditionBuilder#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return super.isEmpty();// && !isPassenger() && !isCrew();
	}

	/* (non-Javadoc)
	 * @see gov.gtas.rule.builder.EntityConditionBuilder#init()
	 */
	@Override
	public void reset() {
		super.reset();
//		passenger = false;
//		crew = false;
	}

	@Override
	protected void addSpecialConditionsWithoutActualConditions(
			StringBuilder bldr) {
//		if(isPassenger()){
//			bldr.append(getDrlVariableName()).append(":").append(EntityEnum.TRAVELER.getEntityName()).append("()");
//		} else if (isCrew()){
//			bldr.append(getDrlVariableName()).append(":").append(EntityLookupEnum.Crew).append("()");			
//		}
	}

	@Override
	protected void addSpecialConditions(StringBuilder bldr) {
//		if(isPassenger()){
//			bldr.append(EntityEnum.TRAVELER.getEntityName()).append("(id == ").append(getDrlVariableName()).append(".id)");
//		} else if (isCrew()){
//			bldr.append(EntityLookupEnum.Crew).append("(id == ").append(getDrlVariableName()).append(".id)");		
//		}
	}
	/* (non-Javadoc)
	 * @see gov.gtas.rule.builder.EntityConditionBuilder#addCondition(gov.gtas.model.udr.RuleCond)
	 */
//	@Override
//	public void addCondition(RuleCond cond) {
//		if(cond.getAttrName().equals("class")){
//			if(!isPassenger() && !isCrew()){
//				String val = RuleConditionBuilderHelper.getSingleStringValue(cond);
//				if(val != null && "P".equals(val)){
//					this.passenger = true;
//				} else if(val != null && "C".equals(val)){
//					this.crew = true;
//				} else {
//					//warning
//					logger.warn("TravelerConditionBuilder.addCondition() - invalid classs value.\n"+cond.toString());					
//				}
//			} else {
//				//warning
//				logger.warn("TravelerConditionBuilder.addCondition() - multiple class specifications.\n"+cond.toString());
//			}
//		} else {
//		  super.addCondition(cond);
//		}
//	}
//	/**
//	 * @return the passenger
//	 */
//	public boolean isPassenger() {
//		return passenger;
//	}
//	/**
//	 * @return the crew
//	 */
//	public boolean isCrew() {
//		return crew;
//	}

}
