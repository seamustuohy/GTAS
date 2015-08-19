package gov.gtas.rule.builder.pnr;

import static gov.gtas.rule.builder.RuleTemplateConstants.LINK_VARIABLE_SUFFIX;
import gov.gtas.enumtype.EntityEnum;
import gov.gtas.rule.builder.EntityConditionBuilder;

public class AddressConditionBuilder extends EntityConditionBuilder {

	public AddressConditionBuilder(final String drlVariableName) {
		super(drlVariableName, EntityEnum.ADDRESS.getEntityName());
	}

	@Override
	protected void addSpecialConditions(StringBuilder bldr) {
	}
	
	public String getLinkVariableName(){
		return getDrlVariableName() + LINK_VARIABLE_SUFFIX;
	}
}
