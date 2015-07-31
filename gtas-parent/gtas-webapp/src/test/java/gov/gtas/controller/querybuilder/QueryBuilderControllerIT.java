package gov.gtas.controller.querybuilder;

import gov.gtas.model.udr.json.QueryEntity;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.model.udr.json.QueryTerm;
import gov.gtas.querybuilder.model.QueryRequest;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class QueryBuilderControllerIT {

//	public static void main(String args[]) {
//        RestTemplate restTemplate = new RestTemplate();
//    }

	@Test
	public void display() throws JsonProcessingException {
		QueryRequest qr = new QueryRequest();
		
		qr.setId(1);
		qr.setUserId("ladebiyi");
		qr.setTitle("Test Query");
		qr.setDescription("Sample Description");
		qr.setQuery(buildSimpleQuery());
		
		ObjectMapper mapper = new ObjectMapper();
		System.out.println("query: " + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(qr));
	}
	
	private QueryObject buildSimpleQuery() {
		QueryObject query = new QueryObject();
		QueryTerm rule = new QueryTerm();
		List<QueryEntity> rules = new ArrayList<>();
		
		rule.setEntity("Pax");
		rule.setField("firstName");
		rule.setOperator("equal");
		rule.setType("string");
		rule.setValue(new String[]{"DAVID"});
		
		rules.add(rule);
		
		query.setCondition("AND");
		query.setRules(rules);
		
		return query;
	}
}
