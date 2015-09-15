package gov.gtas.delegate;

import static org.junit.Assert.assertNotNull;
import gov.gtas.config.CommonServicesConfig;
import gov.gtas.delegates.FlightServiceDelegate;
import gov.gtas.delegates.PassengerServiceDelegate;
import gov.gtas.delegates.vo.DocumentVo;
import gov.gtas.delegates.vo.FlightVo;
import gov.gtas.delegates.vo.PassengerVo;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CommonServicesConfig.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PassengerDelegateIT {
	
	@Autowired
	private FlightServiceDelegate flightDelegate;
	
	@Autowired
	private PassengerServiceDelegate passengerDelegate;
	
	@Test
	public void testFlightDelegate() {
		System.out.println("#####################testPassengerDelegate#############################");
		PassengerVo vo=this.preparePassengerVo();
		passengerDelegate.saveOrUpdate(vo);
		System.out.println("#####################vo.getId()#############################"+vo.getFirstName());
		assertNotNull(vo.getFirstName());
	}
	
	private FlightVo prepareFlightVo(){
		FlightVo vo=new FlightVo();
		vo.setCarrier("AA");
		vo.setDestination("HUR");
		vo.setEta(new Date("8/8/2015"));
		vo.setEtd(new Date("8/8/2015"));
		vo.setFlightDate(new Date("8/8/2015"));
		vo.setFlightNumber("1244");
		vo.setOrigin("IAD");
		vo.setDestinationCountry("USA");
		vo.setOriginCountry("USA");
		vo.setDirection("I");
		return vo;
	}
	private PassengerVo preparePassengerVo(){
		PassengerVo vo = new PassengerVo();
		vo.setAge(30);
		vo.setCitizenshipCountry("USA");
		vo.setCreatedAt(new Date());
		vo.setCreatedBy("TEST");
		vo.setDebarkation("JFK");
		vo.setDebarkCountry("USA");
		vo.setDob(new Date("7/8/1996"));
		vo.setEmbarkation("IAD");
		vo.setFirstName("TESTER");
		vo.setGender("M");
		vo.setLastName("DEVELOPER");
		vo.setPassengerType("P");
		vo.setResidencyCountry("USA");
		vo.setTitle("");
		vo.setSuffix("");
		List<DocumentVo> h = new ArrayList<>();
		h.add(prepareDocumentVo());
		h.add(prepareDocumentVo1());
		h.add(prepareDocumentVo2());
		vo.setDocuments(h);
		vo.getFlights().add(prepareFlightVo());
		
		
		return vo;
	}

	private DocumentVo prepareDocumentVo(){
		DocumentVo dvo = new DocumentVo();
		dvo.setCreatedAt(new Date());
		dvo.setCreatedBy("TEST");
		dvo.setDocumentNumber("P123456");
		dvo.setDocumentType("P");
		dvo.setIssuanceCountry("USA");
		dvo.setExpirationDate(new Date("8/8/2020"));
		return dvo;
	}
	private DocumentVo prepareDocumentVo1(){
		DocumentVo dvo = new DocumentVo();
		dvo.setCreatedAt(new Date());
		dvo.setCreatedBy("TEST");
		dvo.setDocumentNumber("D66666");
		dvo.setDocumentType("OTH");
		dvo.setIssuanceCountry("USA");
		dvo.setExpirationDate(new Date("8/8/2016"));
		return dvo;
	}
	private DocumentVo prepareDocumentVo2(){
		DocumentVo dvo = new DocumentVo();
		dvo.setCreatedAt(new Date());
		dvo.setCreatedBy("TEST");
		dvo.setDocumentNumber("DL5555");
		dvo.setDocumentType("DL");
		dvo.setIssuanceCountry("USA");
		dvo.setExpirationDate(new Date("8/20/2015"));
		return dvo;
	}

}
