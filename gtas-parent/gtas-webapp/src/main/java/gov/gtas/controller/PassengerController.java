package gov.gtas.controller;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import gov.gtas.model.Document;
import gov.gtas.model.HitsSummary;
import gov.gtas.model.Passenger;
import gov.gtas.dataobject.DocumentVo;
import gov.gtas.dataobject.PassengerVo;
import gov.gtas.repository.DocumentRepository;
import gov.gtas.services.HitsSummaryService;
import gov.gtas.services.PassengerService;

@Controller
public class PassengerController {
    private static final Logger logger = LoggerFactory.getLogger(PassengerController.class);
    
    @Autowired
    private PassengerService pService;
    
    @Autowired
    private DocumentRepository docDao;
    
    @Autowired
    private HitsSummaryService hitsSummaryService;
    
    private static List<HitsSummary> hitsList = new ArrayList<HitsSummary>();
       
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
    public List<PassengerVo> getPassengersForUpcomingFlights(@PageableDefault(page= 1 , size=250) Pageable pageable) {
        List<PassengerVo> rv = new ArrayList<>();
        List<Passenger> passengers = null;
        
        passengers = pService.getPassengersFromUpcomingFlights(pageable);
        
        //passengers = pService.findAll();
        
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
    
    
}
