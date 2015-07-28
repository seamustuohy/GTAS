package gov.gtas.parsers.pnrgov;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import gov.gtas.parsers.edifact.EdifactLexer;
import gov.gtas.parsers.edifact.EdifactParser;
import gov.gtas.parsers.exception.ParseException;
import gov.gtas.parsers.pnrgov.vo.PnrMessageVo;

public final class PnrGovParser extends EdifactParser<PnrMessageVo> {
    private static final String[] SEGMENT_NAMES = new String[] { "ABI", "APD", "DAT", "EBD", "EQN", "FAR", "FOP", "FTI",
            "IFT", "LTS", "MON", "MSG", "ORG", "PTK", "RCI", "REF", "RPI", "SAC", "SRC", "SSD", "SSR", "TBD", "TIF",
            "TKT", "TRA", "TRI", "TVL", "TXD" };
    public static final Set<String> PNRGOV_SEGMENT_INDEX = new HashSet<>(Arrays.asList(SEGMENT_NAMES));
    
    public PnrGovParser() {
        this.parsedMessage = new PnrMessageVo();
    }
    
    @Override
    public void parsePayload() throws ParseException {
//        PnrMessageBuilder builder = new PnrMessageBuilder(message, segments);
//        builder.buildMessageObject();
//        System.out.println("Number of Flights  : "+message.getFlights().size());
//        System.out.println("passengers in flight : "+message.getPassengers().size());
    }
    
    @Override
    protected void validateSegmentName(String segmentName) throws ParseException {
        boolean valid = PNRGOV_SEGMENT_INDEX.contains(segmentName)
                || EdifactLexer.EDIFACT_SEGMENT_INDEX.contains(segmentName);
        if (!valid) {
            throw new ParseException("Invalid segment: " + segmentName);
        }        
    }   
}
