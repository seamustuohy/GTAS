package gov.cbp.taspd.gtas.querybuilder.service;

import gov.cbp.taspd.gtas.model.Flight;
import gov.cbp.taspd.gtas.model.Pax;
import gov.cbp.taspd.gtas.querybuilder.model.APIDisplay;
import gov.cbp.taspd.gtas.querybuilder.model.AddressDisplay;
import gov.cbp.taspd.gtas.querybuilder.model.CreditCardDisplay;
import gov.cbp.taspd.gtas.querybuilder.model.DocumentDisplay;
import gov.cbp.taspd.gtas.querybuilder.model.EmailDisplay;
import gov.cbp.taspd.gtas.querybuilder.model.FlightDisplay;
import gov.cbp.taspd.gtas.querybuilder.model.FrequentFlierDisplay;
import gov.cbp.taspd.gtas.querybuilder.model.HitsDisplay;
import gov.cbp.taspd.gtas.querybuilder.model.NameOriginDisplay;
import gov.cbp.taspd.gtas.querybuilder.model.PNRDisplay;
import gov.cbp.taspd.gtas.querybuilder.model.PassengerDisplay;
import gov.cbp.taspd.gtas.querybuilder.model.PhoneDisplay;
import gov.cbp.taspd.gtas.querybuilder.model.TravelAgencyDisplay;
import gov.cbp.taspd.gtas.querybuilder.repository.APIDisplayRepository;
import gov.cbp.taspd.gtas.querybuilder.repository.AddressDisplayRepository;
import gov.cbp.taspd.gtas.querybuilder.repository.CreditCardDisplayRepository;
import gov.cbp.taspd.gtas.querybuilder.repository.DocumentDisplayRepository;
import gov.cbp.taspd.gtas.querybuilder.repository.EmailDisplayRepository;
import gov.cbp.taspd.gtas.querybuilder.repository.FlightDisplayRepository;
import gov.cbp.taspd.gtas.querybuilder.repository.FrequentFlierDisplayRepository;
import gov.cbp.taspd.gtas.querybuilder.repository.HitsDisplayRepository;
import gov.cbp.taspd.gtas.querybuilder.repository.NameOriginDisplayRepository;
import gov.cbp.taspd.gtas.querybuilder.repository.PNRDisplayRepository;
import gov.cbp.taspd.gtas.querybuilder.repository.PassengerDisplayRepository;
import gov.cbp.taspd.gtas.querybuilder.repository.PhoneDisplayRepository;
import gov.cbp.taspd.gtas.querybuilder.repository.TravelAgencyDisplayRepository;

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
	FrequentFlierDisplayRepository frequentFlierRepository;
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
	public List<FrequentFlierDisplay> getFrequentFlierDisplay() {
		
		return (List<FrequentFlierDisplay>) frequentFlierRepository.findAll();
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
