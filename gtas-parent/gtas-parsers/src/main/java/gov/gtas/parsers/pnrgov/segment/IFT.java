package gov.gtas.parsers.pnrgov.segment;

import java.util.ArrayList;
import java.util.List;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Element;
import gov.gtas.parsers.edifact.Segment;

/**
 * <p>
 * IFT: INTERACTIVE FREE TEXT
 * <p>
 * Class to hold Interactive free text
 * <p>
 * Examples
 * <ul>
 * <li>Fare calculation with fare calculation reporting indicator(IFT+4:15:0+DEN
 * UA LAX 01.82 487.27 UA DEN 487.27 USD976.36 ENDXFDEN3LAX+3')
 * <li>OSI information.(IFT+4:28::KL+CTC 7732486972-U‘)
 * <li>Sponsor information.(IFT+4:43+TIMOTHY SIMS+2234 MAIN STREET ATLANTA, GA
 * 30067+770 5632891‘)
 * </ul>
 */
public class IFT extends Segment {
    private String iftCode;
    
    /** A code describing data in message */
    private String freetextType;
    
    /** Fare calculation reporting indicator */
    private String pricingIndicator;
    
    /** Validating carrier airline designator */
    private String airline;
    
    /** ISO Code for Language of free text */
    private String freeTextLanguageCode;
    
    /** Free text message */
    private List<String> messages;

    public IFT(Composite[] composites) {
        super(IFT.class.getSimpleName(), composites);
        this.messages = new ArrayList<>();
        
        Element[] e = this.composites[0].getElements();
        if (e.length >= 1) {
            this.iftCode = e[0].getValue();
        }
        if (e.length >= 2) {
            this.freetextType = e[1].getValue();
        }
        if (e.length >= 3) {
            this.pricingIndicator = e[2].getValue();
        }
        if (e.length >= 4) {
            this.airline = e[3].getValue();
        }
        if (e.length > 5) {
            this.freeTextLanguageCode = e[4].getValue();
        }
        
        for (int i=1; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            if (c != null) {
                messages.add(c.getValue());
            }
        }
    }

    public String getIftCode() {
        return iftCode;
    }

    public String getFreetextType() {
        return freetextType;
    }

    public String getPricingIndicator() {
        return pricingIndicator;
    }

    public String getAirline() {
        return airline;
    }

    public String getFreeTextLanguageCode() {
        return freeTextLanguageCode;
    }

    public List<String> getMessages() {
        return messages;
    }
}
