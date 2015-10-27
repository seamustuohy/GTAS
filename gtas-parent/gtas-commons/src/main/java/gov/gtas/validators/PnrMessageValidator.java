package gov.gtas.validators;

import gov.gtas.vo.*;
import gov.gtas.vo.passenger.*;
import gov.gtas.vo.InvalidObjectInfoVo;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * class PnrMessageValidator accepts the MessageVo and iterate through the objects it contain and 
 * verify the validity.If any of the object is not valid to persist then it creates an error object 
 * and add it to invalidObjects list and set null for the invalid object in MessageVo so that the 
 * calling application typically the loader omit this object from persistence objects.
 * The calling object should check the invalidObjects list and if its size is greater 
 * than 0 it should take care of extract the objects and persist them to DB for Audit purposes.
 * 
 * @author GTAS4
 *
 */

public class PnrMessageValidator {
	
	private List<InvalidObjectInfoVo> invalidObjects = new ArrayList<>();
	
	public PnrMessageVo getValidMessageVo(PnrMessageVo messageVo){
		if(messageVo == null || (!messageVo.validate())){
			InvalidObjectInfoVo error = new InvalidObjectInfoVo();
			error.setCreatedBy("PnrMessageValidator");
			error.setCreatedDate(new Date());
			String key = messageVo == null?"PNRMESSAGE":messageVo.getHashCode();
			String value = messageVo == null?"NULL":messageVo.toString();
			error.setInvalidObjectKey(key);
			error.setInvalidObjectType(messageVo.getClass().getSimpleName());
			error.setInvalidObjectValue(value);
			invalidObjects.add(error);
			messageVo=null;
		}else{
			String messageKey=messageVo.getHashCode();
			PnrVo pnrVo = messageVo.getPnr();
			List<AgencyVo> reportingParties = checkReportingValueObjects(pnrVo,messageKey);
			List<AddressVo> addresses = checkAddressValueObjects(pnrVo,messageKey);
			List<PhoneVo> phoneNumbers = checkPhoneNumberValueObject(pnrVo,messageKey);
			List<CreditCardVo> creditCards = checkCreditCardValueObjects(pnrVo,messageKey);
			List<FrequentFlyerVo> frequentFlyerDetails = checkFrequentFlyerValueObjects(pnrVo,messageKey);
		}
		
		 return messageVo;
	}

	private List<FrequentFlyerVo> checkFrequentFlyerValueObjects(PnrVo pnrVo,String messageKey){
		List<FrequentFlyerVo> flyerList = pnrVo.getFrequentFlyerDetails();
		List<FrequentFlyerVo> modifiedList = new ArrayList<>();
		if(flyerList != null && flyerList.size()>0){
			for(int i = 0;i<flyerList.size();i++){
				FrequentFlyerVo vo = flyerList.get(i);
				if(!vo.validate()){
					populateInvalidObjectList(vo, messageKey);
					vo=null;
				}
				else{
					modifiedList.add(vo);
				}
			}
		}
		return modifiedList;
	}
	private List<CreditCardVo> checkCreditCardValueObjects(PnrVo pnrVo,String messageKey){
		List<CreditCardVo> creditCards = pnrVo.getCreditCards();
		List<CreditCardVo> modifiedList = new ArrayList<>();
		if(creditCards != null && creditCards.size() >0){
			for(int i =0;i<creditCards.size();i++){
				CreditCardVo vo = creditCards.get(i);
				if(!vo.validate()){
					populateInvalidObjectList(vo, messageKey);
					vo=null;
				}
				else{
					modifiedList.add(vo);
				}
			}
		}
		return modifiedList;
	}
	private List<PhoneVo> checkPhoneNumberValueObject(PnrVo pnrVo,String messageKey){
		List<PhoneVo> phoneList = pnrVo.getPhoneNumbers();
		List<PhoneVo> modPhoneList = new ArrayList<>();
		if(phoneList != null && phoneList.size() >0){
			for(int i = 0; i<phoneList.size();i++){
				PhoneVo vo = phoneList.get(i);
				if(!vo.validate()){
					populateInvalidObjectList(vo, messageKey);
					vo=null;					
				}
				else{
					modPhoneList.add(vo);
				}
			}
		}
		return modPhoneList;
	}
	private List<AddressVo> checkAddressValueObjects(PnrVo pnrVo,String messageKey){
		List<AddressVo> addresses = pnrVo.getAddresses();
		List<AddressVo> modifiedList = new ArrayList<>();
		if(addresses != null && addresses.size() >0){
			for (int i = 0; i<addresses.size();i++){
				AddressVo vo = addresses.get(i);
				if(!vo.validate()){
					populateInvalidObjectList(vo, messageKey);
					vo=null;
				}
				else{
					modifiedList.add(vo);
				}
			}
		}
		return modifiedList;
	}
	private List<AgencyVo> checkReportingValueObjects(PnrVo pnrVo,String key){
		List<AgencyVo> reportingParties = pnrVo.getAgencies();
		List<AgencyVo> modifiedList = new ArrayList<>();
		if(reportingParties != null && reportingParties.size() > 0){
			for(int i = 0; i < reportingParties.size();i++){
				AgencyVo vo = reportingParties.get(i);
				if(!vo.validate()){
					populateInvalidObjectList(vo, key);
					vo=null;
				}
			}
		}
		return modifiedList;
	}
	
	private void populateInvalidObjectList(Object o,String hashCode){
		InvalidObjectInfoVo error = new InvalidObjectInfoVo();
		error.setCreatedBy(this.getClass().getSimpleName());
		error.setCreatedDate(new Date());
		error.setInvalidObjectKey(hashCode);
		error.setInvalidObjectType(o.getClass().getSimpleName());
		error.setInvalidObjectValue(o.toString());
		invalidObjects.add(error);
	}
}
