package gov.gtas.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import gov.gtas.model.Flight;
import gov.gtas.services.FlightService;
import gov.gtas.services.FlightsPage;
import gov.gtas.services.PassengerService;
import gov.gtas.services.PassengersPage;
import gov.gtas.vo.passenger.FlightVo;

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

	    List<FlightVo> flights = new ArrayList<>();

		Page<Flight> page = flightService.findAll(Integer.valueOf(pageNumber), Integer.valueOf(pageSize));
		long total = page.getTotalElements();
		for (Flight f : page) {
			FlightVo vo = new FlightVo();
            BeanUtils.copyProperties(f, vo);
			flights.add(vo);
		}

		return new FlightsPage(flights, total);
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
