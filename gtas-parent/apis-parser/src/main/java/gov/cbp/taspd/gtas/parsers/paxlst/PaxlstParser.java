package gov.cbp.taspd.gtas.parsers.paxlst;

import gov.cbp.taspd.gtas.model.Pax;
import gov.cbp.taspd.gtas.util.FileUtils;
import gov.cbp.taspd.gtas.util.ParseUtils;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class PaxlstParser {
	private UNA serviceStrings = null;
	private String filePath = null;
	private List<Segment> segments = null;
	private Message message = null;
	
	public PaxlstParser(String filePath) {
		this.filePath = filePath;
	}
	
	public Message parse() {
		String rawText = FileUtils.readSmallTextFile(this.filePath, StandardCharsets.US_ASCII);
		this.message = new Message();
		this.message.setRaw(rawText);
		segments = new LinkedList<>();

		processRawAndGetSegments(rawText);
		processSegments();
		return this.message;
	}
	
	private void processRawAndGetSegments(String raw) {
		String txt = ParseUtils.stripHeaderAndFooter(raw);
		txt = txt.toUpperCase();
		
		int unaIndex = txt.indexOf("UNA");
		if (unaIndex != -1) {
			int endIndex = unaIndex + "UNA".length() + 6;
			String delims = txt.substring(unaIndex, endIndex);
			serviceStrings = new UNA(delims);
		} else {
			serviceStrings = new UNA();
		}

		int unbIndex = txt.indexOf("UNB");
		if (unbIndex == -1) {
			System.err.println("no UNB segment");
			System.exit(0);
		}
		txt = txt.substring(unbIndex);
		
		txt = txt.replaceAll("\\n|\\r|\\t", "");
		System.out.println("txt: " + txt);

		String segmentRegex = String.format("\\%c", serviceStrings.segmentTerminator);
		String[] stringSegments = txt.split(segmentRegex);
		for (String s : stringSegments) {
			segments.add(new Segment(s, serviceStrings));
		}
	}
		
	private void processSegments() {
		for (ListIterator<Segment> i=segments.listIterator(); i.hasNext(); ) {
			Segment s = i.next();
			System.out.println(s);
			switch (s.getName()) {
			case "UNB":
				processUnb(s);
				break;
			case "UNH":
				break;
			case "NAD":
				processPaxOrContact(s, i);
				break;
			case "TDT":
				processFlight(s, i);
			}
		}		
	}
	
	private void processPaxOrContact(Segment nad, ListIterator<Segment> i) {
		Segment nextSeg = i.next();
		if (nextSeg.getName().equals("COM")) {
			System.out.println(nextSeg);
//			ReportingParty rp = new ReportingParty();
		} else {
			i.previous();
			
			Pax p = new Pax();
			p.setFirstName("dummy");
			message.getPassengers().add(p);
			
			boolean done = false;
			while (!done) {
				Segment s = i.next();
				if (s == null) return;
				switch (s.getName()) {
				case "ATT":
				case "DTM":
				case "GEI":
				case "FTX":
				case "LOC":
				case "COM":
				case "EMP":
				case "NAT":
				case "RFF":
					System.out.println("\t" + s);
					break;
				default:
					return;
				}
			}
		}
	}

	private void processFlight(Segment tdt, ListIterator<Segment> i) {
	}
	
	private void processUnb(Segment unb) {
		Composite[] c = unb.getComposites();
		message.setCode(c[0].getElements()[0].getValue());
		message.setReceiver(c[3].getElements()[0].getValue());
		System.out.println(c.length + " " + message.getCode() + " " + message.getReceiver());
	}
	
	public static void main(String[] argv) {		
		if (argv.length < 1) {
			System.out.println("usage: EdifactParser [filename]");
			System.exit(0);
		}
		
		PaxlstParser parser = new PaxlstParser(argv[0]);
		Message m = parser.parse();		
		System.exit(0);		
	}
}
