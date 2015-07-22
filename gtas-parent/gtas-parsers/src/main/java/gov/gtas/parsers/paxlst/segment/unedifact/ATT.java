package gov.gtas.parsers.paxlst.segment.unedifact;

import java.util.LinkedHashMap;
import java.util.Map;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

public class ATT extends Segment {    
    public enum AttCode {
        GENDER("2");
        
        private final String code;
        private AttCode(String code) { this.code = code; }        
        public String getCode() { return code; }
        
        private static final Map<String, AttCode> BY_CODE_MAP = new LinkedHashMap<>();
        static {
            for (AttCode rae : AttCode.values()) {
                BY_CODE_MAP.put(rae.code, rae);
            }
        }

        public static AttCode forCode(String code) {
            return BY_CODE_MAP.get(code);
        }        
        
    }

    private AttCode functionCode;
    private String attributeDescriptionCode;
    
    public ATT(Composite[] composites) {
        super(ATT.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            switch (i) {
            case 0:
                this.functionCode = AttCode.forCode(c.getValue());
                break;
            case 2:
                this.attributeDescriptionCode = c.getValue();
                break;
            }
        }
    }

    public AttCode getFunctionCode() {
        return functionCode;
    }

    public String getAttributeDescriptionCode() {
        return attributeDescriptionCode;
    }
}
