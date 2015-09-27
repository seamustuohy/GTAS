package gov.gtas.services;

import java.util.List;

import org.springframework.data.domain.Page;

import gov.gtas.model.Passenger;
import gov.gtas.vo.passenger.PassengerVo;

public interface PassengerService {
	public Passenger create(Passenger passenger);
    public Passenger delete(Long id);
    public Passenger update(Passenger passenger) ;
    
    public List<Passenger> findAll();
    public Passenger findById(Long id);
    public Page<Passenger> findAll(int pageNumber, int pageSize);
    public List<PassengerVo> findAllWithFlightInfo(int pageNumber, int pageSize);
    
    public List<Passenger> getPassengersByLastName(String lastName);
    public Page<Passenger> getPassengersByFlightId(Long flightId, Integer pageNumber, Integer pageSize);
}
