package gov.gtas.services;

import gov.gtas.model.Document;
import gov.gtas.model.Flight;
import gov.gtas.model.Pax;
import gov.gtas.model.Traveler;
import gov.gtas.model.lookup.Airport;
import gov.gtas.model.lookup.Carrier;
import gov.gtas.model.lookup.Country;
import gov.gtas.model.lookup.Gender;
import gov.gtas.parsers.paxlst.PaxlstParser;
import gov.gtas.parsers.paxlst.PaxlstParserUNedifact;
import gov.gtas.parsers.paxlst.PaxlstParserUSedifact;
import gov.gtas.parsers.paxlst.vo.ApisMessageVo;
import gov.gtas.parsers.paxlst.vo.DocumentVo;
import gov.gtas.parsers.paxlst.vo.FlightVo;
import gov.gtas.parsers.paxlst.vo.PaxVo;
import gov.gtas.parsers.util.FileUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApisMessageService {
    @Autowired
    private CountryService countryService;
    
    @Autowired
    private AirportService airportService;

    @Autowired
    private CarrierService carrierService;
    
    @Autowired
    private PassengerService passengerService;
    
    @Autowired
    private FlightService flightService;
    
    public void parseAndLoadApisFile(String filePath) {
        byte[] raw = FileUtils.readSmallFile(filePath);
        String msg = new String(raw, StandardCharsets.US_ASCII);

        PaxlstParser parser = null;
        if (isUSEdifactFile(msg)) {
            parser = new PaxlstParserUSedifact(filePath);
        } else {
            parser= new PaxlstParserUNedifact(filePath);
        }
        
        // TODO: save apis message here with status?
        
        ApisMessageVo m = parser.parse();
        loadApisMessage(m);
    }
    
    private boolean isUSEdifactFile(String msg) {
        return (msg.contains("CDT") || msg.contains("PDT"));
    }
    
    private void loadApisMessage(ApisMessageVo m) {
        Set<Traveler> pax = new HashSet<>();
        
        for (PaxVo pvo : m.getPassengers()) {
            Traveler p = convertPaxVo(pvo);
            pax.add(p);
//            System.out.println(p);
//            passengerService.create(p);
        }

        Flight f = null;
        for (FlightVo fvo : m.getFlights()) {
            f = convertFlightVo(fvo);
            f.setPassengers(pax);
            System.out.println(f);
            flightService.create(f);
        }
    }
    
    public Traveler convertPaxVo(PaxVo vo) {
        Pax p = new Pax();
        BeanUtils.copyProperties(vo, p);
        p.setGender(Gender.valueOf(vo.getGender()));
        p.setDebarkCountry(convertCountry(vo.getDebarkCountry()));
        p.setDebarkation(convertAirport(vo.getDebarkation()));
        p.setEmbarkCountry(convertCountry(vo.getEmbarkCountry()));
        p.setEmbarkation(convertAirport(vo.getEmbarkation()));
        p.setCitizenshipCountry(convertCountry(vo.getCitizenshipCountry()));
        p.setResidencyCountry(convertCountry(vo.getResidencyCountry()));
        
        for (DocumentVo dvo : vo.getDocuments()) {
            Document d = new Document();
            BeanUtils.copyProperties(dvo, d);
            d.setIssuanceCountry(convertCountry(dvo.getIssuanceCountry()));
            p.getDocuments().add(d);
        }

        return p;
    }
    
    public Flight convertFlightVo(FlightVo vo) {
        Flight f = new Flight();
        BeanUtils.copyProperties(vo, f);
        f.setCarrier(convertCarrier(vo.getCarrier()));
        f.setDestination(convertAirport(vo.getDestination()));
        f.setDestinationCountry(convertCountry(vo.getDestinationCountry()));
        f.setOrigin(convertAirport(vo.getOrigin()));
        f.setOriginCountry(convertCountry(vo.getOriginCountry()));
        
        // handle flight number specially
        String tmp = vo.getFlightNumber();
        int i;
        for (i=0; i<tmp.length(); i++) {
            char c = tmp.charAt(i);
            if (!StringUtils.isAlpha(c + "")) {
                break;
            }
        }
        StringBuffer buff = new StringBuffer();
        String flightNum = tmp.substring(i);
        for (int j=0; j<4 - flightNum.length(); j++) {
            buff.append("0");
        }
        buff.append(flightNum);
        f.setFlightNumber(buff.toString());
        
        return f;
    }
    
    private Country convertCountry(String c) {
        if (c == null) return null;
        if (c.length() == 2) {
            return countryService.getCountryByTwoLetterCode(c);
        } else if (c.length() == 3) {
            return countryService.getCountryByThreeLetterCode(c);
        }
        
        return null;
    }
    
    private Airport convertAirport(String a) {
        if (a == null) return null;
        if (a.length() == 3) {
            return airportService.getAirportByThreeLetterCode(a);
        } else if (a.length() == 4) {
            return airportService.getAirportByFourLetterCode(a);
        }
        
        return null;
    }
    
    private Carrier convertCarrier(String c) {
        if (c == null) return null;
        if (c.length() == 3) {
            return carrierService.getCarrierByThreeLetterCode(c);
        } else if (c.length() == 2) {
            return carrierService.getCarrierByTwoLetterCode(c);
        }
        
        return null;
    }
}
