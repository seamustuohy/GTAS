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

import gov.gtas.model.EdifactMessage;
import gov.gtas.model.Flight;
import gov.gtas.model.MessageStatus;
import gov.gtas.model.Passenger;
import gov.gtas.model.Pnr;
import gov.gtas.model.PnrMessage;
import gov.gtas.parsers.edifact.EdifactParser;
import gov.gtas.parsers.edifact.MessageVo;
import gov.gtas.parsers.exception.ParseException;
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
            utils.checkHashCode(vo.getHashCode());
            
            this.pnrMessage.setStatus(MessageStatus.PARSED);
            this.pnrMessage.setHashCode(vo.getHashCode());            
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
            return null;
        } finally {
            createMessage(pnrMessage);
        }

        return vo;
    }
    
    public void load(MessageVo message) {
        PnrMessageVo m = (PnrMessageVo)message;
        for (PnrVo vo : m.getPnrRecords()) {
            Pnr pnr = null;
            try {
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
                    pax.add(utils.convertPassengerVo(pvo));
                }

                Flight f = null;
                for (FlightVo fvo : vo.getFlights()) {
                    f = utils.convertFlightVo(fvo);
                    f.setPassengers(pax);
                    pnr.getFlights().add(f);
                }
                this.pnrMessage.setStatus(MessageStatus.LOADED);
    
            } catch (Exception e) {
                this.pnrMessage.setStatus(MessageStatus.FAILED_LOADING);
                String stacktrace = ExceptionUtils.getStackTrace(e);
                this.pnrMessage.setError(stacktrace);
                logger.error(stacktrace);
            } finally {
                createMessage(pnrMessage);            
            }
        }
    }

    @Transactional
    private PnrMessage createMessage(PnrMessage m) {
        return msgDao.save(m);
    }

}