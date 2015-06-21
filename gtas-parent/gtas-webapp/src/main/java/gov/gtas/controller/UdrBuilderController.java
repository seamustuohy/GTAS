package gov.gtas.controller;

import gov.gtas.constants.Constants;
import gov.gtas.model.udr.Rule;
import gov.gtas.udrbuilder.model.RuleFE;
import gov.gtas.udrbuilder.service.UdrBuilderService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.UDR_RULE)
public class UdrBuilderController {

	@Autowired
	UdrBuilderService udrService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public RuleFE findOne(@PathVariable Long id) {
		//
		udrService.getById(id);
		return null;
	}

	@RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
	public List<RuleFE> findAll() {
		List<Rule> rules = udrService.allRules();
		//
		return null;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public void delete(@PathVariable("id") Long id) {
		udrService.deleteById(id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
	public void update(@PathVariable("id") Long id, @RequestBody RuleFE resource) {
		// convert RuleFE--Rule
		// udrService.updateRule(rule);
	}

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public void create(@RequestBody RuleFE resource) {
	}

}
