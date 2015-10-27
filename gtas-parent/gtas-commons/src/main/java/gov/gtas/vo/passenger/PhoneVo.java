package gov.gtas.vo.passenger;

import gov.gtas.validators.Validatable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PhoneVo implements Validatable{
    private String number;
    private String city;
    
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
	@Override
	public boolean isValid() {
		if(StringUtils.isBlank(this.number) ){
			return false;
		}
		return true;
	}
}
