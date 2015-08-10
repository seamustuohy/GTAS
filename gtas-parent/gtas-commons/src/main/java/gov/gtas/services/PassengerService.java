package gov.gtas.services;

import gov.gtas.model.Passenger;

import java.util.List;

import org.springframework.data.repository.query.Param;


public interface PassengerService {
	
	public Passenger create(Passenger passenger);
    public Passenger delete(Long id);
    public List<Passenger> findAll();
    public Passenger update(Passenger passenger) ;
    public Passenger findById(Long id);
    public Passenger getPassengerByName(String firstName,String lastName);
    public List<Passenger> getPassengersByLastName(String lastName);
    public List<Passenger> getPassengersByFlightId(Long flightId);


}
