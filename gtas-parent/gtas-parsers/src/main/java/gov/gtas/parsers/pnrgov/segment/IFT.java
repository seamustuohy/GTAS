package gov.gtas.parsers.pnrgov.segment;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Segment;

/**
 * Class to hold Interactive free text
 * @author GTAS4
 * Ex:Fare calculation with fare calculation reporting indicator(IFT+4:15:0+DEN UA LAX 01.82 487.27 UA DEN 487.27 USD976.36 ENDXFDEN3LAX+3')
 * OSI information.(IFT+4:28::KL+CTC 7732486972-U‘)
 * Sponsor information.(IFT+4:43+TIMOTHY SIMS+2234 MAIN STREET ATLANTA, GA 30067+770 5632891‘)
 * 
 * 
 */
public class IFT extends Segment{

	private String codeSetValue;//See code set values
	private String freetextType;//A code describing data in message
	private String pricingIndicator;//Fare calculation reporting indicator
	private String airlineDesignator;//Validating carrier airline designator
	private String freeTextLanguageCode;//ISO Code for Language of free text
	private String[] messages;//Free text message
	
	public IFT(String name, Composite[] composites) {
		super(name, composites);
		
	}

	public String getCodeSetValue() {
		return codeSetValue;
	}

	public void setCodeSetValue(String codeSetValue) {
		this.codeSetValue = codeSetValue;
	}

	public String getFreetextType() {
		return freetextType;
	}

	public void setFreetextType(String freetextType) {
		this.freetextType = freetextType;
	}

	public String getPricingIndicator() {
		return pricingIndicator;
	}

	public void setPricingIndicator(String pricingIndicator) {
		this.pricingIndicator = pricingIndicator;
	}

	public String getAirlineDesignator() {
		return airlineDesignator;
	}

	public void setAirlineDesignator(String airlineDesignator) {
		this.airlineDesignator = airlineDesignator;
	}

	public String getFreeTextLanguageCode() {
		return freeTextLanguageCode;
	}

	public void setFreeTextLanguageCode(String freeTextLanguageCode) {
		this.freeTextLanguageCode = freeTextLanguageCode;
	}

	public String[] getMessages() {
		return messages;
	}

	public void setMessages(String[] messages) {
		this.messages = messages;
	}

}
