package gov.gtas.services;

import java.util.List;

import gov.gtas.model.Passenger;
import gov.gtas.services.dto.PassengersPageDto;

public interface PassengerService {
	public Passenger create(Passenger passenger);
    public Passenger update(Passenger passenger) ;
    
    public Passenger findById(Long id);
    public List<Passenger> getPassengersByLastName(String lastName);
    
    public PassengersPageDto findAllWithFlightInfo(int pageNumber, int pageSize);
    public PassengersPageDto getPassengersByFlightId(Long flightId, Integer pageNumber, Integer pageSize);
}
