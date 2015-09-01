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
import gov.gtas.model.PnrMessage;
import gov.gtas.parsers.edifact.EdifactParser;
import gov.gtas.parsers.edifact.MessageVo;
import gov.gtas.parsers.pnrgov.PnrGovParser;
import gov.gtas.parsers.pnrgov.PnrMessageVo;
import gov.gtas.parsers.pnrgov.PnrUtils;
import gov.gtas.parsers.pnrgov.PnrVo;
import gov.gtas.parsers.util.FileUtils;
import gov.gtas.parsers.vo.passenger.AddressVo;
import gov.gtas.parsers.vo.passenger.CreditCardVo;
import gov.gtas.parsers.vo.passenger.PhoneVo;
import gov.gtas.repository.PnrMessageRepository;

@Service
public class PnrMessageService implements MessageService {
    private static final Logger logger = LoggerFactory.getLogger(PnrMessageService.class);
   
    @Autowired
    private LoaderUtils utils;

    @Autowired
    private LoaderRepository loaderRepo;

    @Autowired
    private PnrMessageRepository msgDao;
    
    private PnrMessage pnrMessage;
    private EdifactParser<PnrMessageVo> parser = new PnrGovParser();
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
        this.pnrMessage = new PnrMessage();
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
        PnrMessageVo m = (PnrMessageVo)messageVo;
        PnrVo vo = m.getPnr();
        try {
            Pnr pnr = utils.convertPnrVo(vo);
            this.pnrMessage.setPnr(pnr);
            loaderRepo.processPnr(pnr, vo);
            loaderRepo.processFlightsAndPassengers(vo.getFlights(), vo.getPassengers(), pnr.getFlights(), pnr.getPassengers());
            this.pnrMessage.setStatus(MessageStatus.LOADED);

        } catch (Exception e) {
            handleException(e, MessageStatus.FAILED_LOADING);
        } finally {
            createMessage(pnrMessage);            
        }
    }

    private void handleException(Exception e, MessageStatus status) {
        this.pnrMessage.setPnr(null);        
        this.pnrMessage.setStatus(status);
        String stacktrace = ErrorUtils.getStacktrace(e);
        this.pnrMessage.setError(stacktrace);
        logger.error(stacktrace);
    }

    @Transactional
    private void createMessage(PnrMessage m) {
        try {
            msgDao.save(m);
        } catch (Exception e) {
            handleException(e, MessageStatus.FAILED_LOADING);
            msgDao.save(m);
        }
    }
}