package gov.gtas.parsers.paxlst.segment.usedifact;

import gov.gtas.parsers.edifact.Composite;
import gov.gtas.parsers.edifact.Element;
import gov.gtas.parsers.edifact.Segment;
import gov.gtas.parsers.util.ParseUtils;

import java.text.ParseException;
import java.util.Date;

public class PDT extends Segment {
    private static final String DATE_FORMAT = "yyMMdd";
    
    public enum DocType {
        PASSPORT,
        VISA,
        ALIEN_REGISTRATION
    }
    public enum PersonStatus {
        PAX,
        CREW,
        IN_TRANSIT
    }
    private DocType documentType;
    private String documentNumber;
    private Date c_dateOfExpiration;
    private String iataOriginatingCountry;
    private String lastName;
    private String firstName;
    private String c_middleNameOrInitial;
    private Date dob;
    private String gender;
    private PersonStatus personStatus;
    private String iataEmbarkationCountry;
    private String iataEmbarkationAirport;
    private String iataDebarkationCountry;    
    private String iataDebarkationAirport;
    
    public PDT(Composite[] composites) throws ParseException {
        super(PDT.class.getSimpleName(), composites);
        for (int i=0; i<this.composites.length; i++) {
            Composite c = this.composites[i];
            Element[] e = c.getElements();
            switch (i) {
            case 0:
                char code = e[0].getValue().charAt(0);
                switch (code) {
                case 'V':
                    this.documentType = DocType.VISA;
                    break;
                case 'A':
                    this.documentType = DocType.ALIEN_REGISTRATION;
                    break;
                default:
                    this.documentType = DocType.PASSPORT;
                    break;
                }
            
                String[] tmp = e[0].getValue().split("/");
                if (tmp.length >= 2) {
                    this.documentNumber = tmp[1];
                }
                
                if (e.length >= 2) {                    
                    this.c_dateOfExpiration = ParseUtils.parseDateTime(e[1].getValue(), DATE_FORMAT);
                }
                if (e.length >= 3) {
                    this.iataOriginatingCountry = e[2].getValue();
                }
                break;
                
            case 1:
                this.lastName = e[0].getValue();
                this.firstName = e[1].getValue();
                if (e.length >= 3) {
                    this.c_middleNameOrInitial = e[2].getValue();
                }
                this.dob = ParseUtils.parseDateTime(e[3].getValue(), DATE_FORMAT);
                this.gender = e[4].getValue();
                break;
            
            case 2:
                switch (c.getValue()) {
                case "PAX":
                    this.personStatus = PersonStatus.PAX;
                    break;
                case "CRW":
                    this.personStatus = PersonStatus.CREW;
                    break;
                case "ITI":
                    this.personStatus = PersonStatus.IN_TRANSIT;
                    break;
                default:
                    logger.error("unknown person type: " + c.getValue());
                    return;
                }
                break;
                
            case 3:
                String emb = e[0].getValue();
                this.iataEmbarkationCountry = emb.substring(0, 2);
                this.iataEmbarkationAirport = emb.substring(2, emb.length());
                String deb = e[1].getValue();
                this.iataDebarkationCountry = deb.substring(0, 2);
                this.iataDebarkationAirport = deb.substring(2, deb.length());
                break;
            }
        }
    }

    public DocType getDocumentType() {
        return documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public Date getC_dateOfExpiration() {
        return c_dateOfExpiration;
    }

    public String getIataOriginatingCountry() {
        return iataOriginatingCountry;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getC_middleNameOrInitial() {
        return c_middleNameOrInitial;
    }

    public Date getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public PersonStatus getPersonStatus() {
        return personStatus;
    }

    public String getIataEmbarkationCountry() {
        return iataEmbarkationCountry;
    }

    public String getIataEmbarkationAirport() {
        return iataEmbarkationAirport;
    }

    public String getIataDebarkationCountry() {
        return iataDebarkationCountry;
    }

    public String getIataDebarkationAirport() {
        return iataDebarkationAirport;
    }
}