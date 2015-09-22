package gov.gtas.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import gov.gtas.delegates.PnrServiceDelegate;
import gov.gtas.vo.PnrVo;

@Controller
public class PnrController {
    private static final Logger logger = LoggerFactory.getLogger(PnrController.class);
    
    @Autowired
    private PnrServiceDelegate pnrDelegate;
    
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/pnrdata/{pnrId}", method = RequestMethod.GET)
    public PnrVo getPnrData(@PathVariable Long pnrId) {
        return pnrDelegate.getAllPnrData(pnrId);
    }    

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/pnrdata/pax/{paxId}", method = RequestMethod.GET)
    public List<PnrVo> getRecordLocators(@PathVariable Long paxId) {
        return pnrDelegate.getRecordLocators(paxId);
    }    
}
