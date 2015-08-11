package gov.gtas.model;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "phone")
public class Phone extends BaseEntityAudit {
	private String number;
	
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="pnr_id",referencedColumnName="id")    
	private PnrData pnrData;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public PnrData getPnrData() {
		return pnrData;
	}

	public void setPnrData(PnrData pnrData) {
		this.pnrData = pnrData;
	}
	
    @Override
    public int hashCode() {
        return Objects.hash(this.number);
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
        return Objects.equals(this.number, other.number);
    }       
}
