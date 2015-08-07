package gov.gtas.delegates;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import gov.gtas.model.Flight;
import gov.gtas.services.FlightService;
import gov.gtas.util.ServiceUtils;

/**
 * 
 * @author GTAS4
 * Class FlightServiceDelegate receives the requests from calling applications and delegates 
 * the request to appropriate Service.The purpose of the class between Service layer and other layers
 * is to accept value objects and return value objects after service invocation.
 * 
 */

@Component
public class FlightServiceDelegate {
	
	@Resource
	private FlightService flightService;
	
	public FlightVo saveOrUpdate(FlightVo vo) {
		List<Flight> flights=flightService.getUniqueFlightByCriteria(vo.getCarrier(), vo.getFlightNumber(), vo.getOrigin(), vo.getDestination(), vo.getFlightDate());
		System.out.println("flights.size()-"+flights.size());
		Flight f=new Flight();
		if(flights != null && flights.size() >0){
			f = ServiceUtils.mapFlightFromVo(vo,flights.get(0));
			f.setUpdatedBy("JUNIT");
			flightService.update(f);
		}
		else{
			ServiceUtils.mapFlightFromVo(vo,f);
			f=flightService.create(f);
			System.out.println("ID-"+f.getId());
		}
		return ServiceUtils.mapVoFromFlight(f);
		
	}

}
