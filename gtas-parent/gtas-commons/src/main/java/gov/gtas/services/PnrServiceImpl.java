/*
 * All GTAS code is Copyright 2016, Unisys Corporation.
 * 
 * Please see LICENSE.txt for details.
 */
package gov.gtas.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Date;
import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import gov.gtas.model.Address;
import gov.gtas.model.CreditCard;
import gov.gtas.model.Email;
import gov.gtas.model.FlightLeg;
import gov.gtas.model.FrequentFlyer;
import gov.gtas.model.Passenger;
import gov.gtas.model.Phone;
import gov.gtas.model.Pnr;
import gov.gtas.repository.PnrRepository;
import gov.gtas.vo.passenger.FlightLegVo;

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
    
    @Override
    @Transactional
    public List<Pnr> findByPassengerId(Long passengerId) {
        return pnrRespository.getPnrsByPassengerId(passengerId);
    }

    @Override
    public List<Pnr> getPNRsByDates(Date startDate, Date endDate){return pnrRespository.getPNRsByDates();}

    @Override
    @Transactional
    /*A duplicate method to avoid 'LazyInitializationException' in the Controller -- Can be removed after a fix */
    public List<Pnr> findPnrByPassengerIdAndFlightId(Long passengerId, Long flightId) {
        
        Pnr rv = new Pnr();
        List<Pnr> _retList = new ArrayList<Pnr>();
        List<Pnr> _tempPnrList = pnrRespository.getPnrsByPassengerIdAndFlightId(passengerId, flightId);
        
        for(Pnr _tempPnr : _tempPnrList ){
            rv = new Pnr();
            rv.setRecordLocator(_tempPnr.getRecordLocator());
            if(checkPassengerAndFlightOnPNR(_tempPnr, passengerId, flightId)){
                mapPnr(_tempPnr,rv);
                _retList.add(rv);
            }
        }
        return _retList;
    }

    private void mapPnr(Pnr source, Pnr target){
        target.setBagCount(source.getBagCount());
        target.setDateBooked(source.getDateBooked());
        target.setCarrier(source.getCarrier());
        target.setDaysBookedBeforeTravel(source.getDaysBookedBeforeTravel());
        target.setDepartureDate(source.getDepartureDate());
        target.setFormOfPayment(source.getFormOfPayment());
        target.setOrigin(source.getOrigin());
        target.setOriginCountry(source.getOriginCountry()); 
        target.setPassengerCount(source.getPassengerCount());
        target.setDateReceived(source.getDateReceived());
        target.setRaw(source.getRaw());
            
        if(source.getAddresses() != null && source.getAddresses().size() >0){
            Iterator it = source.getAddresses().iterator();
            while(it.hasNext()){
                Address a= (Address)it.next();
                
                //TODO equals contract is not working for address.work around/compare manually
                Address chkAddress = getExistingAddress(a,target.getAddresses());
                if(chkAddress == null){
                    target.addAddress(a);
                }
            }
        }
        if(!CollectionUtils.isEmpty(source.getAgencies())) {
            target.setAgencies(source.getAgencies());
        }
        
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
        if(source.getFlightLegs() != null && source.getFlightLegs().size() > 0){
            List<FlightLeg> _tempFL = source.getFlightLegs();
            for(FlightLeg fl : _tempFL){
                
                if(!target.getFlightLegs().contains(fl)){
                    target.getFlightLegs().add(fl);
                }
                
            }
        }
        
        if(source.getPassengers() != null && source.getPassengers().size() > 0){
            Iterator it6 = source.getPassengers().iterator();
            while(it6.hasNext()){
                Passenger passenger = (Passenger)it6.next();
                if(!target.getPassengers().contains(passenger)){
                    target.addPassenger(passenger);
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
    
    private boolean checkPassengerAndFlightOnPNR(Pnr source, Long passengerId, Long flightId){
        
        boolean flightCheck = false, passengerCheck = false;
        
        if(source.getFlightLegs() != null && source.getFlightLegs().size() > 0){
            List<FlightLeg> _tempFL = source.getFlightLegs();
            for(FlightLeg fl : _tempFL){
                
                if(fl.getFlight().getId().equals(flightId)){
                    flightCheck = true;
                    break;
                }
                
            }
        }
        
        if(source.getPassengers() != null && source.getPassengers().size() > 0){
            Iterator it6 = source.getPassengers().iterator();
            while(it6.hasNext()){
                Passenger passenger = (Passenger)it6.next();
                if(passenger.getId().equals(passengerId)){
                    passengerCheck = true;
                    break;
                }
                
            }
        }
        
        if(flightCheck && passengerCheck) return true;
        else return false;
        
    }
    
}
