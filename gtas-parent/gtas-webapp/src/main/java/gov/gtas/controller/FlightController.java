package gov.gtas.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import gov.gtas.dataobject.PassengerVo;
import gov.gtas.model.Flight;
import gov.gtas.model.HitsSummary;
import gov.gtas.model.Passenger;
import gov.gtas.dataobject.FlightVo;
import gov.gtas.services.FlightService;
import gov.gtas.services.HitsSummaryService;
import gov.gtas.services.PassengerService;

@Controller
public class FlightController {
    private static final Logger logger = LoggerFactory.getLogger(FlightController.class);
    
    @Autowired
    private FlightService flightService;
    
    @Autowired
    private PassengerService pService;
    
    @Autowired
    private HitsSummaryService hitsSummaryService;
    
    private static List<HitsSummary> hitsList = new ArrayList<HitsSummary>();
   
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/flights", method = RequestMethod.GET)
    public List<FlightVo> getAllFlights() {
    	    	
    	
        List<FlightVo> rv = new ArrayList<>();

        try{
        	
        List<Flight> flights = flightService.findAll();
        List<Passenger> passengers = null;
        
        for (Flight f : flights) {
     
        	logger.debug(f.getFlightNumber());
            FlightVo vo = new FlightVo();
            vo.setFlightId(String.valueOf(f.getId()));
            String carrier = f.getCarrier() != null ? f.getCarrier() : null;
            vo.setCarrier(carrier);
            vo.setFlightNumber(f.getFlightNumber());
            String origin = f.getOrigin() != null ? f.getOrigin() : null;
            vo.setOrigin(origin);
            String originCountry = f.getOriginCountry() != null ? f.getOriginCountry() : null;
            vo.setOriginCountry(originCountry);
            vo.setEtd(f.getEtd());
            String dest = f.getDestination() != null ? f.getDestination() : null;
            vo.setDestination(dest);
            String destCountry = f.getDestinationCountry() != null ? f.getDestinationCountry() : null;
            vo.setDestinationCountry(destCountry);
            vo.setEta(f.getEta());

            passengers = pService.getPassengersByFlightId(f.getId());
            vo.setRuleHits(getTotalHitsByFlightId(passengers));
            vo.setTotalPax(passengers.size());
            rv.add(vo);
        }
    
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
        
        hitsList.clear();
        return rv;
    }
    
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/flight/{id}", method = RequestMethod.GET)
    public FlightVo getFlight(@PathVariable Long id) {
    	return getFlightVo(id);
    }
    
    @Transactional
    private FlightVo getFlightVo(long id){
        List<FlightVo> rv = new ArrayList<>();

        try{
        	
        Flight f = flightService.findById(id);
            logger.debug(f.getFlightNumber());
            FlightVo vo = new FlightVo();
            vo.setFlightId(String.valueOf(f.getId()));
            String carrier = f.getCarrier() != null ? f.getCarrier() : null;
            vo.setCarrier(carrier);
            vo.setFlightNumber(f.getFlightNumber());
            String origin = f.getOrigin() != null ? f.getOrigin() : null;
            vo.setOrigin(origin);
            String originCountry = f.getOriginCountry() != null ? f.getOriginCountry() : null;
            vo.setOriginCountry(originCountry);
            vo.setEtd(f.getEtd());
            String dest = f.getDestination() != null ? f.getDestination() : null;
            vo.setDestination(dest);
            String destCountry = f.getDestinationCountry() != null ? f.getDestinationCountry() : null;
            vo.setDestinationCountry(destCountry);
            vo.setEta(f.getEta());
            rv.add(vo);
            
            Map<Long, PassengerVo> travMap = new HashMap<Long, PassengerVo>();
            
            Iterable<HitsSummary> summary = hitsSummaryService.findAll();
            
            for(HitsSummary s:summary){}
    
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
        
        return new FlightVo();
    	
    }
    
    
     
    @Transactional
    public int getTotalHitsByFlightId(List<Passenger> passengers){

    	int totalHits = 0;
    	
    	if(hitsList == null || hitsList.isEmpty()){
    		
    		Iterable<HitsSummary> summary = hitsSummaryService.findAll();
            
            for(HitsSummary s:summary){
            	hitsList.add((HitsSummary)s);
            }
    	
    	} // END IF
    	
    	//Iterate over PAX to get total hit count
    	for(Passenger t: passengers){
    		
    		for(HitsSummary s: hitsList){
    			
    		if(s.getPassengerId().equals(t.getId())){
    			totalHits++;
    			}
    		}
    	}
    	
    	return totalHits;
    }
    
}
