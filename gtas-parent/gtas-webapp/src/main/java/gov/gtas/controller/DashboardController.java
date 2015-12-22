package gov.gtas.controller;

import gov.gtas.model.*;
import gov.gtas.services.*;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
public class DashboardController {
    @Autowired
    private FlightService flightService;

    @Autowired
    private PassengerService paxService;

    @Autowired
    private HitsSummaryService hitsSummaryService;

    @Autowired
    private MessageService apisMessageService;

    @Autowired
    private PnrService pnrService;

    @RequestMapping(method = RequestMethod.GET, value = "/getFlightsAndHitsCount")
    public HashMap<String, Integer> getFlightsAndHitsCount(
            @RequestParam(value="startDate", required=false) String startDate,
            @RequestParam(value="endDate", required=false) String endDate) throws ParseException{

        HashMap _flightAndHitsCount = new HashMap<String, Integer>();
        int ruleHits=0, watchListHits=0, flights = 0;
        List<HitsSummary> _tempHitsSummary = new ArrayList<HitsSummary>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        List<Flight> _tempFlightList = flightService.getFlightsByDates(sdf.parse(startDate), sdf.parse(endDate));
        flights = Integer.valueOf(_tempFlightList.size());

        for(Flight _tempFlight : _tempFlightList) {
            _tempHitsSummary = hitsSummaryService.findHitsByFlightId(_tempFlight.getId());
                for(HitsSummary summ : _tempHitsSummary){
                    ruleHits = summ.getRuleHitCount() + ruleHits;
                    watchListHits = summ.getWatchListHitCount() + watchListHits;
                }
        }

        _flightAndHitsCount.put("flightsCount", new Integer(flights));
        _flightAndHitsCount.put("ruleHitsCount", new Integer(ruleHits));
        _flightAndHitsCount.put("watchListCount", new Integer(watchListHits));


        return _flightAndHitsCount;
    }


    @RequestMapping(method = RequestMethod.GET, value = "/getMessagesCount")
    public HashMap<String, Integer> getMessagesCount(
            @RequestParam(value="startDate", required=false) String startDate,
            @RequestParam(value="endDate", required=false) String endDate) throws ParseException{

        HashMap _apisAndPnrCount = new HashMap<String, Integer>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int apisMessageCount=0, pnrMessageCount=0;

        List<Message> _tempApisList = apisMessageService.getAPIsByDates(sdf.parse(startDate), sdf.parse(endDate));
        List<Pnr> _tempPnrList = pnrService.getPNRsByDates(sdf.parse(startDate), sdf.parse(endDate));

        apisMessageCount = _tempApisList.size();
        pnrMessageCount = _tempPnrList.size();

        _apisAndPnrCount.put("apisMessageCount", apisMessageCount);
        _apisAndPnrCount.put("pnrMessageCount", pnrMessageCount);

        return _apisAndPnrCount;
    }
}