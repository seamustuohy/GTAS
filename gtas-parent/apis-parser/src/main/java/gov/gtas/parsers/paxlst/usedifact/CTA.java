package gov.gtas.parsers.paxlst.usedifact;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Element;
import gov.gtas.parsers.edifact.Segment;

public class CTA extends Segment {
    private String contactFunctionCode;
    private String c_departmentOrEmployee;
    private String telephoneNumber;
    private String faxNumber;
    public CTA(Composite[] composites) {
        super(CTA.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            Element[] e = c.getElements();
            switch (i) {
            case 0:
                this.contactFunctionCode = c.getValue();
                break;
            case 1:
                if (e.length >= 2) {
                    this.c_departmentOrEmployee = e[1].getValue();
                }
                break;
            case 2:
            case 3:
                if (e.length >= 2) {
                    String code = e[1].getValue();
                    if (code.equals("TE")) {
                        this.telephoneNumber = e[0].getValue();
                    } else if (code.equals("FX")) {
                        this.faxNumber = e[0].getValue();
                    }
                }
                break;
            }
        }
    }

    public String getContactFunctionCode() {
        return contactFunctionCode;
    }
    public String getC_departmentOrEmployee() {
        return c_departmentOrEmployee;
    }
    public String getTelephoneNumber() {
        return telephoneNumber;
    }
    public String getFaxNumber() {
        return faxNumber;
    }
}