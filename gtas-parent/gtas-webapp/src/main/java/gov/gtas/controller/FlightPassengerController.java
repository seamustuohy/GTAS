package gov.gtas.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import gov.gtas.model.Document;
import gov.gtas.model.Flight;
import gov.gtas.model.Passenger;
import gov.gtas.services.FlightService;
import gov.gtas.services.PassengerService;
import gov.gtas.vo.passenger.DocumentVo;
import gov.gtas.vo.passenger.FlightVo;
import gov.gtas.vo.passenger.PassengerVo;

@RestController
public class FlightPassengerController {
	private static final Logger logger = LoggerFactory.getLogger(FlightPassengerController.class);

	@Autowired
	private FlightService flightService;

    @Autowired
    private PassengerService paxService;

	public class FlightsPage {
	    private List<FlightVo> flights;
	    private long totalFlights;
        public FlightsPage(List<FlightVo> flights, long totalFlights) {
            this.flights = flights;
            this.totalFlights = totalFlights;
        }
        public List<FlightVo> getFlights() {
            return flights;
        }
        public long getTotalFlights() {
            return totalFlights;
        }
	}

   public class PassengersPage {
        private List<PassengerVo> passengers;
        private long totalPassengers;
        public PassengersPage(List<PassengerVo> passengers, long totalPassengers) {
            this.passengers = passengers;
            this.totalPassengers = totalPassengers;
        }
        public List<PassengerVo> getPassengers() {
            return passengers;
        }
        public long getTotalPassengers() {
            return totalPassengers;
        }
    }

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

        Page<Passenger> results = paxService.getPassengersByFlightId(id, Integer.valueOf(pageNumber), Integer.valueOf(pageSize));
        
        List<PassengerVo> passengers = new ArrayList<>();
        for (Passenger p : results) {
            PassengerVo vo = new PassengerVo();
            BeanUtils.copyProperties(p, vo);
            vo.setFlightId(id);
            for (Document d : p.getDocuments()) {
                DocumentVo docVo = new DocumentVo();
                BeanUtils.copyProperties(d, docVo);
                vo.addDocument(docVo);
            }
            passengers.add(vo);
        }
        
        return new PassengersPage(passengers, results.getTotalElements());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/passengers", method = RequestMethod.GET)
    public PassengersPage getAllPassengers(
            @RequestParam(value = "pageNumber", required = true) String pageNumber,
            @RequestParam(value = "pageSize", required = true) String pageSize) {
            
        List<PassengerVo> vos  = paxService.findAllWithFlightInfo(Integer.valueOf(pageNumber), Integer.valueOf(pageSize));
        PassengersPage page = new PassengersPage(vos, -1);
        return page;
    }
    
    private PassengersPage createPassengerPage(Page<Passenger> results) {
        List<PassengerVo> passengers = new ArrayList<>();
        for (Passenger p : results) {
            PassengerVo vo = new PassengerVo();
            BeanUtils.copyProperties(p, vo);
            vo.setFlightId(Long.valueOf(1));
            for (Document d : p.getDocuments()) {
                DocumentVo docVo = new DocumentVo();
                BeanUtils.copyProperties(d, docVo);
                vo.addDocument(docVo);
            }
            passengers.add(vo);
        }
        
        return new PassengersPage(passengers, results.getTotalElements());
    }
}
