package gov.gtas.parsers.pnrgov.vo;

import gov.gtas.parsers.paxlst.vo.PaxVo;

public class PnrPax extends PaxVo{
	private String travelerReferenceNumber;
	private String daysBookedBeforeTravel;
	private String bagCount;
    public String getTravelerReferenceNumber() {
        return travelerReferenceNumber;
    }
    public void setTravelerReferenceNumber(String travelerReferenceNumber) {
        this.travelerReferenceNumber = travelerReferenceNumber;
    }
    public String getDaysBookedBeforeTravel() {
        return daysBookedBeforeTravel;
    }
    public void setDaysBookedBeforeTravel(String daysBookedBeforeTravel) {
        this.daysBookedBeforeTravel = daysBookedBeforeTravel;
    }
    public String getBagCount() {
        return bagCount;
    }
    public void setBagCount(String bagCount) {
        this.bagCount = bagCount;
    }	
}
