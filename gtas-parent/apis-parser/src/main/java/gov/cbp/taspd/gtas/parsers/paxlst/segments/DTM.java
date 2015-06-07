package gov.cbp.taspd.gtas.parsers.paxlst.segments;

import gov.cbp.taspd.gtas.parsers.unedifact.Composite;
import gov.cbp.taspd.gtas.parsers.unedifact.Element;
import gov.cbp.taspd.gtas.parsers.unedifact.Segment;
import gov.cbp.taspd.gtas.parsers.unedifact.segments.UNB;
import gov.cbp.taspd.gtas.util.ParseUtils;

import java.util.Date;

public class DTM extends Segment {
    public enum DtmCode {
        DEPARTURE,
        ARRIVAL,
        ARRIVAL_AND_DEPARTURE_MCL,
        DATE_OF_BIRTH,
        UNKNOWN
    }
    
    private DtmCode dtmCodeQualifier;
    private Date dtmValue;
    private String dtmFormatCode;
    
    public DTM(Composite[] composites) {
        super(DTM.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            Element[] e = c.getElements();

            String dateFormat = UNB.DATE_TIME_FORMAT;
            switch(Integer.valueOf(e[0].getValue())) {
            case 189:
                this.dtmCodeQualifier = DtmCode.DEPARTURE;
                break;
            case 232:
                this.dtmCodeQualifier = DtmCode.ARRIVAL;
                break;
            case 554:
                this.dtmCodeQualifier = DtmCode.ARRIVAL_AND_DEPARTURE_MCL;
                break;
            case 329:
                this.dtmCodeQualifier = DtmCode.DATE_OF_BIRTH;                    
                dateFormat = "yyMMdd";
                break;
            default:
                System.err.println("DTM: invalid dtm code: " + e[0].getValue());
                this.dtmCodeQualifier = DtmCode.UNKNOWN;                    
                dateFormat = "yyMMdd";
            }
            
            this.dtmValue = ParseUtils.parseDateTime(e[1].getValue(), dateFormat);
            
            if (e.length > 2) {
                this.dtmFormatCode = e[2].getValue();
            }
        }
    }

    public DtmCode getDtmCodeQualifier() {
        return dtmCodeQualifier;
    }

    public Date getDtmValue() {
        return dtmValue;
    }

    public String getDtmFormatCode() {
        return dtmFormatCode;
    }
}
