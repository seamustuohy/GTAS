package gov.gtas.controller;

import java.util.ArrayList;
import java.util.List;

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

import gov.gtas.model.Document;
import gov.gtas.model.Traveler;
import gov.gtas.parsers.vo.air.DocumentVo;
import gov.gtas.parsers.vo.air.TravelerVo;
import gov.gtas.repository.DocumentRepository;
import gov.gtas.services.PassengerService;

@Controller
public class TravelerController {
    private static final Logger logger = LoggerFactory.getLogger(TravelerController.class);
    
    @Autowired
    private PassengerService pService;
    
    @Autowired
    private DocumentRepository docDao;
    
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/travelers", method = RequestMethod.GET)
    public List<TravelerVo> getAllTravelers(@RequestParam(value = "flightId", required = false) String flightId) {
        List<TravelerVo> rv = new ArrayList<>();
        List<Traveler> travelers = null;
        if (flightId != null) {
            Long id = Long.valueOf(flightId);
            travelers = pService.getPassengersByFlightId(id);
        } else {
            travelers = pService.findAll();
        }
        
        if (travelers == null) return rv;
        
        for (Traveler t : travelers) {
            logger.debug(t.getLastName());
            TravelerVo vo = new TravelerVo();
            vo.setTravelerType(t.getTravelerType());
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
            List<Document> docs = docDao.getTravelerDocuments(t.getId());
            
            for (Document d : docs) {
                DocumentVo docVo = new DocumentVo();
                docVo.setDocumentNumber(d.getDocumentNumber());
                docVo.setDocumentType(d.getDocumentType());
                docVo.setIssuanceCountry(d.getIssuanceCountry());
                docVo.setExpirationDate(d.getExpirationDate());
                docVo.setIssuanceDate(d.getIssuanceDate());
                vo.addDocument(docVo);
            }
            rv.add(vo);
        }
        
        return rv;
    }
}
