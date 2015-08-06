package gov.gtas.parsers.pnrgov;

import java.util.ArrayList;
import java.util.List;

import gov.gtas.parsers.edifact.EdifactMessageVo;

public class PnrMessageVo extends EdifactMessageVo {
    private List<PnrVo> pnrRecords = new ArrayList<>();
    public List<PnrVo> getPnrRecords() {
        return pnrRecords;
    }

    public void setPnrRecords(List<PnrVo> pnrRecords) {
        this.pnrRecords = pnrRecords;
    }
}
