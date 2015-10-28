package gov.gtas.vo.passenger;

import org.apache.commons.lang3.StringUtils;

import gov.gtas.validators.Validatable;
import gov.gtas.vo.BaseVo;

public class ReportingPartyVo extends BaseVo implements Validatable {
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
    @Override
    public boolean isValid() {
        return StringUtils.isNotBlank(this.partyName);
    }    
}
