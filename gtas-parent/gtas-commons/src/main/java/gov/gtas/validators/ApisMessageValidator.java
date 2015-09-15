package gov.gtas.validators;

import gov.gtas.delegates.vo.ApisMessageVo;
import gov.gtas.delegates.vo.DocumentVo;
import gov.gtas.delegates.vo.FlightVo;
import gov.gtas.delegates.vo.InvalidObjectInfoVo;
import gov.gtas.delegates.vo.PassengerVo;
import gov.gtas.delegates.vo.ReportingPartyVo;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * class ApisMessageValidator accepts the MessageVo and iterate through the objects it contain and 
 * verify the validity.If any of the object is not valid to persist then it creates an error object 
 * and add it to invalidObjects list and set null for the invalid object in MessageVo so that the 
 * calling application typically the parser omit this object from persistence objects.
 * The calling object should check the invalidObjects list and if its size is greater 
 * than 0 it should take care of extract the objects and persist them to DB for Audit purposes.
 * 
 * @author GTAS4
 *
 */

public class ApisMessageValidator {
	
	private List<InvalidObjectInfoVo> invalidObjects = new ArrayList<>();
	
	public ApisMessageVo getValidMessageVo(ApisMessageVo messageVo){
		if(messageVo == null || (!messageVo.validate())){
			InvalidObjectInfoVo error = new InvalidObjectInfoVo();
			error.setCreatedBy("ApisMessageValidator");
			error.setCreatedDate(new Date());
			String key = messageVo == null?"APISMESSAGE":messageVo.getHashCode();
			String value = messageVo == null?"NULL":messageVo.toString();
			error.setInvalidObjectKey(key);
			error.setInvalidObjectType(messageVo.getClass().getSimpleName());
			error.setInvalidObjectValue(value);
			invalidObjects.add(error);
			messageVo=null;
		}else{
			List<ReportingPartyVo> rList = checkReportingValueObjects(messageVo);
			List<FlightVo> fList = checkFlightValueObjects(messageVo);
			List<PassengerVo> pList = checkPassengerValueObjects(messageVo);
			messageVo.setFlights(fList);
			messageVo.setPassengers(pList);
			messageVo.setReportingParties(rList);
		}
		
		 return messageVo;
	}

	private List<ReportingPartyVo> checkReportingValueObjects(ApisMessageVo messageVo){
		List<ReportingPartyVo> reportingParties = messageVo.getReportingParties();
		List<ReportingPartyVo> modifiedList = new ArrayList<>();
		if(reportingParties != null && reportingParties.size() > 0){
			Iterator<ReportingPartyVo> it  = reportingParties.iterator();
			while(it.hasNext()){
				ReportingPartyVo vo = (ReportingPartyVo) it.next();
				if(!vo.validate()){
					populateInvalidObjectList(vo, messageVo);
					vo=null;
				}
				else{
					modifiedList.add(vo);
				}
			}
		}
		return modifiedList;
	}
	private List<FlightVo> checkFlightValueObjects(ApisMessageVo messageVo){
		List<FlightVo> flights = messageVo.getFlights();
		List<FlightVo> modifiedList = new ArrayList<>();
		if(flights != null && flights.size() > 0){
			Iterator<FlightVo> it = flights.iterator();
			while(it.hasNext()){
				FlightVo vo = (FlightVo) it.next();
				if(!vo.validate()){
					populateInvalidObjectList(vo, messageVo);
					vo=null;
				}
				else{
					modifiedList.add(vo);
				}
			}
		}
		return modifiedList;
	}
	private List<PassengerVo> checkPassengerValueObjects(ApisMessageVo messageVo){
		List<PassengerVo> passengers = messageVo.getPassengers();
		List<PassengerVo> modifiedList = new ArrayList<>();
		if(passengers != null && passengers.size() >0){
			Iterator<PassengerVo> it = passengers.iterator();
			while(it.hasNext()){
				PassengerVo vo = (PassengerVo) it.next();
				if(!vo.validate()){
					populateInvalidObjectList(vo, messageVo);
					vo=null;
				}
				else{
					if(!vo.getDocuments().isEmpty()){
						List<DocumentVo> docs = new ArrayList<>();
						Iterator docSet = vo.getDocuments().iterator();
						while(docSet.hasNext()){
							DocumentVo dvo = (DocumentVo) docSet.next();
							if(!dvo.validate()){
								populateInvalidObjectList(dvo, messageVo);
								dvo=null;
							}
							else{
								docs.add(dvo);
							}
						}
						vo.setDocuments(docs);
					}
					
					modifiedList.add(vo);
				}
			}
		}
		return modifiedList;
	}
	
	private void populateInvalidObjectList(Object o,ApisMessageVo messageVo){
		InvalidObjectInfoVo error = new InvalidObjectInfoVo();
		error.setCreatedBy(this.getClass().getSimpleName());
		error.setCreatedDate(new Date());
		error.setInvalidObjectKey(messageVo.getHashCode());
		error.setInvalidObjectType(o.getClass().getSimpleName());
		error.setInvalidObjectValue(o.toString());
		invalidObjects.add(error);
	}
	public List<InvalidObjectInfoVo> getInvalidObjects() {
		return invalidObjects;
	}

	public void setInvalidObjects(List<InvalidObjectInfoVo> invalidObjects) {
		this.invalidObjects = invalidObjects;
	}
	

}
