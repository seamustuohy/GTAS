package gov.gtas.parsers.pnrgov.vo;

import java.util.ArrayList;
import java.util.List;

import gov.gtas.parsers.paxlst.vo.FlightVo;

public class PnrMessageVo {
	
	  private byte[] raw;
	  private String updateMode;
	  private List<FlightVo> flights = new ArrayList<>();
	  private List<PnrVo> passengers = new ArrayList<>();
	  private List<PnrReportingAgentVo> reportingParties = new ArrayList<>();
	  private Integer numberOfPassengers;
	  
	public Integer getNumberOfPassengers() {
		return numberOfPassengers;
	}
	public void setNumberOfPassengers(Integer numberOfPassengers) {
		this.numberOfPassengers = numberOfPassengers;
	}
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
	public List<PnrVo> getPassengers() {
		return passengers;
	}
	public void setPassengers(List<PnrVo> passengers) {
		this.passengers = passengers;
	}
	public List<PnrReportingAgentVo> getReportingParties() {
		return reportingParties;
	}
	public void setReportingParties(List<PnrReportingAgentVo> reportingParties) {
		this.reportingParties = reportingParties;
	}

}
