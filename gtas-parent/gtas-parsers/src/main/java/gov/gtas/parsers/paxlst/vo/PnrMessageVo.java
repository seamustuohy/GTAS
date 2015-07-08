package gov.gtas.parsers.paxlst.vo;

import java.util.ArrayList;
import java.util.List;

public class PnrMessageVo {
	
	  private byte[] raw;
	  private String updateMode;
	  private List<FlightVo> flights = new ArrayList<>();
	  private List<PaxVo> passengers = new ArrayList<>();
	  private List<ReportingPartyVo> reportingParties = new ArrayList<>();
	  public byte[] getRaw() {
		return raw;
	}
	public void setRaw(byte[] raw) {
		this.raw = raw;
	}
	public String getUpdateMode() {
		return updateMode;
	}
	public void setUpdateMode(String updateMode) {
		this.updateMode = updateMode;
	}
	public List<FlightVo> getFlights() {
		return flights;
	}
	public void setFlights(List<FlightVo> flights) {
		this.flights = flights;
	}
	public List<PaxVo> getPassengers() {
		return passengers;
	}
	public void setPassengers(List<PaxVo> passengers) {
		this.passengers = passengers;
	}
	public List<ReportingPartyVo> getReportingParties() {
		return reportingParties;
	}
	public void setReportingParties(List<ReportingPartyVo> reportingParties) {
		this.reportingParties = reportingParties;
	}
	  
	  

}
