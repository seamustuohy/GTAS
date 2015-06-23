package gov.gtas.controller;

import gov.gtas.constants.Constants;
import gov.gtas.model.udr.Rule;
import gov.gtas.repository.udr.json.QueryObject;
import gov.gtas.repository.udr.json.QueryTerm;
import gov.gtas.udrbuilder.service.UdrBuilderService;
import gov.gtas.wrapper.RuleWrapper;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.UDR_RULE)
public class UdrBuilderController {

	@Autowired
	UdrBuilderService udrService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody QueryObject getById(@PathVariable Long id) {
		//
		//udrService.getRule(id);
		QueryObject qo = new QueryObject();
		qo.setConditionCode("OR");
		List<QueryObject> rules = new LinkedList<QueryObject>();
		QueryTerm trm = new QueryTerm("Pax", "firstName","EQUAL", "John");
		rules.add(trm);
		rules.add(new QueryTerm("Pax", "lastName", "EQUAL", "Jones"));
		qo.setRule(rules);
		return qo;
	}

	@RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<RuleWrapper> getAll() {
		List<Rule> rules = udrService.allRules();
		//
		return null;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable("id") Long id) {
		udrService.deleteRule(id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseStatus(HttpStatus.OK)
	public void update(@PathVariable("id") Long id,
			@RequestBody RuleWrapper resource) {
		// convert RuleWrapper--Rule
		// udrService.updateRule(rule);
	}

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public void create(@RequestBody RuleWrapper resource) {
	}

}
