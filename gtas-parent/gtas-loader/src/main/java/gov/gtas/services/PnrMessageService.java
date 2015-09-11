package gov.gtas.services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.gtas.error.ErrorUtils;
import gov.gtas.model.EdifactMessage;
import gov.gtas.model.MessageStatus;
import gov.gtas.model.Pnr;
import gov.gtas.parsers.edifact.EdifactParser;
import gov.gtas.parsers.edifact.MessageVo;
import gov.gtas.parsers.pnrgov.PnrGovParser;
import gov.gtas.parsers.pnrgov.PnrUtils;
import gov.gtas.parsers.pnrgov.PnrVo;
import gov.gtas.parsers.util.FileUtils;
import gov.gtas.repository.PnrRepository;

@Service
public class PnrMessageService implements MessageService {
    private static final Logger logger = LoggerFactory.getLogger(PnrMessageService.class);
   
    @Autowired
    private LoaderUtils utils;

    @Autowired
    private LoaderRepository loaderRepo;

    @Autowired
    private PnrRepository msgDao;
    
    private Pnr pnrMessage;
    private EdifactParser<PnrVo> parser = new PnrGovParser();
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
        return PnrUtils.getPnrs(message);
    }
    
    public MessageVo parse(String message) {
        this.pnrMessage = new Pnr();
        this.pnrMessage.setCreateDate(new Date());
        this.pnrMessage.setStatus(MessageStatus.RECEIVED);
        this.pnrMessage.setFilePath(this.filePath);
        
        MessageVo vo = null;
        try {
            vo = parser.parse(message);
            loaderRepo.checkHashCode(vo.getHashCode());
            
            this.pnrMessage.setStatus(MessageStatus.PARSED);
            this.pnrMessage.setHashCode(vo.getHashCode());            
            EdifactMessage em = new EdifactMessage();
            em.setTransmissionDate(vo.getTransmissionDate());
            em.setTransmissionSource(vo.getTransmissionSource());
            em.setMessageType(vo.getMessageType());
            em.setVersion(vo.getVersion());
            this.pnrMessage.setEdifactMessage(em);
            
        } catch (Exception e) {
            handleException(e, MessageStatus.FAILED_PARSING);
            return null;
        } finally {
            createMessage(pnrMessage);
        }

        return vo;
    }
    
    public void load(MessageVo messageVo) {
        try {
            PnrVo vo = (PnrVo)messageVo;
            // TODO: fix this, combine methods
            utils.convertPnrVo(this.pnrMessage, vo);
            loaderRepo.processPnr(this.pnrMessage, vo);
            loaderRepo.processFlightsAndPassengers(vo.getFlights(), vo.getPassengers(), 
                    this.pnrMessage.getFlights(), this.pnrMessage.getPassengers());
            this.pnrMessage.setStatus(MessageStatus.LOADED);

        } catch (Exception e) {
            handleException(e, MessageStatus.FAILED_LOADING);
        } finally {
            createMessage(pnrMessage);            
        }
    }

    private void handleException(Exception e, MessageStatus status) {
        pnrMessage.setFlights(null);
        pnrMessage.setPassengers(null);
        pnrMessage.setCreditCards(null);
        // etc
        
        pnrMessage.setStatus(status);
        String stacktrace = ErrorUtils.getStacktrace(e);
        pnrMessage.setError(stacktrace);
        logger.error(stacktrace);
    }

    @Transactional
    private void createMessage(Pnr m) {
        try {
            msgDao.save(m);
        } catch (Exception e) {
            handleException(e, MessageStatus.FAILED_LOADING);
            msgDao.save(m);
        }
    }
}