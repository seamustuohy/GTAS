package gov.gtas.parsers.pnrgov;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import gov.gtas.parsers.edifact.EdifactLexer;
import gov.gtas.parsers.edifact.EdifactParser;
import gov.gtas.parsers.exception.ParseException;
import gov.gtas.parsers.pnrgov.segment.ADD;
import gov.gtas.parsers.pnrgov.segment.DAT;
import gov.gtas.parsers.pnrgov.segment.DAT_G1;
import gov.gtas.parsers.pnrgov.segment.EBD;
import gov.gtas.parsers.pnrgov.segment.EQN_L0;
import gov.gtas.parsers.pnrgov.segment.FAR;
import gov.gtas.parsers.pnrgov.segment.FOP;
import gov.gtas.parsers.pnrgov.segment.FTI;
import gov.gtas.parsers.pnrgov.segment.IFT;
import gov.gtas.parsers.pnrgov.segment.MON;
import gov.gtas.parsers.pnrgov.segment.MSG;
import gov.gtas.parsers.pnrgov.segment.ORG_G1;
import gov.gtas.parsers.pnrgov.segment.ORG_L0;
import gov.gtas.parsers.pnrgov.segment.PTK;
import gov.gtas.parsers.pnrgov.segment.RCI;
import gov.gtas.parsers.pnrgov.segment.REF;
import gov.gtas.parsers.pnrgov.segment.SRC;
import gov.gtas.parsers.pnrgov.segment.SSR;
import gov.gtas.parsers.pnrgov.segment.SSR_G1;
import gov.gtas.parsers.pnrgov.segment.TIF;
import gov.gtas.parsers.pnrgov.segment.TKT;
import gov.gtas.parsers.pnrgov.segment.TVL;
import gov.gtas.parsers.pnrgov.segment.TVL_L0;
import gov.gtas.parsers.pnrgov.segment.TXD;
import gov.gtas.parsers.pnrgov.vo.PnrMessageVo;

public final class PnrGovParser extends EdifactParser<PnrMessageVo> {
    private static final String[] SEGMENT_NAMES = new String[] { "ABI", "ADD", "APD", "DAT", "EBD", "EQN", "FAR", "FOP",
            "FTI", "IFT", "LTS", "MON", "MSG", "ORG", "PTK", "RCI", "REF", "RPI", "SAC", "SRC", "SSD", "SSR", "TBD",
            "TIF", "TKT", "TRA", "TRI", "TVL", "TXD" };
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

        TVL_L0 tvl_l0 = getMandatorySegment(TVL_L0.class);
        System.out.println(tvl_l0);

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

        TIF tif = getMandatorySegment(TIF.class);
        processGroup2(tif);
        for (;;) {
            tif = getConditionalSegment(TIF.class);
            if (tif == null) {
                break;
            }
            processGroup2(tif);
        }
        
        for (;;) {
            TVL tvl = getConditionalSegment(TVL.class);
            if (tvl == null) {
                break;
            }
            processGroup5(tvl);
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

        for (;;) {
            IFT ift = getConditionalSegment(IFT.class);
            if (ift == null) {
                break;
            }
            System.out.println(ift);
        }

        ORG_G1 org = getMandatorySegment(ORG_G1.class);

        for (;;) {
            ADD add = getConditionalSegment(ADD.class);
            if (add == null) {
                break;
            }
            System.out.println(add);
        }

        for (;;) {
            EBD ebd = getConditionalSegment(EBD.class);
            if (ebd == null) {
                break;
            }
            System.out.println(ebd);
        }
    }

    public void processGroup2(TIF tif) throws ParseException {
        System.out.println(tif);
        
        FTI fti = getConditionalSegment(FTI.class);

        for (;;) {
            IFT ift = getConditionalSegment(IFT.class);
            if (ift == null) {
                break;
            }
            System.out.println(ift);
        }

        REF ref = getConditionalSegment(REF.class);
        EBD ebd = getConditionalSegment(EBD.class);
        for (;;) {
            FAR far = getConditionalSegment(FAR.class);
            if (far == null) {
                break;
            }
            System.out.println(far);
        }

        for (;;) {
            SSR ssr = getConditionalSegment(SSR.class);
            if (ssr == null) {
                break;
            }
            System.out.println(ssr);
        }

        for (;;) {
            ADD add = getConditionalSegment(ADD.class);
            if (add == null) {
                break;
            }
            System.out.println(add);
        }

        for (;;) {
            TKT tkt = getConditionalSegment(TKT.class);
            if (tkt == null) {
                break;
            }
            processGroup3(tkt);
        }
    }
    
    public void processGroup3(TKT tkt) throws ParseException {
        System.out.println(tkt);
        MON mon = getConditionalSegment(MON.class);
        System.out.println(mon);

        PTK ptk = getConditionalSegment(PTK.class);
        System.out.println(ptk);

        for (;;) {
            TXD txd = getConditionalSegment(TXD.class);
            if (txd == null) {
                break;
            }
            System.out.println(txd);
        }

        DAT dat = getConditionalSegment(DAT.class);
        System.out.println(dat);

        FOP fop = getConditionalSegment(FOP.class);
        System.out.println(fop);
        processGroup4(fop);
    }
    
    public void processGroup4(FOP fop) throws ParseException {
        IFT ift = getConditionalSegment(IFT.class);
        ADD add = getConditionalSegment(ADD.class);
        
    }
    
    public void processGroup5(TVL tvl) throws ParseException {
        System.out.println(tvl);
    }
}
