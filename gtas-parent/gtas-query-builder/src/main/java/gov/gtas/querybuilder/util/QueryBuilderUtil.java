package gov.gtas.querybuilder.util;

public class QueryBuilderUtil {
	
	public static String getJoinCondition(EntityEnum entity) {
		String joinCondition = "";
		
		switch (entity.getEntityName()) {
			case "Flight":
				joinCondition = " join " + EntityEnum.PAX.getAlias() + ".flights " + EntityEnum.FLIGHT.getAlias();
				break;
	        case "Pax":
	        	joinCondition = " join " + EntityEnum.FLIGHT.getAlias() + ".passengers " + EntityEnum.PAX.getAlias();
	        	break;
	        case "Document":
	        	joinCondition = " join " + EntityEnum.PAX.getAlias() + ".documents " + EntityEnum.DOCUMENT.getAlias();
	            break;
	        default:
	            throw new IllegalArgumentException("Invalid entity");
		}
		
		return joinCondition;
	}
	
}
