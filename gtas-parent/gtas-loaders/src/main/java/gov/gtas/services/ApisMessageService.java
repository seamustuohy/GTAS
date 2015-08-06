package gov.gtas.services;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.gtas.model.ApisMessage;
import gov.gtas.model.EdifactMessage;
import gov.gtas.model.Flight;
import gov.gtas.model.MessageStatus;
import gov.gtas.model.ReportingParty;
import gov.gtas.model.Traveler;
import gov.gtas.parsers.edifact.EdifactParser;
import gov.gtas.parsers.exception.ParseException;
import gov.gtas.parsers.paxlst.PaxlstMessageVo;
import gov.gtas.parsers.paxlst.PaxlstParserUNedifact;
import gov.gtas.parsers.paxlst.PaxlstParserUSedifact;
import gov.gtas.parsers.util.FileUtils;
import gov.gtas.parsers.util.ParseUtils;
import gov.gtas.parsers.vo.air.FlightVo;
import gov.gtas.parsers.vo.air.ReportingPartyVo;
import gov.gtas.parsers.vo.air.TravelerVo;
import gov.gtas.repository.ApisMessageRepository;

@Service
public class ApisMessageService {
    private static final Logger logger = LoggerFactory.getLogger(ApisMessageService.class);

    @Autowired
    private LoaderUtils utils;
    
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
            String payload = utils.getApisMessagePayload(message);
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

    public void loadApisMessage(PaxlstMessageVo m) {
        try {
            for (ReportingPartyVo rvo : m.getReportingParties()) {
                ReportingParty rp = utils.convertReportingPartyVo(rvo);
                rp.setApisMessage(this.apisMessage);
                this.apisMessage.getReportingParties().add(rp);
            }
            
            Set<Traveler> pax = new HashSet<>();        
            for (TravelerVo pvo : m.getPassengers()) {
                Traveler p = utils.convertTravelerVo(pvo);
                pax.add(p);
            }

            Flight f = null;
            for (FlightVo fvo : m.getFlights()) {
                f = utils.convertFlightVo(fvo);
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

    @Transactional
    private ApisMessage createMessage(ApisMessage m) {
        return msgDao.save(m);
    }

    private boolean isUSEdifactFile(String msg) {
        return (msg.contains("CDT") || msg.contains("PDT"));
    }
}
