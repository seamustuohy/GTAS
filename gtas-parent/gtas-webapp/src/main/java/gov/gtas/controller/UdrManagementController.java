package gov.gtas.controller;

import gov.gtas.bo.RuleExecutionStatistics;
import gov.gtas.rule.RuleServiceResult;
import gov.gtas.svc.TargetingService;
import gov.gtas.constants.Constants;
import gov.gtas.model.ApisMessage;
import gov.gtas.model.Flight;
import gov.gtas.model.Pax;
import gov.gtas.model.lookup.Airport;

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
@RequestMapping(Constants.UDR_ROOT)
public class UdrManagementController {
	@Autowired
	TargetingService targetingService;

	@RequestMapping(value = Constants.UDR_TEST, method = RequestMethod.GET)
	public @ResponseBody RuleExecutionStatistics testUDR() {
		Pax p1 = createPassenger("Telomere", "Strong", "Timbuktu");
		ApisMessage msg = new ApisMessage();
		Flight flight = new Flight();
		HashSet<Pax> set = new HashSet<Pax>();
		set.add(p1);
		flight.setPassengers(set);
//		flight.setDestination("Narnia");
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
//		Airport airport = new Airport();
//		airport.setName
//		p.setEmbarkation(embarkation);
		return p;
	}

}
