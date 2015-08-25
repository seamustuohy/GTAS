package gov.gtas.delegate;

import static org.junit.Assert.assertNotNull;
import gov.gtas.config.CommonServicesConfig;
import gov.gtas.delegates.PnrServiceDelegate;
import gov.gtas.delegates.vo.AddressVo;
import gov.gtas.delegates.vo.AgencyVo;
import gov.gtas.delegates.vo.CreditCardVo;
import gov.gtas.delegates.vo.DocumentVo;
import gov.gtas.delegates.vo.EmailVo;
import gov.gtas.delegates.vo.FlightVo;
import gov.gtas.delegates.vo.FrequentFlyerVo;
import gov.gtas.delegates.vo.PassengerVo;
import gov.gtas.delegates.vo.PhoneVo;
import gov.gtas.delegates.vo.PnrDataVo;
import gov.gtas.delegates.vo.PnrMessageVo;
import gov.gtas.model.EdifactMessage;
import gov.gtas.model.MessageStatus;

import java.util.Date;
import java.util.HashSet;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CommonServicesConfig.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PnrDelegateIT {

	private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
	@Autowired
	private PnrServiceDelegate pnrDelegate;

	
	@Test
	public void testPnrServiceDelegate() {
		logger.info("#####################  Begin testPnrServiceDelegate #############################");
		PnrDataVo vo=this.preparePnrDataVo();
		pnrDelegate.saveOrUpdate(vo);
		assertNotNull(vo.getCarrier());
		logger.info("#####################  End testPnrServiceDelegate #############################");
	}	
	
	//prepare PnrDataVo from calling application..Loader
	private PnrDataVo preparePnrDataVo(){
		PnrDataVo vo = new PnrDataVo();
		vo.setBagCount(4);
		vo.setDateBooked(new Date("7/18/2015"));
		vo.setCarrier("DL");
		vo.setCreatedAt(new Date());
		vo.setCreatedBy("SYSTEM");
		vo.setDateReceived(new Date("7/18/2015"));
		vo.setDaysBookedBeforeTravel(20);
		vo.setDepartureDate(new Date("8/18/2015"));
		vo.setFormOfPayment("CC");
		vo.setOrigin("IAD");
		vo.setPassengerCount(2);
		vo.setRecordLocator("D12345");
		vo.setTotalDwellTime(3600000);
		vo.setAgency(new AgencyVo("Test Agency","A12343","ALDIE","VA","USA"));
		vo.getAddresses().add(prepareAddressVo());
		vo.getCreditCards().add(prepareCreditCardVo());
		vo.getFrequentFlyers().add(prepareFrequentFlyerVo());
		vo.getEmails().add(this.prepareEmailVo());
		//use with different hash--throws unique constraint exception.
		//vo.setPnrMessage(this.preparePnrMessageVo());
		vo.getPhones().add(this.preparePhoneData());
		//set flightVo to vo
		vo.getFlights().add(prepareFlightVo());
		//set Passengers
		vo.getPassengers().add(preparePassengerVo());
		//set email
		
		
		return vo;
	}
	 private PhoneVo preparePhoneData(){
		 PhoneVo vo = new PhoneVo();
		 vo.setPhoneNumber("4566547788");
		 vo.setCreatedAt(new Date());
		 vo.setPhoneType("H");
		 vo.setCreatedBy("SYSTEM");
		 return vo;
	 }
	private FlightVo prepareFlightVo(){
		
		FlightVo vo=new FlightVo();
		vo.setCarrier("DL");
		vo.setDestination("ATL");
		vo.setEta(new Date("8/18/2015"));
		vo.setEtd(new Date("8/18/2015"));
		vo.setFlightDate(new Date("8/21/2015"));
		vo.setFlightNumber("2345");
		vo.setOrigin("IAD");
		vo.setDestinationCountry("USA");
		vo.setOriginCountry("USA");
		vo.setDirection("O");
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
		HashSet<DocumentVo> h = new HashSet<>();
		h.add(prepareDocumentVo());
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
		dvo.setIssuanceDate(new Date("8/7/2014"));
		dvo.setExpirationDate(new Date("8/8/2020"));
		return dvo;
	}

	private CreditCardVo prepareCreditCardVo(){
		CreditCardVo vo = new CreditCardVo();
		vo.setAccountHolder("TESTER TEST");
		vo.setCardType("VISA");
		vo.setExpiration(new Date("8/4/2016"));
		vo.setNumber("2233445566778899");
		vo.setCreatedAt(new Date());
		vo.setCreatedBy("JUINT");
		return vo;
	}
	
	private FrequentFlyerVo prepareFrequentFlyerVo(){
		FrequentFlyerVo vo = new FrequentFlyerVo();
		vo.setAirlineCode("AA");
		vo.setFrequentFlyerNumber("AA8888");
		vo.setCreatedBy("JUNIT");
		vo.setCreatedAt(new Date());
		return vo;
	}
	private AddressVo prepareAddressVo(){
		AddressVo vo = new AddressVo();
		vo.setAddressType("H");
		vo.setCity("STERLING");
		vo.setCountry("USA");
		vo.setCreatedAt(new Date());
		vo.setCreatedBy("JUNIT");
		vo.setLine1("2234 NOLINE");
		return vo;
	}

	private PnrMessageVo preparePnrMessageVo(){
		PnrMessageVo vo = new PnrMessageVo();
		EdifactMessage e = new EdifactMessage();
		e.setMessageType("PNR");
		e.setTransmissionDate(new Date("8/18/2016"));
		e.setTransmissionSource("Test Source");
		e.setVersion("13.01");
		vo.setStatus(MessageStatus.RECEIVED);
		vo.setError("No Error");
		vo.setFilePath("C:pnrfiles/some.edi");
		vo.setHashCode("987654321");
		vo.setEdifactMessage(e);
		return vo;
		
	}
	private EmailVo prepareEmailVo(){
		EmailVo vo = new EmailVo();
		vo.setAddress("tester@somedomain.com");
		vo.setDomain("somedomain");
		return vo;
	}
}
