package gov.gtas.rule.builder.pnr;

import gov.gtas.enumtype.EntityEnum;
import gov.gtas.rule.builder.EntityConditionBuilder;

public class PnrConditionBuilder extends EntityConditionBuilder {

	public PnrConditionBuilder(final String drlVariableName) {
		super(drlVariableName, EntityEnum.PNR.getEntityName());
	}

	@Override
	protected void addSpecialConditions(StringBuilder bldr) {
	}
}
