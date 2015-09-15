package gov.gtas.delegates.vo;

import gov.gtas.validators.Validatable;

import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class DocumentVo extends BaseVo implements Validatable {

    private PassengerVo passenger;
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
	public PassengerVo getPassenger() {
		return passenger;
	}
	public void setPassenger(PassengerVo passenger) {
		this.passenger = passenger;
	}
	
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE); 
    }
	@Override
	public boolean validate() {
		if(StringUtils.isBlank(this.documentNumber) || StringUtils.isBlank(this.documentType) 
				|| StringUtils.isBlank(this.issuanceCountry) || this.expirationDate == null
				|| this.issuanceDate == null){
			return false;
		}
		return true;
	}        
    
}
