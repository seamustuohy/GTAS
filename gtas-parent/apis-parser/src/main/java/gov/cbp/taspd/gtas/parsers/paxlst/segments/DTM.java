package gov.cbp.taspd.gtas.parsers.paxlst.segments;

import gov.cbp.taspd.gtas.parsers.unedifact.Composite;
import gov.cbp.taspd.gtas.parsers.unedifact.Element;
import gov.cbp.taspd.gtas.parsers.unedifact.Segment;

public class DTM extends Segment {
    private String dtmCodeQualifier;
    private String dtmValue;
    private String dtmFormatCode;
    
    public DTM(Composite[] composites) {
        super(DTM.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            Element[] e = c.getElements();
            switch (i) {
            case 0:
                this.dtmCodeQualifier = e[0].getValue();
                this.dtmValue = e[1].getValue();
                if (e.length > 2) {
                    this.dtmFormatCode = e[2].getValue();
                }
            }
        }
    }

    public String getDtmCodeQualifier() {
        return dtmCodeQualifier;
    }

    public String getDtmValue() {
        return dtmValue;
    }

    public String getDtmFormatCode() {
        return dtmFormatCode;
    }
}
