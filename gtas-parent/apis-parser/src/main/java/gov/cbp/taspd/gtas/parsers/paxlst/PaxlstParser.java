package gov.cbp.taspd.gtas.parsers.paxlst;

import gov.cbp.taspd.gtas.model.Pax;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class PaxlstParser {
	UNA serviceStrings = null;
	private String fileName = null;
	private String rawText = null;
	private List<Segment> segments = null;
	private Message message = null;
	
	public PaxlstParser(String fn) {
		this.fileName = fn;
		this.message = new Message();
		segments = new ArrayList<>();
	}
	
	public Message parse() {
		readFile();
		preprocessFile();
		getSegments();
		createMessage();
		return message;
	}
	
	private void readFile() {
		Path path = Paths.get(this.fileName);

	    try {
	      byte[] bytes = Files.readAllBytes(path);
	      this.rawText = new String(bytes, StandardCharsets.US_ASCII);
	    } catch (IOException e) {
	      System.out.println(e);
	    }		
	}
	
	private void preprocessFile() {
		final int STX_CODEPOINT = 2;
		final int ETX_CODEPOINT = 3;
		
		int stxIndex = this.rawText.indexOf(STX_CODEPOINT);
		if (stxIndex != -1) {
			this.rawText = this.rawText.substring(stxIndex + 1);
		}
		int etxIndex = this.rawText.indexOf(ETX_CODEPOINT);
		if (etxIndex != -1) {
			this.rawText = this.rawText.substring(0, etxIndex);
		}
		
		this.rawText = this.rawText.toUpperCase();
		
		int unaIndex = this.rawText.indexOf("UNA");
		if (unaIndex != -1) {
			int endIndex = unaIndex + "UNA".length() + 6;
			String delims = this.rawText.substring(unaIndex, endIndex);
			this.serviceStrings = new UNA(delims);
		} else {
			this.serviceStrings = new UNA();
		}

		int unbIndex = this.rawText.indexOf("UNB");
		if (unbIndex == -1) {
			System.err.println("no UNB segment");
			System.exit(0);
		}
		this.rawText = this.rawText.substring(unbIndex);
		
		this.rawText = this.rawText.replaceAll("\\n|\\r|\\t", "");
		System.out.println("rawtext: " + this.rawText);
	}
	
	private void getSegments() {
		String segmentRegex = String.format("\\%c", this.serviceStrings.segmentTerminator);
		String[] stringSegments = this.rawText.split(segmentRegex);

		String regex = String.format("\\%c|\\%c",
				this.serviceStrings.componentDataElementSeparator,
				this.serviceStrings.dataElementSeparator);
		for (String s : stringSegments) {
			this.segments.add(new Segment(s, regex));
		}
	}
	
	private void createMessage() {
		for (ListIterator<Segment> i=segments.listIterator(); i.hasNext(); ) {
			Segment s = i.next();
			System.out.println("seg " + s.getName());
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
//			ReportingParty rp = new ReportingParty();
		} else {
			i.previous();
			
			Pax p = new Pax();
			String[] nadFields = nad.getFields();
			p.setFirstName(nadFields[3]);
			message.getPassengers().add(p);
			
			boolean done = false;
			while (!done) {
				Segment s = i.next();
				if (i == null) return;
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
					System.out.println("\t" + s.getName());
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
		String[] fields = unb.getFields();
		System.out.println(fields.length);
		message.setCode(fields[0]);
		message.setReceiver(fields[3]);
	}
	
	public static void main(String[] argv) {
		if (argv.length < 1) {
			System.out.println("usage: EdifactParser [filename]");
			System.exit(0);
		}
		
		PaxlstParser parser = new PaxlstParser(argv[0]);
		Message m = parser.parse();
		System.out.println(m.getCode() + " " + m.getReceiver());
		for (Pax p : m.getPassengers()) {
			System.out.println("pax: " + p.getFirstName());
		}
		
		System.exit(0);		
	}
}
