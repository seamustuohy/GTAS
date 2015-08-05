package gov.gtas.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "document")
public class Document extends BaseEntity {
    public Document() { }

    @Column(name = "document_type", length = 2)
    private String documentType;
    
    @Column(name = "document_number")
    private String documentNumber;
    
    @Column(name = "expiration_date")
    private Date expirationDate;
    
    @Column(name = "issuance_date")
    private Date issuanceDate;
    
    @Column(name = "issuance_country")
    private String issuanceCountry;
    
    @ManyToOne
    private Traveler traveler;

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

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

    public String getIssuanceCountry() {
        return issuanceCountry;
    }

    public void setIssuanceCountry(String issuanceCountry) {
        this.issuanceCountry = issuanceCountry;
    }

    public Traveler getTraveler() {
        return traveler;
    }

    public void setTraveler(Traveler traveler) {
        this.traveler = traveler;
    }
}
