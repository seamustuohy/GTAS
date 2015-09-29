package gov.gtas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.gtas.controller.dto.FlightsRequestDto;
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

    @RequestMapping(value = "/flights", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody FlightsPage getAllFlights(@RequestBody FlightsRequestDto request) {
//        System.out.println(request);
        return flightService.findAll(request.getPageNumber(), request.getPageSize());
    }

    @RequestMapping(value = "/flights/flight/{id}/passengers", method = RequestMethod.GET)
    public @ResponseBody PassengersPage getFlightPassengers(
            @PathVariable Long id,
            @RequestParam(value = "pageNumber", required = true) String pageNumber,
            @RequestParam(value = "pageSize", required = true) String pageSize) {

        return paxService.getPassengersByFlightId(id, Integer.valueOf(pageNumber), Integer.valueOf(pageSize));
    }

    @RequestMapping(value = "/passengers", method = RequestMethod.GET)
    public @ResponseBody PassengersPage getAllPassengers(
            @RequestParam(value = "pageNumber", required = true) String pageNumber,
            @RequestParam(value = "pageSize", required = true) String pageSize) {
            
        return paxService.findAllWithFlightInfo(Integer.valueOf(pageNumber), Integer.valueOf(pageSize));
    }
}
