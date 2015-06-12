package gov.cbp.taspd.gtas.controller;

import gov.cbp.taspd.gtas.bo.RuleExecutionStatistics;
import gov.cbp.taspd.gtas.constants.URIConstants;
import gov.cbp.taspd.gtas.model.ApisMessage;
import gov.cbp.taspd.gtas.model.Flight;
import gov.cbp.taspd.gtas.model.Pax;
import gov.cbp.taspd.gtas.rule.RuleServiceResult;
import gov.cbp.taspd.gtas.svc.TargetingService;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The REST service end-point controller for creating and managing 
 * user Defined Rules (UDR) for targeting.
 * @author GTAS3 (AB)
 *
 */
@RestController
@RequestMapping(URIConstants.UDR_ROOT)
public class UdrManagementController {
	@Autowired
	TargetingService targetingService;

	@RequestMapping(value = URIConstants.UDR_TEST, method = RequestMethod.GET)
	public @ResponseBody RuleExecutionStatistics testUDR() {
		Pax p1 = createPassenger("Telomere", "Strong", "Timbuktu");
		ApisMessage msg = new ApisMessage();
		Flight flight = new Flight();
		HashSet<Pax> set = new HashSet<Pax>();
		set.add(p1);
		flight.setPassengers(set);
		flight.setDestination("Narnia");
		HashSet<Flight> flightSet = new HashSet<Flight>();
		flightSet.add(flight);
		msg.setFlights(flightSet);
		RuleServiceResult res = targetingService.analyzeApisMessage(msg);

		return res.getExecutionStatistics();
	}

	/**
	 * creates a simple passenger object.
	 * 
	 * @param fn
	 * @param ln
	 * @param embarkation
	 * @return
	 */
	private Pax createPassenger(final String fn, final String ln,
			final String embarkation) {
		Pax p = new Pax();
		p.setFirstName(fn);
		p.setLastName(ln);
		p.setEmbarkation(embarkation);
		return p;
	}

}
