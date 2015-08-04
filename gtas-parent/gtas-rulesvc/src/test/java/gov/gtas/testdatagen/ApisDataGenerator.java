package gov.gtas.testdatagen;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import gov.gtas.model.ApisMessage;
import gov.gtas.model.Document;
import gov.gtas.model.Flight;
import gov.gtas.model.FlightDirection;
import gov.gtas.model.MessageStatus;
import gov.gtas.model.Passport;
import gov.gtas.model.Pax;
import gov.gtas.model.Traveler;
import gov.gtas.repository.AirportRepository;
import gov.gtas.repository.ApisMessageRepository;
import gov.gtas.repository.CarrierRepository;
import gov.gtas.repository.CountryRepository;
import gov.gtas.util.DateCalendarUtils;

@Component
public class ApisDataGenerator {
	public static final String DOCUMENT_NUMBER="12345";
	
	@Resource
	private ApisMessageRepository apisRepository;
	
	@Resource
	CarrierRepository carrierRepository;
	@Resource
	AirportRepository airportRepository;
	@Resource
	CountryRepository countryRepository;
	
	/**
	 * Creates a ApisMessage with 3 flight with 3 passengers
	 * @return
	 */
    public ApisMessage createSimpleTestApisMesssage(){
    	ApisMessage msg = new ApisMessage();
    	msg.setId(123L);
    	msg.setFlights(createFlights());
    	msg.setCreateDate(new Date());
    	msg.setStatus(MessageStatus.LOADED);
    	//apisRepository.save(msg);
    	fixReferences(msg);
    	return msg;
    }
    private void fixReferences(ApisMessage msg){
    	for(Flight fl:msg.getFlights()){
    		for(Traveler tr:fl.getPassengers()){
    			for(Document doc:tr.getDocuments()){
    				if(doc.getTraveler() == null){
    					doc.setTraveler(tr);
    				}
    			}
    		}
    	}
    }
    private Set<Traveler> createPassengerAndDocument(String[][]param){
    	Set<Traveler> travelers = new HashSet<Traveler>();
    	for(String[] args:param){
	    	Pax passenger = new Pax();
	    	passenger.setId(new Long(args[6]));
	    	passenger.setDocuments(createDocuments(new String[]{args[0]}, new String[]{args[1]}));
	    	passenger.setFirstName(args[2]);
	    	passenger.setLastName(args[3]);
	    	passenger.setCitizenshipCountry(args[4]);
	    	passenger.setEmbarkation(args[5]);
	    	travelers.add(passenger);
    	}
   	    return travelers;
    }
    private Set<Flight> createFlights(){
    	Set<Flight> flights = new HashSet<Flight>();
    	
    	Flight flight = new Flight();
    	Set<Traveler> travelers = createPassengerAndDocument(new String[][]{
    			{/*document*/"GB","2012-01-15", /*passenger(name, citzenship, embarkation*/"Ragner", "Yilmaz", "GB", "YHZ","11"},
    			{"US", "2010-01-15", "Gitstash", "Garbled", "US", "BOB","22"},
    			{"CA", "2011-12-31", "Kalimar", "Rultan", "CA", "YHZ","33"}
    	       }
    			);
    	flight.setPassengers(travelers);
    	flight.setCarrier("V7");//Continental
    	flight.setDestination("BOB");
    	flight.setFlightDate(new Date());
    	flight.setFlightNumber("0012");
    	flight.setOrigin("YHZ");
    	flight.setOriginCountry("CA");
    	flight.setDirection(FlightDirection.INBOUND);
    	flights.add(flight);
    	
    	flight = new Flight();
    	travelers = createPassengerAndDocument(new String[][]{
    			{"YE","2012-01-15", "Iphsatz", "Zaglib", "PF", "YHZ","44"},
    			{"US", "2010-01-15", "Loopy", "Lair", "US", "BOB","55"},
    			{"GB", "2010-01-15", "Ikstar", "Crondite", "GB", "LHR","66"}
    	       }
    			);
    	flight.setPassengers(travelers);
    	flight.setCarrier("CO");//Continental
    	flight.setDestination("HOD");
    	Date flDate = null;
    	try{
    	    flDate = DateCalendarUtils.parseJsonDate("2015-07-20");
        	flDate = new Date(flDate.getTime()+36000000L);//add 10 hours
        	flight.setFlightDate(flDate);
    	}catch(ParseException pe){
    		pe.printStackTrace();
    	}
    	flight.setFlightNumber("0017");
    	flight.setOrigin("LHR");//Bora Bora
    	flight.setOriginCountry("GB");
    	flight.setDirection(FlightDirection.INBOUND);
    	flights.add(flight);

    	return flights;
    }
    private Set<Document> createDocuments(String[] iso2Array, String[]issueDates){
    	Set<Document> docs = new HashSet<Document>();
    	for(int i = 0; i < iso2Array.length; ++i){
    		String iso2 = iso2Array[i];
    		Document doc = new Passport();
    		doc.setId(7786L);
    		doc.setDocumentNumber(DOCUMENT_NUMBER);
    		doc.setIssuanceCountry(iso2);
    		try{
    		   doc.setIssuanceDate(DateCalendarUtils.parseJsonDate(issueDates[i]));
    		}catch(ParseException pe){
    			pe.printStackTrace();
    		}
    		docs.add(doc);
    	}
    	return docs;
    }
}
