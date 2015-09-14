package gov.gtas.validators;

import static org.junit.Assert.assertNotNull;
import gov.gtas.config.CommonServicesConfig;
import gov.gtas.delegates.ErrorLoggingDelegate;
import gov.gtas.delegates.FlightServiceDelegate;
import gov.gtas.delegates.vo.ApisMessageVo;
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
public class ApisValidatorIT {

	@Autowired
	ErrorLoggingDelegate delegate;
	
	@Autowired
	FlightServiceDelegate fdelegate;
	
	@Test
	public void testApisValidator() {
		
		ApisMessageValidator validator = new ApisMessageValidator();
		ApisMessageVo vo = this.prepareMassageVo();
		vo = validator.getValidMessageVo(vo);
		if(vo == null && validator.getInvalidObjects() != null && validator.getInvalidObjects().size() >0){
			System.out.println(">>>>>>>>> NO OF INVALID OBJECTS EXIST "+validator.getInvalidObjects().size());
			delegate.saveOrUpdate(validator.getInvalidObjects() );
		}
		if(vo != null && validator.getInvalidObjects() != null && validator.getInvalidObjects().size() >0){
			System.out.println(">>>>>>> NO OF INVALID OBJECTS EXIST "+validator.getInvalidObjects().size());
			delegate.saveOrUpdate(validator.getInvalidObjects() );			
		}
		if(vo.getFlights() != null && vo.getFlights().size() > 0){
			for(int i =0;i<vo.getFlights().size();i++){
				FlightVo f = vo.getFlights().get(i);
				if(vo.getPassengers() != null && vo.getPassengers().size() >0){
					for(int j =0;j< vo.getPassengers().size();j++){
						PassengerVo p = vo.getPassengers().get(j);
						p.getFlights().add(f);
						f.getPassengers().add(p);
						
					}
				}
				fdelegate.saveOrUpdate(f);
			}
		}
		assertNotNull(vo);
	}
	
	private ApisMessageVo prepareMassageVo(){
		ApisMessageVo vo = new ApisMessageVo();
		vo.setHashCode("123ASDFGSFSFS");
		vo.setMessageCode("TEST");
		vo.setMessageType("T");
		vo.setTransmissionDate(new Date());
		vo.setTransmissionSource("SOME THING");
		vo.setVersion("13.01");
		vo.addFlight(prepareFlightVo());
		vo.addFlight(prepareFlightVo1());
		vo.addPax(preparePassengerVo());
		vo.addPax(preparePassengerVo1());
		return vo;
	}
	
	private FlightVo prepareFlightVo(){
		FlightVo vo=new FlightVo();
		//vo.setCarrier("AA");
		//vo.setDestination("HUR");
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

	private FlightVo prepareFlightVo1(){
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
		h.add(this.prepareDocumentVo());
		h.add(this.prepareDocumentVo1());
		vo.setDocuments(h);
		vo.getFlights().add(prepareFlightVo1());
		return vo;
	}
	
	private PassengerVo preparePassengerVo1(){
		PassengerVo vo = new PassengerVo();
		//vo.setAge(30);
		vo.setCitizenshipCountry("USA");
		vo.setCreatedAt(new Date());
		vo.setCreatedBy("TEST");
		vo.setDebarkation("JFK");
		vo.setDebarkCountry("USA");
		vo.setDob(new Date("7/8/1996"));
		vo.setEmbarkation("IAD");
		vo.setFirstName("TESTER");
		vo.setGender("M");
		//vo.setLastName("DEVELOPER");
		vo.setPassengerType("P");
		vo.setResidencyCountry("USA");
		vo.setTitle("");
		vo.setSuffix("");
		List<DocumentVo> h = new ArrayList<>();
		vo.setDocuments(h);
		vo.getFlights().add(prepareFlightVo());
		return vo;
	}
	private DocumentVo prepareDocumentVo(){
		DocumentVo dvo = new DocumentVo();
		dvo.setCreatedAt(new Date());
		dvo.setCreatedBy("TEST");
		//dvo.setDocumentNumber("P123456");
		//dvo.setDocumentType("P");
		dvo.setIssuanceCountry("BAD");
		dvo.setIssuanceDate(new Date("8/17/2014"));
		//dvo.setExpirationDate(new Date("8/8/2020"));
		return dvo;
	}
	private DocumentVo prepareDocumentVo1(){
		DocumentVo dvo = new DocumentVo();
		dvo.setCreatedAt(new Date());
		dvo.setCreatedBy("GOOD");
		dvo.setDocumentNumber("P99999");
		dvo.setDocumentType("P");
		dvo.setIssuanceCountry("USA");
		dvo.setIssuanceDate(new Date("8/7/2014"));
		dvo.setExpirationDate(new Date("8/8/2020"));
		return dvo;
	}
	
}
