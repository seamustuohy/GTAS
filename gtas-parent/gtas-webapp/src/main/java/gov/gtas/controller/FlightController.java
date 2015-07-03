package gov.gtas.controller;

import gov.gtas.model.Flight;
import gov.gtas.parsers.paxlst.vo.FlightVo;
import gov.gtas.services.FlightService;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class FlightController {
    private static final Logger logger = LoggerFactory.getLogger(FlightController.class);
    
    @Autowired
    private FlightService flightService;
    
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/flights", method = RequestMethod.GET)
    public List<FlightVo> getAllFlights() {
        List<FlightVo> rv = new ArrayList<>();

        List<Flight> flights = flightService.findAll();
        for (Flight f : flights) {
            logger.debug(f.getFlightNumber());
            FlightVo vo = new FlightVo();
            vo.setFlightId(String.valueOf(f.getId()));
            String carrier = f.getCarrier() != null ? f.getCarrier().getIata() : null;
            vo.setCarrier(carrier);
            vo.setFlightNumber(f.getFlightNumber());
            String origin = f.getOrigin() != null ? f.getOrigin().getIata() : null;
            vo.setOrigin(origin);
            String originCountry = f.getOriginCountry() != null ? f.getOriginCountry().getIso2() : null;
            vo.setOriginCountry(originCountry);
            vo.setEtd(f.getEtd());
            String dest = f.getDestination() != null ? f.getDestination().getIata() : null;
            vo.setDestination(dest);
            String destCountry = f.getDestinationCountry() != null ? f.getDestinationCountry().getIso2() : null;
            vo.setDestinationCountry(destCountry);
            vo.setEta(f.getEta());
            rv.add(vo);
        }
        
        return rv;
    }
}
