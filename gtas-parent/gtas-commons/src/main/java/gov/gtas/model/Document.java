package gov.gtas.model;

import gov.gtas.model.lookup.Country;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "document")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="document_type",
    discriminatorType=DiscriminatorType.STRING
)
public abstract class Document extends BaseEntity {
    public Document() { }

    @Column(name = "document_number")
    private String documentNumber;
    
    @Column(name = "expiration_date")
    private Date expirationDate;
    
    @Column(name = "issuance_date")
    private Date issuanceDate;
    
    @ManyToOne
    @JoinColumn(name = "issuance_country_id")
    private Country issuanceCountry;
    
    @ManyToOne
    @JoinColumn
    private Traveler traveler;

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Date getIssuanceDate() {
        return issuanceDate;
    }

    public void setIssuanceDate(Date issuanceDate) {
        this.issuanceDate = issuanceDate;
    }

    public Country getIssuanceCountry() {
        return issuanceCountry;
    }

    public void setIssuanceCountry(Country issuanceCountry) {
        this.issuanceCountry = issuanceCountry;
    }

    public Traveler getTraveler() {
        return traveler;
    }

    public void setTraveler(Traveler traveler) {
        this.traveler = traveler;
    }
}
