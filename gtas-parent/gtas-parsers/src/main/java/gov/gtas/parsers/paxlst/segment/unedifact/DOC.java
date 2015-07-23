package gov.gtas.parsers.paxlst.segment.unedifact;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Element;
import gov.gtas.parsers.edifact.Segment;

/**
 * <p>
 * DOC DOCUMENT/MESSAGE DETAILS
 * <p>
 * Function: To identify the official travel document and/or other document used
 * for travel.
 * <ul>
 * <li>P: Indicates that the document type is a passport and its number.
 * <li>V: Indicates that the document type is a visa and its number.
 * <li>I: Indicates that the document type is state issued document of identity
 * and its number.
 * </ul>
 */
public class DOC extends Segment {
    private String docCode;
    private String documentIdentifier;

    public DOC(Composite[] composites) {
        super(DOC.class.getSimpleName(), composites);
        for (int i = 0; i < this.composites.length; i++) {
            Composite c = this.composites[i];
            Element[] e = c.getElements();
            switch (i) {
            case 0:
                if (e != null) {
                    // DHS specification
                    // DOC+L:110:111+AA299167
                    this.docCode = e[0].getValue();
                } else {
                    // DOC+P+QG176295
                    this.docCode = c.getValue();
                }

                break;

            case 1:
                this.documentIdentifier = c.getValue();
                break;
            }
        }
    }

    public String getDocCode() {
        return docCode;
    }

    public void setDocCode(String docCode) {
        this.docCode = docCode;
    }

    public String getDocumentIdentifier() {
        return documentIdentifier;
    }

    public void setDocumentIdentifier(String documentIdentifier) {
        this.documentIdentifier = documentIdentifier;
    }
}
