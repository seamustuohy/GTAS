package gov.gtas.parsers.pnrgov;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import gov.gtas.parsers.edifact.EdifactLexer;
import gov.gtas.parsers.edifact.EdifactParser;
import gov.gtas.parsers.exception.ParseException;
import gov.gtas.parsers.pnrgov.segment.DAT_G1;
import gov.gtas.parsers.pnrgov.segment.EQN_L0;
import gov.gtas.parsers.pnrgov.segment.MSG;
import gov.gtas.parsers.pnrgov.segment.ORG_L0;
import gov.gtas.parsers.pnrgov.segment.RCI;
import gov.gtas.parsers.pnrgov.segment.SRC;
import gov.gtas.parsers.pnrgov.segment.SSR_G1;
import gov.gtas.parsers.pnrgov.segment.TVL_L0;
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
    protected void validateSegmentName(String segmentName) throws ParseException {
        boolean valid = PNRGOV_SEGMENT_INDEX.contains(segmentName)
                || EdifactLexer.EDIFACT_SEGMENT_INDEX.contains(segmentName);
        if (!valid) {
            throw new ParseException("Invalid segment: " + segmentName);
        }        
    }   

    @Override
    public void parsePayload() throws ParseException {
        MSG msg = getMandatorySegment(MSG.class);
        System.out.println(msg);

        ORG_L0 org = getMandatorySegment(ORG_L0.class);
        System.out.println(org);

        TVL_L0 tvl = getMandatorySegment(TVL_L0.class);
        System.out.println(tvl);

        EQN_L0 eqn = getMandatorySegment(EQN_L0.class);
        System.out.println(eqn);

        getMandatorySegment(SRC.class);
        processGroup1();
        for (;;) {
            SRC src = getConditionalSegment(SRC.class);
            if (src == null) {
                break;
            }
            processGroup1();
        }


    }
    
    public void processGroup1() throws ParseException {
        RCI rci = getMandatorySegment(RCI.class);
        System.out.println(rci);

        for (;;) {
            SSR_G1 ssr = getConditionalSegment(SSR_G1.class, "SSR");
            if (ssr == null) {
                break;
            }
        }

        DAT_G1 dat = getConditionalSegment(DAT_G1.class, "DAT");
        System.out.println(dat);
    }
}
