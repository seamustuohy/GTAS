package gov.gtas.services;

import gov.gtas.model.Flight;
import gov.gtas.repository.FlightRepository;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

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
			//flightToUpdate.setCarrier(flight.getCarrier());
			//flightToUpdate.setChangeDate(new Date());
			flightToUpdate.setDestination(flight.getDestination());
			flightToUpdate.setDestinationCountry(flight.getDestinationCountry());
			flightToUpdate.setEta(flight.getEta());
			flightToUpdate.setEtd(flight.getEtd());
			flight.setFlightDate(flight.getFlightDate());
			flightToUpdate.setFlightNumber(flight.getFlightNumber());
			flightToUpdate.setOrigin(flight.getOrigin());
			flightToUpdate.setOriginCountry(flight.getOriginCountry());
			flightToUpdate.setPassengers(flight.getPassengers());
			flightToUpdate.setUpdatedAt(new Date());
			//TODO replace with logged in user id
			flightToUpdate.setUpdatedBy("Logged in userid");
		}
		return flightToUpdate;
	}

	@Override
	@Transactional
	public Flight findById(Long id) {
		Flight flight = flightRespository.findOne(id);
		return flight;
	}

}