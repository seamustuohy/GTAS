package gov.gtas.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import gov.gtas.model.Flight;
import gov.gtas.services.FlightService;
import gov.gtas.vo.passenger.FlightVo;

@Controller
public class FlightController {
	private static final Logger logger = LoggerFactory.getLogger(FlightController.class);

	@Autowired
	private FlightService flightService;

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
	
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/flights", method = RequestMethod.GET)
	public FlightsPage getAllFlights(
	        @RequestParam(value = "pageNumber", required = true) String pageNumber,
	        @RequestParam(value = "pageSize", required = true) String pageSize) {

	    List<FlightVo> flights = new ArrayList<>();
	    long totalFlights = 0;

		try {
			Page<Flight> page = flightService.findAll(Integer.valueOf(pageNumber), Integer.valueOf(pageSize));
			totalFlights = page.getTotalElements();
			for (Flight f : page) {
				FlightVo vo = new FlightVo();
	            BeanUtils.copyProperties(f, vo);
				flights.add(vo);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return new FlightsPage(flights, totalFlights);
	}
}
