package gov.gtas.services;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.gtas.error.ErrorUtils;
import gov.gtas.model.EdifactMessage;
import gov.gtas.model.Flight;
import gov.gtas.model.MessageStatus;
import gov.gtas.model.Passenger;
import gov.gtas.model.Pnr;
import gov.gtas.model.PnrMessage;
import gov.gtas.parsers.edifact.EdifactParser;
import gov.gtas.parsers.edifact.MessageVo;
import gov.gtas.parsers.pnrgov.PnrGovParser;
import gov.gtas.parsers.pnrgov.PnrMessageVo;
import gov.gtas.parsers.pnrgov.PnrVo;
import gov.gtas.parsers.util.FileUtils;
import gov.gtas.parsers.vo.passenger.AddressVo;
import gov.gtas.parsers.vo.passenger.CreditCardVo;
import gov.gtas.parsers.vo.passenger.FlightVo;
import gov.gtas.parsers.vo.passenger.PassengerVo;
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
    
    public MessageVo parse(String filePath) {
        this.pnrMessage = new PnrMessage();
        this.pnrMessage.setCreateDate(new Date());
        this.pnrMessage.setStatus(MessageStatus.RECEIVED);
        this.pnrMessage.setFilePath(filePath);
        
        MessageVo vo = null;
        try {
            byte[] raw = FileUtils.readSmallFile(filePath);
            String message = new String(raw, StandardCharsets.US_ASCII);
            
            EdifactParser<PnrMessageVo> parser = new PnrGovParser();
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
    
    public void load(MessageVo message) {
        PnrMessageVo m = (PnrMessageVo)message;
        try {
            for (PnrVo vo : m.getPnrRecords()) {
                Pnr pnr = null;

                pnr = utils.convertPnrVo(vo);
                this.pnrMessage.addPnr(pnr);
                
                for (AddressVo addressVo : vo.getAddresses()) {
                    pnr.addAddress(utils.convertAddressVo(addressVo));
                }
                
                for (PhoneVo phoneVo : vo.getPhoneNumbers()) {
                    pnr.addPhone(utils.convertPhoneVo(phoneVo));
                }
        
                for (CreditCardVo creditVo : vo.getCreditCards()) {
                    pnr.addCreditCard(utils.convertCreditVo(creditVo));
                }
                
                Set<Passenger> pax = new HashSet<>();        
                for (PassengerVo pvo : vo.getPassengers()) {
                    pax.add(utils.createNewPassenger(pvo));
                }
              
                pnr.setPassengers(pax);
        
                Flight f = null;
                for (FlightVo fvo : vo.getFlights()) {
                    f = utils.createNewFlight(fvo);
                    f.setPassengers(pax);
                    pnr.getFlights().add(f);
                }
                this.pnrMessage.setStatus(MessageStatus.LOADED);
            }

        } catch (Exception e) {
            handleException(e, MessageStatus.FAILED_LOADING);
        } finally {
            createMessage(pnrMessage);            
        }
    }

    private void handleException(Exception e, MessageStatus status) {
        this.pnrMessage.setPnrs(null);        
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