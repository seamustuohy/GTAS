package gov.gtas.parsers.paxlst;

import gov.gtas.parsers.edifact.EdifactParser;
import gov.gtas.parsers.edifact.Segment;
import gov.gtas.parsers.paxlst.vo.ApisMessageVo;
import gov.gtas.parsers.util.FileUtils;
import gov.gtas.parsers.util.ParseUtils;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PaxlstParser {
    private static final Logger logger = LoggerFactory.getLogger(PaxlstParser.class);

    private String filePath;
    private String segmentPackageName;

    protected enum GROUP {
        NONE,
        HEADER,
        REPORTING_PARTY,
        FLIGHT,
        PAX
    }
    
    protected GROUP currentGroup;

    protected ApisMessageVo message;
    protected List<Segment> segments;
    
    public PaxlstParser(String filePath, String segmentPackageName) {
        this.filePath = filePath;
        this.segmentPackageName = segmentPackageName;
    }

    protected abstract void parseSegments();
    
    public ApisMessageVo parse() throws ParseException {
        this.segments = new LinkedList<>();
        this.message = new ApisMessageVo();

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
    
    private void processMessageAndGetSegments(String msg) throws ParseException {
        String txt = ParseUtils.stripApisHeaderAndFooter(msg);
        txt = txt.toUpperCase();
        txt = txt.replaceAll("\\n|\\r", "");
        
        String payload = getApisMessagePayload(txt);
        if (payload == null) {
            throw new ParseException("Could not extract message payload. Missing NAD or UNT segment.", -1);
        }
        String md5 = ParseUtils.getMd5Hash(payload, StandardCharsets.US_ASCII);
        message.setHashCode(md5);
        
        SegmentFactory factory = new SegmentFactory(segmentPackageName);
        EdifactParser p = new EdifactParser();
        LinkedList<Segment> edifactSegments = p.parse(txt);
        for (Segment s: edifactSegments) {
            Segment paxlstSegment = factory.build(s);
            segments.add(paxlstSegment);
        }
    }
    
    /**
     * Return everything from the start of the first NAD segment to the
     * start of the UNT trailing header segment.
     */
    public String getApisMessagePayload(String text) {
        if (text == null) return null;
        
        int nadIndex = text.indexOf("NAD");
        if (nadIndex == -1) {
            logger.error("NAD segment missing");
            return null;
        }
        
        int untIndex = text.indexOf("UNT");
        if (untIndex == -1) {
            logger.error("UNT segment missing");
            return null;
        }
        
        return text.substring(nadIndex, untIndex);
    }
}
