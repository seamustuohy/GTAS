package gov.gtas.testdatagen;

import gov.gtas.model.ApisMessage;
import gov.gtas.model.Document;
import gov.gtas.model.Flight;
import gov.gtas.model.MessageStatus;
import gov.gtas.model.Passport;
import gov.gtas.model.Pax;
import gov.gtas.model.Traveler;
import gov.gtas.repository.AirportRepository;
import gov.gtas.repository.ApisMessageRepository;
import gov.gtas.repository.CarrierRepository;
import gov.gtas.repository.CountryRepository;
import gov.gtas.util.DateCalendarUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

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
    	msg.setFlights(createFlights());
    	msg.setCreateDate(new Date());
    	msg.setStatus(MessageStatus.LOADED);
    	apisRepository.save(msg);
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
	    	passenger.setDocuments(createDocuments(new String[]{args[0]}, new String[]{args[1]}));
	    	passenger.setFirstName(args[2]);
	    	passenger.setLastName(args[3]);
	    	passenger.setCitizenshipCountry(countryRepository.getCountryByTwoLetterCode(args[4]).get(0));
	    	passenger.setEmbarkation(airportRepository.getAirportByThreeLetterCode(args[5]).get(0));
	    	travelers.add(passenger);
    	}
    	
//    	passenger = new Pax();
//    	passenger.setDocuments(createDocuments(new String[]{"US"}, new String[]{"2010-01-15"}));
//    	passenger.setFirstName("Gitstash");
//    	passenger.setLastName("Garbled");
//    	passenger.setCitizenshipCountry(countryRepository.getCountryByTwoLetterCode("US").get(0));
//    	passenger.setEmbarkation(airportRepository.getAirportByThreeLetterCode("BOB").get(0));//Bora Bora
//    	travelers.add(passenger);
    	
   	    return travelers;
    }
    private Set<Flight> createFlights(){
    	Set<Flight> flights = new HashSet<Flight>();
    	
    	Flight flight = new Flight();
    	Set<Traveler> travelers = createPassengerAndDocument(new String[][]{
    			{"GB","2012-01-15", "Ragner", "Yilmaz", "GB", "YHZ"},
    			{"US", "2010-01-15", "Gitstash", "Garbled", "US", "BOB"}
    	       }
    			);
    	flight.setPassengers(travelers);
    	flight.setCarrier( carrierRepository.getCarrierByTwoLetterCode("V7").get(0));//Continental
    	flight.setDestination(airportRepository.getAirportByThreeLetterCode("BOB").get(0));
    	flight.setFlightDate(new Date());
    	flight.setFlightNumber("0012");
    	flight.setOrigin(airportRepository.getAirportByThreeLetterCode("YHZ").get(0));//Bora Bora
    	flight.setOriginCountry(countryRepository.getCountryByTwoLetterCode("CA").get(0));
    	flights.add(flight);
    	
    	flight = new Flight();
    	travelers = createPassengerAndDocument(new String[][]{
    			{"YE","2012-01-15", "Iphsatz", "Zaglib", "PF", "YHZ"},
    			{"US", "2010-01-15", "Loopy", "Lair", "US", "BOB"}
    	       }
    			);
    	flight.setPassengers(travelers);
    	flight.setCarrier( carrierRepository.getCarrierByTwoLetterCode("CO").get(0));//Continental
    	flight.setDestination(airportRepository.getAirportByThreeLetterCode("HOD").get(0));
    	flight.setFlightDate(new Date());
    	flight.setFlightNumber("0017");
    	flight.setOrigin(airportRepository.getAirportByThreeLetterCode("LHR").get(0));//Bora Bora
    	flight.setOriginCountry(countryRepository.getCountryByTwoLetterCode("GB").get(0));
    	flights.add(flight);

    	return flights;
    }
    private Set<Document> createDocuments(String[] iso2Array, String[]issueDates){
    	Set<Document> docs = new HashSet<Document>();
    	for(int i = 0; i < iso2Array.length; ++i){
    		String iso2 = iso2Array[i];
    		Document doc = new Passport();
    		doc.setDocumentNumber(DOCUMENT_NUMBER);
    		doc.setIssuanceCountry(countryRepository.getCountryByTwoLetterCode(iso2).get(0));
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
