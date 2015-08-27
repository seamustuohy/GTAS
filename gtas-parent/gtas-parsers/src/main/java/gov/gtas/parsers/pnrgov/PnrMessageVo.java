package gov.gtas.parsers.pnrgov;

import gov.gtas.parsers.edifact.MessageVo;
import gov.gtas.parsers.pnrgov.segment.MSG.MsgCode;

public class PnrMessageVo extends MessageVo {
    private MsgCode messageCode;
    private PnrVo pnr;
    
    public MsgCode getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(MsgCode messageCode) {
        this.messageCode = messageCode;
    }

    public PnrVo getPnr() {
        return pnr;
    }

    public void setPnr(PnrVo pnr) {
        this.pnr = pnr;
    }
}
