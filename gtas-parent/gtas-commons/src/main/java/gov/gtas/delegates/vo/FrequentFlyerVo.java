package gov.gtas.delegates.vo;

import gov.gtas.validators.Validatable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class FrequentFlyerVo extends BaseVo implements Validatable  {

	private String airline;
    private String number;
    
	public String getAirline() {
		return airline;
	}
	public void setAirline(String airline) {
		this.airline = airline;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
	@Override
	public boolean validate() {
		if(StringUtils.isBlank(this.airline) || StringUtils.isBlank(this.number) ){
			return false;
		}
		return true;
	}
    
}
