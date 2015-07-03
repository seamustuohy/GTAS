package gov.gtas.services;

import gov.gtas.model.Traveler;

import java.util.List;

import org.springframework.data.repository.query.Param;


public interface PassengerService {
	
	public Traveler create(Traveler passenger);
    public Traveler delete(Long id);
    public List<Traveler> findAll();
    public Traveler update(Traveler passenger) ;
    public Traveler findById(Long id);
    public Traveler getPassengerByName(String firstName,String lastName);
    public List<Traveler> getPassengersByLastName(String lastName);
    public List<Traveler> getPassengersByFlightId(Long flightId);


}
