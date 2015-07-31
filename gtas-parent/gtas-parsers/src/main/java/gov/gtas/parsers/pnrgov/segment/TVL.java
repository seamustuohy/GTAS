package gov.gtas.parsers.pnrgov.segment;

import java.util.Date;
import java.util.List;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;
import gov.gtas.parsers.exception.ParseException;
import gov.gtas.parsers.pnrgov.PnrUtils;

/**
 * <p>
 * TVL: TRAVEL PRODUCT INFORMATION (Gr5 at Level 2 and Gr.12 at Level 4)
 * <p>
 * Specifies flight (departure date/time, origin, destination, operating airline
 * code, flight number, and operation suffix) for which passenger data is being
 * sent.
 * <p>
 * Dates and times in the TVL are in Local Time Departure and arrival points of
 * the transborder segment for a given country are the ones of the leg which
 * makes the segment eligible for push to a given country
 * <p>
 * For OPEN and ARNK segments, the date, place of departure and place of arrival
 * are conditional. For an Airline/ Flight Number / class/ date / segment, the
 * date, place of departure and place of arrival are mandatory.
 * <p>
 * When referring to a codeshare flight, two TVLs are required (one as difined
 * in 5.28.2 for the marketing flight and one providing the operating flight
 * information as defined in 5.28.3). If the marketing and operating
 * carrier/flight are the same, only one TVL is used as defined in 5.28.2.
 * <p>
 * Flown segments are to be included in history. Departure and arrival
 * city/airport codes as contained in the passenger’s booked itinerary.
 */
public class TVL extends Segment {
    private String groupName;
    private String level;
    
    private Date etd;
    private Date eta;
    private String origin;
    private String destination;
    private String carrier;
    private String operatingCarrier;
    private String flightNumber;
    private String reservationBookingDesignator;

    private String dateVariation;
    private String lastIataCode;
    private String lastIataName;

    private String operationFlightSuffix;
    private String placeOfDeparture;
    private String placeOfArrival;
    private String airlineCode;
    private String serviceProductCode;
    private String serviceIdentificationCode;

    public TVL(List<Composite> composites) throws ParseException {
        super(TVL.class.getSimpleName(), composites);
        for (int i = 0; i < numComposites(); i++) {
            Composite c = getComposite(i);
            
            switch (i) {
            case 0:
                Date[] tmp = getEtdEta(c);
                etd = tmp[0];
                eta = tmp[1];
                break;
            case 1:
                this.origin = c.getElement(0);
                break;
            case 2:
                this.destination = c.getElement(0);
                break;
            case 3:
                this.carrier = c.getElement(0);
                this.operatingCarrier = c.getElement(1);
                break;
            case 4:
                
            }                
        }
    }
    
    /**
     * @return [etd, eta]
     */
    public static Date[] getEtdEta(Composite c) throws ParseException {
        Date[] rv = new Date[2];
        String departureDt = null;
        String arrivalDt = null;
        
        departureDt = c.getElement(0);
        String time = c.getElement(1);
        if (time != null) {
            departureDt += time;
        }
        arrivalDt = c.getElement(2);
        time = c.getElement(3);
        if (time != null) {
            arrivalDt += time;
        }
        
        if (departureDt != null) {
            rv[0] = PnrUtils.parseDateTime(departureDt);
        }
        if (arrivalDt != null) {
            rv[1] = PnrUtils.parseDateTime(arrivalDt);
        }
        
        return rv;
    }
}
