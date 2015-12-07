package gov.gtas.parsers.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FlightHistoryVo {

	private HashMap<String, List<FlightVo>> flightHistoryMap = new HashMap<String, List<FlightVo>>();

	public HashMap<String, List<FlightVo>> getFlightHistoryMap() {
		return flightHistoryMap;
	}

	public void setFlightHistoryMap(
			HashMap<String, List<FlightVo>> flightHistoryMap) {
		this.flightHistoryMap = flightHistoryMap;
	}

	
}
