package gov.gtas.controller;

import gov.gtas.model.Flight;
import gov.gtas.parsers.paxlst.vo.FlightVo;
import gov.gtas.services.FlightService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class FlightController {
    @Autowired
    private FlightService flightService;
    
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/flights", method = RequestMethod.GET)
    public List<FlightVo> getAllFlights() {
        List<FlightVo> rv = new ArrayList<>();

        List<Flight> flights = flightService.findAll();
        for (Flight f : flights) {
            System.out.println(f.getFlightNumber());
            FlightVo vo = new FlightVo();
            vo.setFlightNumber(f.getFlightNumber());
            vo.setCarrier(f.getCarrier().getIata());
            rv.add(vo);
        }
        
        return rv;
    }
}
