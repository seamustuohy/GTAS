package gov.gtas.vo.passenger;

import org.apache.commons.lang3.StringUtils;

import gov.gtas.validators.Validatable;
import gov.gtas.vo.BaseVo;

public class AgencyVo extends BaseVo implements Validatable {
	private String name;
    private String location;
	private String identifier;
	private String country;
    private String phone;
	
	public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getIdentifier() {
        return identifier;
    }
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
	@Override
	public boolean isValid() {
		return StringUtils.isNotBlank(this.name);
	}
}
