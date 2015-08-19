package gov.gtas.parsers.pnrgov;

import java.util.ArrayList;
import java.util.List;

import gov.gtas.parsers.edifact.MessageVo;
import gov.gtas.parsers.pnrgov.segment.MSG.MsgCode;

public class PnrMessageVo extends MessageVo {
    private MsgCode messageCode;
    private List<PnrVo> pnrRecords;
    
    public PnrMessageVo() {
        pnrRecords = new ArrayList<>();
    }
    
    public MsgCode getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(MsgCode messageCode) {
        this.messageCode = messageCode;
    }

    public void setPnrRecords(List<PnrVo> pnrRecords) {
        this.pnrRecords = pnrRecords;
    }

    public void addPnr(PnrVo pnr) {
        pnrRecords.add(pnr);
    }
    
    public List<PnrVo> getPnrRecords() {
        return pnrRecords;
    }
}
