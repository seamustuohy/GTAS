package gov.gtas.parsers.util;

import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;
import gov.gtas.parsers.exception.ParseException;
import gov.gtas.parsers.pnrgov.PnrVo;
import gov.gtas.parsers.vo.air.AddressVo;
import gov.gtas.parsers.vo.air.FlightVo;
import gov.gtas.parsers.vo.air.PnrReportingAgentVo;

public class PnrMessageBuilder {
//
//	private static final Logger logger = LoggerFactory.getLogger(PnrMessageBuilder.class);
//	private static final String DATE_TIME_FORMAT = "ddMMyyhhmm";
//    enum GROUP {
//        NONE,ONE,TWO,THREE,FOUR,FIVE,SIX,SEVEN,EIGHT,NINE,TEN,ELEVEN,TWELVE
//    }
//	PnrMessageVo message;
//	List<Segment> segments;
//	private GROUP currentGroup;
//	
//	public PnrMessageBuilder(PnrMessageVo messageVo,List<Segment> segmentList){
//		this.message=messageVo;
//		this.segments=segmentList;
//	}
//	
//	public void buildMessageObject(){
//		//initially set group to none for segments UNA UNB UNG UNH MSG ORG TVL EQN UNT UNE UNZ
//		currentGroup = GROUP.NONE;
//    	String flightSavingMode="";
//    	int counter = -1;
//    
//        for (ListIterator<Segment> i=segments.listIterator(); i.hasNext(); ) {
//            Segment s = i.next();
//            String segmentName=s.getName();
//             
//             switch (segmentName) {
//             case "MSG":
//                 if(currentGroup == GROUP.NONE){
//                	 message.setUpdateMode(updateFlightMode(s));
//                	 System.out.println("Flight mode="+message.getUpdateMode());
//                 }
//                 if(currentGroup ==GROUP.EIGHT){
//                	 currentGroup = GROUP.NINE;
//                 }
//                 if(currentGroup == GROUP.NINE){
//                	 //use this TVL  for a hotel/car segment.(MSG+8')
//                 }
//                 break;
//             case "ABI":
//            	 if(currentGroup == GROUP.NINE){
//            		 currentGroup = GROUP.TEN;
//            	 }
//            	 break;
//             case "SAC":
//            	 if(currentGroup == GROUP.TEN){
//            		 currentGroup = GROUP.ELEVEN;
//            	 }
//            	 break;
//             case "ORG":
//            	 if(currentGroup == GROUP.NONE){
//            		 updateReportingParty(currentGroup.name(),s);
//            		 
//            	 }
//            	 if(currentGroup == GROUP.ONE){
//            		 updateAgencyInformation(counter,s);
//            		
//            	 }
//            	 if(currentGroup == GROUP.SIX){
//            		// updateAgencyInformation(counter,s);
//            		 
//            	 }
//                 break;
//             case "TVL":
//            	 if(currentGroup == GROUP.NONE){
//            		 updateFlightDetails(currentGroup.name(),s);
//            	 }
//            	 if(currentGroup == GROUP.FOUR){
//            		 currentGroup=GROUP.FIVE;
//            		// printInfo(s,currentGroup.name());
//            		 
//            	 }
//            	 if(currentGroup == GROUP.NINE){
//            		// printInfo(s,currentGroup.name());
//            	 }
//            	 if(currentGroup == GROUP.ELEVEN){
//            		 currentGroup = GROUP.TWELVE;
//            		// printInfo(s,currentGroup.name());
//            	 }
//            	 break;
//          
//             case "EQN":
//            	 if(currentGroup == GROUP.NONE){
//            	     updateNumberOfPassengers(s);
//            	 }
//            	 if(currentGroup == GROUP.SEVEN){
//            		 currentGroup =GROUP.EIGHT;
//            	 }
//            	 break;
//             case "SRC":
//            	 counter++;
//            	 currentGroup =GROUP.ONE;
//            	 message.getPassengers().add(new PnrVo());
//            	 updatePassengerInfo(counter,s,currentGroup.name());
//            	 break;
//             case "RCI":
//            	 if(currentGroup == GROUP.ONE){
//            		 updatePnrReservarionInfo(counter,s,currentGroup.name());
//            	 }
//            	 break;
//             case "SSR":
//            	 break;
//             case "DAT":
//            	 if(currentGroup == GROUP.THREE){
//            		 //ticket Issue Date
//            	 }
//            	 if(currentGroup == GROUP.FIVE){
//            		 currentGroup = GROUP.SIX;
//            		 updatePnrCheckInDate(counter,s);
//            	 }
//            	 if(currentGroup == GROUP.TEN){
//            		 //history data
//            		 updatePnrCheckInDate(counter,s);
//            	 }
//            	 break;
//             case "IFT":
//             	addFreeTextToPnr(counter,s);
//             	break;
//             case "ADD":
//            	 if(currentGroup == GROUP.ONE){
//            		 updateContactInfo(counter,s);
//            	 }
//            	 if(currentGroup == GROUP.TWO){
//            		 //emergency contact
//            		 updateContactInfo(counter,s);
//            	 }
//            	 if(currentGroup == GROUP.FOUR){
//            		 //billing address of credit card
//            		 updateContactInfo(counter,s);
//            	 }
//            	 
//            	break;
//             case "TIF":
//            	 if(currentGroup ==GROUP.ONE){
//            		 currentGroup = GROUP.TWO;
//            		 updatePassengerInfo(counter,s,currentGroup.name());
//            		 
//            	 }
//            	 if(currentGroup ==GROUP.SEVEN){
//            		 //updatePassengerInfo(counter,s,currentGroup.name());
//            	 }
//            	 if(currentGroup ==GROUP.ELEVEN){
//            		 //printInfo(s,currentGroup.name());
//            	 }
//            	 break;
//             case "FTI":
//            	 updateFreequentTravellerInfo(counter,s);
//            	 break;
//             case "TKT":
//            	 if(currentGroup == GROUP.TWO){
//            		 currentGroup = GROUP.THREE;
//            		 updateTicketInformation(counter,s);
//              	 } 
//            	 break;
//             case "FOP":
//            	 if(currentGroup == GROUP.THREE){
//            		 currentGroup = GROUP.FOUR;
//            		 updateCreditCardInformation(counter,s);
//            	 } 
//            	 break;
//            	 
//             case "TRI":
//            	 if(currentGroup == GROUP.SIX){
//            		 currentGroup = GROUP.SEVEN; 
//            	 }
//            	 break;
//             case "REF":
//            	 updateUniquePassengerNumber(counter,s);
//            	 break;
//             
//             } 
//         }		
//	}
//	
//	private void updateAgencyInformation(int counter,Segment s){
//
//		PnrVo vo = message.getPassengers().get(counter);
//		if(s.getComposites() != null)
//		for (int i=0; i<s.getComposites().length; i++) {
//    		Composite c = s.getComposites()[i];
//    		Element[] e = c.getElements();
//    		if(i == 0 && e != null && e.length == 2){
//    			vo.setPnrAgencyAirlineCode(e[0].getValue());
//    			vo.setPnrAgencyLocationCode(e[1].getValue());
//    		}
//    		if(i == 1 && e != null && e.length == 2){
//    			vo.setPnrAgentIdentification(e[0].getValue());
//    			vo.setPnrAgencyId(e[1].getValue());
//    		}
//    		if(i == 4 && e != null && e.length >= 1){
//    			vo.setPnrAgencyTypeCode(e[0].getValue());
//    		}
//       		if(i == 5 && e != null && e.length >= 1){
//    			vo.setPnrAgencyCountry(e[0].getValue());
//    		}
//		}
//	}
//	
//	private void updateCreditCardInformation(int counter,Segment s){
//
//		PnrVo vo = message.getPassengers().get(counter);
//		for (int i=0; i<s.getComposites().length; i++) {
//    		Composite c = s.getComposites()[i];
//    		Element[] e = c.getElements();
//    		if(e!= null && e.length >= 3){
//    			vo.setFormOfPayment(e[0].getValue());
//    		}
//    		if(StringUtils.isNoneEmpty(vo.getFormOfPayment()) && vo.getFormOfPayment().equals("CC")){
//    			vo.setCreditCardType(e[3].getValue());
//    			vo.setCreditCardNumber(e[4].getValue());
//    			vo.setCreditCardExpiration(e[5].getValue());
//    		}
//		}
//	}
//	
//	private void updateUniquePassengerNumber(int counter,Segment s){
//		PnrVo vo = message.getPassengers().get(counter);
//		for (int i=0; i<s.getComposites().length; i++) {
//    		Composite c = s.getComposites()[i];
//    		Element[] e = c.getElements();
//    		if(e!= null && e.length == 2){
//    			vo.setRecordLocator(e[1].getValue());
//    		}
//		}
//	}
//	
//	private void updateTicketInformation(int counter,Segment s){
//		PnrVo vo = message.getPassengers().get(counter);
//		for (int i=0; i<s.getComposites().length; i++) {
//    		Composite c = s.getComposites()[i];
//    		Element[] e = c.getElements();
//    		if( c.getElements() != null && c.getElements().length >=3 ){
//    			vo.setTicketNumber(e[0].getValue());
//    			vo.setTicketType(e[1].getValue());
//    			vo.setNumberOfBooklets(e[2].getValue());
//    		}
//		}
//	}
//	
//	private void updateContactInfo(int counter,Segment s){
//		PnrVo vo = message.getPassengers().get(counter);
//	    AddressVo avo=new AddressVo();
//   	   	for (int i=0; i<s.getComposites().length; i++) {
//    		Composite c = s.getComposites()[i];
//    		Element[] e = c.getElements();
//    		if( i == 1 && c.getElements() != null && c.getElements().length >=6 ){
//    			avo.setAddressType(e[0].getValue());
//    			avo.setAddressLine1(e[1].getValue());
//    			avo.setAddressCity(e[2].getValue());
//    			avo.setAddressState(e[3].getValue());
//    			avo.setAddressCountry(e[5].getValue());
//    			avo.setAddressPostalCode(e[6].getValue());
//    			if(e.length >=8)
//    				avo.setPhoneNumber(e[7].getValue());
//    		}
//   	   	}
//   	   	vo.getAdresses().add(avo);
//	}
//	
//	private void addFreeTextToPnr(int counter,Segment s){
//		PnrVo vo = message.getPassengers().get(counter);
//		StringBuilder sb=new StringBuilder();
//		sb=sb.append(vo.getOtherServiceInformation());
//		sb=sb.append("[");
//		for (int i=0; i<s.getComposites().length; i++) {
//			Composite c = s.getComposites()[i];
//			if(i == 0){
//				Element[] e = c.getElements();
//				sb=sb.append("Identifier=");
//				for(int j=0;j<e.length;j++){
//					sb=sb.append(e[j].getValue()+":");
//				}
//			}
//			if(StringUtils.isNotEmpty(c.getValue()))
//			sb.append(" Value="+c.getValue());
//		}
//		sb=sb.append("],");
//		vo.setOtherServiceInformation(sb.toString());
//	}
//	
//	private void updatePnrCheckInDate(int counter,Segment s){
//		PnrVo vo = message.getPassengers().get(counter);
//		for (int i=0; i<s.getComposites().length; i++) {
//			Composite c = s.getComposites()[i];
//			Element[] e = c.getElements();
//			if(i == 0 && e != null && e.length >=3){
//				vo.setChkDateQualifier(e[0].getValue());
//				String temp=e[1].getValue()+e[2].getValue();
//				if(StringUtils.isNumeric(temp)){
//					try {
//					    vo.setCheckInDate(ParseUtils.parseDateTime(temp, DATE_TIME_FORMAT));
//					} catch (ParseException pe) {
//					    pe.printStackTrace();
//					}
//				}
//			}
//		}
//	}
//	
//	private void updatePnrReservarionInfo(int counter,Segment s,String name){
//		
//		PnrVo vo = message.getPassengers().get(counter);
//		//{Composite[value=<null>,elements={Element [value=DL],Element [value=MFN4TI]}]}]
//		for (int i=0; i<s.getComposites().length; i++) {
//			Composite c = s.getComposites()[i];
//			Element[] e = c.getElements();
//			if(i == 0 && e != null && e.length >=2){
//				vo.setAirlineCode(e[0].getValue());
//				vo.setReservationControlNumber(e[1].getValue());
//			}
//			
//		}
//	}
//	
//	private void printInfo(Segment s,String group){
//		System.out.println("In Segment "+s.getName()+" Group "+group);
//		for (int i=0; i<s.getComposites().length; i++) {
//			Composite c = s.getComposites()[i];
//			System.out.println("composite value"+c.getValue());
//			
//    		Element[] e = c.getElements();
//    		if(e!= null){
//    			for(int j=0;j<e.length;j++){
//    				System.out.println(j+" element "+e[j].getValue());
//    			}
// 
//    		}
//		}
//	}
//	
//    private void updateFreequentTravellerInfo(int counter,Segment s){
//    	PnrVo vo = message.getPassengers().get(counter);
//    	for (int i=0; i<s.getComposites().length; i++) {
//    		Composite c = s.getComposites()[i];
//    		Element[] e = c.getElements();
//    		if(e != null ){
//    			vo.setFreequentFlyerAirline(e[0].getValue());
//    			vo.setFreequentFlyerNumber(e[1].getValue());
//    			if(e.length >= 5){
//    				vo.setFrequentMemberLevelInfo(e[4].getValue());
//    			}
//    		}
//    	}
//    }
//    
//
//    private void updatePassengerInfo(int counter,Segment s,String level){
//    	PnrVo vo = message.getPassengers().get(counter);
//    	if("SRC".equals(s.getName())){
//        	vo.setCarrierCode(message.getFlights().get(0).getCarrier());
//        	vo.setFlight(message.getFlights().get(0));
//    	}
//
//    	if("TIF".equals(s.getName()) && (level.equals("SEVEN") || level.equals("TWO")  )){
//    		for (int i=0; i<s.getComposites().length; i++) {
//    			Composite c = s.getComposites()[i];
//    			if(i == 0 && StringUtils.isNotBlank(c.getValue())){
//    				vo.setSuffix(this.getSuffixFromName(c.getValue()));
//    				vo.setLastName(this.getNameWithoutSuffix(c.getValue()));
//    				
//    				if(c.getElements() != null && c.getElements().length >0){
//    					vo.setPassengerType(c.getElements()[0].getValue());//for group reservations
//    				}
//    			}
//    			if(i == 1 && StringUtils.isBlank(c.getValue()) && c.getElements() != null){
//    				Element[] e = c.getElements();
//    				for(int j =0;j<e.length;j++){
//    					if(j == 0){
//    						vo.setTitle(this.getTitleFromName(e[0].getValue()));
//    						vo.setFirstName(this.getNameWithoutTitle(e[0].getValue()));
//    					}
//    					if(j == 1){
//    						vo.setPassengerType(e[1].getValue());//A forAdult/IN for INFANT etc
//    					}
//    				}
//    			}
//    		}
//    	}
//    }
// 
//    private String getNameWithoutSuffix(String name){
//       	
//    	if(StringUtils.isNotBlank(name) && (name.contains("JR") || name.contains("SR") )){
//    		name=name.substring(0,name.length()-2);
//     	}
//    	return name;  	
//    }
//    
//    private String getSuffixFromName(String name){
//       	String pfx="";
//    	if(StringUtils.isNotBlank(name) && name.contains("JR")){
//    		pfx=name.substring(name.length()-2, name.length());
//    	}
//    	if(StringUtils.isNotBlank(name) && name.contains("SR")){
//    		pfx=name.substring(name.length()-2, name.length());
//    	}
//    	return pfx;  	
//    }
//    private String getTitleFromName(String name){
//    	String title="";
//    	if(StringUtils.isNotBlank(name) && name.contains("MR")){
//    		title=name.substring(name.length()-2, name.length());
//    	}
//    	if(StringUtils.isNotBlank(name) && name.contains("MISS")){
//    		title=name.substring(name.length()-4, name.length());
//    	}
//    	return title;
//    }
//    
//    private String getNameWithoutTitle(String name){
//    	
//    	if(StringUtils.isNotBlank(name) && name.contains("MR")){
//    		name=name.substring(0, name.length()-2);
//    	}
//    	if(StringUtils.isNotBlank(name) && name.contains("MISS")){
//    		name=name.substring(0, name.length()-4);
//    	}
//    	return name;
//    }
//    private void updateNumberOfPassengers(Segment s){
//    	for (int i=0; i<s.getComposites().length; i++) {
//    		Composite c = s.getComposites()[i];
//    		if(StringUtils.isNotBlank(c.getValue())){
//    			message.setNumberOfPassengers(new Integer(c.getValue()));
//    		}
//    	}
//    }
//    
//	private void updateFlightDetails(String group,Segment s){
//		   
//	    if("NONE".equals(group)){
//	    	FlightVo flight = new FlightVo();
//	    	for (int i=0; i<s.getComposites().length; i++) {
//	        	Composite c = s.getComposites()[i];
//	        		Element[] e = c.getElements();
//	        		if(i == 0 && e !=null && e.length == 2){
//	        		    try {
//    	        			flight.setFlightDate(ParseUtils.parseDateTime(e[0].getValue()+e[1].getValue(), DATE_TIME_FORMAT));
//    	        			System.out.println("---flight date----"+flight.getFlightDate());
//	        		    } catch (ParseException pe) {
//	        		        pe.printStackTrace();
//	        		    }
//	        		}
//	        		if(i == 1 && c.getValue() != null){
//	        			flight.setOrigin(c.getValue());
//	        		}
//	        		if( i == 2 && c.getValue() != null ){
//	        			flight.setDestination(c.getValue());
//	        		}
//	           		if( i == 3 && c.getValue() != null ){
//	        			flight.setCarrier(c.getValue());
//	        		}
//	          		if( i == 4 && c.getValue() != null ){
//	        			flight.setFlightNumber(c.getValue());
//	        		}
//	        } 
//	    	   	message.getFlights().add(flight);
//	    }
//	}
//	   
//    private void updateReportingParty(String mode,Segment s){
//
//    	PnrReportingAgentVo vo = new PnrReportingAgentVo();
//    	for (int i=0; i<s.getComposites().length; i++) {
//     		Composite c = s.getComposites()[i];
//     		if(i == 0 && c.getElements() != null && c.getElements().length ==2){
//     			Element[] e = c.getElements();
//     			vo.setAgentAirlineCode(e[0].getValue());
//				vo.setAgentLocationCode(e[1].getValue());
//     		}
//     		if(i == 1 && c.getValue() != null){
//     			vo.setAgentIdentificationNumber(c.getValue());
//     		}
//    	}
//    	message.getReportingParties().add(vo);
//    }
//    
//    private String updateFlightMode(Segment s){
//    	String mode="";
//    	for (int i=0; i<s.getComposites().length; i++) {
//            Composite c = s.getComposites()[i];
//            Element[] e = c.getElements();
//            if (e != null && e.length == 2) {
//                String type = e[1].getValue();
//                switch (type) {
//                case "22":
//                    mode="NEW";
//                    break;
//                case "141":
//                	mode="UPDATE";
//                    break;
//                }
//            }
//        }
//    	return mode;
//    }
}
