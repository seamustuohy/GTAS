/*
 * All GTAS code is Copyright 2016, Unisys Corporation.
 * 
 * Please see LICENSE.txt for details.
 */
package gov.gtas.controller;

import gov.gtas.model.*;
import gov.gtas.services.*;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
    private MessageStatisticsService messageStatsService;


    @Autowired
    private YTDStatisticsService ytdStatsService;

    @Autowired
    private PnrService pnrService;

    private static final String commaStringToAppend = ", ";
    private static final String EMPTY_STRING="";

    @RequestMapping(method = RequestMethod.GET, value = "/getFlightsAndHitsCount")
    public HashMap<String, Integer> getFlightsAndHitsCount(
            @RequestParam(value="startDate", required=false) String startDate,
            @RequestParam(value="endDate", required=false) String endDate) throws ParseException{

        HashMap _flightAndHitsCount = new HashMap<String, Integer>();
        int ruleHits=0, watchListHits=0, flights = 0;
        List<HitsSummary> _tempHitsSummary = new ArrayList<HitsSummary>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        List<Flight> _tempFlightList = flightService.getFlightsThreeDaysForward();
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

    class MessageCount implements Serializable {
        String STATE = EMPTY_STRING;
        String API = EMPTY_STRING;
        String PNR = EMPTY_STRING;

        public MessageCount(String STATE, String API, String PNR) {
            this.STATE = STATE;
            this.API = API;
            this.PNR = PNR;
        }

        public String getSTATE() {
            return STATE;
        }

        public void setSTATE(String STATE) {
            this.STATE = STATE;
        }

        public String getAPI() {
            return API;
        }

        public void setAPI(String API) {
            this.API = API;
        }

        public String getPNR() {
            return PNR;
        }

        public void setPNR(String PNR) {
            this.PNR = PNR;
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getMessagesCount")
    public ArrayList<MessageCount> getMessagesCount(
            @RequestParam(value="startDate", required=false) String startDate,
            @RequestParam(value="endDate", required=false) String endDate) throws ParseException{

        ArrayList<MessageCount> _apisAndPnrCount = new ArrayList<MessageCount>();
        String stateLabel = "State";
        String ApiMessages = "API 148";
        String PnrMessages = "PNR 252";
        String[] displayTokens = new String[]   {"12 - 1 AM","1 - 2 AM","2 - 3 AM","3 - 4 AM","4 - 5 AM","5 - 6 AM","6 - 7 AM",
                                                    "7 - 8 AM","8 - 9 AM","9 - 10 AM","10 - 11 AM","11 - 12 PM","12 - 1 PM",
                                                    "1 - 2 PM","2 - 3 PM","3 - 4 PM","4 - 5 PM","5 - 6 PM","6 - 7 PM",
                                                    "7 - 8 PM","8 - 9 PM","9 - 10 PM","10 - 11 PM","11 - 12 AM"};

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int apisMessageCount=0, pnrMessageCount=0;
        int clockTicks = 24;

        List<Message> _tempApisList = apisMessageService.getAPIsByDates(sdf.parse(startDate), sdf.parse(endDate));
        List<Pnr> _tempPnrList = pnrService.getPNRsByDates(sdf.parse(startDate), sdf.parse(endDate));

        DashboardMessageStats apisStatistics = messageStatsService.getDashboardAPIMessageStats();
        DashboardMessageStats pnrStatistics = messageStatsService.getDashboardPNRMessageStats();

        apisMessageCount = _tempApisList.size();
        pnrMessageCount = _tempPnrList.size();

        MessageCount mc = new MessageCount(stateLabel,EMPTY_STRING,EMPTY_STRING);
        for(int i=0; i<clockTicks; i++){

            mc = new MessageCount(displayTokens[i]+EMPTY_STRING, Arrays.asList(getApiPnrCountPerRow(apisStatistics, pnrStatistics, i).split(",")).get(0),
                    Arrays.asList(getApiPnrCountPerRow(apisStatistics, pnrStatistics, i).split(",")).get(1));
            _apisAndPnrCount.add(mc);
        }

        return _apisAndPnrCount;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getYtdRulesCount")
    public List<YTDRules> getYtdRulesCount(){
      List<YTDRules> ruleList = new ArrayList<YTDRules>();

        ruleList = ytdStatsService.getYTDRules();

        return ruleList;
    }


    @RequestMapping(method = RequestMethod.GET, value = "/getYtdAirportStats")
    public List<YTDAirportStatistics> getYtdAirportStats(){
        List<YTDAirportStatistics> ruleList = new ArrayList<YTDAirportStatistics>();

        ruleList = ytdStatsService.getYTDAirportStats();

        return ruleList;
    }


    private String getApiPnrCountPerRow(DashboardMessageStats apisStatistics,DashboardMessageStats pnrStatistics, int position ){

        String returnString = "";

        switch (position){

            case 0: returnString = apisStatistics.getZero()+ commaStringToAppend + pnrStatistics.getZero(); break;
            case 1: returnString = apisStatistics.getOne()+ commaStringToAppend + pnrStatistics.getOne(); break;
            case 2: returnString = apisStatistics.getTwo()+ commaStringToAppend + pnrStatistics.getTwo(); break;
            case 3: returnString = apisStatistics.getThree()+ commaStringToAppend + pnrStatistics.getThree(); break;
            case 4: returnString = apisStatistics.getFour()+ commaStringToAppend + pnrStatistics.getFour(); break;
            case 5: returnString = apisStatistics.getFive()+ commaStringToAppend + pnrStatistics.getFive(); break;
            case 6: returnString = apisStatistics.getSix()+ commaStringToAppend + pnrStatistics.getSix(); break;
            case 7: returnString = apisStatistics.getSeven()+ commaStringToAppend + pnrStatistics.getSeven(); break;
            case 8: returnString = apisStatistics.getEight()+ commaStringToAppend + pnrStatistics.getEight(); break;
            case 9: returnString = apisStatistics.getNine()+ commaStringToAppend + pnrStatistics.getNine(); break;
            case 10: returnString = apisStatistics.getTen()+ commaStringToAppend + pnrStatistics.getTen(); break;
            case 11: returnString = apisStatistics.getEleven()+ commaStringToAppend + pnrStatistics.getEleven(); break;
            case 12: returnString = apisStatistics.getTwelve()+ commaStringToAppend + pnrStatistics.getTwelve(); break;
            case 13: returnString = apisStatistics.getThirteen()+ commaStringToAppend + pnrStatistics.getThirteen(); break;
            case 14: returnString = apisStatistics.getFourteen()+ commaStringToAppend + pnrStatistics.getFourteen(); break;
            case 15: returnString = apisStatistics.getFifteen()+ commaStringToAppend + pnrStatistics.getFifteen(); break;
            case 16: returnString = apisStatistics.getSixteen()+ commaStringToAppend + pnrStatistics.getSixteen(); break;
            case 17: returnString = apisStatistics.getSeventeen()+ commaStringToAppend + pnrStatistics.getSeventeen(); break;
            case 18: returnString = apisStatistics.getEighteen()+ commaStringToAppend + pnrStatistics.getEighteen(); break;
            case 19: returnString = apisStatistics.getNineteen()+ commaStringToAppend + pnrStatistics.getNineteen(); break;
            case 20: returnString = apisStatistics.getTwenty()+ commaStringToAppend + pnrStatistics.getTwenty(); break;
            case 21: returnString = apisStatistics.getTwentyOne()+ commaStringToAppend + pnrStatistics.getTwentyOne(); break;
            case 22: returnString = apisStatistics.getTwentyTwo()+ commaStringToAppend + pnrStatistics.getTwentyTwo(); break;
            case 23: returnString = apisStatistics.getTwentyThree()+ commaStringToAppend + pnrStatistics.getTwentyThree(); break;

        }

        return returnString;
    }
}