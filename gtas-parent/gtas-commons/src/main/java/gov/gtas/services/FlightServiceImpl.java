package gov.gtas.services;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import gov.gtas.model.Flight;
import gov.gtas.repository.FlightRepository;

@Service
public class FlightServiceImpl implements FlightService{
	
	@Resource
	private FlightRepository flightRespository;

	@Override
	@Transactional
	public Flight create(Flight flight) {
		return flightRespository.save(flight);
	}

	@Override
	@Transactional
	public Flight delete(Long id) {
		Flight flightToDelete = this.findById(id);
		if(flightToDelete != null){
			flightRespository.delete(flightToDelete);
		}
		return flightToDelete;
	}

	@Override
	@Transactional
	public List<Flight> findAll() {
		return (List<Flight>)flightRespository.findAll();
	}

	@Override
	@Transactional
	public Flight update(Flight flight) {
		Flight flightToUpdate = this.findById(flight.getId());
		if(flightToUpdate != null){
			flightToUpdate.setCarrier(flight.getCarrier());
			flightToUpdate.setChangeDate();
			flightToUpdate.setDestination(flight.getDestination());
			flightToUpdate.setDestinationCountry(flight.getDestinationCountry());
			flightToUpdate.setEta(flight.getEta());
			flightToUpdate.setEtd(flight.getEtd());
			flightToUpdate.setFlightDate(flight.getFlightDate());
			flightToUpdate.setFlightNumber(flight.getFlightNumber());
			flightToUpdate.setOrigin(flight.getOrigin());
			flightToUpdate.setOriginCountry(flight.getOriginCountry());
			flightToUpdate.setPassengers(flight.getPassengers());
			flightToUpdate.setUpdatedAt(new Date());
			//TODO replace with logged in user id
			flightToUpdate.setUpdatedBy(flight.getUpdatedBy());
		}
		return flightToUpdate;
	}

	@Override
	@Transactional
	public Flight findById(Long id) {
		Flight flight = flightRespository.findOne(id);
		return flight;
	}

	@Override
	public Flight getUniqueFlightByCriteria(String carrier, String flightNumber,
			String origin, String destination, Date flightDate) {
	    Flight flight = flightRespository.getFlightByCriteria(carrier, flightNumber, origin, destination, flightDate);
		return flight;
	}

	@Override
	@Transactional
	public List<Flight> getFlightByPaxId(Long paxId) {
		return flightRespository.getFlightByPaxId(paxId);
	}
	
}
