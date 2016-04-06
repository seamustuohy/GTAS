package gov.gtas.services;
import gov.gtas.config.CommonServicesConfig;
import gov.gtas.dto.FlightDto;
import gov.gtas.parsers.exception.ParseException;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author GTAS4
 * Class MessageGeneratorIT generates the combined PNR and APIS messages for 
 * specific carrier and flight. Uncomment the @Test annotation and run by 
 * changing the for loop values.Also create a folder in your local to store the
 * messages in your local.(C:\\PNR) 
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CommonServicesConfig.class)
@Transactional
public class MessageGeneratorIT {

    @Autowired
    private ServiceUtil svc;
    private static List<FlightDto> flights=new ArrayList<FlightDto>();
    
    @Before
    public void setUp() throws Exception {
    	
    }

    @After
    public void tearDown() throws Exception {
    }

   // @Test()
    public void testRunService() throws ParseException {
    	int j=0;
		for(int i=1;i <=99;i++){
			j=i;
			FlightDto dto = new FlightDto();
			StringBuilder sb = new StringBuilder();
			String carrier=GenUtil.getCarrier();
			String origin=GenUtil.getAirport();
			String dest=GenUtil.getAirport();
			String temp=GenUtil.getUsAirport();
			if(!origin.equals(temp)){
				dest=temp;
			}

			String fNumber=GenUtil.getFlightNumber();
			String dString=GenUtil.getPnrDate();
			String originCountry=svc.getCountry(origin);
			String destCountry=svc.getCountry(dest);
			int numPax=GenUtil.getRandomNumber(3)+2;
			dto.setCarrier(carrier);
			dto.setDebark(dest);
			dto.setEmbark(origin);
			dto.setFlightNum(fNumber);
			dto.setEmbarkCountry(originCountry);
			dto.setDebarkCountry(destCountry);
			
			PnrGen.buildHeader(carrier,sb);
			PnrGen.buildMessage("22",sb);
			PnrGen.buildOrigDestinations(carrier, origin,dest,fNumber,dString,sb);
			PnrGen.buildEqn(numPax,sb);
			PnrGen.buildSrc(carrier, origin,dest,fNumber,dString,numPax,dto,sb);
			PnrGen.buildFooter(sb);
			flights.add(dto);
			System.out.println(sb.toString());	
			PnrGen.writeToFile(i,sb);
			sb=null;
		}
		PnrGen.buildApisMessages(flights,j);
    }
}
