package gov.gtas.repository;

import java.util.List;

import gov.gtas.model.Flight;
import gov.gtas.services.dto.FlightsRequestDto;

public interface FlightRepositoryCustom {
    public List<Flight> getAllSorted(FlightsRequestDto dto);
}