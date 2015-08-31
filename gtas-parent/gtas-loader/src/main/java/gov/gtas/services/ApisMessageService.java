package gov.gtas.services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.gtas.error.ErrorUtils;
import gov.gtas.model.ApisMessage;
import gov.gtas.model.EdifactMessage;
import gov.gtas.model.MessageStatus;
import gov.gtas.parsers.edifact.EdifactParser;
import gov.gtas.parsers.edifact.MessageVo;
import gov.gtas.parsers.paxlst.ApisMessageVo;
import gov.gtas.parsers.paxlst.PaxlstParserUNedifact;
import gov.gtas.parsers.paxlst.PaxlstParserUSedifact;
import gov.gtas.parsers.util.FileUtils;
import gov.gtas.repository.ApisMessageRepository;

@Service
public class ApisMessageService implements MessageService {
    private static final Logger logger = LoggerFactory.getLogger(ApisMessageService.class);

    @Autowired
    private ApisMessageRepository msgDao;

    @Autowired
    private LoaderRepository loaderRepo;

    private ApisMessage apisMessage;
    private String filePath;

    public List<String> preprocess(String filePath) {
        this.filePath = filePath;
        byte[] raw = null;
        try {
            raw = FileUtils.readSmallFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        String message = new String(raw, StandardCharsets.US_ASCII);
        return Arrays.asList(message);
    }
    
    public MessageVo parse(String message) {
        this.apisMessage = new ApisMessage();
        this.apisMessage.setCreateDate(new Date());
        this.apisMessage.setStatus(MessageStatus.RECEIVED);
        this.apisMessage.setFilePath(filePath);
        
        MessageVo vo = null;
        try {            
            EdifactParser<ApisMessageVo> parser = null;
            if (isUSEdifactFile(message)) {
                parser = new PaxlstParserUSedifact();
            } else {
                parser = new PaxlstParserUNedifact();                
            }
    
            vo = parser.parse(message);
            loaderRepo.checkHashCode(vo.getHashCode());

            this.apisMessage.setStatus(MessageStatus.PARSED);
            this.apisMessage.setHashCode(vo.getHashCode());
            EdifactMessage em = new EdifactMessage();
            em.setTransmissionDate(vo.getTransmissionDate());
            em.setTransmissionSource(vo.getTransmissionSource());
            em.setMessageType(vo.getMessageType());
            em.setVersion(vo.getVersion());
            this.apisMessage.setEdifactMessage(em);

        } catch (Exception e) {
            handleException(e, MessageStatus.FAILED_PARSING);
            return null;
        } finally {
            createMessage(apisMessage);
        }
        
        return vo;
    }

    public void load(MessageVo messageVo) {
        ApisMessageVo m = (ApisMessageVo)messageVo;
        try {
            loaderRepo.processReportingParties(this.apisMessage, m.getReportingParties());
            loaderRepo.processFlightsAndPassengers(m.getFlights(), m.getPassengers(), this.apisMessage.getFlights(), this.apisMessage.getPassengers());
            this.apisMessage.setStatus(MessageStatus.LOADED);

        } catch (Exception e) {
            handleException(e, MessageStatus.FAILED_LOADING);
        } finally {
            createMessage(apisMessage);            
        }
    }  

    private void handleException(Exception e, MessageStatus status) {
        this.apisMessage.setFlights(null);
        this.apisMessage.setStatus(status);
        String stacktrace = ErrorUtils.getStacktrace(e);
        this.apisMessage.setError(stacktrace);
        logger.error(stacktrace);
    }

    @Transactional
    private void createMessage(ApisMessage m) {
        try {
            msgDao.save(m);
        } catch (Exception e) {
            handleException(e, MessageStatus.FAILED_LOADING);
            msgDao.save(m);
        }
    }

    private boolean isUSEdifactFile(String msg) {
        return (msg.contains("CDT") || msg.contains("PDT"));
    }
}
