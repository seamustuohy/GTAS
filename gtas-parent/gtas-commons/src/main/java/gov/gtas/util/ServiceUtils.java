package gov.gtas.util;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import gov.gtas.delegates.vo.AddressVo;
import gov.gtas.delegates.vo.AgencyVo;
import gov.gtas.delegates.vo.CreditCardVo;
import gov.gtas.delegates.vo.DocumentVo;
import gov.gtas.delegates.vo.FlightVo;
import gov.gtas.delegates.vo.FrequentFlyerVo;
import gov.gtas.delegates.vo.PassengerVo;
import gov.gtas.delegates.vo.PhoneVo;
import gov.gtas.delegates.vo.PnrDataVo;
import gov.gtas.model.Address;
import gov.gtas.model.Agency;
import gov.gtas.model.CreditCard;
import gov.gtas.model.Document;
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
			System.out.println("############FLIGHT ID###########"+flight.getId());
			vo.setFlightId(flight.getId().toString());
		}
		vo.setOrigin(flight.getOrigin());
		vo.setOriginCountry(flight.getOriginCountry());
		vo.setDirection(flight.getDestination());
		return vo;
	}
	
	public static Passenger mapPassengerFromVo(PassengerVo  vo,Passenger p){
		BeanUtils.copyProperties(vo, p);
		p.setFlights(new HashSet());
		p.setDocuments(new HashSet());
		p.setPnrs(new HashSet());
		
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
				Pnr pnr = mapPnrFromPnrVo((PnrDataVo)pnrs.next(),new Pnr() );
			}
		}
		return p;
		
	}
	public static Document mapDocumentFromVo(DocumentVo vo, Document d){
		BeanUtils.copyProperties(vo, d);
		return d;
		
	}
	public static Pnr mapPnrFromPnrVo(PnrDataVo vo,Pnr pnr ){
		BeanUtils.copyProperties(vo, pnr);
		pnr.setAddresses(new HashSet());
		if (vo.getAgency() != null && vo.getAgency().getAgencyIdentifier() != null){
			pnr.setAgency(mapAgencyFromAgencyVo(vo.getAgency(),new Agency()));
		
		}
		pnr.setAddresses(new HashSet());
		if(vo.getAddresses().size() >0){
			Iterator adresses = vo.getAddresses().iterator();
			while(adresses.hasNext()){
				pnr.addAddress(mapAddressFromAddressVo((AddressVo)adresses.next(),new Address()));
			}
		}
		pnr.setPhones(new HashSet());
		if(vo.getPhones().size() >0){
			Iterator phones = vo.getPhones().iterator();
			while(phones.hasNext()){
				pnr.addPhone(mapPhoneFromPhoneVo((PhoneVo)phones.next(),new Phone()));
				
			}
		}
		pnr.setCreditCards(new HashSet());
		if(vo.getCreditCard() != null && StringUtils.isNotBlank(vo.getCreditCard().getCardNumber())){
			CreditCard cc = new CreditCard();
			mapCreditCardFromCreditCardVo(vo.getCreditCard(),cc);
			pnr.addCreditCard(cc);
		}
		pnr.setFrequentFlyers(new HashSet());
		if(vo.getFrequentFlyer() != null && StringUtils.isNotBlank(vo.getFrequentFlyer().getFrequentFlyerNumber())){
			FrequentFlyer ff = new FrequentFlyer();
			mapFrequentFlyerFromFrequentFlyerVo(vo.getFrequentFlyer(),ff);
			pnr.addFrequentFlyer(ff);
		}

		return pnr;
	}
	
	public static FrequentFlyer mapFrequentFlyerFromFrequentFlyerVo(FrequentFlyerVo vo,FrequentFlyer ff){
		BeanUtils.copyProperties(vo, ff);
		return ff;
	}
	
	public static CreditCard mapCreditCardFromCreditCardVo(CreditCardVo vo,CreditCard cc){
		BeanUtils.copyProperties(vo, cc);
		return cc;
	}
	public static Phone mapPhoneFromPhoneVo(PhoneVo vo,Phone phone){
		BeanUtils.copyProperties(vo, phone);
		return phone;
	}
	
	public static Address mapAddressFromAddressVo(AddressVo vo, Address add){
		BeanUtils.copyProperties(vo, add);
		return add;
	}
	
	public static Agency mapAgencyFromAgencyVo(AgencyVo vo,Agency a){
		BeanUtils.copyProperties(vo, a);
		return a;
	}
}

