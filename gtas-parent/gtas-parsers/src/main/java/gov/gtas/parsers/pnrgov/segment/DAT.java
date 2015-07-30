package gov.gtas.parsers.pnrgov.segment;

import java.util.Date;

import gov.gtas.parsers.edifact.Composite;
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
        this.dateTime = processDt(composites[0]);
	}
	
    public static Date processDt(Composite c) throws ParseException {
        String dt = null;
        dt = c.getElement(1);
        String time = c.getElement(2);
        if (time != null) {
            dt += time;
        }
        return PnrUtils.parseDateTime(dt);
    }

    public Date getDateTime() {
        return dateTime;
    }
}
