package gov.gtas.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import gov.gtas.model.Flight;

public interface FlightRepositoryCustom {
    public List<Flight> getAllSorted(Pageable pageable);
}