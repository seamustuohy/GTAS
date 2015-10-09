package gov.gtas.repository;

import java.util.List;

import gov.gtas.services.dto.PassengersRequestDto;

public interface PassengerRepositoryCustom {
    /**
     * 
     * @param flightId optional. When specified, only return the passengers 
     * for the given flight.  When null, return passengers on all flights.
     * @param request query criteria.
     */
    public List<Object[]> getPassengersByCriteria(Long flightId, PassengersRequestDto request);
}
