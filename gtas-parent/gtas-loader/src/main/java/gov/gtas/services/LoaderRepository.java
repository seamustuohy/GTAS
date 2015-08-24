package gov.gtas.services;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import gov.gtas.model.ApisMessage;
import gov.gtas.model.Document;
import gov.gtas.model.Flight;
import gov.gtas.model.Message;
import gov.gtas.model.Passenger;
import gov.gtas.model.ReportingParty;
import gov.gtas.parsers.exception.ParseException;
import gov.gtas.parsers.vo.passenger.DocumentVo;
import gov.gtas.parsers.vo.passenger.FlightVo;
import gov.gtas.parsers.vo.passenger.PassengerVo;
import gov.gtas.parsers.vo.passenger.ReportingPartyVo;
import gov.gtas.repository.DocumentRepository;
import gov.gtas.repository.FlightRepository;
import gov.gtas.repository.MessageRepository;
import gov.gtas.repository.PassengerRepository;
import gov.gtas.repository.ReportingPartyRepository;
import gov.gtas.util.DateCalendarUtils;

@Repository
public class LoaderRepository {
	@PersistenceContext 
 	private EntityManager entityManager;
	
    @Autowired
    private ReportingPartyRepository rpDao;
    
    @Autowired
    private FlightRepository flightDao;

    @Autowired
    private PassengerRepository passengerDao;

    @Autowired
    private DocumentRepository docDao;

    // TODO: can't instantiate generic message repo?
    @Autowired
    private MessageRepository<ApisMessage> messageDao;

    @Autowired
    private LoaderUtils utils;

    public void checkHashCode(String hash) throws ParseException {
        Message m = messageDao.findByHashCode(hash);
        if (m != null) {
            throw new ParseException("duplicate message hashcode: " + hash);
        }
    }

    @Transactional
    public void processReportingParties(ApisMessage apisMessage, List<ReportingPartyVo> parties) {
        for (ReportingPartyVo rvo : parties) {
            ReportingParty existingRp = rpDao.getReportingParty(rvo.getPartyName(), rvo.getTelephone());
            if (existingRp == null) {
                ReportingParty newRp = utils.createNewReportingParty(rvo);
                apisMessage.getReportingParties().add(newRp);
            } else {
                utils.updateReportingParty(rvo, existingRp);
                apisMessage.addReportingParty(existingRp);
            }
        }
    }
    
    @Transactional
    public void processFlightsAndPassengers(ApisMessage apisMessage, List<FlightVo> flights, List<PassengerVo> passengers) throws ParseException {
        for (FlightVo fvo : flights) {
            Flight currentFlight = null;
            Flight existingFlight = flightDao.getFlightByCriteria(fvo.getCarrier(), fvo.getFlightNumber(), fvo.getOrigin(), fvo.getDestination(), fvo.getFlightDate());
            if (existingFlight == null) {
                Flight newFlight = utils.createNewFlight(fvo);
                currentFlight = newFlight;
            } else {
                utils.updateFlight(fvo, existingFlight);
                currentFlight = existingFlight;
            }
            apisMessage.getFlights().add(currentFlight);
            
            for (PassengerVo pvo : passengers) {
                Passenger currentPassenger = null;
                Passenger existingPassenger = findPassengerOnFlight(currentFlight, pvo);
                boolean isNewPax = false;
                if (existingPassenger == null) {
                    Passenger p = utils.createNewPassenger(pvo);
                    currentPassenger = p;
                    isNewPax = true;
                } else {
                    utils.updatePassenger(pvo, existingPassenger);
                    currentPassenger = existingPassenger;
                }
                currentFlight.getPassengers().add(currentPassenger);

                for (DocumentVo dvo : pvo.getDocuments()) {
                    if (isNewPax) {
                        currentPassenger.addDocument(utils.createNewDocument(dvo));
                    } else {
                        Document existingDoc = docDao.findByDocumentNumberAndPassenger(dvo.getDocumentNumber(), currentPassenger);
                        if (existingDoc == null) {
                            currentPassenger.addDocument(utils.createNewDocument(dvo));
                        } else {
                            utils.updateDocument(dvo, existingDoc);
                        }                        
                    }
                }
            }
        }
    }

    /**
     * TODO: update how we find passengers here, use document ,etc
     */
    private Passenger findPassengerOnFlight(Flight f, PassengerVo pvo) {
        if (f.getId() == null) {
            return null;
        }
        
        List<Passenger> pax = passengerDao.getPassengersByFlightIdAndName(f.getId(), pvo.getFirstName(), pvo.getLastName());
        if (pax != null && pax.size() >= 1) {
            return pax.get(0);
        } else {
            return null;
        }
    }
}
