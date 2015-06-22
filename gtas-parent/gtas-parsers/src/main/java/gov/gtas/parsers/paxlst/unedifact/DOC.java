package gov.gtas.parsers.paxlst.unedifact;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Element;
import gov.gtas.parsers.edifact.Segment;

public class DOC extends Segment {
    private String docCode;
    private String c_codeListIdentificationCode;
    private String c_codeListResponsibleAgencyCode;
    private String documentIdentifier;
    
    public DOC(Composite[] composites) {
        super(DOC.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            Element[] e = c.getElements();
            switch (i) {
            case 0:
                this.docCode = e[0].getValue();
                if (e.length >= 2) {
                    this.c_codeListIdentificationCode = e[1].getValue();
                }
                if (e.length >= 3) {
                    this.c_codeListResponsibleAgencyCode = e[2].getValue();
                }
                break;
                
            case 1:
                this.documentIdentifier = c.getValue();
            }
        }
    }

    public String getDocCode() {
        return docCode;
    }

    public void setDocCode(String docCode) {
        this.docCode = docCode;
    }

    public String getC_codeListIdentificationCode() {
        return c_codeListIdentificationCode;
    }

    public void setC_codeListIdentificationCode(String c_codeListIdentificationCode) {
        this.c_codeListIdentificationCode = c_codeListIdentificationCode;
    }

    public String getC_codeListResponsibleAgencyCode() {
        return c_codeListResponsibleAgencyCode;
    }

    public void setC_codeListResponsibleAgencyCode(
            String c_codeListResponsibleAgencyCode) {
        this.c_codeListResponsibleAgencyCode = c_codeListResponsibleAgencyCode;
    }

    public String getDocumentIdentifier() {
        return documentIdentifier;
    }

    public void setDocumentIdentifier(String documentIdentifier) {
        this.documentIdentifier = documentIdentifier;
    }
}
