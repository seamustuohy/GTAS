package gov.gtas.dataobject;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonFormat;

public class FlightVo {
    public static final String DATE_FORMAT = "yyyy-MM-dd hh:mm aaa";
    private String flightId;
    private String carrier;
    private String flightNumber;
    private String origin;
    private String originCountry;
    private String destination;
    private String destinationCountry;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private Date flightDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private Date etd;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private Date eta;
    private String direction;
    private int ruleHits;
    private int listHits;
    private int paxListHit;
    private int docListHit;
    private int totalPax;
    
    
    public int getPaxListHit() {
		return paxListHit;
	}
	public void setPaxListHit(int paxListHit) {
		this.paxListHit = paxListHit;
	}
	public int getDocListHit() {
		return docListHit;
	}
	public void setDocListHit(int docListHit) {
		this.docListHit = docListHit;
	}
	/**
	 * @return the ruleHits
	 */
	public int getRuleHits() {
		return ruleHits;
	}
	/**
	 * @param ruleHits the ruleHits to set
	 */
	public void setRuleHits(int ruleHits) {
		this.ruleHits = ruleHits;
	}
	/**
	 * @return the listHits
	 */
	public int getListHits() {
		return listHits;
	}
	/**
	 * @param listHits the listHits to set
	 */
	public void setListHits(int listHits) {
		this.listHits = listHits;
	}
	public String getFlightId() {
        return flightId;
    }
    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }
    public String getCarrier() {
        return carrier;
    }
    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }
    public String getFlightNumber() {
        return flightNumber;
    }
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }
    public String getOrigin() {
        return origin;
    }
    public void setOrigin(String origin) {
        this.origin = origin;
    }
    public String getOriginCountry() {
        return originCountry;
    }
    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }
    public String getDestination() {
        return destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }
    public String getDestinationCountry() {
        return destinationCountry;
    }
    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }
    public Date getFlightDate() {
        return flightDate;
    }
    public void setFlightDate(Date flightDate) {
        this.flightDate = flightDate;
    }
    public Date getEtd() {
        return etd;
    }
    public void setEtd(Date etd) {
        this.etd = etd;
    }
    public int getTotalPax() {
		return totalPax;
	}
	public void setTotalPax(int totalPax) {
		this.totalPax = totalPax;
	}
	public Date getEta() {
        return eta;
    }
    public void setEta(Date eta) {
        this.eta = eta;
    }
    
    public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE); 
    }    
}
