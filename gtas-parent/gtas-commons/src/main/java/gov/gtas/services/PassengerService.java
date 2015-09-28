package gov.gtas.services;

import java.util.List;

import gov.gtas.model.Passenger;

public interface PassengerService {
	public Passenger create(Passenger passenger);
    public Passenger update(Passenger passenger) ;
    
    public Passenger findById(Long id);
    public List<Passenger> getPassengersByLastName(String lastName);
    
    public PassengersPage findAllWithFlightInfo(int pageNumber, int pageSize);
    public PassengersPage getPassengersByFlightId(Long flightId, Integer pageNumber, Integer pageSize);
}
