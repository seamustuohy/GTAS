package gov.gtas.parsers.pnrgov.vo;

import java.util.ArrayList;
import java.util.List;

import gov.gtas.parsers.edifact.MessageVo;

public class PnrMessageVo extends MessageVo {
    private List<PnrVo> pnrRecords = new ArrayList<>();

    public List<PnrVo> getPnrRecords() {
        return pnrRecords;
    }

    public void setPnrRecords(List<PnrVo> pnrRecords) {
        this.pnrRecords = pnrRecords;
    }
}
