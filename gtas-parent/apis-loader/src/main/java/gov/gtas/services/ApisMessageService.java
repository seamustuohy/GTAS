package gov.gtas.services;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.gtas.model.ApisMessage;
import gov.gtas.model.Flight;
import gov.gtas.model.Gender;
import gov.gtas.model.MessageStatus;
import gov.gtas.model.Passport;
import gov.gtas.model.Pax;
import gov.gtas.model.ReportingParty;
import gov.gtas.model.Traveler;
import gov.gtas.model.lookup.Airport;
import gov.gtas.model.lookup.Carrier;
import gov.gtas.model.lookup.Country;
import gov.gtas.parsers.edifact.EdifactParser;
import gov.gtas.parsers.edifact.segment.UNA;
import gov.gtas.parsers.paxlst.PaxlstParser;
import gov.gtas.parsers.paxlst.PaxlstParserUNedifact;
import gov.gtas.parsers.paxlst.PaxlstParserUSedifact;
import gov.gtas.parsers.paxlst.vo.ApisMessageVo;
import gov.gtas.parsers.paxlst.vo.DocumentVo;
import gov.gtas.parsers.paxlst.vo.FlightVo;
import gov.gtas.parsers.paxlst.vo.PaxVo;
import gov.gtas.parsers.paxlst.vo.ReportingPartyVo;
import gov.gtas.parsers.util.FileUtils;
import gov.gtas.parsers.util.ParseUtils;
import gov.gtas.repository.ApisMessageRepository;

@Service
public class ApisMessageService {
    private static final Logger logger = LoggerFactory.getLogger(ApisMessageService.class);

    @Autowired
    private CountryService countryService;
    
    @Autowired
    private AirportService airportService;

    @Autowired
    private CarrierService carrierService;
    
    @Autowired
    private ApisMessageRepository msgDao;
    
    private ApisMessage apisMessage;
    
    public ApisMessageVo parseApisMessage(String filePath) {
        this.apisMessage = new ApisMessage();
        this.apisMessage.setCreateDate(new Date());
        this.apisMessage.setStatus(MessageStatus.RECEIVED);
        this.apisMessage.setSource(filePath);
        
        ApisMessageVo vo = null;
        try {            
            byte[] raw = FileUtils.readSmallFile(filePath);
            this.apisMessage.setRaw(raw);

            String message = new String(raw, StandardCharsets.US_ASCII);
            String payload = getApisMessagePayload(message);
            if (payload == null) {
                throw new ParseException("Could not extract message payload. Missing BGM and/or UNT segments", -1);
            }
            String md5 = ParseUtils.getMd5Hash(payload, StandardCharsets.US_ASCII);
            this.apisMessage.setHashCode(md5);
            
            PaxlstParser parser = null;
            if (isUSEdifactFile(message)) {
                parser = new PaxlstParserUSedifact(message);
            } else {
                parser= new PaxlstParserUNedifact(message);
            }
    
            vo = parser.parse();
            this.apisMessage.setStatus(MessageStatus.PARSED);

        } catch (Exception e) {
            this.apisMessage.setStatus(MessageStatus.FAILED_PARSING);
            this.apisMessage.setError(ExceptionUtils.getStackTrace(e));
            e.printStackTrace();
        } finally {
            createMessage(apisMessage);
        }
        
        return vo;
    }

    @Transactional
    public ApisMessage createMessage(ApisMessage m) {
        return msgDao.save(m);
    }

    private boolean isUSEdifactFile(String msg) {
        return (msg.contains("CDT") || msg.contains("PDT"));
    }
    
    public void loadApisMessage(ApisMessageVo m) {
        try {
            for (ReportingPartyVo rvo : m.getReportingParties()) {
                ReportingParty rp = convertReportingPartyVo(rvo);
                rp.setApisMessage(this.apisMessage);
                this.apisMessage.getReportingParties().add(rp);
            }
            
            Set<Traveler> pax = new HashSet<>();        
            for (PaxVo pvo : m.getPassengers()) {
                Traveler p = convertPaxVo(pvo);
                pax.add(p);
            }
    
            Flight f = null;
            for (FlightVo fvo : m.getFlights()) {
                f = convertFlightVo(fvo);
                f.setPassengers(pax);
                this.apisMessage.getFlights().add(f);
            }
            this.apisMessage.setStatus(MessageStatus.LOADED);

        } catch (Exception e) {
            this.apisMessage.setStatus(MessageStatus.FAILED_LOADING);
            this.apisMessage.setError(ExceptionUtils.getStackTrace(e));
            e.printStackTrace();
        } finally {
            createMessage(apisMessage);            
        }
    }
    
    private Traveler convertPaxVo(PaxVo vo) {
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
            Passport d = new Passport();
            BeanUtils.copyProperties(dvo, d);
            d.setTraveler(p);
            d.setIssuanceCountry(convertCountry(dvo.getIssuanceCountry()));
            p.getDocuments().add(d);
        }

        return p;
    }
    
    private ReportingParty convertReportingPartyVo(ReportingPartyVo vo) {
        ReportingParty rp = new ReportingParty();
        BeanUtils.copyProperties(vo, rp);
        return rp;
    }
    
    private Flight convertFlightVo(FlightVo vo) {
        Flight f = new Flight();
        BeanUtils.copyProperties(vo, f);
        f.setCarrier(convertCarrier(vo.getCarrier()));
        Airport dest = convertAirport(vo.getDestination());
        f.setDestination(dest);
        if (dest != null) {
            f.setDestinationCountry(dest.getCountry());
        }
        Airport origin = convertAirport(vo.getOrigin());
        f.setOrigin(origin);
        if (origin != null) {
            f.setOriginCountry(origin.getCountry());
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
    
    /**
     * Return everything from the start of the first BGM segment to the
     * start of the UNT trailing header segment.
     */
    private String getApisMessagePayload(String text) {
        if (text == null) return null;
        
        UNA una = EdifactParser.getUnaSegment(text);
        int bgmIndex = EdifactParser.getStartOfSegment("BGM", text, una);
        if (bgmIndex == -1) {
            return null;
        }

        int untIndex = EdifactParser.getStartOfSegment("UNT", text, una);
        if (untIndex == -1) {
            return null;
        }
        
        return text.substring(bgmIndex, untIndex);
    }   
}
