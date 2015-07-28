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
 * Unless specifically stated otherwise in bilateral agreement, the time is in
 * Universal Time Coordinated (UTC)
 */
public class DAT extends Segment {
	private Date dateTime;
	
	public DAT(Composite[] composites) throws ParseException {
		super(DAT.class.getSimpleName(), composites);
        Element[] e = this.composites[0].getElements();
        String dt = null;
        if (e.length >= 2) {
            dt = e[1].getValue();
            if (e.length >= 3) {
                dt += e[2].getValue();
            }
            this.dateTime = PnrUtils.parseDateTime(dt);
        }
	}

    public Date getDateTime() {
        return dateTime;
    }
}
