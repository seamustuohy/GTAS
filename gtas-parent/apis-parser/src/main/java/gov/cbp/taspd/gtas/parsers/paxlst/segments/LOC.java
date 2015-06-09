package gov.cbp.taspd.gtas.parsers.paxlst.segments;

import gov.cbp.taspd.gtas.parsers.unedifact.Composite;
import gov.cbp.taspd.gtas.parsers.unedifact.Element;
import gov.cbp.taspd.gtas.parsers.unedifact.Segment;

public class LOC extends Segment {
    public enum FunctionCode {
        GATE_PASS_LOCATION,
        DEPARTURE_AIRPORT,
        ARRIVAL_AIRPORT,
        BOTH_DEPARTURE_AND_ARRIVAL_AIRPORT,
        FILING_LOCATION,
        REPORTING_LOCATION,
        
        // from pax LOC
        AIRPORT_OF_FIRST_US_ARRIVAL,
        COUNTRY_OF_RESIDENCE,
        PORT_OF_EMBARKATION,
        PORT_OF_DEBARKATION,
        PLACE_OF_BIRTH        
    }
    
    private FunctionCode functionCode;
    private String locationNameCode;
    private String firstRelatedLocationName;
    private String secondRelatedLocationName;
    
    public LOC(Composite[] composites) {
        super(LOC.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            Element[] e = c.getElements();
            
            switch (i) {
            case 0:
                switch(Integer.valueOf(c.getValue())) {
                case 87:
                    this.functionCode = FunctionCode.ARRIVAL_AIRPORT;
                    break;                    
                case 125:
                    this.functionCode = FunctionCode.DEPARTURE_AIRPORT;
                    break;                    
                case 92:
                    this.functionCode = FunctionCode.BOTH_DEPARTURE_AND_ARRIVAL_AIRPORT;
                    break;                   
                case 91:
                    this.functionCode = FunctionCode.GATE_PASS_LOCATION;
                    break;                    
                case 188:
                    this.functionCode = FunctionCode.FILING_LOCATION;
                    break;                    
                case 172:
                    this.functionCode = FunctionCode.REPORTING_LOCATION;
                    break;                    
                    
                case 22:
                    this.functionCode = FunctionCode.AIRPORT_OF_FIRST_US_ARRIVAL;
                    break;
                case 174:
                    this.functionCode = FunctionCode.COUNTRY_OF_RESIDENCE;
                    break;
                case 178:
                    this.functionCode = FunctionCode.PORT_OF_EMBARKATION;
                    break;
                case 179:
                    this.functionCode = FunctionCode.PORT_OF_DEBARKATION;
                    break;
                case 180:
                    this.functionCode = FunctionCode.PLACE_OF_BIRTH;
                    break;
                    
                }
                break;

            case 1:
                this.locationNameCode = c.getValue();
                break;
                
            case 2:
                if (e.length >= 3) {
                    this.firstRelatedLocationName = e[2].getValue();
                }
                break;

            case 3:
                if (e.length >= 3) {
                    this.secondRelatedLocationName = e[2].getValue();
                }
                break;
            }
        }
    }

    public FunctionCode getFunctionCode() {
        return functionCode;
    }

    public String getLocationNameCode() {
        return locationNameCode;
    }

    public String getFirstRelatedLocationName() {
        return firstRelatedLocationName;
    }

    public String getSecondRelatedLocationName() {
        return secondRelatedLocationName;
    }
}
