package gov.gtas.util;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import gov.gtas.model.Flight;
import gov.gtas.parsers.vo.air.FlightVo;

public class ServiceUtils {
	
	public static Flight mapFlightFromVo(FlightVo vo,Flight flight){
		
		flight.setCarrier(vo.getCarrier());
		flight.setCreatedAt(new Date());
		flight.setCreatedBy("GTAS");
		flight.setDestination(vo.getDestination());
		flight.setDestinationCountry(vo.getDestinationCountry());
		flight.setEta(vo.getEta());
		flight.setEtd(vo.getEtd());
		flight.setFlightDate(vo.getFlightDate());
		if(StringUtils.isNoneBlank(vo.getFlightId())){
			flight.setId(new Long(vo.getFlightId()));
		}
		flight.setOrigin(vo.getOrigin());
		flight.setOriginCountry(vo.getOriginCountry());
		flight.setDirection(vo.getDirection());
		flight.setFlightNumber(vo.getFlightNumber());
		return flight;
	}

	public static FlightVo mapVoFromFlight(Flight flight){
		FlightVo vo = new FlightVo();
		vo.setCarrier(flight.getCarrier());
		vo.setDestination(flight.getDestination());
		vo.setDestinationCountry(flight.getDestinationCountry());
		vo.setEta(flight.getEta());
		vo.setEtd(flight.getEtd());
		vo.setFlightDate(flight.getFlightDate());
		vo.setFlightNumber(flight.getFlightNumber());
		if(flight.getId() != null){
			System.out.println("############FLIGHT ID###########"+flight.getId());
			vo.setFlightId(flight.getId().toString());
		}
		vo.setOrigin(flight.getOrigin());
		vo.setOriginCountry(flight.getOriginCountry());
		vo.setDirection(flight.getDestination());
		return vo;
	}
}
