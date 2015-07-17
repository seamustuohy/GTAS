package gov.gtas.parsers.pnrgov.segment;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Element;
import gov.gtas.parsers.edifact.Segment;
import gov.gtas.parsers.util.ParseUtils;

import java.util.Date;

public class UNB extends Segment {
    private static final String DATE_TIME_FORMAT = "yyMMddhhmm";
    
    private String syntaxIdentifier;
    private String version;
    private String sender;
    private String senderQualifier;
    private String recipient;
    private String recipientQualifier;
    private Date dateAndTimeOfPreparation;
    private String interchangeControlReference;
    private String applicationReference;
    private String c_priorityCode;
    private String testIndicator;
    
    public UNB(Composite[] composites) {
        super(UNB.class.getSimpleName(), composites);
        
        /**name=UNB,composites=
    	{Composite[value=<null>,
    	elements={Element [value=IATA],
    	Element [value=1]}],
    	Composite[value=DL,elements=<null>],
    	Composite[value=,elements=<null>],
    	Composite[value=<null>,
    	elements={Element [value=101209],
    	Element [value=2100]}],
    	Composite[value=020A07,elements=<null>]}]
    	UNB+IATA:1+DL++101209:2100+020A07
       */

    }

    public String getSyntaxIdentifier() {
        return syntaxIdentifier;
    }

    public String getVersion() {
        return version;
    }

    public String getSender() {
        return sender;
    }

    public String getSenderQualifier() {
        return senderQualifier;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getRecipientQualifier() {
        return recipientQualifier;
    }

    public Date getDateAndTimeOfPreparation() {
        return dateAndTimeOfPreparation;
    }

    public String getInterchangeControlReference() {
        return interchangeControlReference;
    }

    public String getApplicationReference() {
        return applicationReference;
    }

    public String getC_priorityCode() {
        return c_priorityCode;
    }

    public String getTestIndicator() {
        return testIndicator;
    }

	public void setSyntaxIdentifier(String syntaxIdentifier) {
		this.syntaxIdentifier = syntaxIdentifier;
	}

	public void setVersion(String version) {
		this.version = version;
	}
    
}