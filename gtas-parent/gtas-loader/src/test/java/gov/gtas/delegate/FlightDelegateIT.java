package gov.gtas.delegate;

import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import gov.gtas.config.CommonServicesConfig;
import gov.gtas.vo.passenger.FlightVo;
import gov.gtas.services.FlightServiceDelegate2;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CommonServicesConfig.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FlightDelegateIT {

	@Autowired
	private FlightServiceDelegate2 flightDelegate;
	
	@Test
	public void testFlightDelegate() {
		System.out.println("#####################testFlightDelegate#############################");
		FlightVo vo=this.prepareFlightVo();
		flightDelegate.saveOrUpdate(vo);
		System.out.println("#####################vo.getId()#############################"+vo.getId());
		assertNull(vo.getId());
	}
	
	private FlightVo prepareFlightVo(){
		FlightVo vo=new FlightVo();
		vo.setCarrier("BB");
		vo.setDestination("JFK");
		vo.setEta(new Date("8/5/2015"));
		vo.setEtd(new Date("8/5/2015"));
		vo.setFlightDate(new Date("8/5/2015"));
		vo.setFlightNumber("1234");
		vo.setOrigin("IAD");
		vo.setDestinationCountry("USA");
		vo.setOriginCountry("USA");
		vo.setDirection("I");
		
		return vo;
	}
}
