package gov.gtas.services;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.gtas.model.Address;
import gov.gtas.model.CreditCard;
import gov.gtas.model.Document;
import gov.gtas.model.Flight;
import gov.gtas.model.Passenger;
import gov.gtas.model.Phone;
import gov.gtas.model.Pnr;
import gov.gtas.model.ReportingParty;
import gov.gtas.model.lookup.Airport;
import gov.gtas.model.lookup.FlightDirectionCode;
import gov.gtas.parsers.exception.ParseException;
import gov.gtas.parsers.pnrgov.PnrVo;
import gov.gtas.parsers.vo.passenger.AddressVo;
import gov.gtas.parsers.vo.passenger.CreditCardVo;
import gov.gtas.parsers.vo.passenger.DocumentVo;
import gov.gtas.parsers.vo.passenger.FlightVo;
import gov.gtas.parsers.vo.passenger.PassengerVo;
import gov.gtas.parsers.vo.passenger.PhoneVo;
import gov.gtas.parsers.vo.passenger.ReportingPartyVo;

@Service
public class LoaderUtils {
    private static final String LOADER_USER = "SYSTEM";
    
    @Autowired
    private AirportService airportService;
    
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
        
        p.setCitizenshipCountry(vo.getCitizenshipCountry());
        p.setResidencyCountry(vo.getResidencyCountry());
        
        for (DocumentVo dvo : vo.getDocuments()) {
            Document d = new Document();
            BeanUtils.copyProperties(dvo, d);
            d.setPassenger(p);
            d.setIssuanceCountry(dvo.getIssuanceCountry());
            p.getDocuments().add(d);
        }
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
    
    public static String[] getNullPropertyNames (Object source) {
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
    
    public void updateFlight(FlightVo vo, Flight f) throws ParseException {
        // TODO: hardcoded for now
        String homeCountry = "USA";
        f.setUpdatedBy(LOADER_USER);

        BeanUtils.copyProperties(vo, f, getNullPropertyNames(vo));
        f.setCarrier(vo.getCarrier());
        
        f.setDestination(vo.getDestination());
        Airport dest = getAirport(vo.getDestination());
        String destCountry = null;
        if (dest != null) {
            destCountry = dest.getCountry();
            f.setDestinationCountry(destCountry);
        }
        
        f.setOrigin(vo.getOrigin());
        Airport origin = getAirport(vo.getOrigin());
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
        
        // handle flight number specially: assume first 2 letters are carrier and rest is flight #
        StringBuffer buff = new StringBuffer();
        for (int j=0; j<4 - vo.getFlightNumber().length(); j++) {
            buff.append("0");
        }
        buff.append(vo.getFlightNumber());
        f.setFlightNumber(buff.toString());
    }

    public Pnr convertPnrVo(PnrVo vo) throws ParseException {
        Pnr pnr = new Pnr();
        pnr.setCreatedBy(LOADER_USER);
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

        return pnr;
    }
    
    public Address convertAddressVo(AddressVo vo) throws ParseException {
        Address addr = new Address();
        addr.setCreatedBy(LOADER_USER);
        BeanUtils.copyProperties(vo, addr);
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
    
    private Airport getAirport(String a) throws ParseException {
        if (a == null) return null;
        
        Airport rv = null;
        if (a.length() == 3) {
            rv = airportService.getAirportByThreeLetterCode(a);
        } else if (a.length() == 4) {
            rv = airportService.getAirportByFourLetterCode(a);
        }

        return rv;
    }
}
