package gov.gtas.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;
import gov.gtas.delegates.vo.AddressVo;
import gov.gtas.delegates.vo.AgencyVo;
import gov.gtas.delegates.vo.CreditCardVo;
import gov.gtas.delegates.vo.DocumentVo;
import gov.gtas.delegates.vo.EmailVo;
import gov.gtas.delegates.vo.FlightVo;
import gov.gtas.delegates.vo.FrequentFlyerVo;
import gov.gtas.delegates.vo.PassengerVo;
import gov.gtas.delegates.vo.PhoneVo;
import gov.gtas.delegates.vo.PnrVo;
import gov.gtas.delegates.vo.PnrMessageVo;
import gov.gtas.model.Address;
import gov.gtas.model.Agency;
import gov.gtas.model.CreditCard;
import gov.gtas.model.Document;
import gov.gtas.model.Email;
import gov.gtas.model.Flight;
import gov.gtas.model.FrequentFlyer;
import gov.gtas.model.Passenger;
import gov.gtas.model.Phone;
import gov.gtas.model.Pnr;

public class ServiceUtils {
	
	public static Flight mapFlightFromVo(FlightVo vo,Flight flight){
		
		flight.setCarrier(vo.getCarrier());
		flight.setCreatedAt(new Date());
		flight.setCreatedBy("GTAS");
		flight.setDestination(vo.getDestination());
		flight.setDestinationCountry(vo.getDestinationCountry());
		flight.setEta(vo.getEta());
		flight.setEtd(vo.getEtd());
		flight.setFlightDate(vo.getFlightDate());
		if(StringUtils.isNoneBlank(vo.getFlightId())){
			flight.setId(new Long(vo.getFlightId()));
		}
		flight.setOrigin(vo.getOrigin());
		flight.setOriginCountry(vo.getOriginCountry());
		flight.setDirection(vo.getDirection());
		flight.setFlightNumber(vo.getFlightNumber());
		if(vo.getPassengers() != null && vo.getPassengers().size() >0){
			Iterator it = vo.getPassengers().iterator();
			while(it.hasNext()){
				PassengerVo pvo = (PassengerVo) it.next();
				Passenger p = mapPassengerFromVo(pvo,new Passenger());
				p.getFlights().add(flight);
				flight.addPassenger(p);
			}
		}
		
		return flight;
	}

	public static FlightVo mapVoFromFlight(Flight flight){
		FlightVo vo = new FlightVo();
		vo.setCarrier(flight.getCarrier());
		vo.setDestination(flight.getDestination());
		vo.setDestinationCountry(flight.getDestinationCountry());
		vo.setEta(flight.getEta());
		vo.setEtd(flight.getEtd());
		vo.setFlightDate(flight.getFlightDate());
		vo.setFlightNumber(flight.getFlightNumber());
		if(flight.getId() != null){
			vo.setFlightId(flight.getId().toString());
		}
		vo.setOrigin(flight.getOrigin());
		vo.setOriginCountry(flight.getOriginCountry());
		vo.setDirection(flight.getDestination());
		return vo;
	}
	
	public static Passenger mapPassengerFromVo(PassengerVo  vo,Passenger p){
		//BeanUtils.copyProperties(vo, p);
		p.setAge(vo.getAge());
		p.setCitizenshipCountry(vo.getCitizenshipCountry());
		p.setCreatedAt(vo.getCreatedAt());
		p.setCreatedBy(vo.getCreatedBy());
		p.setDebarkation(vo.getDebarkation());
		p.setDebarkCountry(vo.getDebarkCountry());
		p.setDob(vo.getDob());
		p.setEmbarkation(vo.getEmbarkation());
		p.setEmbarkCountry(vo.getEmbarkCountry());
		p.setFirstName(vo.getFirstName());
		p.setGender(vo.getGender());
		p.setId(vo.getId());
		p.setLastName(vo.getLastName());
		p.setMiddleName(vo.getMiddleName());
		p.setPassengerType(vo.getPassengerType());
		p.setResidencyCountry(vo.getResidencyCountry());
		p.setSeat(vo.getSeat());
		p.setSuffix(vo.getSuffix());
		p.setTitle(vo.getTitle());
		p.setUpdatedAt(vo.getUpdatedAt());
		p.setUpdatedBy(vo.getUpdatedBy());
		if(vo.getDocuments().size() >0){
			Iterator it = vo.getDocuments().iterator();
			while(it.hasNext()){
				Document d = mapDocumentFromVo((DocumentVo) it.next(),new Document());
				d.setPassenger(p);
				p.getDocuments().add(d);
			}
		}
		if(vo.getPnrs() != null && vo.getPnrs().size() >0){
			Iterator pnrs = vo.getPnrs().iterator();
			while(pnrs.hasNext()){
				Pnr pnr = mapPnrFromPnrVo((PnrVo)pnrs.next(),new Pnr() );
				pnr.addPassenger(p);
				p.getPnrs().add(pnr);
			}
		}
		return p;
	}
	
	public static Document mapDocumentFromVo(DocumentVo vo, Document d){
		//BeanUtils.copyProperties(vo, d);
		d.setDocumentNumber(vo.getDocumentNumber());
		d.setDocumentType(vo.getDocumentType());
		d.setExpirationDate(vo.getExpirationDate());
		d.setId(vo.getId());
		d.setIssuanceCountry(vo.getIssuanceCountry());
		d.setIssuanceDate(vo.getIssuanceDate());
		return d;
	}

	public static Pnr mapPnrFromPnrVo(PnrVo vo,Pnr pnr ){
		//BeanUtils.copyProperties(vo, pnr);
		pnr.setBagCount(vo.getBagCount());
		pnr.setCarrier(vo.getCarrier());
		pnr.setDateBooked(vo.getDateBooked());
		pnr.setDateReceived(vo.getDateReceived());
		pnr.setDaysBookedBeforeTravel(vo.getDaysBookedBeforeTravel());
		pnr.setDepartureDate(vo.getDepartureDate());
		pnr.setFormOfPayment(vo.getFormOfPayment());
		if(vo.getId() != null){
			pnr.setId(vo.getId());
		}
		pnr.setOrigin(vo.getOrigin());
		pnr.setOriginCountry(vo.getOriginCountry());
		pnr.setPassengerCount(vo.getPassengerCount());
		pnr.setRecordLocator(vo.getRecordLocator());
		pnr.setTotalDwellTime(vo.getTotalDwellTime());
		if (vo.getAgency() != null && vo.getAgency().getAgencyIdentifier() != null){
			Agency a = mapAgencyFromAgencyVo(vo.getAgency(),new Agency());
			a.addPnr(pnr);
			pnr.setAgency(a);
		}
		if(vo.getPnrMessage() != null && vo.getPnrMessage().getEdifactMessage() != null){
			Pnr p = mapPnrMessageFromPnrVo(vo.getPnrMessage(),new Pnr());
			// TODO
//			p.setPnr(pnr);
//			pnr.getPnrMessages().add(p);
		}
		if(vo.getAddresses().size() >0){
			Iterator adresses = vo.getAddresses().iterator();
			while(adresses.hasNext()){
				Address a = mapAddressFromAddressVo((AddressVo)adresses.next(),new Address());
				a.getPnrs().add(pnr);
				pnr.addAddress(a);
			}
		}
		if(vo.getPhoneNumbers().size() >0){
			Iterator phones = vo.getPhoneNumbers().iterator();
			while(phones.hasNext()){
				Phone p = mapPhoneFromPhoneVo((PhoneVo)phones.next(),new Phone());
				p.getPnrs().add(pnr);
				pnr.addPhone(p);
			}
		}
		if(vo.getCreditCards().size() > 0){
			Iterator ccards = vo.getCreditCards().iterator();
			while(ccards.hasNext()){
				CreditCard cc = mapCreditCardFromCreditCardVo((CreditCardVo)ccards.next(),new CreditCard());
				cc.getPnrs().add(pnr);
				pnr.addCreditCard(cc);
			}
		}
		if(vo.getFrequentFlyerDetails().size() > 0){
			Iterator ffs=vo.getFrequentFlyerDetails().iterator();
			while(ffs.hasNext()){
				FrequentFlyer ff = mapFrequentFlyerFromFrequentFlyerVo((FrequentFlyerVo)ffs.next(),new FrequentFlyer());
				ff.getPnrs().add(pnr);
				pnr.addFrequentFlyer(ff);				
			}
		}
		if(vo.getEmails().size() >0){
			Iterator emails = vo.getEmails().iterator();
			while(emails.hasNext()){
				Email e = mapEmailFromEmailVo((EmailVo)emails.next(),new Email());
				e.getPnrs().add(pnr);
				pnr.addEmail(e);
			}
		}
		return pnr;
	}
	
	public static Pnr mapPnrMessageFromPnrVo(PnrMessageVo vo,Pnr p){
		p.setCreateDate(new Date());
		p.setEdifactMessage(vo.getEdifactMessage());
		p.setFilePath(vo.getFilePath());
		p.setError(vo.getError());
		p.setHashCode(vo.getHashCode());
		p.setStatus(vo.getStatus());
		return p;
	}
	
	public static Email mapEmailFromEmailVo(EmailVo vo,Email e){
		e.setAddress(vo.getAddress());
		e.setCreatedAt(new Date());
		e.setCreatedBy("SYSTEM");
		e.setDomain(vo.getDomain());
		return e;
		
	}
	
	public static FrequentFlyer mapFrequentFlyerFromFrequentFlyerVo(FrequentFlyerVo vo,FrequentFlyer ff){
		//BeanUtils.copyProperties(vo, ff);
		ff.setCarrier(vo.getAirline());
		ff.setCreatedAt(vo.getCreatedAt());
		ff.setCreatedBy(vo.getCreatedBy());
		ff.setNumber(vo.getNumber());
		ff.setId(vo.getId());
		ff.setUpdatedAt(vo.getUpdatedAt());
		
		return ff;
	}
	
	public static CreditCard mapCreditCardFromCreditCardVo(CreditCardVo vo,CreditCard cc){
		//BeanUtils.copyProperties(vo, cc);
		cc.setAccountHolder(vo.getAccountHolder());
		cc.setCardType(vo.getCardType());
		cc.setCreatedAt(vo.getCreatedAt());
		cc.setCreatedBy(vo.getCreatedBy());
		cc.setExpiration(vo.getExpiration());
		cc.setId(vo.getId());
		cc.setNumber(vo.getNumber());
		cc.setUpdatedAt(vo.getUpdatedAt());
		cc.setUpdatedBy("SYSTEM");
		return cc;
	}
	
	public static Phone mapPhoneFromPhoneVo(PhoneVo vo,Phone p){
		//BeanUtils.copyProperties(vo, phone);
		p.setCreatedAt(new Date());
		p.setCreatedBy("SYSTEM");
		if(vo.getId() != null){
			p.setId(vo.getId());
		}
		p.setNumber(vo.getNumber());
		p.setUpdatedAt(vo.getUpdatedAt());
		p.setUpdatedBy(vo.getUpdatedBy());
		return p;
	}
	
	public static Address mapAddressFromAddressVo(AddressVo vo, Address add){
		//BeanUtils.copyProperties(vo, add);
		add.setCity(vo.getCity());
		add.setCountry(vo.getCountry());
		add.setCreatedAt(new Date());
		add.setCreatedBy("SYSTEM");
		if(vo.getId() != null){
			add.setId(vo.getId());
		}
		add.setLine1(vo.getLine1());
		add.setLine2(vo.getLine2());
		add.setLine3(vo.getLine3());
		add.setPostalCode(vo.getPostalCode());
		add.setState(vo.getState());
		return add;
	}
	
	public static Agency mapAgencyFromAgencyVo(AgencyVo vo,Agency a){
		//BeanUtils.copyProperties(vo, a);
		a.setCity(vo.getAgencyCity());
		a.setCountry(vo.getAgencyCountry());
		a.setIdentifier(vo.getAgencyIdentifier());
		a.setName(vo.getAgencyName());
		a.setState(vo.getAgencyState());
		a.setCreatedAt(new Date());
		a.setCreatedBy("SYSTEM");
		return a;
	}
	
    public static Date stripTime(Date d) {
        Calendar cal = Calendar.getInstance(); // locale-specific
        cal.setTime(d);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}

