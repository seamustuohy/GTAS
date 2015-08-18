package gov.gtas.delegates;

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
import gov.gtas.util.ServiceUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

@Component
public class PnrServiceDelegate {
	
	@Resource
	PassengerService passengerService;
	
	@Resource
	private FlightService flightService;
	
	@Resource
	private PnrService pnrService;
	
	@Transactional
	public PnrDataVo saveOrUpdate(PnrDataVo pnrVo){
		Iterator it = pnrVo.getFlights().iterator();
		//check the associated flightVos
		while(it.hasNext()){
			FlightVo fvo = (FlightVo)it.next();
			Flight f=flightService.getUniqueFlightByCriteria(fvo.getCarrier(), fvo.getFlightNumber(), fvo.getOrigin(), fvo.getDestination(), fvo.getFlightDate());
			
			if(f != null && f.getId() != null){
				
				//The flight is already exist in database.Verify the passengers 
				Iterator it1 = pnrVo.getPassengers().iterator();
				while(it1.hasNext()){
					Passenger currentPassenger = ServiceUtils.mapPassengerFromVo((PassengerVo)it.next(), new Passenger());
					Passenger existingPassenger = this.getExistingPassengerInFlight(f,currentPassenger);
					if(existingPassenger != null && currentPassenger.equals(existingPassenger)){
						updateExistingPassenger(existingPassenger,currentPassenger);
					}
					else{
						f.getPassengers().add(currentPassenger);
					}
				}
				
				
			}
			else{
				//create new flight and passengers/pnrs
			}
		}
		return pnrVo;
	}

	private void updateExistingPassenger(Passenger existingPassenger,Passenger cp){
		existingPassenger.setChangeDate();
		existingPassenger.setUpdatedBy("SYSTEM");
		existingPassenger.setAge(cp.getAge());
		Iterator it = cp.getDocuments().iterator();
		while(it.hasNext()){
			existingPassenger.addDocument((Document) it.next());
		}
		Iterator pnrs = cp.getPnrs().iterator();
		while(pnrs.hasNext()){
			existingPassenger.getPnrs().add((Pnr) it.next());
		}
		
	}
	private Passenger getExistingPassengerInFlight(Flight f, Passenger currentPassenger){
		Passenger passengerInFlight = null;
		Iterator it = f.getPassengers().iterator();
		while(it.hasNext()){
			passengerInFlight = (Passenger)it.next();
			
		}
		return passengerInFlight;
	}
}
