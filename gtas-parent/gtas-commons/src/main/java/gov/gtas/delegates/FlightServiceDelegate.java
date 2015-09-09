package gov.gtas.delegates;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import gov.gtas.delegates.vo.FlightVo;
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
	private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
	
	@Transactional
	public FlightVo saveOrUpdate(FlightVo vo) {
		Flight f=flightService.getUniqueFlightByCriteria(vo.getCarrier(), vo.getFlightNumber(), vo.getOrigin(), vo.getDestination(), vo.getFlightDate());
		if(f != null && f.getId() != null){
			f = ServiceUtils.mapFlightFromVo(vo,f);
			f.setUpdatedBy("JUNIT");
			flightService.update(f);
			logger.debug("Flight with id "+f.getId()+"  number "+f.getFlightNumber()+" got updated to database");
		}
		else{
			f = ServiceUtils.mapFlightFromVo(vo,new Flight());
			f=flightService.create(f);
			logger.debug("Flight with id "+f.getId()+"  number "+f.getFlightNumber()+" got saved to database");
		}
		return ServiceUtils.mapVoFromFlight(f);
		
	}

}
