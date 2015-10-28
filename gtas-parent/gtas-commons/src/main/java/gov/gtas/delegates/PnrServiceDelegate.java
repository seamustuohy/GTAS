package gov.gtas.delegates;

import gov.gtas.vo.passenger.*;
import gov.gtas.vo.PnrVo;
import gov.gtas.model.Address;
import gov.gtas.model.CreditCard;
import gov.gtas.model.Document;
import gov.gtas.model.Email;
import gov.gtas.model.Flight;
import gov.gtas.model.FrequentFlyer;
import gov.gtas.model.Passenger;
import gov.gtas.model.Phone;
import gov.gtas.model.Pnr;
import gov.gtas.services.FlightService;
import gov.gtas.services.PassengerService;
import gov.gtas.services.PnrService;
import gov.gtas.util.PnrServiceDelegateUtils;
import gov.gtas.util.ServiceUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class PnrServiceDelegate {
	
	private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
	
	@Resource
	PassengerService passengerService;
	
	@Resource
	private FlightService flightService;
	
	@Resource
	private PnrService pnrService;

	@Transactional
	public PnrVo saveOrUpdate(PnrVo pnrVo){
		
		Pnr newReservation = ServiceUtils.mapPnrFromPnrVo(pnrVo,new Pnr() );
		//Iterate through the PnrDataVo and see if there is an existing flight for this reservation in the DB
		Iterator it = pnrVo.getFlights().iterator();
		while(it.hasNext()){
			FlightVo fvo = (FlightVo)it.next();
			Flight f=flightService.getUniqueFlightByCriteria(fvo.getCarrier(), fvo.getFlightNumber(), fvo.getOrigin(), fvo.getDestination(), fvo.getFlightDate());
			if(f != null && f.getId() != null){
				f.setUpdatedAt(new Date());
				f.setUpdatedBy("SYSTEM");
				//flight exists in DB ..verify the passengers with new reservation passengers
				Iterator it1 = pnrVo.getPassengers().iterator();
				while(it1.hasNext()){
					Passenger currentPassenger = ServiceUtils.mapPassengerFromVo((PassengerVo)it1.next(), new Passenger());
					Passenger existingPassenger = this.getExistingPassengerInFlight(f,currentPassenger);
					if(existingPassenger != null && existingPassenger.getId() != null){
						existingPassenger.setChangeDate();
						existingPassenger.setUpdatedBy("SYSTEM");
						//verify if PNR exist in DB
						Pnr existingReservation = getExistingReservationForPassenger(existingPassenger,newReservation);
						if(existingReservation == null){
							logger.info("********* OLD FLIGHT/OLD PASSENGER/NEW PNR *************");
							existingPassenger.setChangeDate();
							existingPassenger.setUpdatedBy("SYSTEM");
							existingPassenger.getPnrs().add(newReservation);
							newReservation.getPassengers().add(existingPassenger);
							newReservation.getFlights().add(f);
							f.getPnrs().add(newReservation);
							pnrService.create(newReservation);
						}
						else{
							logger.info("*********  OLD FLIGHT/OLD PASSENGER/OLD PNR ********* ");
							newReservation.setId(existingReservation.getId());
							pnrService.update(newReservation);
						}
					}
					else{
						logger.info("*********  OLD FLIGHT/NEW PASSENGER/NEW PNR ********* ");
						currentPassenger.setCreationDate();
						currentPassenger.setCreatedBy("JUNIT");
						currentPassenger.getPnrs().add(newReservation);
						currentPassenger.getFlights().add(f);
						newReservation.getFlights().add(f);
						newReservation.getPassengers().add(currentPassenger);
						f.getPassengers().add(currentPassenger);
						f.getPnrs().add(newReservation);
						pnrService.create(newReservation);
					}
				}
			}
			else{
				logger.info("*********  NEW FLIGHT/NEW PASSENGER/NEW PNR ********* ");
				//new flight..persist the new reservation data
				f=ServiceUtils.mapFlightFromVo(fvo, new Flight());
				Iterator passengers = pnrVo.getPassengers().iterator();
				while(passengers.hasNext()){
					Passenger newPassenger = ServiceUtils.mapPassengerFromVo((PassengerVo)passengers.next(), new Passenger());
					newPassenger.getPnrs().add(newReservation);
					newReservation.getPassengers().add(newPassenger);
					f.getPassengers().add(newPassenger);
				}
				f.getPnrs().add(newReservation);
				newReservation.getFlights().add(f);
				pnrService.create(newReservation);
			}
		}
		return pnrVo;
	}
	
	@Transactional
    public PnrVo getAllPnrData(Long pnrId) {
	    Pnr pnr = pnrService.findById(pnrId);
	    PnrVo rv = new PnrVo();
	    if (pnr != null) {
	        String[] ignore = new String[] { "creditCards", "frequentFlyers", "addresses", "phones", "emails", "pnrMessage", "flights", "passengers" };
	        BeanUtils.copyProperties(pnr, rv, ignore);
	        for (CreditCard entity : pnr.getCreditCards()) {
	            CreditCardVo vo = new CreditCardVo();
	            BeanUtils.copyProperties(entity, vo);
	            rv.addCreditCard(vo);
	        }
            for (FrequentFlyer entity : pnr.getFrequentFlyers()) {
                FrequentFlyerVo vo = new FrequentFlyerVo();
                BeanUtils.copyProperties(entity, vo);
                rv.getFrequentFlyerDetails().add(vo);
            }
            for (Address entity : pnr.getAddresses()) {
                AddressVo vo = new AddressVo();
                BeanUtils.copyProperties(entity, vo);
                rv.getAddresses().add(vo);
            }
            for (Phone entity : pnr.getPhones()) {
                PhoneVo vo = new PhoneVo();
                BeanUtils.copyProperties(entity, vo);
                rv.getPhoneNumbers().add(vo);
            }
           /* for (Email entity : pnr.getEmails()) {
                EmailVo vo = new EmailVo();
                BeanUtils.copyProperties(entity, vo);
                rv.getEmails().add(vo);
            }     */       
	    }
	    return rv;
	}
	
    @Transactional
    public List<PnrVo> getRecordLocators(Long passengerId) {
        List<PnrVo> rv = new ArrayList<>();
        List<Pnr> pnrs = pnrService.findByPassengerId(passengerId);
        for (Pnr pnr : pnrs) {
            PnrVo vo = new PnrVo();
            //vo.setId(pnr.getId());
            vo.setRecordLocator(pnr.getRecordLocator());
            rv.add(vo);
        }
        return rv;
    }

	private void updateExistingPassenger(Passenger existingPassenger,Passenger cp,Flight f){
		existingPassenger.setChangeDate();
		existingPassenger.setUpdatedBy("SYSTEM");
		existingPassenger.setAge(cp.getAge());
		Iterator it = cp.getDocuments().iterator();
		while(it.hasNext()){
			existingPassenger.addDocument((Document) it.next());
		}
		Iterator pnrs = cp.getPnrs().iterator();
		while(pnrs.hasNext()){
			Pnr p =(Pnr) it.next();
			p.getFlights().add(f);
			p.addPassenger(existingPassenger);
			existingPassenger.getPnrs().add(p);
		}
	}
	
	private Passenger getExistingPassengerInFlight(Flight f, Passenger currentPassenger){
		Passenger passengerInFlight = null;
		Iterator it = f.getPassengers().iterator();
		while(it.hasNext()){
			Passenger existing = (Passenger)it.next();
			
			if(StringUtils.equals(currentPassenger.getFirstName(), existing.getFirstName())
					&& StringUtils.equals(currentPassenger.getLastName(), existing.getLastName())
							&& StringUtils.equals(currentPassenger.getGender(), existing.getGender())
							&& StringUtils.equals(currentPassenger.getMiddleName(), existing.getMiddleName())
							&& currentPassenger.getAge() == existing.getAge()
							&& currentPassenger.getDob().equals(existing.getDob())){
				
				return existing;
				//break;
			}
		}
		return passengerInFlight;
	}
	
	private Pnr getExistingReservationForPassenger(Passenger existingPassenger,Pnr newReservation){
		Pnr p = null;
		Iterator it = existingPassenger.getPnrs().iterator();
		while(it.hasNext()){
			p=(Pnr)it.next();
			if(p.equals(newReservation)){
				PnrServiceDelegateUtils.updateExistingReservation(newReservation,p);
				break;
			}
		}
		return p;
	}
}
