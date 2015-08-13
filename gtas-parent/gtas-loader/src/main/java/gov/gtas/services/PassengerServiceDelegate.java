package gov.gtas.services;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import gov.gtas.model.Document;
import gov.gtas.model.Flight;
import gov.gtas.model.Passenger;
import gov.gtas.parsers.vo.air.FlightVo;
import gov.gtas.parsers.vo.air.PassengerVo;

@Component
public class PassengerServiceDelegate {

	@Resource
	PassengerService passengerService;
	
	@Resource
	private FlightService flightService;
	
//	public PassengerVo saveOrUpdate(PassengerVo passengerVo){
//		System.out.println("XXXXXXXXXXXPassengerVo saveOrUpdateXXXXXXXXXXXXXX");
//		FlightVo vo=new FlightVo();
//		if(passengerVo != null && passengerVo.getFlights() != null && passengerVo.getFlights().size() >0 ){
//			Iterator it = passengerVo.getFlights().iterator();
//			while(it.hasNext()){
//				vo=(FlightVo) it.next();
//				break;
//			}
//			
//			List<Flight> flights=flightService.getUniqueFlightByCriteria(vo.getCarrier(), vo.getFlightNumber(), vo.getOrigin(), vo.getDestination(), vo.getFlightDate());
//			if(flights != null && flights.size() >0){
//				
//				Flight f = flights.get(0);
//				System.out.println("XXXXXXXXXXXFlight idXXXXXXXXXXXXXX"+f.getId());
//				Passenger p = ServiceUtils.mapPassengerFromVo(passengerVo,new Passenger());
//				Passenger existingPassenger = this.getPassengerIfExists(f,p);
//				if(existingPassenger != null){
//					updateExistingPassenger(p,existingPassenger);
//				}
//				//f.getPassengers().add(p);
//				passengerService.update(existingPassenger);
//				System.out.println("XXXXXXXXXXXUPDATEDXXXXXXXXXXXXXX"+existingPassenger.getId());
//				//flightService.update(f);
//				
//			}
//			else{
//				Flight f = ServiceUtils.mapFlightFromVo(vo, new Flight());
//				Passenger p =ServiceUtils.mapPassengerFromVo(passengerVo,new Passenger());
//				f.getPassengers().add(p);
//				p.getFlights().add(f);
//				//passengerService.create(p);
//				flightService.create(f);
//				System.out.println("XXXXXXXXXXXPassenger idXXXXXXXXXXXXXX"+p.getId());
//			}
//			
//		}
//		return passengerVo;
//	}
	
	private void updateExistingPassenger(Passenger source,Passenger target){
		target.setChangeDate();
		target.setCitizenshipCountry(source.getCitizenshipCountry());
		target.setDebarkation(source.getDebarkation());
		target.setDebarkCountry(source.getDebarkCountry());
		target.setEmbarkation(source.getEmbarkation());
		target.setEmbarkCountry(source.getEmbarkCountry());
		target.setGender(source.getGender());
		target.setMiddleName(source.getMiddleName());
		target.setPassengerType(source.getPassengerType());
		target.setResidencyCountry(source.getResidencyCountry());
		target.setSuffix(source.getSuffix());
		target.setTitle(source.getTitle());
		target.setUpdatedAt(new Date());
		target.setUpdatedBy("JUNIT");
		Iterator it = source.getDocuments().iterator();
		System.out.println("DDDDDDDDDDDDDDDDDDDDDD SIZE"+source.getDocuments().size());
		while(it.hasNext()){
			Document d= (Document) it.next();
			System.out.println("DDDDDDDDDDDDDDDDDDDDDD NUmBER"+d.getDocumentNumber());
			d.setPassenger(target);
			target.getDocuments().add(d);
		}
		
	}
	private Passenger getPassengerIfExists(Flight f,Passenger p){
		Iterator it = f.getPassengers().iterator();
		Passenger chkPsgr=null;
		while(it.hasNext()){
			chkPsgr=(Passenger) it.next();
			if(StringUtils.equalsIgnoreCase(chkPsgr.getFirstName(), p.getFirstName())
					&& StringUtils.equalsIgnoreCase(chkPsgr.getLastName(), p.getLastName())
							&& chkPsgr.getAge().equals(p.getAge())
									&& chkPsgr.getDob().equals(p.getDob())){
								break;
							}
		}
		return chkPsgr;
	}
}
