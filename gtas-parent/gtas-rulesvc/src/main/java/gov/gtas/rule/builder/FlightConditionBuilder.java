package gov.gtas.rule.builder;

import gov.gtas.model.udr.enumtype.EntityLookupEnum;
import gov.gtas.querybuilder.enums.EntityEnum;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlightConditionBuilder extends EntityConditionBuilder {
	/*
	 * The logger for the FlightConditionBuilder.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(FlightConditionBuilder.class);
	
	private String defaultTravelerVariableName;
    private List<String> linkedTravelerList;
	public FlightConditionBuilder(final String drlVariableName, final String defaultTravelerVarName){
		super(drlVariableName, EntityEnum.FLIGHT.getEntityName());
		this.linkedTravelerList = new LinkedList<String>();
		this.defaultTravelerVariableName = defaultTravelerVarName;
	}
	public void addLinkedTraveler(final String travelerVariable){
		this.linkedTravelerList.add(travelerVariable);
	}
	
	
	/* (non-Javadoc)
	 * @see gov.gtas.rule.builder.EntityConditionBuilder#init()
	 */
	@Override
	public void init() {
		super.init();
	}
	@Override
	protected void addSpecialConditionsWithoutActualConditions(
			StringBuilder bldr) {
		//NO OP
		if(logger.isDebugEnabled()){
		    logger.debug("FlightConditionBuilder - no flight condition specified.");
		}
	}

	@Override
	protected void addSpecialConditions(StringBuilder bldr) {
		if(linkedTravelerList.isEmpty()){
			bldr.append(defaultTravelerVariableName).append(":")
			.append(EntityLookupEnum.Traveler).append("()")
		    .append(" from ")
			.append(getDrlVariableName())
			.append(".passengers\n");
		} else {
			if(linkedTravelerList.size() == 1){
				String travelerVariable = linkedTravelerList.get(0);
				bldr.append(EntityLookupEnum.Traveler).append("(id == ")
				    .append(travelerVariable).append(".id) from ")
					.append(getDrlVariableName())
					.append(".passengers\n");
	        } else {
				bldr.append(EntityLookupEnum.Traveler).append("(id in (");
				boolean firstTime = true;
	        	for(String travelerVariable:linkedTravelerList){
	        		if(firstTime){       			
	        			firstTime = false;
	        		} else {
	        			bldr.append(", ");
	        		}
	        		bldr.append(travelerVariable).append(".id");
	        	}
				
	        	bldr.append(")) from ")
				.append(getDrlVariableName())
				.append(".passengers\n");
			}
		}
	}
}
