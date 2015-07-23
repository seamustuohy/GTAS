package gov.gtas.parsers.paxlst.segment.usedifact;

import java.text.ParseException;
import java.util.Date;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;
import gov.gtas.parsers.util.ParseUtils;

public class DTM extends Segment {
    public enum DtmCode {
        DEPARTURE_DATETIME,
        ARRIVAL_DATETIME
    }
    private DtmCode dtmCode;
    private String date;
    private String time;
    private Date c_dateTime;
    private String c_timezone;
    
    public DTM(Composite[] composites) throws ParseException {
        super(DTM.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            switch (i) {
            case 0:
                switch (c.getValue()) {
                case "136":
                    this.dtmCode = DtmCode.DEPARTURE_DATETIME;
                    break;
                case "132":
                    this.dtmCode = DtmCode.ARRIVAL_DATETIME;
                    break;
                default:
                    logger.error("unknown dtm code: " + c.getValue());
                    return;
                }
                break;
                
            case 1:
                this.date = c.getValue();
                break;
            case 2:
                this.time = c.getValue();
                break;
            case 3:
                // TODO: handle timezone
                break;
            }
        }
        
        String tmp = this.date + this.time;
        this.c_dateTime = ParseUtils.parseDateTime(tmp, "yyMMddhhmm");
    }

    public DtmCode getDtmCode() {
        return dtmCode;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public Date getC_dateTime() {
        return c_dateTime;
    }

    public String getC_timezone() {
        return c_timezone;
    }
}