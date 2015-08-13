package gov.gtas.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "reporting_party")
public class ReportingParty extends BaseEntity {
    private static final long serialVersionUID = 1L;  
    public ReportingParty() { }
    
    @Column(name = "party_name")
    private String partyName;
    
    private String telephone;
    
    private String fax;

    @ManyToOne
    @JoinColumn(name = "apis_message_id")
    private ApisMessage apisMessage;
    
    public ApisMessage getApisMessage() {
        return apisMessage;
    }
    public void setApisMessage(ApisMessage apisMessage) {
        this.apisMessage = apisMessage;
    }
    public String getPartyName() {
        return partyName;
    }
    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getFax() {
        return fax;
    }
    public void setFax(String fax) {
        this.fax = fax;
    }
    
    @Override
    public int hashCode() {
       return Objects.hash(this.partyName, this.telephone);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ReportingParty other = (ReportingParty)obj;
        return Objects.equals(this.partyName, other.partyName)
                && Objects.equals(this.telephone, other.telephone);
    }
}
