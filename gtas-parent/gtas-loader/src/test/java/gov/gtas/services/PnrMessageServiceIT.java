package gov.gtas.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import gov.gtas.config.CommonServicesConfig;
import gov.gtas.model.Flight;
import gov.gtas.model.FlightLeg;
import gov.gtas.model.Passenger;
import gov.gtas.delegates.vo.MessageVo;
import gov.gtas.parsers.exception.ParseException;
import gov.gtas.delegates.vo.FlightVo;
import gov.gtas.delegates.vo.PassengerVo;
import gov.gtas.repository.FlightRepository;
import gov.gtas.repository.PassengerRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CommonServicesConfig.class)
public class PnrMessageServiceIT extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    private PnrMessageService svc;

    private String filePath;
    
    @Autowired
    private FlightRepository flightDao;

    @Autowired
    private LoaderRepository loaderRepo;
    
    @Autowired
    private PassengerRepository paxDao;

    @Before
    public void setUp() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("pnr-messages/2_pnrs_basic.edi").getFile());
        this.filePath = file.getAbsolutePath();
        
    }

    @After
    public void tearDown() throws Exception {
    }
    
    @Test
    public void testme() throws ParseException {
        Flight f = new Flight();        
        f.setCarrier("DL");
        f.setDirection("O");
        f.setFlightDate(new Date());
        f.setFlightNumber("0012");
        f.setOrigin("LAX");
        f.setDestination("IAD");        
        Passenger p = new Passenger();
        p.setPassengerType("P");
        p.setFirstName("john");
        p.setLastName("doe");
        f.getPassengers().add(p);
        flightDao.save(f);        
        assertNotNull(f.getId());
    }
    
//    @Test
    public void testFlightAndPax() throws ParseException {
        Flight f = new Flight();        
        f.setCarrier("DL");
        f.setDirection("O");
        f.setFlightDate(new Date());
        f.setFlightNumber("0012");
        f.setOrigin("LAX");
        f.setDestination("IAD");        

        FlightVo fvo = new FlightVo();
        BeanUtils.copyProperties(f, fvo);
        List<FlightVo> flights = new ArrayList<>();
        flights.add(fvo);
        PassengerVo pvo = new PassengerVo();
        pvo.setPassengerType("P");
        pvo.setFirstName("sam");
        pvo.setLastName("doe");
        List<PassengerVo> passengers = new ArrayList<>();
        passengers.add(pvo);
        PassengerVo pvo2 = new PassengerVo();
        pvo2.setPassengerType("P");
        pvo2.setFirstName("sam2");
        pvo2.setLastName("doe2");
        passengers.add(pvo2);
        
        Set<Flight> dummy = new HashSet<>();
        Set<Passenger> paxDummy = new HashSet<>();
        loaderRepo.processFlightsAndPassengers(flights, passengers, dummy, paxDummy, new ArrayList<FlightLeg>());
        List<Passenger> pax = paxDao.getPassengersByLastName("doe");
        assertEquals(2, pax.size());        
    }

    @Test()
    public void testRunService() throws ParseException {
        List<String> messages = svc.preprocess(this.filePath);
        assertEquals(2, messages.size());
        for (String m : messages) {
            MessageVo vo = svc.parse(m);
            assertNotNull(vo);
            svc.load(vo);
        }
    }
}
