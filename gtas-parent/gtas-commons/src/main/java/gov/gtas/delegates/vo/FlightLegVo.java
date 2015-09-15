package gov.gtas.delegates.vo;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import gov.gtas.validators.Validatable;

public class FlightLegVo implements Validatable{
	
	private String flightReference;
	private String pnrReference;
	private String flightLeg;
	
	public String getFlightReference() {
		return flightReference;
	}
	public void setFlightReference(String flightReference) {
		this.flightReference = flightReference;
	}
	public String getPnrReference() {
		return pnrReference;
	}
	public void setPnrReference(String pnrReference) {
		this.pnrReference = pnrReference;
	}
	public String getFlightLeg() {
		return flightLeg;
	}
	public void setFlightLeg(String flightLeg) {
		this.flightLeg = flightLeg;
	}

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
	@Override
	public boolean validate() {
		if(StringUtils.isBlank(this.flightLeg) || StringUtils.isBlank(this.flightReference) ||StringUtils.isBlank(this.pnrReference)){
			return false;
		}
		return true;
	}

}
