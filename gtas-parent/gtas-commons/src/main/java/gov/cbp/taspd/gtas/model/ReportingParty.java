package gov.cbp.taspd.gtas.model;

import javax.persistence.Entity;

@Entity
public class ReportingParty extends BaseEntity {
    private String partyName;
    private String telephone;
    private String fax;
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
}
