/*
 * All GTAS code is Copyright 2016, Unisys Corporation.
 * 
 * Please see LICENSE.txt for details.
 */
package gov.gtas.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import gov.gtas.model.Document;
import gov.gtas.model.Flight;
import gov.gtas.model.Passenger;
import gov.gtas.repository.FlightRepository;
import gov.gtas.services.dto.FlightsPageDto;
import gov.gtas.services.dto.FlightsRequestDto;
import gov.gtas.vo.passenger.FlightVo;

@Service
public class FlightServiceImpl implements FlightService {
    
    @Resource
    private FlightRepository flightRespository;

    @Override
    @Transactional
    public Flight create(Flight flight) {
        return flightRespository.save(flight);
    }

    @Override
    @Transactional
    public FlightsPageDto findAll(FlightsRequestDto dto) {
        List<FlightVo> vos = new ArrayList<>();
        Pair<Long, List<Flight>> tuple = flightRespository.findByCriteria(dto);
        for (Flight f : tuple.getRight()) {
            FlightVo vo = new FlightVo();
            BeanUtils.copyProperties(f, vo);
            vos.add(vo);
        }

        return new FlightsPageDto(vos, tuple.getLeft());
    }
    
    @Override
    @Transactional
    public HashMap<Document, List<Flight>> getFlightsByPassengerNameAndDocument(String firstName,
            String lastName, Set<Document> documents) {
        
        HashMap<Document, List<Flight>> _tempMap = new HashMap<Document, List<Flight>>();
        
        try{
        for(Document document : documents){
            _tempMap.put(document, flightRespository.getFlightsByPassengerNameAndDocument(firstName, lastName, document.getDocumentNumber()));
        }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return _tempMap;
    
    }

    @Override
    @Transactional
    public Flight update(Flight flight) {
        Flight flightToUpdate = this.findById(flight.getId());
        if(flightToUpdate != null){
            flightToUpdate.setCarrier(flight.getCarrier());
            flightToUpdate.setChangeDate();
            flightToUpdate.setDestination(flight.getDestination());
            flightToUpdate.setDestinationCountry(flight.getDestinationCountry());
            flightToUpdate.setEta(flight.getEta());
            flightToUpdate.setEtd(flight.getEtd());
            flightToUpdate.setFlightDate(flight.getFlightDate());
            flightToUpdate.setFlightNumber(flight.getFlightNumber());
            flightToUpdate.setOrigin(flight.getOrigin());
            flightToUpdate.setOriginCountry(flight.getOriginCountry());
            flightToUpdate.setPassengers(flight.getPassengers());
            flightToUpdate.setUpdatedAt(new Date());
            //TODO replace with logged in user id
            flightToUpdate.setUpdatedBy(flight.getUpdatedBy());
            if(flight.getPassengers() != null && flight.getPassengers().size() >0){
                Iterator it = flight.getPassengers().iterator();
                while(it.hasNext()){
                    Passenger p = (Passenger) it.next();
                    flightToUpdate.addPassenger(p);
                }
            }
        }
        return flightToUpdate;
    }

    @Override
    @Transactional
    public Flight findById(Long id) {
        Flight flight = flightRespository.findOne(id);
        return flight;
    }

    @Override
    public Flight getUniqueFlightByCriteria(String carrier, String flightNumber,
            String origin, String destination, Date flightDate) {
        Flight flight = flightRespository.getFlightByCriteria(carrier, flightNumber, origin, destination, flightDate);
        return flight;
    }

    @Override
    @Transactional
    public List<Flight> getFlightByPaxId(Long paxId) {
        return flightRespository.getFlightByPaxId(paxId);
    }

    @Override
    @Transactional
    public List<Flight> getFlightsByDates(Date startDate, Date endDate) {
        return flightRespository.getFlightsByDates(startDate, endDate);
    }

    @Override
    public List<Flight> getFlightsThreeDaysForward(){
        return flightRespository.getFlightsThreeDaysForward();
    }

}
