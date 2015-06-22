package gov.gtas.parsers.paxlst.vo;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class DocumentVo {
    private String documentType;
    private String documentNumber;
    private Date expirationDate;
    private Date issuanceDate;
    private String issuanceCountry;

    public String getDocumentType() {
        return documentType;
    }
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }
    public String getDocumentNumber() {
        return documentNumber;
    }
    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
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
    public String getIssuanceCountry() {
        return issuanceCountry;
    }
    public void setIssuanceCountry(String issuanceCountry) {
        this.issuanceCountry = issuanceCountry;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE); 
    }    
}
