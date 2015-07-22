package gov.gtas.parsers.pnrgov;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.gtas.parsers.edifact.EdifactParser;
import gov.gtas.parsers.edifact.Segment;
import gov.gtas.parsers.paxlst.SegmentFactory;
import gov.gtas.parsers.pnrgov.segment.MSG;
import gov.gtas.parsers.pnrgov.vo.PnrMessageVo;
import gov.gtas.parsers.util.FileUtils;
import gov.gtas.parsers.util.ParseUtils;
import gov.gtas.parsers.util.PnrMessageBuilder;
public class PnrGovParser {

	private static final Logger logger = LoggerFactory.getLogger(PnrGovParser.class);
    private String filePath;
    private String segmentPackageName;
    private PnrMessageVo message;
    private List<Segment> segments;
       
    public PnrGovParser(String filePath){
        this.filePath = filePath;
        this.segmentPackageName = MSG.class.getPackage().getName();
    }
	
    public PnrMessageVo parse() throws ParseException {
        this.segments = new LinkedList<>();
        this.message = new PnrMessageVo();
        byte[] raw = FileUtils.readSmallFile(this.filePath);
        if (raw == null) {
            return null;
        }
        this.message.setRaw(raw);
        String msg = new String(raw, StandardCharsets.US_ASCII);
        processMessageAndGetSegments(msg);
        parseSegments();
        return this.message;    	
    }

    private void parseSegments() throws ParseException{
    	PnrMessageBuilder builder = new PnrMessageBuilder(message,segments);
    	builder.buildMessageObject();
    	System.out.println("Number of Flights  : "+message.getFlights().size());
    	System.out.println("passengers in flight : "+message.getPassengers().size());
    }
    private void processMessageAndGetSegments(String msg) throws ParseException {
    	String txt = ParseUtils.stripStxEtxHeaderAndFooter(msg);
        txt = txt.toUpperCase();
        txt = txt.replaceAll("\\n|\\r", "");
        SegmentFactory factory = new SegmentFactory(segmentPackageName);
        EdifactParser p = new EdifactParser();
        LinkedList<Segment> edifactSegments = p.parse(txt);
        for (Segment s: edifactSegments) {
        	System.out.println("SEGMENT >>> "+s.toString());
            Segment paxlstSegment = factory.build(s);
            segments.add(paxlstSegment);
        }
    }

    public static void main(String[] args){
    	
    	PnrGovParser parser= new PnrGovParser("C:\\PNR-FILES\\pnrgov.edi.txt");
    	try {
			parser.parse();
			
		} catch (ParseException e) {
			e.printStackTrace();
			e.printStackTrace();
		}
    	
    }

}
