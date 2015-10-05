package gov.gtas.services;

import java.util.List;

import gov.gtas.model.Passenger;
import gov.gtas.services.dto.PassengersPageDto;
import gov.gtas.services.dto.PassengersRequestDto;
import gov.gtas.vo.passenger.PassengerVo;

public interface PassengerService {
	public Passenger create(Passenger passenger);
    public Passenger update(Passenger passenger) ;
    
    public Passenger findById(Long id);
    public List<Passenger> getPassengersByLastName(String lastName);
    
    public PassengersPageDto findAllWithFlightInfo(PassengersRequestDto request);
    
    public PassengersPageDto getPassengersByFlightId(Long flightId, PassengersRequestDto request);
    public void fillWithHitsInfo(PassengerVo vo, Long flightId, Long passengerId);
}
