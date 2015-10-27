package gov.gtas.vo.passenger;

import gov.gtas.validators.Validatable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class FrequentFlyerVo implements Validatable{
    private String carrier;
    private String number;

    public String getCarrier() {
        return carrier;
    }
    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE); 
    }

	@Override
	public boolean isValid() {
		if(StringUtils.isBlank(this.carrier) || StringUtils.isBlank(this.number) 
				){
			return false;
		}
		return true;
	}
}
