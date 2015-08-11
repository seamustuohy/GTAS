package gov.gtas.services;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.gtas.model.Document;
import gov.gtas.model.Flight;
import gov.gtas.model.Passenger;
import gov.gtas.model.PnrData;
import gov.gtas.model.ReportingParty;
import gov.gtas.model.lookup.Airport;
import gov.gtas.model.lookup.FlightDirectionCode;
import gov.gtas.parsers.edifact.EdifactLexer;
import gov.gtas.parsers.edifact.segment.UNA;
import gov.gtas.parsers.exception.ParseException;
import gov.gtas.parsers.pnrgov.PnrVo;
import gov.gtas.parsers.vo.air.DocumentVo;
import gov.gtas.parsers.vo.air.FlightVo;
import gov.gtas.parsers.vo.air.PassengerVo;
import gov.gtas.parsers.vo.air.ReportingPartyVo;

@Service
public class LoaderUtils {
    private static final String LOADER_USER = "LOADER";
    
    @Autowired
    private AirportService airportService;

    public Passenger convertPassengerVo(PassengerVo vo) throws ParseException {
        Passenger p = new Passenger();
        BeanUtils.copyProperties(vo, p);
        
        String airportCode = vo.getDebarkation();
        p.setDebarkation(airportCode);
        Airport debark = getAirport(airportCode);
        if (debark != null) {
            p.setDebarkCountry(debark.getCountry());
        }

        airportCode = vo.getEmbarkation();
        p.setEmbarkation(airportCode);
        Airport embark = getAirport(airportCode);
        if (embark != null) {
            p.setEmbarkCountry(embark.getCountry());
        }
        
        p.setCitizenshipCountry(vo.getCitizenshipCountry());
        p.setResidencyCountry(vo.getResidencyCountry());
        
        for (DocumentVo dvo : vo.getDocuments()) {
            Document d = new Document();
            BeanUtils.copyProperties(dvo, d);
            d.setPassenger(p);
            d.setIssuanceCountry(dvo.getIssuanceCountry());
            p.getDocuments().add(d);
        }

        return p;
    }
    
    public ReportingParty convertReportingPartyVo(ReportingPartyVo vo) {
        ReportingParty rp = new ReportingParty();
        BeanUtils.copyProperties(vo, rp);
        return rp;
    }
    
    public Flight convertFlightVo(FlightVo vo) throws ParseException {
        // TODO: hardcoded for now
        String homeCountry = "USA";

        Flight f = new Flight();
        f.setCreatedBy(LOADER_USER);
        BeanUtils.copyProperties(vo, f);
        f.setCarrier(vo.getCarrier());
        
        f.setDestination(vo.getDestination());
        Airport dest = getAirport(vo.getDestination());
        String destCountry = null;
        if (dest != null) {
            destCountry = dest.getCountry();
            f.setDestinationCountry(destCountry);
        }
        
        f.setOrigin(vo.getOrigin());
        Airport origin = getAirport(vo.getOrigin());
        String originCountry = null;
        if (origin != null) {
            originCountry = origin.getCountry();
            f.setOriginCountry(originCountry);
        }
        
        if (homeCountry.equals(originCountry) && homeCountry.equals(destCountry)) {
            f.setDirection(FlightDirectionCode.C.name());
        } else if (homeCountry.equals(originCountry)) {
            f.setDirection(FlightDirectionCode.O.name());            
        } else if (homeCountry.equals(destCountry)) {
            f.setDirection(FlightDirectionCode.I.name());                        
        } else {
            f.setDirection(FlightDirectionCode.OTH.name());
        }
        
        // handle flight number specially: assume first 2 letters are carrier and rest is flight #
        StringBuffer buff = new StringBuffer();
        for (int j=0; j<4 - vo.getFlightNumber().length(); j++) {
            buff.append("0");
        }
        buff.append(vo.getFlightNumber());
        f.setFlightNumber(buff.toString());
        return f;
    }

    public PnrData convertPnrVo(PnrVo vo) throws ParseException {
        PnrData pnr = new PnrData();
        pnr.setCreatedBy(LOADER_USER);
        BeanUtils.copyProperties(vo, pnr);
        
        Airport origin = getAirport(vo.getOrigin());
        String originCountry = null;
        if (origin != null) {
            originCountry = origin.getCountry();
            pnr.setOriginCountry(originCountry);
        }
        
        pnr.setPassengerCount(vo.getPassengers().size());
        if (vo.getDateBooked() != null && vo.getDepartureDate() != null) {
//            LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            // TODO: won't work for leap years
            long diff = vo.getDepartureDate().getTime() - vo.getDateBooked().getTime(); 
            int days = (int)TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            pnr.setDaysBookedBeforeTravel(days);
        }

        return pnr;
    }
    
    private Airport getAirport(String a) throws ParseException {
        if (a == null) return null;
        
        Airport rv = null;
        if (a.length() == 3) {
            rv = airportService.getAirportByThreeLetterCode(a);
        } else if (a.length() == 4) {
            rv = airportService.getAirportByFourLetterCode(a);
        }

        return rv;
    }
    
    /**
     * Return everything from the start of the first BGM segment to the
     * start of the UNT trailing header segment.
     */
    public String getApisMessagePayload(String text) {
        if (text == null) return null;
        
        UNA una = EdifactLexer.getUnaSegment(text);
        int bgmIndex = EdifactLexer.getStartOfSegment("BGM", text, una);
        if (bgmIndex == -1) {
            return null;
        }

        int untIndex = EdifactLexer.getStartOfSegment("UNT", text, una);
        if (untIndex == -1) {
            return null;
        }
        
        return text.substring(bgmIndex, untIndex);
    }   
}
