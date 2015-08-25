package gov.gtas.delegates;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import gov.gtas.delegates.vo.FlightVo;
import gov.gtas.delegates.vo.PnrDataVo;
import gov.gtas.delegates.vo.PassengerVo;
import gov.gtas.model.Document;
import gov.gtas.model.Flight;
import gov.gtas.model.Passenger;
import gov.gtas.model.Pnr;
import gov.gtas.services.FlightService;
import gov.gtas.services.PassengerService;
import gov.gtas.services.PnrService;
import gov.gtas.util.PnrServiceDelegateUtils;
import gov.gtas.util.ServiceUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
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
	public PnrDataVo saveOrUpdate(PnrDataVo pnrVo){
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
							logger.info("########## OLD FLIGHT/OLD PASSENGER/NEW PNR ###########");
							existingPassenger.setChangeDate();
							existingPassenger.setUpdatedBy("SYSTEM");
							existingPassenger.getPnrs().add(newReservation);
							newReservation.getPassengers().add(existingPassenger);
							newReservation.getFlights().add(f);
							f.getPnrs().add(newReservation);
							pnrService.create(newReservation);
						}
						else{
							logger.info("########## OLD FLIGHT/OLD PASSENGER/OLD PNR ###########");
							newReservation.setId(existingReservation.getId());
							newReservation.setChangeDate();
							newReservation.setUpdatedBy("SYSTEM");
							pnrService.update(newReservation);
						}
					}
					else{
						logger.info("########## OLD FLIGHT/NEW PASSENGER/NEW PNR ###########");
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
				
				logger.info("########## NEW FLIGHT/NEW PASSENGER/NEW PNR ###########");
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
