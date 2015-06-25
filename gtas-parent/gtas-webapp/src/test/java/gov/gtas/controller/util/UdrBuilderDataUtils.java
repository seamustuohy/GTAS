package gov.gtas.controller.util;

import gov.gtas.model.udr.json.MetaData;
import gov.gtas.model.udr.json.QueryEntity;
import gov.gtas.model.udr.json.QueryObject;
import gov.gtas.model.udr.json.QueryTerm;
import gov.gtas.model.udr.json.UdrSpecification;
import gov.gtas.model.udr.json.ValueObject;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class UdrBuilderDataUtils {

	public UdrSpecification createSpec() {
		QueryObject queryObject = new QueryObject();
		queryObject.setCondition("OR");
		List<QueryEntity> rules = new LinkedList<QueryEntity>();
		QueryTerm trm = new QueryTerm("Pax", "embarkationDate", "EQUAL",
				new ValueObject(new Date()));
		rules.add(trm);
		rules.add(new QueryTerm("Pax", "lastName", "EQUAL", new ValueObject(
				"Jones")));

		QueryObject queryObjectEmbedded = new QueryObject();
		queryObjectEmbedded.setCondition("AND");
		List<QueryEntity> rules2 = new LinkedList<QueryEntity>();

		QueryTerm trm2 = new QueryTerm("Pax", "embarkation.name", "IN",
				new ValueObject("String", new String[] { "DBY", "PKY", "FLT" }));
		rules2.add(trm2);
		rules2.add(new QueryTerm("Pax", "debarkation.name", "EQUAL",
				new ValueObject("IAD")));
		queryObjectEmbedded.setRules(rules2);

		queryObject.setRules(rules);

		UdrSpecification resp = new UdrSpecification(queryObject, new MetaData(
				"Hello Rule 1", "This is a test", new Date(), "jpjones"));
		return resp;
	}

	public UdrSpecification createSimpleSpec() {
		QueryObject queryObject = new QueryObject();
		queryObject.setCondition("OR");
		List<QueryEntity> rules = new LinkedList<QueryEntity>();
		QueryTerm trm = new QueryTerm("Pax", "embarkationDate", "EQUAL",
				new ValueObject(new Date()));
		rules.add(trm);
		rules.add(new QueryTerm("Pax", "lastName", "EQUAL", new ValueObject(
				"Jones")));

		queryObject.setRules(rules);

		UdrSpecification resp = new UdrSpecification(queryObject, new MetaData(
				"Hello Rule 1", "This is a test", new Date(), "jpjones"));
		return resp;
	}
}
