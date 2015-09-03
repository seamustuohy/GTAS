package gov.gtas.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import gov.gtas.model.Address;
import gov.gtas.model.CreditCard;
import gov.gtas.model.Document;
import gov.gtas.model.Email;
import gov.gtas.model.Flight;
import gov.gtas.model.FrequentFlyer;
import gov.gtas.model.HitsSummary;
import gov.gtas.model.Passenger;
import gov.gtas.model.Phone;
import gov.gtas.model.Pnr;
import gov.gtas.dataobject.AddressVo;
import gov.gtas.dataobject.AgencyVo;
import gov.gtas.dataobject.CreditCardVo;
import gov.gtas.dataobject.DocumentVo;
import gov.gtas.dataobject.EmailVo;
import gov.gtas.dataobject.FrequentFlyerVo;
import gov.gtas.dataobject.PassengerVo;
import gov.gtas.dataobject.PhoneVo;
import gov.gtas.dataobject.PnrVo;
import gov.gtas.repository.DocumentRepository;
import gov.gtas.services.FlightService;
import gov.gtas.services.PnrService;
import gov.gtas.services.HitsSummaryService;
import gov.gtas.services.PassengerService;

@Controller
public class PassengerController {
    private static final Logger logger = LoggerFactory.getLogger(PassengerController.class);
    
    @Autowired
    private PassengerService pService;
    
    @Autowired
    private FlightService fService;
    
    @Autowired
    private PnrService pnrService;
            
    @Autowired
    private DocumentRepository docDao;
    
    @Autowired
    private HitsSummaryService hitsSummaryService;
    
    private static List<HitsSummary> hitsList = new ArrayList<HitsSummary>();
    
    private static final int PAGE_SIZE = 25;
    private static final String EMPTY_STRING="";
       
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/passengers", method = RequestMethod.GET)
    public List<PassengerVo> getAllPassengers(@RequestParam(value = "flightId", required = false) String flightId) {
        List<PassengerVo> rv = new ArrayList<>();
        List<Passenger> passengers = null;
        if (flightId != null) {
            Long id = Long.valueOf(flightId);
            passengers = pService.getPassengersByFlightId(id);
        } else {
            passengers = pService.findAll();
        }
        
        if (passengers == null) return rv;
        
        for (Passenger t : passengers) {
            logger.debug(t.getLastName());
            PassengerVo vo = new PassengerVo();
            vo.setPaxId(String.valueOf(t.getId()));
            vo.setPassengerType(t.getPassengerType());
            vo.setLastName(t.getLastName());
            vo.setFirstName(t.getFirstName());
            vo.setMiddleName(t.getMiddleName());
            vo.setCitizenshipCountry(t.getCitizenshipCountry());
            vo.setDebarkation(t.getDebarkation());
            vo.setDebarkCountry(t.getDebarkCountry());
            vo.setDob(t.getDob());
            vo.setEmbarkation(t.getEmbarkation());
            vo.setEmbarkCountry(t.getEmbarkCountry());
            vo.setGender(t.getGender().toString());
            vo.setResidencyCountry(t.getResidencyCountry());
            vo.setSuffix(t.getSuffix());
            vo.setTitle(t.getTitle());
            List<Document> docs = docDao.getPassengerDocuments(t.getId());
            
            for (Document d : docs) {
                DocumentVo docVo = new DocumentVo();
                docVo.setDocumentNumber(d.getDocumentNumber());
                docVo.setDocumentType(d.getDocumentType());
                docVo.setIssuanceCountry(d.getIssuanceCountry());
                docVo.setExpirationDate(d.getExpirationDate());
                docVo.setIssuanceDate(d.getIssuanceDate());
                vo.addDocument(docVo);
            }
            
            vo.setRuleHits(getTotalHitsByPaxID(t.getId()));

            rv.add(vo);
        }

        hitsList.clear();
        return rv;
    }
    
    
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/passengers/all", method = RequestMethod.GET)
    @Transactional
    public LinkedList getPassengersForUpcomingFlights(@PageableDefault(page= 1 , size=25) Pageable pageable,
													    	 @RequestParam(value = "startDate", required = false) String startDate,
															 @RequestParam(value = "endDate", required = false) String endDate) {
        List<PassengerVo> rv = new ArrayList<>();
        List<Passenger> passengers = null;
        Iterator _tempIter;
        LinkedList _tempLL = new LinkedList();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        
        try {
			
        	passengers = pService.getPassengersByFlightDates(sdf.parse(startDate), sdf.parse(endDate));
		
			
        } catch (ParseException ex) {
			ex.printStackTrace();
		}
        
        //passengers = pService.getPassengersFromUpcomingFlights(pageable);
        //passengers = pService.findAll();
        
        if (passengers == null) return new LinkedList();//rv;
        
        for (Passenger t : passengers) {
        	
        	PassengerVo vo = new PassengerVo();
        	try {
            vo.setPaxId(String.valueOf(t.getId()));
            vo.setPassengerType(t.getPassengerType());
            vo.setLastName(t.getLastName());
            vo.setFirstName(t.getFirstName());
            vo.setMiddleName(t.getMiddleName());
            vo.setCitizenshipCountry(t.getCitizenshipCountry());
            vo.setDebarkation(t.getDebarkation());
            vo.setDebarkCountry(t.getDebarkCountry());
            vo.setDob(t.getDob());
            vo.setEmbarkation(t.getEmbarkation());
            vo.setEmbarkCountry(t.getEmbarkCountry());
            vo.setGender(t.getGender().toString());
            vo.setResidencyCountry(t.getResidencyCountry());
            vo.setSuffix(t.getSuffix());
            vo.setTitle(t.getTitle());
            
            vo.setRuleHits(getTotalHitsByPaxID(t.getId()));
            
        	List<Flight> flightList = fService.getFlightByPaxId(t.getId());
        	for(Flight _tempFlight : flightList) {
        		PassengerVo tempVo = new PassengerVo();
        		
				BeanUtils.copyProperties(tempVo, vo);
				
        		tempVo.setFlightNumber(_tempFlight.getFlightNumber());
        		tempVo.setCarrier(_tempFlight.getCarrier());
        		tempVo.setFlightOrigin(_tempFlight.getOrigin());
        		tempVo.setFlightDestination(_tempFlight.getDestination());
        		tempVo.setFlightETA((_tempFlight.getEta()!=null)?_tempFlight.getEta().toString(): EMPTY_STRING);
        		tempVo.setFlightETD((_tempFlight.getEtd()!=null)?_tempFlight.getEtd().toString(): EMPTY_STRING);
        		tempVo.setFlightId(_tempFlight.getId().toString());
                rv.add(tempVo);
            
        	} // for loop
        	

        	} catch (IllegalAccessException e) {
        		e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}catch(Exception ex){
				ex.printStackTrace();
			}
                
        	
        } // End of Passengers Loop
        
        
        _tempLL.add(0, passengers.size());
        _tempLL.add(1, rv);
       // return rv;
        return _tempLL;
    }
    
    
    
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/passengers/page/{pageNumber}", method = RequestMethod.GET)
    @Transactional
    public List<PassengerVo> getPassengersByPage(@PathVariable(value = "pageNumber") String pageNumber,
    											@RequestParam(value = "name", required = false) String name,
    											@RequestParam(value = "field", required = false) String field) {
    	
        List<PassengerVo> rv = new ArrayList<>();
        List<Passenger> passengers = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
        Iterator _tempIter;
        PageRequest request =
                new PageRequest(Integer.parseInt(pageNumber) - 1, PAGE_SIZE/*, Sort.Direction.ASC, "eta"*/);
        
        if(field != null && !field.isEmpty() && name != null && !name.isEmpty())
        {
        	passengers = pService.getPaxByLastName(name, request);
        }else{
        	passengers = pService.getPassengersFromUpcomingFlights(request);
        }
        
        //passengers = pService.findAll();
        
        if (passengers == null) return rv;
        
        for (Passenger t : passengers) {
        	
        	PassengerVo vo = new PassengerVo();
            vo.setPaxId(String.valueOf(t.getId()));
            vo.setPassengerType(t.getPassengerType());
            vo.setLastName(t.getLastName());
            vo.setFirstName(t.getFirstName());
            vo.setMiddleName(t.getMiddleName());
            vo.setCitizenshipCountry(t.getCitizenshipCountry());
            vo.setDebarkation(t.getDebarkation());
            vo.setDebarkCountry(t.getDebarkCountry());
            vo.setEmbarkation(t.getEmbarkation());
            vo.setEmbarkCountry(t.getEmbarkCountry());
            vo.setGender(t.getGender().toString());
            vo.setResidencyCountry(t.getResidencyCountry());
            vo.setSuffix(t.getSuffix());
            vo.setTitle(t.getTitle());
            try {
				vo.setDob(sdf.parse(t.getDob().toString()));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}            
            vo.setRuleHits(getTotalHitsByPaxID(t.getId()));
            
        	List<Flight> flightList = fService.getFlightByPaxId(t.getId());
        	for(Flight _tempFlight : flightList) {
        		PassengerVo tempVo = new PassengerVo();
        		try {
					BeanUtils.copyProperties(tempVo, vo);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		tempVo.setFlightNumber(_tempFlight.getFlightNumber());
        		tempVo.setCarrier(_tempFlight.getCarrier());
        		tempVo.setFlightOrigin(_tempFlight.getOrigin());
        		tempVo.setFlightDestination(_tempFlight.getDestination());
        		tempVo.setFlightETA(_tempFlight.getEta().toString());
        		tempVo.setFlightETD(_tempFlight.getEtd().toString());
        		tempVo.setFlightId(_tempFlight.getId().toString());
                rv.add(tempVo);
            
        	} // while loop
        } // End of Passengers Loop
        
        return rv;
    }
    
    
    
    @Transactional
    public int getTotalHitsByPaxID(Long passengerID){

    	int totalHits = 0;
    	if(hitsList == null || hitsList.isEmpty()){
    		Iterable<HitsSummary> summary = hitsSummaryService.findAll();
            
            for(HitsSummary s:summary){
            	hitsList.add((HitsSummary)s);
            }
    	} // END IF
    	    		
    	for(HitsSummary s: hitsList){
    		if(s.getPassengerId().equals(passengerID)){
    			totalHits++;
    			}
    		}
    	    	
    	return totalHits;
    }
    
    
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/passengers/{id}", method = RequestMethod.GET)
    public PassengerVo getPassengerByPaxIdAndFlightId(@PathVariable(value = "id") String paxId, @RequestParam(value = "flightId", required = false) String flightId) {
        List<PassengerVo> rv = new ArrayList<>();
        PassengerVo vo = new PassengerVo();
        Iterator _tempIter;
        Pnr _tempPnr = new Pnr();
        List _tempPnrList = new ArrayList();
        
        Long id = Long.valueOf(paxId);
        Passenger t = pService.findById(id);
    	List<Flight> flightList = fService.getFlightByPaxId(t.getId());
    	for(Flight _tempFlight : flightList) {
	    		if(flightId != null && _tempFlight.getId().toString().equals(flightId)){
		    		vo.setFlightNumber(_tempFlight.getFlightNumber());
		    		vo.setCarrier(_tempFlight.getCarrier());
		    		vo.setFlightOrigin(_tempFlight.getOrigin());
		    		vo.setFlightDestination(_tempFlight.getDestination());
		    		vo.setFlightETA(_tempFlight.getEta().toString());
		    		vo.setFlightETD(_tempFlight.getEtd().toString());
		    		vo.setFlightId(_tempFlight.getId().toString());
	    		}
			}
        
        vo.setPaxId(String.valueOf(t.getId()));
        vo.setPassengerType(t.getPassengerType());
        vo.setLastName(t.getLastName());
        vo.setFirstName(t.getFirstName());
        vo.setMiddleName(t.getMiddleName());
        vo.setCitizenshipCountry(t.getCitizenshipCountry());
        vo.setDebarkation(t.getDebarkation());
        vo.setDebarkCountry(t.getDebarkCountry());
        vo.setDob(t.getDob());
        vo.setSeat(t.getSeat());
        vo.setEmbarkation(t.getEmbarkation());
        vo.setEmbarkCountry(t.getEmbarkCountry());
        vo.setGender(t.getGender().toString());
        vo.setResidencyCountry(t.getResidencyCountry());
        vo.setSuffix(t.getSuffix());
        vo.setTitle(t.getTitle());
        //        List<Document> docs = docDao.getPassengerDocuments(t.getId());
      _tempIter = t.getDocuments().iterator();
      
      while(_tempIter.hasNext()){
      	Document d = (Document)_tempIter.next();
      //for (Document d : docs) {
          DocumentVo docVo = new DocumentVo();
          docVo.setDocumentNumber(d.getDocumentNumber());
          docVo.setDocumentType(d.getDocumentType());
          docVo.setIssuanceCountry(d.getIssuanceCountry());
          docVo.setExpirationDate(d.getExpirationDate());
          docVo.setIssuanceDate(d.getIssuanceDate());
          vo.addDocument(docVo);
      }
      
      //   vo.setRuleHits(getTotalHitsByPaxID(t.getId()));
        
      //Gather PNR Details
      _tempPnrList = pnrService.findPnrByPassengerIdAndFlightId(t.getId(), new Long(flightId));
      
      if(_tempPnrList.size() > 0)
      	vo.setPnrVo(mapPnrToPnrVo((Pnr)_tempPnrList.get(0)));
      
        hitsList.clear();
        return vo;
    }
    
    
    
    public PnrVo mapPnrToPnrVo(Pnr source){
    	
    	PnrVo target = new PnrVo();
    	
    	target.setRecordLocator(source.getRecordLocator());
		target.setBagCount(source.getBagCount());
		target.setDateBooked(source.getDateBooked());
		target.setCarrier(source.getCarrier());
		//target.setChangeDate();
		target.setDaysBookedBeforeTravel(source.getDaysBookedBeforeTravel());
		target.setDepartureDate(source.getDepartureDate());
		target.setFormOfPayment(source.getFormOfPayment());
		target.setOrigin(source.getOrigin());
		target.setOriginCountry(source.getOriginCountry());	
		target.setPassengerCount(source.getPassengerCount());
		target.setDateReceived(source.getDateReceived());
		target.setTotalDwellTime(source.getTotalDwellTime());
		//target.setUpdatedAt(new Date());
		//target.setUpdatedBy(source.getUpdatedBy());
		
		if(source.getAddresses() != null && source.getAddresses().size() >0){
			Iterator it = source.getAddresses().iterator();
			while(it.hasNext()){
				Address a= (Address)it.next();
				AddressVo aVo = new AddressVo();
				
				try {
					
					BeanUtils.copyProperties(aVo, a);
					
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				target.getAddresses().add(aVo);
				
			} // End of While Loop
			
		}
				
				
		if(source.getAgency() != null && target.getAgency() == null){
			AgencyVo aVo = new AgencyVo();
			copyModelToVo(source.getAgency(), aVo);
			target.setAgency(aVo);
		}
//		
//		// TODO
////		if(source.getPnrMessage() != null && target.getPnrMessage() == null){
////			target.setPnrMessage(source.getPnrMessage());
////		}
		if(source.getCreditCards()!= null && source.getCreditCards().size() >0){
			Iterator it1 = source.getCreditCards().iterator();
			while(it1.hasNext()){
				CreditCard cc = (CreditCard)it1.next();
				CreditCardVo cVo = new CreditCardVo();
				copyModelToVo(cc, cVo);
				target.getCreditCards().add(cVo);
			}
		}
		if(source.getFrequentFlyers() != null && source.getFrequentFlyers().size() >0){
			Iterator it2 = source.getFrequentFlyers().iterator();
			while(it2.hasNext()){
				FrequentFlyer ff = (FrequentFlyer)it2.next();
				FrequentFlyerVo fVo = new FrequentFlyerVo();
				copyModelToVo(ff, fVo);
				target.getFrequentFlyers().add(fVo);
			}
		}
		
		if(source.getEmails() != null && source.getEmails().size() >0){
			Iterator it3 = source.getEmails().iterator();
			while(it3.hasNext()){
				Email e = (Email)it3.next();
				EmailVo eVo = new EmailVo();
				copyModelToVo(e, eVo);
				target.getEmails().add(eVo);
			}
		}

		if(source.getPhones() != null && source.getPhones().size()>0){
			Iterator it4 = source.getPhones().iterator();
			while(it4.hasNext()){
				Phone p = (Phone)it4.next();
				PhoneVo pVo = new PhoneVo();
				copyModelToVo(p, pVo);
				target.getPhones().add(pVo);
				
			}
		}
    	
    	return target;
    }
    
    
    private void copyModelToVo(Object source, Object target){
    	
    	try {
			
			BeanUtils.copyProperties(target, source);
			
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
}
