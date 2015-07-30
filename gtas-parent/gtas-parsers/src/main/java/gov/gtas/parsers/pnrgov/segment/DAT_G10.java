package gov.gtas.parsers.pnrgov.segment;

import java.util.Date;
import java.util.List;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;
import gov.gtas.parsers.exception.ParseException;

/**
 * <p>
 * DAT: DATE AND TIME INFORMATION
 * <p>
 * To convey information regarding estimated or actual dates and times of
 * operational events.
 * <p>
 * DAT at GR10 will hold PNR History transaction date/time
 * <p>
 * Unless specifically stated otherwise in bilateral agreement, the time is in
 * Universal Time Coordinated (UTC)
 */
public class DAT_G10 extends Segment {
	private Date pnrHistorytDateTime;
	
	public DAT_G10(List<Composite> composites) throws ParseException {
		super(DAT_G10.class.getSimpleName(), composites);
        this.pnrHistorytDateTime = DAT.processDt(getComposite(0));
	}

    public Date getPnrHistorytDateTime() {
        return pnrHistorytDateTime;
    }
}
