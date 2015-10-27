package gov.gtas.vo.passenger;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import gov.gtas.validators.Validatable;

public class EmailVo implements Validatable{

	private String address;
	private String domain;

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
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
	@Override
	public boolean isValid() {
		if(StringUtils.isBlank(this.address) ){
			return false;
		}
		return true;
	}
	
}
