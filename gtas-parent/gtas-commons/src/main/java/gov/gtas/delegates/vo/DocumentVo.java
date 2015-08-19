package gov.gtas.delegates.vo;

import java.io.Serializable;
import java.util.Date;

public class DocumentVo extends BaseVo implements Serializable {

    private String documentType;
    private String documentNumber;
    private Date expirationDate;
    private Date issuanceDate;
    private String issuanceCountry;
    private PassengerVo passenger;
    
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
    
    
}
