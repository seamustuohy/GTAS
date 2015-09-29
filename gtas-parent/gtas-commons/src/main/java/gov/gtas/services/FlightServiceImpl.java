package gov.gtas.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import gov.gtas.model.Flight;
import gov.gtas.model.Passenger;
import gov.gtas.repository.FlightRepository;
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
    public FlightsPage findAll(int pageNumber, int pageSize) {
        int pn = pageNumber > 0 ? pageNumber - 1 : 0;
        List<FlightVo> vos = new ArrayList<>();

        Page<Flight> page = flightRespository.getAllSorted(new PageRequest(pn, pageSize));
        long total = page.getTotalElements();
        for (Flight f : page) {
            FlightVo vo = new FlightVo();
            BeanUtils.copyProperties(f, vo);
            vos.add(vo);
        }

        return new FlightsPage(vos, total);
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
					System.out.println("XXXXXXXXXXX");
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
}
