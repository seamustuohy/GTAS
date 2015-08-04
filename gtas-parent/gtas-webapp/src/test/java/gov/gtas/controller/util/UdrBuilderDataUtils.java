package gov.gtas.controller.util;

import gov.gtas.model.udr.json.MetaData;
import gov.gtas.model.udr.json.QueryEntity;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.model.udr.json.QueryTerm;
import gov.gtas.model.udr.json.UdrSpecification;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class UdrBuilderDataUtils {

	public UdrSpecification createSpec() {
		QueryObject queryObject = new QueryObject();
		queryObject.setCondition("OR");
		List<QueryEntity> rules = new LinkedList<QueryEntity>();
		QueryTerm trm = new QueryTerm("Pax", "embarkationDate", "String",
				"EQUAL", new String[] { new Date().toString() });
		rules.add(trm);
		rules.add(new QueryTerm("Pax", "lastName", "String", "EQUAL",
				new String[] { "Jones" }));

		QueryObject queryObjectEmbedded = new QueryObject();
		queryObjectEmbedded.setCondition("AND");
		List<QueryEntity> rules2 = new LinkedList<QueryEntity>();

		QueryTerm trm2 = new QueryTerm("Pax", "embarkation", "String",
				"IN", new String[] { "DBY", "PKY", "FLT" });
		rules2.add(trm2);
		rules2.add(new QueryTerm("Pax", "debarkation", "String", "EQUAL",
				new String[] { "IAD" }));
		queryObjectEmbedded.setRules(rules2);

		queryObject.setRules(rules);

		UdrSpecification resp = new UdrSpecification(null,queryObject, new MetaData(
				"Hello Rule 1", "This is a test", new Date(), "jpjones"));
		return resp;
	}

	public UdrSpecification createSimpleSpec() {
		QueryObject queryObject = new QueryObject();
		queryObject.setCondition("OR");
		List<QueryEntity> rules = new LinkedList<QueryEntity>();
		QueryTerm trm = new QueryTerm("Pax", "embarkationDate", "String",
				"EQUAL", new String[] { new Date().toString() });
		rules.add(trm);
		rules.add(new QueryTerm("Pax", "lastName", "String", "EQUAL",
				new String[] { "Jones" }));

		queryObject.setRules(rules);

		UdrSpecification resp = new UdrSpecification(null, queryObject, new MetaData(
				"Hello Rule 1", "This is a test", new Date(), "jpjones"));
		return resp;
	}
}
