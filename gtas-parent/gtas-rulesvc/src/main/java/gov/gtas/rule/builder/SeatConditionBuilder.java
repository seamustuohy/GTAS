package gov.gtas.rule.builder;


//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class SeatConditionBuilder extends EntityConditionBuilder {
	/*
	 * The logger for the SeatConditionBuilder.
	 */
//	private static final Logger logger = LoggerFactory
//			.getLogger(SeatConditionBuilder.class);
	
	private boolean apis;
	public SeatConditionBuilder(final String drlVariableName, final boolean apis){
		super(drlVariableName, RuleTemplateConstants.SEAT_ENTITY_NAME);
		this.apis = apis;
	}
	public void addApisCondition(){
		if(isApis()){
		    super.addConditionAsString("apis == true");
		} else {
		    super.addConditionAsString("apis == false");			
		}
	}
	/* (non-Javadoc)
	 * @see gov.gtas.rule.builder.EntityConditionBuilder#addSpecialConditions(java.lang.StringBuilder)
	 */
	@Override
	protected void addSpecialConditions(StringBuilder bldr) {
	}
	/**
	 * @return the apis
	 */
	public boolean isApis() {
		return apis;
	}
	
}
