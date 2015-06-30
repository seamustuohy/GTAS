package gov.gtas.querybuilder.util;

public class QueryBuilderUtil {

	public static String getEntityAlias(EntityEnum entity) {
		String alias;
		
		switch (entity.getName()) {
	        case "Flight":
	            alias = "f";
	            break;
	        case "Passenger":
	        	alias = "p";
	        	break;
	        case "Pax":
	        	alias = "p";
	        	break;
	        case "Document":
	            alias = "d";
	            break;
	        default:
	            throw new IllegalArgumentException("Invalid entity");
		}

		return alias;
	}
	
	public static String getJoinCondition(EntityEnum entity) {
		String joinCondition = "";
		
		switch (entity.getName()) {
			case "Flight":
				joinCondition = " join " + getEntityAlias(EntityEnum.PASSENGER) + ".flights " + getEntityAlias(EntityEnum.FLIGHT);
				break;
	        case "Passenger":
	        	joinCondition = " join " + getEntityAlias(EntityEnum.FLIGHT) + ".passengers " + getEntityAlias(EntityEnum.PASSENGER);
	        	break;
	        case "Pax":
	        	joinCondition = " join " + getEntityAlias(EntityEnum.FLIGHT) + ".passengers " + getEntityAlias(EntityEnum.PASSENGER);
	        	break;
	        case "Document":
	        	joinCondition = " join " + getEntityAlias(EntityEnum.PASSENGER) + ".documents " + getEntityAlias(EntityEnum.DOCUMENT);
	            break;
	        default:
	            throw new IllegalArgumentException("Invalid entity");
		}
		
		return joinCondition;
	}
	
	public static String getOperator(String operator) {
		String op = "";
		
		switch (operator.toLowerCase()) {
	        case "equal":
	        	op = "=";
	        	break;
	        case "in":
	        	op = "in";
	        	break;
	        default:
	            throw new IllegalArgumentException("Invalid operator");
		}
		
		return op;
	}
}
