package gov.gtas.controller;

import gov.gtas.model.Crew;
import gov.gtas.model.Pax;
import gov.gtas.model.Traveler;
import gov.gtas.model.lookup.Airport;
import gov.gtas.model.lookup.Country;
import gov.gtas.parsers.paxlst.vo.PaxVo;
import gov.gtas.services.PassengerService;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class TravelerController {
    private static final Logger logger = LoggerFactory.getLogger(TravelerController.class);
    
    @Autowired
    private PassengerService pService;
    
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/travelers", method = RequestMethod.GET)
    public List<PaxVo> getAllTravelers() {
        List<PaxVo> rv = new ArrayList<>();

        List<Traveler> travelers = pService.findAll();
        for (Traveler t : travelers) {
            logger.debug(t.getLastName());
            PaxVo vo = new PaxVo();
            vo.setLastName(t.getLastName());
            vo.setFirstName(t.getFirstName());
            vo.setMiddleName(t.getMiddleName());
            vo.setCitizenshipCountry(getCountryCode(t.getCitizenshipCountry()));
            vo.setDebarkation(getAirportCode(t.getDebarkation()));
            vo.setDebarkCountry(getCountryCode(t.getDebarkCountry()));
            vo.setDob(t.getDob());
            vo.setEmbarkation(getAirportCode(t.getEmbarkation()));
            vo.setEmbarkCountry(getCountryCode(t.getEmbarkCountry()));
            vo.setGender(t.getGender().toString());
            if (t instanceof Pax) {
                vo.setPaxType("P");
            } else if (t instanceof Crew) {
                vo.setPaxType("C");
            }
            vo.setResidencyCountry(getCountryCode(t.getResidencyCountry()));
            vo.setSuffix(t.getSuffix());
            vo.setTitle(t.getTitle());
            rv.add(vo);
        }
        
        return rv;
    }

    private String getCountryCode(Country c) {
        return c != null ? c.getIso2() : null;
    }

    private String getAirportCode(Airport a) {
        return a != null ? a.getIata() : null;
    }
}
