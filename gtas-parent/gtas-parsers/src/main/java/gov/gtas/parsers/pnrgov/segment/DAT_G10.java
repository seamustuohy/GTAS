package gov.gtas.parsers.pnrgov.segment;

import java.util.Date;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Element;
import gov.gtas.parsers.edifact.Segment;
import gov.gtas.parsers.exception.ParseException;
import gov.gtas.parsers.pnrgov.PnrUtils;

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
	
	public DAT_G10(Composite[] composites) throws ParseException {
		super(DAT_G10.class.getSimpleName(), composites);
        Element[] e = this.composites[0].getElements();
        String dt = null;
        if (e.length >= 2) {
            dt = e[1].getValue();
            if (e.length >= 3) {
                dt += e[2].getValue();
            }
            this.pnrHistorytDateTime = PnrUtils.parseDateTime(dt);
        }
	}

    public Date getPnrHistorytDateTime() {
        return pnrHistorytDateTime;
    }
}
