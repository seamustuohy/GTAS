/*
 * All GTAS code is Copyright 2016, Unisys Corporation.
 * 
 * Please see LICENSE.txt for details.
 */
package gov.gtas.services;

import java.util.List;

import gov.gtas.model.Disposition;
import gov.gtas.model.HitsSummary;
import gov.gtas.model.Passenger;
import gov.gtas.model.lookup.DispositionStatus;
import gov.gtas.services.dto.PassengersPageDto;
import gov.gtas.services.dto.PassengersRequestDto;
import gov.gtas.vo.passenger.CaseVo;
import gov.gtas.vo.passenger.PassengerVo;

public interface PassengerService {
    public Passenger create(Passenger passenger);
    public Passenger update(Passenger passenger) ;
    
    public Passenger findById(Long id);
    public List<Passenger> getPassengersByLastName(String lastName);
    
    public List<Disposition> getPassengerDispositionHistory(Long passengerId, Long flightId);
    public void createDisposition(DispositionData disposition);
    public void createDisposition(HitsSummary hit);
    public List<DispositionStatus> getDispositionStatuses();
    public List<CaseVo> getAllDispositions();
    
    /**
     * 
     * @param flightId optional
     * @param request
     * @return
     */
    public PassengersPageDto getPassengersByCriteria(Long flightId, PassengersRequestDto request);

    public void fillWithHitsInfo(PassengerVo vo, Long flightId, Long passengerId);
}
