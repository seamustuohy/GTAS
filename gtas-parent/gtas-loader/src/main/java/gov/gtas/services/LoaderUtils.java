package gov.gtas.services;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.gtas.model.Address;
import gov.gtas.model.CreditCard;
import gov.gtas.model.Document;
import gov.gtas.model.Flight;
import gov.gtas.model.FrequentFlyer;
import gov.gtas.model.Passenger;
import gov.gtas.model.Phone;
import gov.gtas.model.Pnr;
import gov.gtas.model.ReportingParty;
import gov.gtas.model.lookup.Airport;
import gov.gtas.model.lookup.Country;
import gov.gtas.model.lookup.FlightDirectionCode;
import gov.gtas.parsers.exception.ParseException;
import gov.gtas.vo.PnrVo;
import gov.gtas.vo.passenger.AddressVo;
import gov.gtas.vo.passenger.CreditCardVo;
import gov.gtas.vo.passenger.DocumentVo;
import gov.gtas.vo.passenger.FlightVo;
import gov.gtas.vo.passenger.FrequentFlyerVo;
import gov.gtas.vo.passenger.PassengerVo;
import gov.gtas.vo.passenger.PhoneVo;
import gov.gtas.vo.passenger.ReportingPartyVo;

@Service
public class LoaderUtils {
    private static final Logger logger = LoggerFactory.getLogger(LoaderUtils.class);

    private static final String LOADER_USER = "SYSTEM";
    
    @Autowired
    private AirportService airportService;

    @Autowired
    private CountryService countryService;

    public Passenger createNewPassenger(PassengerVo vo) throws ParseException {
        Passenger p = new Passenger();
        p.setCreatedBy(LOADER_USER);
        updatePassenger(vo, p);
        return p;
    }
    
    public void updatePassenger(PassengerVo vo, Passenger p) throws ParseException {
        BeanUtils.copyProperties(vo, p, getNullPropertyNames(vo));
        p.setUpdatedBy(LOADER_USER);
        
        String airportCode = vo.getDebarkation();
        p.setDebarkation(airportCode);
        Airport debark = getAirport(airportCode);
        if (debark != null) {
            p.setDebarkCountry(debark.getCountry());
        }

        airportCode = vo.getEmbarkation();
        p.setEmbarkation(airportCode);
        Airport embark = getAirport(airportCode);
        if (embark != null) {
            p.setEmbarkCountry(embark.getCountry());
        }
        
        p.setCitizenshipCountry(normalizeCountryCode(vo.getCitizenshipCountry()));
        p.setResidencyCountry(normalizeCountryCode(vo.getResidencyCountry()));
    }

    public Document createNewDocument(DocumentVo vo) throws ParseException {
        Document d = new Document();
        updateDocument(vo, d);
        return d;
    }

    public void updateDocument(DocumentVo vo, Document d) throws ParseException {
        BeanUtils.copyProperties(vo, d, getNullPropertyNames(vo));
    }
        
    public ReportingParty createNewReportingParty(ReportingPartyVo vo) {
        ReportingParty rp = new ReportingParty();
        updateReportingParty(vo, rp);
        return rp;
    }
    
    public void updateReportingParty(ReportingPartyVo vo, ReportingParty rp) {
        BeanUtils.copyProperties(vo, rp);
    }
    
    public Flight createNewFlight(FlightVo vo) throws ParseException {
        Flight f = new Flight();
        f.setCreatedBy(LOADER_USER);
        updateFlight(vo, f);
        return f;
    }
    
    public void updateFlight(FlightVo vo, Flight f) throws ParseException {
        // TODO: hardcoded for now
        String homeCountry = "USA";
        f.setUpdatedBy(LOADER_USER);

        BeanUtils.copyProperties(vo, f, getNullPropertyNames(vo));
        
        Airport dest = getAirport(f.getDestination());
        String destCountry = null;
        if (dest != null) {
            destCountry = dest.getCountry();
            f.setDestinationCountry(destCountry);
        }
        
        Airport origin = getAirport(f.getOrigin());
        String originCountry = null;
        if (origin != null) {
            originCountry = origin.getCountry();
            f.setOriginCountry(originCountry);
        }
        
        if (homeCountry.equals(originCountry) && homeCountry.equals(destCountry)) {
            f.setDirection(FlightDirectionCode.C.name());
        } else if (homeCountry.equals(originCountry)) {
            f.setDirection(FlightDirectionCode.O.name());            
        } else if (homeCountry.equals(destCountry)) {
            f.setDirection(FlightDirectionCode.I.name());                        
        } else {
            f.setDirection(FlightDirectionCode.OTH.name());
        }
    }

    public void convertPnrVo(Pnr pnr, PnrVo vo) throws ParseException {
        BeanUtils.copyProperties(vo, pnr);
        
        Airport origin = getAirport(vo.getOrigin());
        String originCountry = null;
        if (origin != null) {
            originCountry = origin.getCountry();
            pnr.setOriginCountry(originCountry);
        }
        
        pnr.setPassengerCount(vo.getPassengers().size());
        if (vo.getDateBooked() != null && vo.getDepartureDate() != null) {
//            LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            // TODO: won't work for leap years
            long diff = vo.getDepartureDate().getTime() - vo.getDateBooked().getTime(); 
            int days = (int)TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            pnr.setDaysBookedBeforeTravel(days);
        }
    }
    
    public Address convertAddressVo(AddressVo vo) throws ParseException {
        Address addr = new Address();
        addr.setCreatedBy(LOADER_USER);
        BeanUtils.copyProperties(vo, addr);
        addr.setCountry(normalizeCountryCode(vo.getCountry()));
        return addr;
    }
    
    public Phone convertPhoneVo(PhoneVo vo) {
        Phone p = new Phone();
        p.setCreatedBy(LOADER_USER);
        BeanUtils.copyProperties(vo, p);
        return p;
    }

    public CreditCard convertCreditVo(CreditCardVo vo) {
        CreditCard cc = new CreditCard();
        cc.setCreatedBy(LOADER_USER);
        BeanUtils.copyProperties(vo, cc);
        return cc;
    }
    
    public FrequentFlyer convertFrequentFlyerVo(FrequentFlyerVo vo) {
        FrequentFlyer ff = new FrequentFlyer();
        ff.setCreatedBy(LOADER_USER);
        BeanUtils.copyProperties(vo, ff);
        return ff;
    }
    
    /**
     * try returning ISO_3 code
     */
    private String normalizeCountryCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        
        if (code.length() == 2) {
            Country c = countryService.getCountryByTwoLetterCode(code);
            if (c != null) {
                return c.getIso3();
            }
        } else if (code.length() == 3) {
            Country c = countryService.getCountryByThreeLetterCode(code);
            if (c != null) {
                return code;
            }
        }
        
        logger.warn("Unknown country code: " + code);
        return code;
    }
    
    private Airport getAirport(String code) throws ParseException {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        
        if (code.length() == 3) {
            return airportService.getAirportByThreeLetterCode(code);
        } else if (code.length() == 4) {
            return airportService.getAirportByFourLetterCode(code);
        }

        logger.warn("Unknown airport code: " + code);
        return null;
    }

    private static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
