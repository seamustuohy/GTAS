package gov.cbp.taspd.gtas.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Document extends BaseEntity {
    @Enumerated(EnumType.STRING)    
    private DocumentCode documentType;
    
    private String number;
    
    private Date expirationDate;
    
    private Date issuanceDate;
    
    @ManyToOne
    @JoinColumn(name="country", referencedColumnName="id")         
    private Country issuanceCountry;
    
    @ManyToOne
    @JoinColumn(name="pax_id", referencedColumnName="id")         
    private Pax pax;

    public DocumentCode getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentCode documentType) {
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
