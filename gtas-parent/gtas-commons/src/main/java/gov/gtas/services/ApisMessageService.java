package gov.gtas.services;

import gov.gtas.model.Pax;
import gov.gtas.model.lookup.Airport;
import gov.gtas.model.lookup.Country;
import gov.gtas.model.lookup.Gender;
import gov.gtas.parsers.paxlst.PaxlstParser;
import gov.gtas.parsers.paxlst.PaxlstParserUNedifact;
import gov.gtas.parsers.paxlst.PaxlstParserUSedifact;
import gov.gtas.parsers.paxlst.vo.ApisMessageVo;
import gov.gtas.parsers.paxlst.vo.PaxVo;
import gov.gtas.parsers.util.FileUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

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
    private PassengerService passengerService;
    
    public void parseAndLoadApisFile(String filePath) {
        byte[] raw = FileUtils.readSmallFile(filePath);
        String msg = new String(raw, StandardCharsets.US_ASCII);

        PaxlstParser parser = null;
        if (isUSEdifactFile(msg)) {
            parser = new PaxlstParserUSedifact(filePath);
        } else {
            parser= new PaxlstParserUNedifact(filePath);
        }
        
        ApisMessageVo m = parser.parse();
        loadApisMessage(m);
    }
    
    private boolean isUSEdifactFile(String msg) {
        return (msg.contains("CDT") || msg.contains("PDT"));
    }
    
    private void loadApisMessage(ApisMessageVo m) {
        Set<Pax> pax = new HashSet<>();
        for (PaxVo vo : m.getPassengers()) {
            Pax p = convertPaxVo(vo);
            System.out.println(p);
            passengerService.create(p);
        }
    }
    
    public Pax convertPaxVo(PaxVo vo) {
        Pax p = new Pax();
        BeanUtils.copyProperties(vo, p);
        p.setGender(Gender.valueOf(vo.getGender()));
        p.setDebarkCountry(convertCountry(vo.getDebarkCountry()));
        p.setDebarkation(convertAirport(vo.getDebarkation()));
        p.setEmbarkCountry(convertCountry(vo.getEmbarkCountry()));
        p.setEmbarkation(convertAirport(vo.getEmbarkation()));
        
        return p;
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
}
