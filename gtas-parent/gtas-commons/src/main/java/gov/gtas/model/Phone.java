package gov.gtas.model;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "phone")
public class Phone extends BaseEntityAudit {

	@Column(name = "phone_type")
	private String phoneType;
	
	@Column(name = "phone_number")
	private String phoneNumber;
	
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="pnr_id",referencedColumnName="id")    
	private PnrData pnrData;

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public PnrData getPnrData() {
		return pnrData;
	}

	public void setPnrData(PnrData pnrData) {
		this.pnrData = pnrData;
	}
	
    @Override
    public int hashCode() {
        return Objects.hash(this.phoneNumber,this.phoneType);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Phone other = (Phone) obj;
        return Objects.equals(this.phoneNumber, other.phoneNumber);
    }       
}
