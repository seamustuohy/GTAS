package gov.cbp.taspd.gtas.bo;

import java.util.List;

public interface RuleServiceRequest {
	List<?> getRequestObjects();
	RuleServiceRequestType getRequestType();
}
