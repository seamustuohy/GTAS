package gov.cbp.taspd.gtas.parsers.paxlst;

import java.util.Arrays;

public class Segment {
	private String name;
	private String[] fields;
	
	public Segment(String txt, String delims) {
		String[] tmp = txt.split(delims);
		name = tmp[0];
		fields = Arrays.copyOfRange(tmp, 1, tmp.length);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getFields() {
		return fields;
	}

	public void setFields(String[] fields) {
		this.fields = fields;
	}
	
	
}
