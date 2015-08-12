package gov.gtas.model;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "email")
public class Email extends BaseEntityAudit {
	private String address;
	private String domain;
	
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="pnr_id")    
	private Pnr pnr;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.address);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Email other = (Email) obj;
        return Objects.equals(this.address, other.address);
    }       
}
