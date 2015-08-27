package gov.gtas.services;

import gov.gtas.model.Address;
import gov.gtas.model.CreditCard;
import gov.gtas.model.Email;
import gov.gtas.model.FrequentFlyer;
import gov.gtas.model.Phone;
import gov.gtas.model.Pnr;
import gov.gtas.repository.PnrRepository;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class PnrServiceImpl implements PnrService {

	@Resource
	private PnrRepository pnrRespository;
	
	@Override
	@Transactional
	public Pnr create(Pnr pnr) {
		return pnrRespository.save(pnr);
	}

	@Override
	@Transactional
	public Pnr delete(Long id) {
		Pnr pnr = this.findById(id);
		if(pnr != null){
			pnrRespository.delete(pnr);
		}
		return pnr;
	}

	@Override
	@Transactional
	public Pnr update(Pnr pnr) {
		Pnr rv = this.findById(pnr.getId());
		if(rv != null){
			mapPnr(pnr,rv);
		}
		return rv;
	}

	@Override
	@Transactional
	public Pnr findById(Long id) {
		return pnrRespository.findOne(id);
	}

	@Override
	@Transactional
	public List<Pnr> findAll() {
		return (List<Pnr>) pnrRespository.findAll();
	}
	
	private void mapPnr(Pnr source, Pnr target){
		target.setBagCount(source.getBagCount());
		target.setDateBooked(source.getDateBooked());
		target.setCarrier(source.getCarrier());
		target.setChangeDate();
		target.setDaysBookedBeforeTravel(source.getDaysBookedBeforeTravel());
		target.setDepartureDate(source.getDepartureDate());
		target.setFormOfPayment(source.getFormOfPayment());
		target.setOrigin(source.getOrigin());
		target.setOriginCountry(source.getOriginCountry());	
		target.setPassengerCount(source.getPassengerCount());
		target.setDateReceived(source.getDateReceived());
		target.setTotalDwellTime(source.getTotalDwellTime());
		target.setUpdatedAt(new Date());
		target.setUpdatedBy(source.getUpdatedBy());
		
		if(source.getAddresses() != null && source.getAddresses().size() >0){
			Iterator it = source.getAddresses().iterator();
			while(it.hasNext()){
				Address a= (Address)it.next();
				
				//TODO equals contract is not working for address.work around/compare mannually
				Address chkAddress = getExistingAddress(a,target.getAddresses());
				if(chkAddress == null){
					target.addAddress(a);
				}
			}
		}
		if(source.getAgency() != null && target.getAgency() == null){
			target.setAgency(source.getAgency());
		}
		
		// TODO
//		if(source.getPnrMessage() != null && target.getPnrMessage() == null){
//			target.setPnrMessage(source.getPnrMessage());
//		}
		if(source.getCreditCards()!= null && source.getCreditCards().size() >0){
			Iterator it1 = source.getCreditCards().iterator();
			while(it1.hasNext()){
				CreditCard cc = (CreditCard)it1.next();
				if(!target.getCreditCards().contains(cc)){
					target.addCreditCard(cc);
				}
			}
		}
		if(source.getFrequentFlyers() != null && source.getFrequentFlyers().size() >0){
			Iterator it2 = source.getFrequentFlyers().iterator();
			while(it2.hasNext()){
				FrequentFlyer ff = (FrequentFlyer)it2.next();
				if(!target.getFrequentFlyers().contains(ff)){
					target.addFrequentFlyer(ff);
				}
			}
		}
		if(source.getEmails() != null && source.getEmails().size() >0){
			Iterator it3 = source.getEmails().iterator();
			while(it3.hasNext()){
				Email e = (Email)it3.next();
				if(!target.getEmails().contains(e)){
					target.addEmail(e);
				}
			}
		}
		if(source.getPhones() != null && source.getPhones().size()>0){
			Iterator it4 = source.getPhones().iterator();
			while(it4.hasNext()){
				Phone p = (Phone)it4.next();
				if(!target.getPhones().contains(p)){
					target.addPhone(p);
				}
			}
		}
		
	}
	private Address getExistingAddress(Address a, Set<Address> addresses){
		Address chk = null;
		if(addresses != null && addresses.size() >0){
			Iterator it = addresses.iterator();
			while(it.hasNext()){
				chk = (Address) it.next();
				if(StringUtils.equals(a.getCity(), chk.getCity()) && StringUtils.equals(a.getCountry(), chk.getCountry())
						&& StringUtils.equals(a.getLine1(), chk.getLine1()) && StringUtils.equals(a.getState(), chk.getState())
								&& StringUtils.equals(a.getPostalCode(), chk.getPostalCode())){
					return chk;
				}
				chk=null;
			}
		}
		return chk;
		
	}
}
