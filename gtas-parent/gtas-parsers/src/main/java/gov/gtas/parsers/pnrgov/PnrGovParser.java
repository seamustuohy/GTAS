package gov.gtas.parsers.pnrgov;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import gov.gtas.parsers.edifact.EdifactLexer;
import gov.gtas.parsers.edifact.EdifactParser;
import gov.gtas.parsers.exception.ParseException;
import gov.gtas.parsers.paxlst.vo.FlightVo;
import gov.gtas.parsers.pnrgov.segment.ABI;
import gov.gtas.parsers.pnrgov.segment.ADD;
import gov.gtas.parsers.pnrgov.segment.APD;
import gov.gtas.parsers.pnrgov.segment.DAT_G1;
import gov.gtas.parsers.pnrgov.segment.DAT_G10;
import gov.gtas.parsers.pnrgov.segment.DAT_G6;
import gov.gtas.parsers.pnrgov.segment.EBD;
import gov.gtas.parsers.pnrgov.segment.EQN;
import gov.gtas.parsers.pnrgov.segment.EQN_L0;
import gov.gtas.parsers.pnrgov.segment.FAR;
import gov.gtas.parsers.pnrgov.segment.FOP;
import gov.gtas.parsers.pnrgov.segment.FTI;
import gov.gtas.parsers.pnrgov.segment.IFT;
import gov.gtas.parsers.pnrgov.segment.LTS;
import gov.gtas.parsers.pnrgov.segment.MON;
import gov.gtas.parsers.pnrgov.segment.MSG;
import gov.gtas.parsers.pnrgov.segment.ORG_G1;
import gov.gtas.parsers.pnrgov.segment.ORG_G6;
import gov.gtas.parsers.pnrgov.segment.ORG_L0;
import gov.gtas.parsers.pnrgov.segment.PTK;
import gov.gtas.parsers.pnrgov.segment.RCI;
import gov.gtas.parsers.pnrgov.segment.REF;
import gov.gtas.parsers.pnrgov.segment.RPI;
import gov.gtas.parsers.pnrgov.segment.SAC;
import gov.gtas.parsers.pnrgov.segment.SRC;
import gov.gtas.parsers.pnrgov.segment.SSD;
import gov.gtas.parsers.pnrgov.segment.SSR;
import gov.gtas.parsers.pnrgov.segment.TBD;
import gov.gtas.parsers.pnrgov.segment.TIF;
import gov.gtas.parsers.pnrgov.segment.TKT;
import gov.gtas.parsers.pnrgov.segment.TRA;
import gov.gtas.parsers.pnrgov.segment.TRI;
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

        ORG_L0 org = getMandatorySegment(ORG_L0.class);

        TVL_L0 tvl = getMandatorySegment(TVL_L0.class);
        FlightVo f = new FlightVo();
        f.setCarrier(tvl.getCarrier());
        f.setDestination(tvl.getDestination());
        f.setOrigin(tvl.getOrigin());
        f.setEta(tvl.getEta());
        f.setEtd(tvl.getEtd());
        f.setFlightNumber(tvl.getFlightNumber());
        this.parsedMessage.getFlights().add(f);

        EQN_L0 eqn = getMandatorySegment(EQN_L0.class);

        for (;;) {
            SRC src = getConditionalSegment(SRC.class);
            if (src == null) {
                break;
            }
            processGroup1();
        }
        
        System.out.println(this.parsedMessage);
    }

    public void processGroup1() throws ParseException {
        RCI rci = getMandatorySegment(RCI.class);

        for (;;) {
            SSR ssr = getConditionalSegment(SSR.class, "SSR");
            if (ssr == null) {
                break;
            }
        }

        DAT_G1 dat = getConditionalSegment(DAT_G1.class, "DAT");

        for (;;) {
            IFT ift = getConditionalSegment(IFT.class);
            if (ift == null) {
                break;
            }
        }

        ORG_G1 org = getMandatorySegment(ORG_G1.class);

        for (;;) {
            ADD add = getConditionalSegment(ADD.class);
            if (add == null) {
                break;
            }
        }

        for (;;) {
            EBD ebd = getConditionalSegment(EBD.class);
            if (ebd == null) {
                break;
            }
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

    public void processGroup2(TIF tif) throws ParseException {

        FTI fti = getConditionalSegment(FTI.class);

        for (;;) {
            IFT ift = getConditionalSegment(IFT.class);
            if (ift == null) {
                break;
            }
        }

        REF ref = getConditionalSegment(REF.class);
        EBD ebd = getConditionalSegment(EBD.class);
        for (;;) {
            FAR far = getConditionalSegment(FAR.class);
            if (far == null) {
                break;
            }
        }

        // SSR’s in GR.2 apply to the specific passenger.
        for (;;) {
            SSR ssr = getConditionalSegment(SSR.class);
            if (ssr == null) {
                break;
            }
        }

        for (;;) {
            ADD add = getConditionalSegment(ADD.class);
            if (add == null) {
                break;
            }
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
        MON mon = getConditionalSegment(MON.class);

        PTK ptk = getConditionalSegment(PTK.class);

        for (;;) {
            TXD txd = getConditionalSegment(TXD.class);
            if (txd == null) {
                break;
            }
        }

        DAT_G1 dat = getConditionalSegment(DAT_G1.class, "DAT");

        FOP fop = getConditionalSegment(FOP.class);
        processGroup4(fop);
    }

    public void processGroup4(FOP fop) throws ParseException {
        IFT ift = getConditionalSegment(IFT.class);
        ADD add = getConditionalSegment(ADD.class);

    }

    public void processGroup5(TVL tvl) throws ParseException {
        TRA tra = getConditionalSegment(TRA.class);
        RPI rpi = getConditionalSegment(RPI.class);
        APD apd = getConditionalSegment(APD.class);
        
        for (;;) {
            SSR ssr = getConditionalSegment(SSR.class);
            if (ssr == null) {
                break;
            }
        }

        RCI rci = getConditionalSegment(RCI.class);

        for (;;) {
            IFT ift = getConditionalSegment(IFT.class);
            if (ift == null) {
                break;
            }
        }

        for (;;) {
            DAT_G6 dat = getConditionalSegment(DAT_G6.class, "DAT");
            if (dat == null) {
                break;
            }
            processGroup6(dat);
        }
        
        for (;;) {
            EQN eqn = getConditionalSegment(EQN.class);
            if (eqn == null) {
                break;
            }
            processGroup8(eqn);
        }

        for (;;) {
            MSG msg = getConditionalSegment(MSG.class);
            if (msg == null) {
                break;
            }
            processGroup9(msg);
        }

        for (;;) {
            ABI abi = getConditionalSegment(ABI.class);
            if (abi == null) {
                break;
            }
            processGroup10(abi);
        }

        for (;;) {
            LTS lts = getConditionalSegment(LTS.class);
            if (lts == null) {
                break;
            }
        }        
    }
    
    public void processGroup6(DAT_G6 dat) throws ParseException {
        ORG_G6 rci = getConditionalSegment(ORG_G6.class, "ORG");

        TRI tri = getMandatorySegment(TRI.class);
        processGroup7(tri);
        for (;;) {
            tri = getConditionalSegment(TRI.class);
            if (tri == null) {
                break;
            }
            processGroup7(tri);
        }        
    }
    
    public void processGroup7(TRI tri) throws ParseException {
        TIF tif = getConditionalSegment(TIF.class);
        SSD ssd = getConditionalSegment(SSD.class);
        TBD tbd = getConditionalSegment(TBD.class);
    }
    
    public void processGroup8(EQN eqn) throws ParseException {
        RCI rci = getMandatorySegment(RCI.class);
    }

    public void processGroup9(MSG msg) throws ParseException {
        for (;;) {
            TVL tvl = getConditionalSegment(TVL.class);
            if (tvl == null) {
                break;
            }
        }
    }

    public void processGroup10(ABI abi) throws ParseException {
        DAT_G10 dat = getConditionalSegment(DAT_G10.class, "DAT");
        for (;;) {
            SAC sac = getConditionalSegment(SAC.class);
            if (sac == null) {
                break;
            }
            processGroup11(sac);
        }        
    }

    public void processGroup11(SAC sac) throws ParseException {
        TIF tif = getConditionalSegment(TIF.class);
        SSR ssr = getConditionalSegment(SSR.class);
        IFT ift = getConditionalSegment(IFT.class);
        TBD tbd = getConditionalSegment(TBD.class);
        for (;;) {
            TVL tvl = getConditionalSegment(TVL.class);
            if (tvl == null) {
                break;
            }
            processGroup12(tvl);
        }        

    }

    public void processGroup12(TVL tvl) throws ParseException {
        RPI rpi = getConditionalSegment(RPI.class);
   
    }
}
