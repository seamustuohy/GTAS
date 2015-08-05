package gov.gtas.services;

import java.nio.charset.StandardCharsets;
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
import gov.gtas.model.Document;
import gov.gtas.model.EdifactMessage;
import gov.gtas.model.Flight;
import gov.gtas.model.MessageStatus;
import gov.gtas.model.ReportingParty;
import gov.gtas.model.Traveler;
import gov.gtas.model.lookup.Airport;
import gov.gtas.model.lookup.FlightDirectionCode;
import gov.gtas.parsers.edifact.EdifactLexer;
import gov.gtas.parsers.edifact.EdifactParser;
import gov.gtas.parsers.edifact.segment.UNA;
import gov.gtas.parsers.exception.ParseException;
import gov.gtas.parsers.paxlst.PaxlstMessageVo;
import gov.gtas.parsers.paxlst.PaxlstParserUNedifact;
import gov.gtas.parsers.paxlst.PaxlstParserUSedifact;
import gov.gtas.parsers.util.FileUtils;
import gov.gtas.parsers.util.ParseUtils;
import gov.gtas.parsers.vo.air.DocumentVo;
import gov.gtas.parsers.vo.air.FlightVo;
import gov.gtas.parsers.vo.air.ReportingPartyVo;
import gov.gtas.parsers.vo.air.TravelerVo;
import gov.gtas.repository.ApisMessageRepository;

@Service
public class ApisMessageService {
    private static final Logger logger = LoggerFactory.getLogger(ApisMessageService.class);

    private String homeCountry;
    
    @Autowired
    private AirportService airportService;

    @Autowired
    private ApisMessageRepository msgDao;
    
    private ApisMessage apisMessage;
    
    public PaxlstMessageVo parseApisMessage(String filePath) {
        this.apisMessage = new ApisMessage();
        this.apisMessage.setCreateDate(new Date());
        this.apisMessage.setStatus(MessageStatus.RECEIVED);
        this.apisMessage.setFilePath(filePath);
        
        PaxlstMessageVo vo = null;
        try {            
            byte[] raw = FileUtils.readSmallFile(filePath);
            String message = new String(raw, StandardCharsets.US_ASCII);
            String payload = getApisMessagePayload(message);
            if (payload == null) {
                throw new ParseException("Could not extract message payload. Missing BGM and/or UNT segments");
            }
            String md5 = ParseUtils.getMd5Hash(payload, StandardCharsets.US_ASCII);
            this.apisMessage.setHashCode(md5);
            
            EdifactParser<PaxlstMessageVo> parser = null;
            if (isUSEdifactFile(message)) {
                parser = new PaxlstParserUSedifact();
            } else {
                parser = new PaxlstParserUNedifact();                
            }
    
            vo = parser.parse(message);
            this.apisMessage.setStatus(MessageStatus.PARSED);
            EdifactMessage em = new EdifactMessage();
            em.setTransmissionDate(vo.getTransmissionDate());
            em.setTransmissionSource(vo.getTransmissionSource());
            em.setMessageType(vo.getMessageType());
            em.setVersion(vo.getVersion());
            this.apisMessage.setEdifactMessage(em);

        } catch (Exception e) {
            this.apisMessage.setStatus(MessageStatus.FAILED_PARSING);
            String stacktrace = ExceptionUtils.getStackTrace(e);
            this.apisMessage.setError(stacktrace);
            logger.error(stacktrace);
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
    
    public void loadApisMessage(PaxlstMessageVo m) {
        try {
            for (ReportingPartyVo rvo : m.getReportingParties()) {
                ReportingParty rp = convertReportingPartyVo(rvo);
                rp.setApisMessage(this.apisMessage);
                this.apisMessage.getReportingParties().add(rp);
            }
            
            Set<Traveler> pax = new HashSet<>();        
            for (TravelerVo pvo : m.getPassengers()) {
                Traveler p = convertTravelerVo(pvo);
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
            String stacktrace = ExceptionUtils.getStackTrace(e);
            this.apisMessage.setError(stacktrace);
            logger.error(stacktrace);
        } finally {
            createMessage(apisMessage);            
        }
    }
    
    private Traveler convertTravelerVo(TravelerVo vo) throws ParseException {
        Traveler p = new Traveler();
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
            d.setTraveler(p);
            d.setIssuanceCountry(dvo.getIssuanceCountry());
            p.getDocuments().add(d);
        }

        return p;
    }
    
    private ReportingParty convertReportingPartyVo(ReportingPartyVo vo) {
        ReportingParty rp = new ReportingParty();
        BeanUtils.copyProperties(vo, rp);
        return rp;
    }
    
    private Flight convertFlightVo(FlightVo vo) throws ParseException {
        // TODO: hardcoded for now
        homeCountry = "USA";

        Flight f = new Flight();
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
        
        if (vo.isOverFlight()) {
            f.setDirection(FlightDirectionCode.OF.name());
        } else {
            if (homeCountry.equals(originCountry) && homeCountry.equals(destCountry)) {
                f.setDirection(FlightDirectionCode.C.name());
            } else if (homeCountry.equals(originCountry)) {
                f.setDirection(FlightDirectionCode.O.name());            
            } else if (homeCountry.equals(destCountry)) {
                f.setDirection(FlightDirectionCode.I.name());                        
            }
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
    private String getApisMessagePayload(String text) {
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
