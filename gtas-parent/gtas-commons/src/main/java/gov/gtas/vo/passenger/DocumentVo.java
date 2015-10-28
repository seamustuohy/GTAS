package gov.gtas.vo.passenger;

import gov.gtas.validators.Validatable;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class DocumentVo implements Validatable {
    private String documentType;
    private String documentNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = FlightVo.DATE_FORMAT)
    private Date expirationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = FlightVo.DATE_FORMAT)
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
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE); 
    }
    
	@Override
	public boolean isValid() {
		return StringUtils.isNotBlank(this.documentNumber) 
		       && StringUtils.isNotBlank(this.documentType);
	}    
}
