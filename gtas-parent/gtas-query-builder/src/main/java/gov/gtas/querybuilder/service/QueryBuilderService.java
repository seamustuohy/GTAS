package gov.gtas.querybuilder.service;

import gov.gtas.model.Flight;
import gov.gtas.model.Pax;
import gov.gtas.querybuilder.model.APIDisplay;
import gov.gtas.querybuilder.model.AddressDisplay;
import gov.gtas.querybuilder.model.CreditCardDisplay;
import gov.gtas.querybuilder.model.DocumentDisplay;
import gov.gtas.querybuilder.model.EmailDisplay;
import gov.gtas.querybuilder.model.FlightDisplay;
import gov.gtas.querybuilder.model.FrequentFlyerDisplay;
import gov.gtas.querybuilder.model.HitsDisplay;
import gov.gtas.querybuilder.model.NameOriginDisplay;
import gov.gtas.querybuilder.model.PNRDisplay;
import gov.gtas.querybuilder.model.PassengerDisplay;
import gov.gtas.querybuilder.model.PhoneDisplay;
import gov.gtas.querybuilder.model.TravelAgencyDisplay;
import gov.gtas.querybuilder.repository.APIDisplayRepository;
import gov.gtas.querybuilder.repository.AddressDisplayRepository;
import gov.gtas.querybuilder.repository.CreditCardDisplayRepository;
import gov.gtas.querybuilder.repository.DocumentDisplayRepository;
import gov.gtas.querybuilder.repository.EmailDisplayRepository;
import gov.gtas.querybuilder.repository.FlightDisplayRepository;
import gov.gtas.querybuilder.repository.FrequentFlyerDisplayRepository;
import gov.gtas.querybuilder.repository.HitsDisplayRepository;
import gov.gtas.querybuilder.repository.NameOriginDisplayRepository;
import gov.gtas.querybuilder.repository.PNRDisplayRepository;
import gov.gtas.querybuilder.repository.PassengerDisplayRepository;
import gov.gtas.querybuilder.repository.PhoneDisplayRepository;
import gov.gtas.querybuilder.repository.TravelAgencyDisplayRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author GTAS5
 *
 */
public class QueryBuilderService {

	@Autowired
	FlightDisplayRepository flightRepository;
	@Autowired
	AddressDisplayRepository addressRepository;
	@Autowired
	APIDisplayRepository apiRepository;
	@Autowired
	CreditCardDisplayRepository ccRepository;
	@Autowired
	DocumentDisplayRepository docRepository;
	@Autowired
	EmailDisplayRepository emailRepository;
	@Autowired
	FrequentFlyerDisplayRepository frequentFlyerRepository;
	@Autowired
	HitsDisplayRepository hitsRepository;
	@Autowired
	NameOriginDisplayRepository nameOriginRepository;
	@Autowired
	PassengerDisplayRepository passengerRepository;
	@Autowired
	PhoneDisplayRepository phoneRepository;
	@Autowired
	PNRDisplayRepository pnrRepository;
	@Autowired
	TravelAgencyDisplayRepository travelAgencyRepository;
		
	/**
	 * 
	 * @return
	 */

	public List<FlightDisplay> getFlightDisplay() {
		
		return (List<FlightDisplay>) flightRepository.findAll();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<AddressDisplay> getAddressDisplay() {
		
		return (List<AddressDisplay>) addressRepository.findAll();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<APIDisplay> getAPIDisplay() {
		
		return (List<APIDisplay>) apiRepository.findAll();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<CreditCardDisplay> getCreditCardDisplay() {
		
		return (List<CreditCardDisplay>) ccRepository.findAll(); 
	}
	
	/**
	 * 
	 * @return
	 */
	public List<DocumentDisplay> getDocumentDisplay() {
		
		return (List<DocumentDisplay>) docRepository.findAll();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<EmailDisplay> getEmailDisplay() {
		
		return (List<EmailDisplay>) emailRepository.findAll();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<FrequentFlyerDisplay> getFrequentFlyerDisplay() {
		
		return (List<FrequentFlyerDisplay>) frequentFlyerRepository.findAll();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<HitsDisplay> getHitsDisplay() {
		
		return (List<HitsDisplay>) hitsRepository.findAll();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<NameOriginDisplay> getNameOriginDisplay() {
		
		return (List<NameOriginDisplay>) nameOriginRepository.findAll();		
	}
	
	/**
	 * 
	 * @return
	 */
	public List<PassengerDisplay> getPassengerDisplay() {
		
		return (List<PassengerDisplay>) passengerRepository.findAll();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<PhoneDisplay> getPhoneDisplay() {
		
		return (List<PhoneDisplay>) phoneRepository.findAll();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<PNRDisplay> getPNRDisplay() {
		
		return (List<PNRDisplay>) pnrRepository.findAll();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<TravelAgencyDisplay> getTravelAgencyDisplay() {
		
		return (List<TravelAgencyDisplay>) travelAgencyRepository.findAll();
	}
	
	/**
	 * 
	 */
	public List<Flight> runQueryOnFlight(String query) {
		query = "flight.carrier = 'DELTA' OR flight.carrier = 'UNITED' AND " + 
				"(document.citizenship = 'USA' AND document.doc_type = 'Passport')";
		
		return null;
	}
	
	/**
	 * 
	 * @param query
	 * @return
	 */
	public List<Pax> runQueryOnPassenger(String query) {
		List<Pax> result = new ArrayList<>();
		
		return result;
	}
	
	/**
	 * 
	 */
	public void saveQuery() {
		
	}
	
	/**
	 * 
	 */
	public void viewQuery() {
		
	}
	
	/**
	 * 
	 */
	public void editQuery() {
		
	}
	
	/**
	 * 
	 */
	public void deleteQuery() {
		
	}
}
