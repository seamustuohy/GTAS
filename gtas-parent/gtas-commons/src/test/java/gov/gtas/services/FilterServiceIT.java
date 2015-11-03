package gov.gtas.services;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import gov.gtas.config.CommonServicesConfig;
import gov.gtas.services.Filter.FilterData;
import gov.gtas.services.Filter.FilterService;
import gov.gtas.services.Filter.FilterServiceUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CommonServicesConfig.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FilterServiceIT {

	@Autowired
	FilterService filterService;

	@Autowired
	FilterServiceUtil filterServiceUtil;

	@Test
	public void testCreateUserFilter() {
		// Arrange

		Set<String> originAirports = new HashSet<String>();

		originAirports.add("GKA");
		originAirports.add("MAG");
		originAirports.add("HGU");

		Set<String> destinationAirports = new HashSet<String>();
		destinationAirports.add("LAE");
		destinationAirports.add("POM");
		destinationAirports.add("WWK");
		Date etaStart = new Date();
		Date etaEnd = DateUtils.addDays(etaStart, 3);
		Date etdStart = new Date();
		Date etdEnd = DateUtils.addDays(etaStart, 3);

		FilterData acutalFilter = null;

		FilterData expectedFilter = new FilterData("bStygar", "I", originAirports, destinationAirports, etaStart, etaEnd,
				null, null);

		// Act
		try {
	//		acutalFilter = filterService.create(expectedFilter);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Assert
		assertEquals(expectedFilter, acutalFilter);
	}
	
	@Test
	public void testgFilterByUserId() {
		
		//Arrange
		String userId="bStygar";
		
		//Act
		FilterData filterData=filterService.findById(userId);
		
		System.out.println(filterData);
	}
	
	@Test
	public void testUpdateFilter()
	{
		//Arrange
		String userId="bStygar";
		
		FilterData existingFilter=filterService.findById(userId);
		
		Set<String> originAirports = new HashSet<String>();

		originAirports.add("UAK");
		originAirports.add("GOH");
		originAirports.add("SFJ");

		Set<String> destinationAirports = new HashSet<String>();
		destinationAirports.add("THU");
		destinationAirports.add("AEY");
		destinationAirports.add("EGS");
		
		FilterData expectedFilter=new FilterData(existingFilter.getUserId(),"O",originAirports,
				destinationAirports,DateUtils.addDays(existingFilter.getEtaStart(),10),
				DateUtils.addDays(existingFilter.getEtaStart(),20),
				existingFilter.getEtaStart(),
				null);
						
		//Act
		FilterData actualFilter=filterService.update(expectedFilter);
		
		
		
		System.out.println(actualFilter);
				
	}
	
	

}
