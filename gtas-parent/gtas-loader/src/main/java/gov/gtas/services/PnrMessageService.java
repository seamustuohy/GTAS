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
import gov.gtas.model.FlightLeg;
import gov.gtas.model.MessageStatus;
import gov.gtas.model.Pnr;
import gov.gtas.parsers.edifact.EdifactParser;
import gov.gtas.delegates.vo.MessageVo;
import gov.gtas.parsers.pnrgov.PnrGovParser;
import gov.gtas.parsers.pnrgov.PnrUtils;
import gov.gtas.delegates.vo.PnrVo;
import gov.gtas.parsers.util.FileUtils;
import gov.gtas.parsers.util.ParseUtils;
import gov.gtas.repository.PnrRepository;
import gov.gtas.util.LobUtils;


@Service
public class PnrMessageService implements MessageService {
    private static final Logger logger = LoggerFactory.getLogger(PnrMessageService.class);
   
    @Autowired
    private LoaderUtils utils;

    @Autowired
    private LoaderRepository loaderRepo;

    @Autowired
    private PnrRepository msgDao;
    
    private Pnr pnr;
    private EdifactParser<PnrVo> parser;
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
        
        String tmp = new String(raw, StandardCharsets.US_ASCII);        
        String message = ParseUtils.stripStxEtxHeaderAndFooter(tmp);
        return PnrUtils.getPnrs(message);
    }
    
    public MessageVo parse(String message) {
        this.pnr = new Pnr();
        this.pnr.setCreateDate(new Date());
        this.pnr.setStatus(MessageStatus.RECEIVED);
        this.pnr.setFilePath(this.filePath);
        
        MessageVo vo = null;
        try {
            parser = new PnrGovParser();
            vo = parser.parse(message);
            loaderRepo.checkHashCode(vo.getHashCode());
            this.pnr.setRaw(LobUtils.createClob(vo.getRaw()));

            this.pnr.setStatus(MessageStatus.PARSED);
            this.pnr.setHashCode(vo.getHashCode());            
            EdifactMessage em = new EdifactMessage();
            em.setTransmissionDate(vo.getTransmissionDate());
            em.setTransmissionSource(vo.getTransmissionSource());
            em.setMessageType(vo.getMessageType());
            em.setVersion(vo.getVersion());
            this.pnr.setEdifactMessage(em);
            
        } catch (Exception e) {
            handleException(e, MessageStatus.FAILED_PARSING);
            return null;
        } finally {
            createMessage(pnr);
        }

        return vo;
    }
    
    public void load(MessageVo messageVo) {
        try {
            PnrVo vo = (PnrVo)messageVo;
            // TODO: fix this, combine methods
            utils.convertPnrVo(this.pnr, vo);
            loaderRepo.processPnr(this.pnr, vo);
            loaderRepo.processFlightsAndPassengers(vo.getFlights(), vo.getPassengers(), 
                    this.pnr.getFlights(), this.pnr.getPassengers(), pnr.getFlightLegs());
            for (FlightLeg leg : pnr.getFlightLegs()) {
                leg.setPnr(this.pnr);
            }
            this.pnr.setStatus(MessageStatus.LOADED);

        } catch (Exception e) {
            handleException(e, MessageStatus.FAILED_LOADING);
        } finally {
            createMessage(pnr);            
        }
    }

    private void handleException(Exception e, MessageStatus status) {
        pnr.setFlights(null);
        pnr.setPassengers(null);
        pnr.setCreditCards(null);
        // etc
        
        pnr.setStatus(status);
        String stacktrace = ErrorUtils.getStacktrace(e);
        pnr.setError(stacktrace);
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