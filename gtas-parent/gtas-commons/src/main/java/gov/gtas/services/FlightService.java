/*
 * All GTAS code is Copyright 2016, Unisys Corporation.
 * 
 * Please see LICENSE.txt for details.
 */
package gov.gtas.services;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import gov.gtas.model.Document;
import gov.gtas.model.Flight;
import gov.gtas.services.dto.FlightsPageDto;
import gov.gtas.services.dto.FlightsRequestDto;

public interface FlightService {    
    public Flight create(Flight flight);
    public Flight update(Flight flight) ;
    public Flight findById(Long id);
    public FlightsPageDto findAll(FlightsRequestDto dto);
    public Flight getUniqueFlightByCriteria(String carrier, String flightNumber, String origin, String destination, Date flightDate);
    public List<Flight> getFlightByPaxId(Long paxId);
    public List<Flight> getFlightsByDates(Date startDate, Date endDate);
    public HashMap<Document, List<Flight>> getFlightsByPassengerNameAndDocument(String firstName, String lastName, Set<Document> documents);
    public List<Flight> getFlightsThreeDaysForward();
}
