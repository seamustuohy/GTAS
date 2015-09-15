package gov.gtas.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	private static final Logger logger = LoggerFactory
			.getLogger(FlightController.class);

	@Autowired
	private FlightService flightService;

	@Autowired
	private PassengerService pService;

	@Autowired
	private HitsSummaryService hitsSummaryService;

	private static List<HitsSummary> hitsList = new ArrayList<HitsSummary>();
	private static final String EMPTY_STRING = "";

	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	//@RequestMapping(value = "/flights", method = RequestMethod.GET)
	public List<FlightVo> getAllFlights() {

		List<FlightVo> rv = new ArrayList<>();

		try {

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

				passengers = pService.getPassengersByFlightId(f.getId());
				vo.setRuleHits(getTotalHitsByFlightId(passengers));
				vo.setTotalPax(passengers.size());
				rv.add(vo);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		hitsList.clear();
		return rv;
	}
	
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/flights", method = RequestMethod.GET)
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
				vo.setRuleHits(getTotalHitsByFlightId(passengers));
				vo.setTotalPax(passengers.size());
				rv.add(vo);
			
        	} catch(Exception ex){
				ex.printStackTrace();
			}
                
        	
        } // End of Passengers Loop
        
        
        _tempLL.add(0, flights.size());
        _tempLL.add(1, rv);

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

		return new FlightVo();

	}

	@Transactional
	public int getTotalHitsByFlightId(List<Passenger> passengers) {

		int totalHits = 0;

		if (hitsList == null || hitsList.isEmpty()) {

			Iterable<HitsSummary> summary = hitsSummaryService.findAll();

			for (HitsSummary s : summary) {
				hitsList.add((HitsSummary) s);
			}

		} // END IF

		// Iterate over PAX to get total hit count
		for (Passenger t : passengers) {

			for (HitsSummary s : hitsList) {

				if (s.getPassengerId().equals(t.getId())) {
					totalHits++;
				}
			}
		}

		return totalHits;
	}

}
