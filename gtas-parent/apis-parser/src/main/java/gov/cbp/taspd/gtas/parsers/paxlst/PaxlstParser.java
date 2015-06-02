package gov.cbp.taspd.gtas.parsers.paxlst;

import gov.cbp.taspd.gtas.model.Pax;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
		File file = new File(this.fileName);
		FileInputStream fis = null;
		byte[] data = null;
		try {
			fis = new FileInputStream(file);
			data = new byte[(int) file.length()];
			fis.read(data);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();			
		} finally {
		    try {
		        if (fis != null) {
		            fis.close();
		        }
		    } catch (IOException e) {
		    }
		}

		String tmp = null;
		try {
			tmp = new String(data, "ascii");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		this.rawText = tmp;
	}
	
	private void preprocessFile() {
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
			// bail out
			System.exit(0);
		}
		this.rawText = this.rawText.substring(unbIndex);
		
		String regex = "[^ 0-9A-Za-z" + this.serviceStrings.getDelimsRegex() + "]";
		System.out.println(regex);
		this.rawText = this.rawText.replaceAll(regex, "");
		System.out.println(this.rawText);
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
			}
		}		
	}
	
	private void processPaxOrContact(Segment nad, ListIterator<Segment> i) {
		Segment nextSeg = i.next();
		if (nextSeg.getName().equals("COM")) {
//			ReportingParty rp = new ReportingParty();
		} else {
			Pax p = new Pax();
			String[] nadFields = nad.getFields();
			p.setFirstName(nadFields[3]);
			message.getPassengers().add(p);
		}
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
