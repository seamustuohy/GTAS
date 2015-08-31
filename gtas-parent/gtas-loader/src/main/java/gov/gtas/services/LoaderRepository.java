package gov.gtas.services;

import java.util.List;
import java.util.Set;

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
    public void processFlightsAndPassengers(List<FlightVo> flights, List<PassengerVo> passengers, Set<Flight> messageFlights) throws ParseException {
        for (FlightVo fvo : flights) {
            Flight existingFlight = flightDao.getFlightByCriteria(fvo.getCarrier(), fvo.getFlightNumber(), fvo.getOrigin(), fvo.getDestination(), fvo.getFlightDate());

            if (existingFlight == null) {
                // new flight: just add new pax + docs
                Flight newFlight = utils.createNewFlight(fvo);
                messageFlights.add(newFlight);
                for (PassengerVo pvo : passengers) {
                    Passenger p = utils.createNewPassenger(pvo);
                    for (DocumentVo dvo : pvo.getDocuments()) {
                        p.addDocument(utils.createNewDocument(dvo));
                    }
                    newFlight.getPassengers().add(p);
                }
                                
            } else {
                // existing flight: lookup pax and update
                utils.updateFlight(fvo, existingFlight);
                messageFlights.add(existingFlight);
                for (PassengerVo pvo : passengers) {
                    Passenger existingPassenger = findPassengerOnFlight(existingFlight, pvo);
                    if (existingPassenger == null) {
                        Passenger p = utils.createNewPassenger(pvo);
                        for (DocumentVo dvo : pvo.getDocuments()) {
                            p.addDocument(utils.createNewDocument(dvo));
                        }
                        passengerDao.save(p);
                        p.getFlights().add(existingFlight);
                        existingFlight.getPassengers().add(p);
                        
                    } else {
                        utils.updatePassenger(pvo, existingPassenger);
                        for (DocumentVo dvo : pvo.getDocuments()) {
                            Document existingDoc = docDao.findByDocumentNumberAndPassenger(dvo.getDocumentNumber(), existingPassenger);
                            if (existingDoc == null) {
                                existingPassenger.addDocument(utils.createNewDocument(dvo));
                            } else {
                                utils.updateDocument(dvo, existingDoc);
                            }                        
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
