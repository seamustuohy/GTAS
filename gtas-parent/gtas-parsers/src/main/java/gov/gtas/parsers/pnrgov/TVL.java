package gov.gtas.parsers.pnrgov;

import java.util.Date;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * class TVL holds the flight (departure date/time, origin, destination, operating airline code, 
 * flight number, and operation suffix) for which passenger data is being sent.
 * @author GTAS4
 *
 * Dates and times in the TVL are in Local Time
 * Departure and arrival points of the transborder segment for a given country are the ones of the 
 * leg which makes the segment eligible for push to a given country
 * 
 * Examples
 * 
 * The passenger information being sent is for Delta flight 10 from ATL to LGW on 30MAR which 
 * departs at 5:00 pm.(TVL+300310:1700+ATL+DFW+DL+10’)
 * The passenger information being sent is for Delta flight 9375 from ATL to AMS on 24 FEB which 
 * departs at 9:35 pm.(TVL+240210:2135+ATL+AMS+DL+9375’)
 * 
 * This example is only concerned with the push to Canada. While the US will also have a push, 
 * the US is not demonstrated in this example. CX888 is a multileg flight with the following 
 * routing and times,
 * HKG 10May 0100 YVR 09May 2030
 * YVR 09May 2230 JFK 10May 0420
 * 
 * The leg eligible for Canada is HKG YVR. The passenger information to push are for CX888 
 * from HKG YVR (terminate YVR Canada) and HKG to JFK (transit YVR Canada). The push will occur at 
 * Scheduled Departure Time out of HKG.
 * For the flight departing on 10th May at 0100 (Local Time) from HKG and arriving at YVR at 
 * 2030 on 09May,the following segment TVL in PNRGOV level 0 will be sent:TVL+100512:0100:090512:2030+HKG+YVR+CX+888
 * 
 * TVL in Gr5 at Level 2 and Gr.12 at Level 4
 * 
 * For OPEN and ARNK segments, the date, place of departure and place of arrival are conditional. 
 * For an Airline/ Flight Number / class/ date / segment, the date, place of departure and place of 
 * arrival are mandatory.
 * 
 * For OPEN and ARNK segments, the date, place of departure and place of arrival are conditional. 
 * For an Airline/ Flight Number / class/ date / segment, the date, place of departure and place of 
 * arrival are mandatory.
 * 
 * When referring to a codeshare flight, two TVLs are required (one as difined in 5.28.2 for the 
 * marketing flight and one providing the operating flight information as defined in 5.28.3). If the 
 * marketing and operating carrier/flight are the same, only one TVL is used as defined in 5.28.2.
 * 
 * Flown segments are to be included in history.
 * Departure and arrival city/airport codes as contained in the passenger’s booked itinerary.
 * 
 * Examples:
 * The flight segment in the passenger’s itinerary is Delta flight 10 from ATL to LHR on April 1 
 * which departs at 10:35 p.m. and arrives at noon and the reservation booking designator is K. 
 * The operating carrier is KL.(TVL+010410:2235:020410:1200+ATL+LHR+DL:KL+10:K’)
 * An ARNK segment is used to fill a gap in the itinerary.(TVL+++++ARNK’)
 * 
 * An OPEN segment is used where the passenger has purchased a ticket between two cities/airports 
 * but does not know the flight number or date.(TVL++LHR+ORD++OPEN’)
 * 
 * For the flight departing on 10th May at 0100 (Local Time) from HKG and arriving at YVR at 
 * 2030 on 09May,the following segment TVL in PNRGOV will be sent:
 * Level 0 - TVL+100512:0100:090512:2030+HKG+YVR+CX+888
 * Grp 5 level 2 for HKG YVR passengers - TVL+100512:0100:090512:2030+HKG+YVR+CX+888
 * Grp 5 level 2 for HKG JFK passengers - TVL+100512:0100:100512:0420+HKG+JFK+CX+888
 * 
 * Second TVL in GR5 at level 2 to send codeshare flight number and RBD.
 * When referring to a codeshare flight, two TVLs are required (one as defined in 5.28.2 for the marketing 
 * flight and one providing the operating flight information as defined in 5.28.3). If the marketing and 
 * operating carrier/flight are the same, only one TVL is used as defined in 5.28.2
 * 
 * Ex:The sold as flight (marketing carrier flight) is operated as flight 2345 and the RBD is K. 
 * This example only demonstrates the operating information however a preceding TVL would be required for 
 * the marketing information(TVL+++++2345:K’)
 * This example contains an illustration of both the operating and the marketing TVLs for a codeshare 
 * situation where the marketing carrier is DL and the operating carrier is KL..
 * TVL+010410:2235: 020410:1200+ATL+AMS+DL:KL+9362:K’
 * TVL+++++972:M’
 * 
 * TVL in GR.9 at level 3 is used to carry non-air segments (car, hotel, rail, etc.)
 * Car segment
 * TVL+290110:1050:310110:0900+ATL++ZE+:FCAR’
 * Hotel segment.
 * TVL+100910:1600:120910+MCI:HYATT REGENCY CROWN++HY+918W2:ROH’
 * 
 */
public class TVL extends Segment{

	private String groupName;
	private String level;
	private String departureDate;//ddmmyy
	private String departureTime;//hhmm
	private String arrivalDate;
	private String arrivalTime;
	private String dateVariation;
	//IATA airport / city code of departure	prior to crossing the border
	private String lastIataCode;
	private String lastIataName;//null set
	private String operatingAirlineCode;
	private String flightNumber;
	private String operationFlightSuffix;
	private String placeOfDeparture;
	private String placeOfArrival;
	private String airlineCode;
	private String serviceProductCode;//HH/ZE etc for hotel,car
	private String serviceIdentificationCode;

	public TVL(String name, Composite[] composites) {
		super(name, composites);
		
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getLevel() {
		return level;
	}


	public void setLevel(String level) {
		this.level = level;
	}

	public String getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}


	public String getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	public String getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(String arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getDateVariation() {
		return dateVariation;
	}

	public void setDateVariation(String dateVariation) {
		this.dateVariation = dateVariation;
	}

	public String getLastIataCode() {
		return lastIataCode;
	}

	public void setLastIataCode(String lastIataCode) {
		this.lastIataCode = lastIataCode;
	}


	public String getLastIataName() {
		return lastIataName;
	}

	public void setLastIataName(String lastIataName) {
		this.lastIataName = lastIataName;
	}

	public String getOperatingAirlineCode() {
		return operatingAirlineCode;
	}
	
	public void setOperatingAirlineCode(String operatingAirlineCode) {
		this.operatingAirlineCode = operatingAirlineCode;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public String getOperationFlightSuffix() {
		return operationFlightSuffix;
	}

	public void setOperationFlightSuffix(String operationFlightSuffix) {
		this.operationFlightSuffix = operationFlightSuffix;
	}

	public String getPlaceOfDeparture() {
		return placeOfDeparture;
	}

	public void setPlaceOfDeparture(String placeOfDeparture) {
		this.placeOfDeparture = placeOfDeparture;
	}

	public String getPlaceOfArrival() {
		return placeOfArrival;
	}

	public void setPlaceOfArrival(String placeOfArrival) {
		this.placeOfArrival = placeOfArrival;
	}

	public String getAirlineCode() {
		return airlineCode;
	}

	public void setAirlineCode(String airlineCode) {
		this.airlineCode = airlineCode;
	}

	public String getServiceProductCode() {
		return serviceProductCode;
	}

	public void setServiceProductCode(String serviceProductCode) {
		this.serviceProductCode = serviceProductCode;
	}

	public String getServiceIdentificationCode() {
		return serviceIdentificationCode;
	}

	public void setServiceIdentificationCode(String serviceIdentificationCode) {
		this.serviceIdentificationCode = serviceIdentificationCode;
	}

}
