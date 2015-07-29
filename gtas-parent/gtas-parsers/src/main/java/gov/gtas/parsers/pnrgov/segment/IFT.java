package gov.gtas.parsers.pnrgov.segment;

import java.util.ArrayList;
import java.util.List;

import gov.gtas.parsers.edifact.Composite;
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
        Composite c = getComposite(0);
        this.iftCode = c.getElement(0);
        this.freetextType = c.getElement(1);
        this.pricingIndicator = c.getElement(2);
        this.airline = c.getElement(3);
        this.freeTextLanguageCode = c.getElement(4);
        
        for (int i=1; i<numComposites(); i++) {
            c = getComposite(i);
            if (c != null) {
                messages.add(c.getElement(0));
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
