package gov.cbp.taspd.gtas.parsers.paxlst.segments;

import gov.cbp.taspd.gtas.parsers.unedifact.Composite;
import gov.cbp.taspd.gtas.parsers.unedifact.Element;
import gov.cbp.taspd.gtas.parsers.unedifact.Segment;

public class FTX extends Segment {
    public enum SubjectCode {
        BAG
    }
    
    private SubjectCode subjectCodeQualifier;
    private String text1;
    private String text2;
    
    public FTX(Composite[] composites) {
        super(FTX.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            Element[] e = c.getElements();
            switch (i) {
            case 0:
                switch(c.getValue()) {
                case "BAG":
                    this.subjectCodeQualifier = SubjectCode.BAG;
                    break;
                }
                break;
                
            case 3:
                if (e.length >= 1) {
                    this.text1 = e[0].getValue();
                }
                if (e.length >= 2) {
                    this.text2 = e[1].getValue();                    
                }
                break;
            }
        }
    }

    public SubjectCode getSubjectCodeQualifier() {
        return subjectCodeQualifier;
    }

    public String getText1() {
        return text1;
    }

    public String getText2() {
        return text2;
    }
}
