package gov.gtas.rule.builder;

import gov.gtas.enumtype.EntityEnum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PassengerConditionBuilder extends EntityConditionBuilder {
	private static final Logger logger = LoggerFactory
			.getLogger(PassengerConditionBuilder.class);
	
	public PassengerConditionBuilder(final String drlVariableName){
		super(drlVariableName, EntityEnum.PASSENGER.getEntityName());
	}

	@Override
	protected void addSpecialConditionsWithoutActualConditions(
			StringBuilder bldr) {
	}

	@Override
	protected void addSpecialConditions(StringBuilder bldr) {
	}
}
