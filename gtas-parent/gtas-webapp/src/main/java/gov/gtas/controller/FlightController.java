package gov.gtas.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import gov.gtas.dataobject.FlightVo;
import gov.gtas.dataobject.PassengerVo;
import gov.gtas.model.Flight;
import gov.gtas.model.HitsSummary;
import gov.gtas.model.Passenger;
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
	private static final String EMPTY_STRING = "";
	private static final String RULE_HIT_TYPE = "R";
	private static final String WL_PASSENGER_HIT_TYPE = "P";
	private static final String WL_DOCUMENT_HIT_TYPE = "D";

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
	
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
//    @RequestMapping(value = "/flights", method = RequestMethod.GET)
    @Transactional
    public LinkedList getUpcomingFlights(@PageableDefault(page= 1 , size=25) Pageable pageable,
													    	 @RequestParam(value = "startDate", required = false) String startDate,
															 @RequestParam(value = "endDate", required = false) String endDate) {
        List<FlightVo> rv = new ArrayList<>();
        List<Flight> flights = null;
        List<Passenger> passengers = null;
        Iterator _tempIter;
        LinkedList _tempLL = new LinkedList();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        
        try {
			
        	flights = flightService.getFlightsByDates(sdf.parse(startDate), sdf.parse(endDate));
		
			
        } catch (ParseException ex) {
			ex.printStackTrace();
		}
        
        if (flights == null) return new LinkedList();//rv;
        
        for (Flight f : flights) {
        	
        	FlightVo vo = new FlightVo();
        	try {
        		vo.setFlightId(String.valueOf(f.getId()));
				String carrier = (f.getCarrier() != null ? f.getCarrier():EMPTY_STRING);
				vo.setCarrier(carrier);
				//vo.setFlightNumber(f.getFlightNumber());
				vo.setFlightNumber(carrier+f.getFlightNumber());
				String origin = f.getOrigin() != null ? f.getOrigin() : EMPTY_STRING;
				vo.setOrigin(origin);
				String originCountry = f.getOriginCountry() != null ? f.getOriginCountry() : EMPTY_STRING;
				vo.setOriginCountry(originCountry);
				vo.setEtd(f.getEtd());
				String dest = f.getDestination() != null ? f.getDestination() : EMPTY_STRING;
				vo.setDestination(dest);
				String destCountry = f.getDestinationCountry() != null ? f.getDestinationCountry() : EMPTY_STRING;
				vo.setDestinationCountry(destCountry);
				vo.setEta(f.getEta());
				vo.setEtd(f.getEtd());
				vo.setDirection(f.getDirection());

				passengers = pService.getPassengersByFlightId(f.getId());
				List<Integer> _tempHitCountList = getTotalHitsByFlightId(passengers, f.getId());
				vo.setRuleHits(_tempHitCountList.get(0));
				vo.setListHits(0);
				
				// Pax Watchlist                                // Document Watchlist
				if((_tempHitCountList.get(1).intValue()>0) && (_tempHitCountList.get(2).intValue()>0)){
				vo.setListHits(3);								// Both 'PD'
				}else if(_tempHitCountList.get(1).intValue()>0){// Pax Watchlist -- P
				vo.setListHits(1);	
				}else if(_tempHitCountList.get(2).intValue()>0){// Document Watchlist -- D
				vo.setListHits(2);
				}
				
				vo.setTotalPax(passengers.size());
				rv.add(vo);
			
        	} catch(Exception ex){
				ex.printStackTrace();
			}
                
        	
        } // End of Passengers Loop
        
        
        _tempLL.add(0, flights.size());
        _tempLL.add(1, rv);

    	hitsList.clear();
        return _tempLL;
    }
    

	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/flight/{id}", method = RequestMethod.GET)
	public FlightVo getFlight(@PathVariable Long id) {
		return getFlightVo(id);
	}

	@Transactional
	private FlightVo getFlightVo(long id) {
		List<FlightVo> rv = new ArrayList<>();

		try {

			Flight f = flightService.findById(id);
			logger.debug(f.getFlightNumber());
			FlightVo vo = new FlightVo();
			vo.setFlightId(String.valueOf(f.getId()));
			String carrier = f.getCarrier() != null ? f.getCarrier() : null;
			vo.setCarrier(carrier);
			vo.setFlightNumber(f.getFlightNumber());
			String origin = f.getOrigin() != null ? f.getOrigin() : null;
			vo.setOrigin(origin);
			String originCountry = f.getOriginCountry() != null ? f
					.getOriginCountry() : null;
			vo.setOriginCountry(originCountry);
			vo.setEtd(f.getEtd());
			String dest = f.getDestination() != null ? f.getDestination()
					: null;
			vo.setDestination(dest);
			String destCountry = f.getDestinationCountry() != null ? f
					.getDestinationCountry() : null;
			vo.setDestinationCountry(destCountry);
			vo.setEta(f.getEta());
			rv.add(vo);

			Map<Long, PassengerVo> travMap = new HashMap<Long, PassengerVo>();

			Iterable<HitsSummary> summary = hitsSummaryService.findAll();

			for (HitsSummary s : summary) {
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		hitsList.clear();
		return new FlightVo();

	}

	@Transactional
	public List<Integer> getTotalHitsByFlightId(List<Passenger> passengers, Long flightId) {

		int totalHits = 0;
		int wlPassengerHit = 0;
		int wlDocumentHit = 0;
		List<Integer> _tempHitCountList = new ArrayList<Integer>();

		if (hitsList == null || hitsList.isEmpty()) {
			Iterable<HitsSummary> summary = hitsSummaryService.findAll();
			for (HitsSummary s : summary) {
				hitsList.add((HitsSummary) s);
			}
		} // END IF

		// Iterate over PAX to get total hit count
		for (Passenger t : passengers) {

			for (HitsSummary s : hitsList) {

				if ((s.getPassengerId().equals(t.getId())) 
						&& (s.getFlightId().equals(flightId)) && (s.getHitType().equalsIgnoreCase(RULE_HIT_TYPE))) {
					totalHits++;
				}else if((s.getPassengerId().equals(t.getId())) 
						&& (s.getFlightId().equals(flightId)) && (s.getHitType().equalsIgnoreCase(WL_PASSENGER_HIT_TYPE))){
					wlPassengerHit++;
				}else if((s.getPassengerId().equals(t.getId())) 
						&& (s.getFlightId().equals(flightId)) && (s.getHitType().equalsIgnoreCase(WL_DOCUMENT_HIT_TYPE))){
					wlDocumentHit++;
				}
			}
		}
		
		_tempHitCountList.add(new Integer(totalHits));
		_tempHitCountList.add(new Integer(wlPassengerHit));
		_tempHitCountList.add(new Integer(wlDocumentHit));
		
		return _tempHitCountList;
	}
	
}
