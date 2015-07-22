package gov.gtas.parsers.paxlst.segment.unedifact;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Element;
import gov.gtas.parsers.edifact.Segment;

/**
 * <p>
 * RFF: REFERENCE
 * <p>
 * The use of this segment is Mandatory for Secure Flight. The value sent by the
 * aircraft operator system in data element C506:1153 below will be returned to
 * the aircraft operator CUSRES message to facilitate the reconciliation of the
 * messages exchanged. Additionally, the numeric value in data element C506:1153
 * may be used to sequence any follow-on messages related to updates applied to
 * the same passenger manifest. This value will also be returned in the CURES
 * response message. ￼
 * <p>
 * Example: RFF+TN:OZ56789034:::2’ Indicates transaction reference number
 * OZ56789034 assigned by an airline system. The Revision Identifier may
 * optionally be used to identify this passenger data submission as the second
 * submission for this passenger (i.e updated passenger data).
 */
public class RFF extends Segment {
    private String referenceCodeQualifier;
    private String referenceIdentifier;
    private int revisionIdentifier;
    
    public RFF(Composite[] composites) {
        super(RFF.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            Element[] e = c.getElements();
            switch (i) {
            case 0:
                this.referenceCodeQualifier = e[0].getValue();
                if (this.referenceCodeQualifier.equals("TN")) {
                    this.referenceIdentifier = e[1].getValue();
                    if (e.length >= 5) {
                        this.revisionIdentifier = Integer.valueOf(e[4].getValue());
                    }
                }
            }
        }
    }

    public String getReferenceCodeQualifier() {
        return referenceCodeQualifier;
    }

    public String getReferenceIdentifier() {
        return referenceIdentifier;
    }

    public int getRevisionIdentifier() {
        return revisionIdentifier;
    }
}
