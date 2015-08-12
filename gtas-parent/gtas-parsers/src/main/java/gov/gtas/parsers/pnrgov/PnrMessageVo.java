package gov.gtas.parsers.pnrgov;

import java.util.ArrayList;
import java.util.List;

import gov.gtas.parsers.edifact.MessageVo;

public class PnrMessageVo extends MessageVo {
    private List<PnrVo> pnrRecords;
    
    public PnrMessageVo() {
        pnrRecords = new ArrayList<>();
    }
    
    public void addPnr(PnrVo pnr) {
        pnrRecords.add(pnr);
    }
    
    public List<PnrVo> getPnrRecords() {
        return pnrRecords;
    }
}
