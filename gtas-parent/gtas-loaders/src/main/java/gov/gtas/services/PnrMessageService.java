package gov.gtas.services;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.gtas.model.EdifactMessage;
import gov.gtas.model.MessageStatus;
import gov.gtas.model.PnrMessage;
import gov.gtas.parsers.edifact.EdifactParser;
import gov.gtas.parsers.pnrgov.PnrGovParser;
import gov.gtas.parsers.pnrgov.PnrMessageVo;
import gov.gtas.parsers.util.FileUtils;
import gov.gtas.repository.PnrMessageRepository;

@Service
public class PnrMessageService {
    private static final Logger logger = LoggerFactory.getLogger(PnrMessageService.class);
   
    @Autowired
    private AirportService airportService;

    @Autowired
    private PnrMessageRepository msgDao;
    
    private PnrMessage pnrMessage;
    
    public PnrMessageVo parsePnrMessage(String filePath) {
        this.pnrMessage = new PnrMessage();
        this.pnrMessage.setCreateDate(new Date());
        this.pnrMessage.setStatus(MessageStatus.RECEIVED);
        this.pnrMessage.setFilePath(filePath);
        
        PnrMessageVo vo = null;
        try {
            byte[] raw = FileUtils.readSmallFile(filePath);
            String message = new String(raw, StandardCharsets.US_ASCII);
            // TODO: get hash of entire message?
            
            EdifactParser<PnrMessageVo> parser = new PnrGovParser();
            vo = parser.parse(message);
            this.pnrMessage.setStatus(MessageStatus.PARSED);
            EdifactMessage em = new EdifactMessage();
            em.setTransmissionDate(vo.getTransmissionDate());
            em.setTransmissionSource(vo.getTransmissionSource());
            em.setMessageType(vo.getMessageType());
            em.setVersion(vo.getVersion());
            this.pnrMessage.setEdifactMessage(em);
            
        } catch (Exception e) {
            this.pnrMessage.setStatus(MessageStatus.FAILED_PARSING);
            String stacktrace = ExceptionUtils.getStackTrace(e);
            this.pnrMessage.setError(stacktrace);
            logger.error(stacktrace);
        } finally {
            createMessage(pnrMessage);
        }

        return vo;
    }
    
    @Transactional
    public PnrMessage createMessage(PnrMessage m) {
        return msgDao.save(m);
    }

}