package gov.gtas.rule.builder;

import gov.gtas.enumtype.EntityEnum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TravelerConditionBuilder extends EntityConditionBuilder {
	/*
	 * The logger for the TravelerConditionBuilder.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(TravelerConditionBuilder.class);
	
	public TravelerConditionBuilder(final String drlVariableName){
		super(drlVariableName, EntityEnum.TRAVELER.getEntityName());
	}

	@Override
	protected void addSpecialConditionsWithoutActualConditions(
			StringBuilder bldr) {
	}

	@Override
	protected void addSpecialConditions(StringBuilder bldr) {
	}
}
