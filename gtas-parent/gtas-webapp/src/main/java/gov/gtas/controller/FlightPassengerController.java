package gov.gtas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.gtas.services.FlightService;
import gov.gtas.services.PassengerService;
import gov.gtas.services.dto.FlightsPageDto;
import gov.gtas.services.dto.FlightsRequestDto;
import gov.gtas.services.dto.PassengersPageDto;
import gov.gtas.services.dto.PassengersRequestDto;

@RestController
public class FlightPassengerController {
	@Autowired
	private FlightService flightService;

    @Autowired
    private PassengerService paxService;

    @RequestMapping(value = "/flights", method = RequestMethod.POST, 
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody FlightsPageDto getAllFlights(@RequestBody FlightsRequestDto request) {
//        System.out.println(request);
        return flightService.findAll(request);
    }

    @RequestMapping(value = "/flights/flight/{id}/passengers", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody PassengersPageDto getFlightPassengers(@PathVariable(value = "id") Long flightId, @RequestBody PassengersRequestDto request) {
//        System.out.println(request);
        return paxService.getPassengersByFlightId(flightId, request);
    }

    @RequestMapping(value = "/passengers", method = RequestMethod.POST)
    public @ResponseBody PassengersPageDto getAllPassengers(@RequestBody PassengersRequestDto request) {
        System.out.println(request);
        return paxService.findAllWithFlightInfo(request);
    }
}
