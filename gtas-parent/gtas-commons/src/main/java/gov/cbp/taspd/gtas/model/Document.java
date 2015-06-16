package gov.cbp.taspd.gtas.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "document")
public class Document extends BaseEntity {
    public Document() { }
    
    @Enumerated(EnumType.STRING)
    @Column(name = "document_type")
    private DocumentType documentType;
    
    private String number;
    
    @Column(name = "expiration_date")
    private Date expirationDate;
    
    @Column(name = "issuance_date")
    private Date issuanceDate;
    
    @ManyToOne
    @JoinColumn(name = "issuance_country_id", referencedColumnName="id")         
    private Country issuanceCountry;
    
    @ManyToOne
    @JoinColumn(referencedColumnName="id")         
    private Pax pax;

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
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

    public Pax getPax() {
        return pax;
    }

    public void setPax(Pax pax) {
        this.pax = pax;
    }
}
