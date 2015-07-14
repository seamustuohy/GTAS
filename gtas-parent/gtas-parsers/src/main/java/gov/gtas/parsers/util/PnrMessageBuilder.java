package gov.gtas.parsers.util;

import java.util.List;
import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Element;
import gov.gtas.parsers.edifact.Segment;
import gov.gtas.parsers.paxlst.vo.FlightVo;
import gov.gtas.parsers.paxlst.vo.PnrMessageVo;
import gov.gtas.parsers.paxlst.vo.PnrReportingAgentVo;
import gov.gtas.parsers.paxlst.vo.PnrVo;
import org.apache.commons.lang3.StringUtils;

public class PnrMessageBuilder {

	private static final Logger logger = LoggerFactory.getLogger(PnrMessageBuilder.class);
	private static final String DATE_TIME_FORMAT = "ddMMyyhhmm";
    enum GROUP {
        NONE,ONE,TWO,THREE,FOUR,FIVE,SIX,SEVEN,EIGHT,NINE,TEN,ELEVEN,TWELVE
    }
    
	PnrMessageVo message;
	List<Segment> segments;
	private GROUP currentGroup;
	
	public PnrMessageBuilder(PnrMessageVo messageVo,List<Segment> segmentList){
		this.message=messageVo;
		this.segments=segmentList;
	}
	
	public void buildMessageObject(){
		//set group none for segments UNA UNB UNG UNH MSG ORG TVL EQN UNT UNE UNZ
		currentGroup = GROUP.NONE;
    	String flightSavingMode="";
    	int counter = -1;
    
        for (ListIterator<Segment> i=segments.listIterator(); i.hasNext(); ) {
            Segment s = i.next();
             System.out.println(">>>"+s.toString());
             String segmentName=s.getName();
             
             switch (segmentName) {
             case "MSG":
                 if(currentGroup == GROUP.NONE){
                	 message.setUpdateMode(updateFlightMode(s));
                	 System.out.println("Flight mode="+message.getUpdateMode());
                 }
                 if(currentGroup == GROUP.NINE){
                	 //To specify that the TVL is for a hotel segment.(MSG+8')
                 }
                 break;
             case "ORG":
            	 if(currentGroup == GROUP.NONE){
            		 updateReportingParty(currentGroup.name(),s);
            	 }
            	 if(currentGroup == GROUP.ONE){
            		 printInfo(s,currentGroup.name());
            	 }
            	 if(currentGroup == GROUP.SIX){
            		 printInfo(s,currentGroup.name());
            	 }
                 break;
             case "TVL":
            	 if(currentGroup == GROUP.NONE){
            		 updateFlightDetails(currentGroup.name(),s);
            	 }
            	 if(currentGroup == GROUP.FOUR){
            		 printInfo(s,currentGroup.name());
            		 currentGroup=GROUP.FIVE;
            	 }
            	 if(currentGroup == GROUP.NINE){
            		 printInfo(s,currentGroup.name());
            	 }
            	 break;
             case "EQN":
            	 if(currentGroup == GROUP.NONE){
            	     updateNumberOfPassengers(s);
            	 }
            	 if(currentGroup == GROUP.SEVEN){
            		 currentGroup =GROUP.EIGHT;
            	 }
            	 break;
             case "SRC":
            	 counter++;
            	 currentGroup =GROUP.ONE;
            	 message.getPassengers().add(new PnrVo());
            	 updatePassengerInfo(counter,s,currentGroup.name());
            	 break;
             case "RCI":
             case "SSR":
             case "DAT":
             case "IFT":
             case "ADD":
            	 if(currentGroup == GROUP.ONE){
            		 updateContactInfo(counter,s);
            	 }
            	 if(currentGroup == GROUP.TWO){
            		 printInfo(s,currentGroup.name()); 
            	 }
            	 if(currentGroup == GROUP.FOUR){
            		 printInfo(s,currentGroup.name());
            	 }
            	 
            	break;
             case "TIF":
            	 if(currentGroup ==GROUP.ONE){
            		 updatePassengerInfo(counter,s,currentGroup.name());
            		 currentGroup = GROUP.TWO;
            		 PnrVo vo = (PnrVo) message.getPassengers().get(0);
            		 System.out.println(" VO.."+vo.getLastName());
            		 System.out.println(" VO.."+vo.getFirstName());
            		 System.out.println(" VO.."+vo.getFlightNumber());
            		 System.out.println(" VO.."+vo.getFreequentFlyerAirline());
            	 }
            	 if(currentGroup ==GROUP.SEVEN){
            		 printInfo(s,currentGroup.name());
            	 }
            	 if(currentGroup ==GROUP.ELEVEN){
            		 printInfo(s,currentGroup.name());
            	 }
            	 break;
             case "FTI":
            	 updateFreequentTravellerInfo(counter,s);
            	 break;
             case "TKT":
            	 if(currentGroup == GROUP.TWO){
            		 printInfo(s,currentGroup.name());
            		 currentGroup = GROUP.THREE;
            	 } 
             case "FOP":
            	 if(currentGroup == GROUP.THREE){
            		 printInfo(s,currentGroup.name());
            		 currentGroup = GROUP.FOUR;
            	 } 
             } 

             
        }		
	}

	private void printInfo(Segment s,String group){
		System.out.println("In Segment"+s.getName()+" Group "+group);
		for (int i=0; i<s.getComposites().length; i++) {
			Composite c = s.getComposites()[i];
			System.out.println("composite value"+c.getValue());
			System.out.println("");
    		Element[] e = c.getElements();
    		if(e!= null){
    			for(int j=0;j<e.length;j++){
    				System.out.println(j+" element "+e[j].getValue());
    			}
 
    		}
		}
	}
    private void updateFreequentTravellerInfo(int counter,Segment s){
    	PnrVo vo = message.getPassengers().get(counter);
    	for (int i=0; i<s.getComposites().length; i++) {
    		Composite c = s.getComposites()[i];
    		Element[] e = c.getElements();
    		if(e != null ){
    			vo.setFreequentFlyerAirline(e[0].getValue());
    			vo.setFreequentFlyerNumber(e[1].getValue());
    			if(e.length >= 5){
    				vo.setFrequentMemberLevelInfo(e[4].getValue());
    			}
    		}
    	}
    }
    
    private void updateContactInfo(int counter,Segment s){
    	PnrVo vo = message.getPassengers().get(counter);
   	   	for (int i=0; i<s.getComposites().length; i++) {
    		Composite c = s.getComposites()[i];
    		Element[] e = c.getElements();
    		if( i == 1 && c.getElements() != null && c.getElements().length >5 ){
    			vo.setPnrAddressLine1(e[1].getValue());
    			vo.setPnrAddressCity(e[2].getValue());
    			vo.setPnrAddressState(e[3].getValue());
    			vo.setPnrAddressCountry(e[5].getValue());
    			vo.setPnrAddressPostalCode(e[6].getValue());
    		}
   	   	}
    }
    
    private void updatePassengerInfo(int counter,Segment s,String level){
    	PnrVo vo = message.getPassengers().get(counter);
    	if("SRC".equals(s.getName())){
        	vo.setCarrierCode(message.getFlights().get(0).getCarrier());
        	vo.setFlight(message.getFlights().get(0));
    	}
    	if("TIF".equals(s.getName()) && level.equals("ONE")){
    		//composites={Composite[value=SMITHJR,elements=<null>],
    		//Composite[value=<null>,elements={Element [value=JOHNMR],Element [value=A],Element [value=1]}]}]
    		for (int i=0; i<s.getComposites().length; i++) {
    			Composite c = s.getComposites()[i];
    			if(i == 0 && StringUtils.isNotBlank(c.getValue())){
    				vo.setLastName(c.getValue());
    				if(c.getElements() != null && c.getElements().length >0){
    					vo.setPassengerType(c.getElements()[0].getValue());//for group reservations
    				}
    			}
    			if(i == 1 && StringUtils.isBlank(c.getValue()) && c.getElements() != null){
    				Element[] e = c.getElements();
    				for(int j =0;j<e.length;j++){
    					if(j == 0){
    						vo.setFirstName(e[0].getValue());
    					}
    					if(j == 1){
    						vo.setPassengerType(e[1].getValue());//A forAdult/IN for INFANT etc
    					}
    				}
    			}
    		}
    	}
    }
    
    private void updateNumberOfPassengers(Segment s){
    	for (int i=0; i<s.getComposites().length; i++) {
    		Composite c = s.getComposites()[i];
    		if(StringUtils.isNotBlank(c.getValue())){
    			message.setNumberOfPassengers(new Integer(c.getValue()));
    		}
    	}
    }
    
	private void updateFlightDetails(String group,Segment s){
		   
	    	if("NONE".equals(group)){
	    		FlightVo flight = new FlightVo();
	    	   	for (int i=0; i<s.getComposites().length; i++) {
	        		Composite c = s.getComposites()[i];
	        		Element[] e = c.getElements();
	        		if(i == 0 && e !=null && e.length == 2){
	        			flight.setFlightDate(ParseUtils.parseDateTime(e[0].getValue()+e[1].getValue(), DATE_TIME_FORMAT));
	        			System.out.println("---flight date----"+flight.getFlightDate());
	        		}
	        		if(i == 1 && c.getValue() != null){
	        			flight.setOrigin(c.getValue());
	        		}
	        		if( i == 2 && c.getValue() != null ){
	        			flight.setDestination(c.getValue());
	        		}
	           		if( i == 3 && c.getValue() != null ){
	        			flight.setCarrier(c.getValue());
	        		}
	          		if( i == 4 && c.getValue() != null ){
	        			flight.setFlightNumber(c.getValue());
	        		}
	        		
	        	} 
	    	   	message.getFlights().add(flight);
	    	}
	    	
	    }
	   
    private void updateReportingParty(String mode,Segment s){
    	//Segment[name=ORG,
    	//composites={Composite[value=<null>,elements={Element [value=DL],Element [value=ATL]}],
    			//Composite[value=52519950,elements=<null>]}]
    	PnrReportingAgentVo vo = new PnrReportingAgentVo();
    	for (int i=0; i<s.getComposites().length; i++) {
     		Composite c = s.getComposites()[i];
     		if(i == 0 && c.getElements() != null && c.getElements().length ==2){
     			Element[] e = c.getElements();
     			vo.setAgentAirlineCode(e[0].getValue());
				vo.setAgentLocationCode(e[1].getValue());
				System.out.println(" ---AgentAirline-- "+e[0].getValue()+" "+e[1].getValue());
     		}
     		if(i == 1 && c.getValue() != null){
     			vo.setAgentIdentificationNumber(c.getValue());
     		}

    	}
    	
    	message.getReportingParties().add(vo);
    }
    
    private String updateFlightMode(Segment s){
    	String mode="";
    	for (int i=0; i<s.getComposites().length; i++) {
            Composite c = s.getComposites()[i];
            Element[] e = c.getElements();
            if (e != null && e.length == 2) {
                String type = e[1].getValue();
                switch (type) {
                case "22":
                    mode="NEW";
                    break;
                case "141":
                	mode="UPDATE";
                    break;
                }
            }
        }
    	return mode;
    }
}
