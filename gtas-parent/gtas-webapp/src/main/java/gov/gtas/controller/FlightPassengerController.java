package gov.gtas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import gov.gtas.services.FlightService;
import gov.gtas.services.FlightsPage;
import gov.gtas.services.PassengerService;
import gov.gtas.services.PassengersPage;

@RestController
public class FlightPassengerController {
	@Autowired
	private FlightService flightService;

    @Autowired
    private PassengerService paxService;

	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/flights", method = RequestMethod.GET)
	public FlightsPage getAllFlights(
	        @RequestParam(value = "pageNumber", required = true) String pageNumber,
	        @RequestParam(value = "pageSize", required = true) String pageSize) {

		return flightService.findAll(Integer.valueOf(pageNumber), Integer.valueOf(pageSize));
	}
	
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/flights/flight/{id}/passengers", method = RequestMethod.GET)
    public PassengersPage getFlightPassengers(
            @PathVariable Long id,
            @RequestParam(value = "pageNumber", required = true) String pageNumber,
            @RequestParam(value = "pageSize", required = true) String pageSize) {

        return paxService.getPassengersByFlightId(id, Integer.valueOf(pageNumber), Integer.valueOf(pageSize));
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/passengers", method = RequestMethod.GET)
    public PassengersPage getAllPassengers(
            @RequestParam(value = "pageNumber", required = true) String pageNumber,
            @RequestParam(value = "pageSize", required = true) String pageSize) {
            
        return paxService.findAllWithFlightInfo(Integer.valueOf(pageNumber), Integer.valueOf(pageSize));
    }
}
