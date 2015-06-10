package gov.cbp.taspd.gtas.parsers.paxlst.usedifact;

import gov.cbp.taspd.gtas.parsers.edifact.Composite;
import gov.cbp.taspd.gtas.parsers.edifact.Element;
import gov.cbp.taspd.gtas.parsers.edifact.Segment;

public class UNB extends Segment {
    public static final String DATE_TIME_FORMAT = "yyMMddhhmm";
    private String syntaxIdentifier;
    private String version;
    private String sender;
    private String senderQualifier;
    private String recipient;
    private String recipientQualifier;
    private String dateAndTimeOfPreparation;
    private String interchangeControlReference;
    private String applicationReference;
    private String c_priorityCode;
    private String testIndicator;
    
    public UNB(Composite[] composites) {
        super(UNB.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            Element[] e = c.getElements();
            switch (i) {
            case 0:
                this.syntaxIdentifier = e[0].getValue();
                this.version = e[1].getValue();
            case 1:
                this.sender = e[0].getValue();
                this.senderQualifier = e[1].getValue();
                break;
            }
        }
    }
}
