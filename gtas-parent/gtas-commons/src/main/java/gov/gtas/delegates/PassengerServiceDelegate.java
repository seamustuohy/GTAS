package gov.gtas.delegates;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import gov.gtas.delegates.vo.FlightVo;
import gov.gtas.delegates.vo.PassengerVo;
import gov.gtas.model.Document;
import gov.gtas.model.Flight;
import gov.gtas.model.Passenger;
import gov.gtas.services.FlightService;
import gov.gtas.services.PassengerService;
import gov.gtas.util.ServiceUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class PassengerServiceDelegate {

	@Resource
	PassengerService passengerService;
	
	@Resource
	private FlightService flightService;
	
	@Transactional
	public PassengerVo saveOrUpdate(PassengerVo passengerVo){
		FlightVo vo=new FlightVo();
		if(passengerVo != null && passengerVo.getFlights() != null && passengerVo.getFlights().size() >0 ){
			Iterator it = passengerVo.getFlights().iterator();
			while(it.hasNext()){
				vo=(FlightVo) it.next();
				break;
			}
			
			Flight f=flightService.getUniqueFlightByCriteria(vo.getCarrier(), vo.getFlightNumber(), vo.getOrigin(), vo.getDestination(), vo.getFlightDate());
			if(f != null && f.getId() != null){
				Passenger p = ServiceUtils.mapPassengerFromVo(passengerVo,new Passenger());
				Passenger existingPassenger = this.getPassengerIfExists(f,p);
				if(existingPassenger != null){
					updateExistingPassenger(p,existingPassenger);
				}
				passengerService.update(existingPassenger);
				
			}
			else{
				f=ServiceUtils.mapFlightFromVo(vo, new Flight());
				Passenger p =ServiceUtils.mapPassengerFromVo(passengerVo,new Passenger());
				f.getPassengers().add(p);
				p.getFlights().add(f);
				flightService.create(f);
			}
			
		}
		return passengerVo;
	}
	
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
		while(it.hasNext()){
			Document d= (Document) it.next();
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
