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
    	return msg;
    }
    private Set<Traveler> createPassengerAndDocument(){
    	Set<Traveler> travelers = new HashSet<Traveler>();
    	
    	Pax passenger = new Pax();
    	passenger.setDocuments(createDocuments(new String[]{"YE"}, new String[]{"2012-01-15"}));
    	passenger.setCitizenshipCountry(countryRepository.getCountryByTwoLetterCode("PF").get(0));
    	passenger.setFirstName("Ragner");
    	passenger.setLastName("Yilmaz");
    	passenger.setEmbarkation(airportRepository.getAirportByThreeLetterCode("HOD").get(0));//Yemen, Hodeidah
    	travelers.add(passenger);
    	
    	passenger = new Pax();
    	passenger.setDocuments(createDocuments(new String[]{"GB"}, new String[]{"2010-01-15"}));
    	passenger.setCitizenshipCountry(countryRepository.getCountryByTwoLetterCode("GB").get(0));
    	passenger.setFirstName("Gitstash");
    	passenger.setLastName("Garbled");
    	passenger.setEmbarkation(airportRepository.getAirportByThreeLetterCode("BOB").get(0));//Bora Bora
    	travelers.add(passenger);
    	
   	    return travelers;
    }
    private Set<Flight> createFlights(){
    	Set<Flight> flights = new HashSet<Flight>();
    	
    	Flight flight = new Flight();
    	Set<Traveler> travelers = createPassengerAndDocument();
    	flight.setPassengers(travelers);
    	flight.setCarrier( carrierRepository.getCarrierByTwoLetterCode("CO").get(0));//Continental
    	flight.setDestination(airportRepository.getAirportByThreeLetterCode("LHR").get(0));
    	flight.setFlightDate(new Date());
    	flight.setFlightNumber("0012");
    	flight.setOrigin(airportRepository.getAirportByThreeLetterCode("BOB").get(0));//Bora Bora
    	flight.setOriginCountry(countryRepository.getCountryByTwoLetterCode("PF").get(0));
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
