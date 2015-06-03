package gov.cbp.taspd.gtas.parsers.paxlst;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Segment {
	private String name;
	private String[] fields;
	private UNA serviceStrings;
	
	public Segment(String txt, UNA serviceStrings) {
		this.serviceStrings = serviceStrings;
		
		String regex = String.format(
				"[^\\%c]*(\\%c\\%c)+[^\\%c]*|[^\\%c]+",
				this.serviceStrings.dataElementSeparator,
				this.serviceStrings.releaseCharacter,
				this.serviceStrings.dataElementSeparator,
				this.serviceStrings.dataElementSeparator,
				this.serviceStrings.dataElementSeparator);
		Pattern tokenPattern = Pattern.compile(regex);
		Matcher matcher = tokenPattern.matcher(txt);
		List<String> tokens = new LinkedList<>();
		while (matcher.find()) {
		    tokens.add(matcher.group());
		}

		this.name = tokens.get(0);
		tokens.remove(0);
		fields = tokens.toArray(new String[tokens.size()]);
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

	@Override
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append(this.name + " ");
		for (String x : this.fields) {
			b.append(x + " ");
		}
		return b.toString();
	}
	
	
}
