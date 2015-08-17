package gov.gtas.parsers.pnrgov;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gov.gtas.parsers.edifact.EdifactLexer;
import gov.gtas.parsers.edifact.EdifactParser;
import gov.gtas.parsers.exception.ParseException;
import gov.gtas.parsers.pnrgov.segment.ABI;
import gov.gtas.parsers.pnrgov.segment.ADD;
import gov.gtas.parsers.pnrgov.segment.APD;
import gov.gtas.parsers.pnrgov.segment.DAT_G1;
import gov.gtas.parsers.pnrgov.segment.DAT_G10;
import gov.gtas.parsers.pnrgov.segment.DAT_G6;
import gov.gtas.parsers.pnrgov.segment.EBD;
import gov.gtas.parsers.pnrgov.segment.EQN;
import gov.gtas.parsers.pnrgov.segment.FAR;
import gov.gtas.parsers.pnrgov.segment.FOP;
import gov.gtas.parsers.pnrgov.segment.FTI;
import gov.gtas.parsers.pnrgov.segment.FTI.FrequentFlierDetails;
import gov.gtas.parsers.pnrgov.segment.IFT;
import gov.gtas.parsers.pnrgov.segment.LTS;
import gov.gtas.parsers.pnrgov.segment.MON;
import gov.gtas.parsers.pnrgov.segment.MSG;
import gov.gtas.parsers.pnrgov.segment.ORG;
import gov.gtas.parsers.pnrgov.segment.PTK;
import gov.gtas.parsers.pnrgov.segment.RCI;
import gov.gtas.parsers.pnrgov.segment.RCI.ReservationControlInfo;
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
import gov.gtas.parsers.util.ParseUtils;
import gov.gtas.parsers.vo.passenger.AddressVo;
import gov.gtas.parsers.vo.passenger.CreditCardVo;
import gov.gtas.parsers.vo.passenger.FlightVo;
import gov.gtas.parsers.vo.passenger.FrequentFlierVo;
import gov.gtas.parsers.vo.passenger.PassengerVo;
import gov.gtas.parsers.vo.passenger.PhoneVo;
import gov.gtas.parsers.vo.passenger.PnrReportingAgentVo;

public final class PnrGovParser extends EdifactParser<PnrMessageVo> {
    private static final String[] SEGMENT_NAMES = new String[] { "ABI", "ADD", "APD", "DAT", "EBD", "EQN", "FAR", "FOP",
            "FTI", "IFT", "LTS", "MON", "MSG", "ORG", "PTK", "RCI", "REF", "RPI", "SAC", "SRC", "SSD", "SSR", "TBD",
            "TIF", "TKT", "TRA", "TRI", "TVL", "TXD" };
    public static final Set<String> PNRGOV_SEGMENT_INDEX = new HashSet<>(Arrays.asList(SEGMENT_NAMES));

    private PnrVo currentPnr;
    
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

    protected String getPayloadText(String message) throws ParseException {
        return EdifactLexer.getMessagePayload(message, "MSG", "UNT");
    }
    
    @Override
    public void parsePayload() throws ParseException {
        MSG msg = getMandatorySegment(MSG.class);

        // specifies the sender/reporting party of the message
        ORG org = getMandatorySegment(ORG.class);

        TVL_L0 tvl = getMandatorySegment(TVL_L0.class);

        EQN eqn = getMandatorySegment(EQN.class);
        int expectedNumberOfPnrs = eqn.getValue();

        int numPnrs = 0;
        for (;;) {
            SRC src = getConditionalSegment(SRC.class);
            if (src == null) {
                break;
            }
            
            PnrVo pnr = new PnrVo();
            parsedMessage.addPnr(pnr);
            this.currentPnr = pnr;
            numPnrs++;
            processGroup1(tvl);
        }
        
        if (expectedNumberOfPnrs != numPnrs) {
            throw new ParseException(String.format("Parsed %d PNR records but expected %d", numPnrs, expectedNumberOfPnrs));
        }
        
        for (PnrVo vo : this.parsedMessage.getPnrRecords()) {
            System.out.println(vo + "\n\n");            
        }
    }

    /**
     * start of a new PNR
     */
    private void processGroup1(TVL_L0 tvl_l0) throws ParseException {
        currentPnr.setCarrier(tvl_l0.getCarrier());
        currentPnr.setOrigin(tvl_l0.getOrigin());
        currentPnr.setDepartureDate(tvl_l0.getEtd());
        
        RCI rci = getMandatorySegment(RCI.class);
        ReservationControlInfo controlInfo = rci.getReservations().get(0);
        currentPnr.setRecordLocator(controlInfo.getReservationControlNumber());

        for (;;) {
            SSR ssr = getConditionalSegment(SSR.class, "SSR");
            if (ssr == null) {
                break;
            }
        }

        DAT_G1 dat = getConditionalSegment(DAT_G1.class, "DAT");
        currentPnr.setDateBooked(dat.getTicketIssueDate());

        for (;;) {
            IFT ift = getConditionalSegment(IFT.class);
            if (ift == null) {
                break;
            }
        }

        ORG org = getMandatorySegment(ORG.class);
        PnrReportingAgentVo agentVo = new PnrReportingAgentVo();
        agentVo.setAirlineCode(org.getAirlineCode());
        agentVo.setCountryCode(org.getOriginatorCountryCode());
        agentVo.setCurrencyCode(org.getOriginatorCurrencyCode());
        agentVo.setIdentificationCode(org.getCompanyIdentification());
        agentVo.setLocationCode(org.getLocationCode());
        currentPnr.getReportingParties().add(agentVo);

        for (;;) {
            ADD add = getConditionalSegment(ADD.class);
            if (add == null) {
                break;
            }
            AddressVo address = createAddress(add);
            currentPnr.getAddresses().add(address);
            if (address.getPhoneNumber() != null) {
                PhoneVo p = createPhone(address.getPhoneNumber());
                currentPnr.getPhoneNumbers().add(p);
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

    /**
     * Passenger
     */
    private void processGroup2(TIF tif) throws ParseException {
        FTI fti = getConditionalSegment(FTI.class);
        FrequentFlierVo ffvo = new FrequentFlierVo();
        FrequentFlierDetails ffdetails = fti.getFrequentFlierInfo().get(0);
        ffvo.setAirline(ffdetails.getAirlineCode());
        ffvo.setNumber(ffdetails.getFreqTravelerNumber());
        currentPnr.getFrequentFlierDetails().add(ffvo);

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
        boolean paxCreated = false;
        for (;;) {
            SSR ssr = getConditionalSegment(SSR.class);
            if (ssr == null) {
                break;
            }
            SSR.SsrCode code = SSR.SsrCode.valueOf(ssr.getTypeOfRequest());
            if (code == SSR.SsrCode.DOCS) {
                PassengerVo p = PnrUtils.createPassenger(ssr, tif);
                currentPnr.getPassengers().add(p);
                paxCreated = true;
            }
        }

        if (!paxCreated) {
            // all we can do is create the passenger from the TIF+REF
            PassengerVo p = PnrUtils.createPassenger(tif);
            currentPnr.getPassengers().add(p);
        }
        
        for (;;) {
            ADD add = getConditionalSegment(ADD.class);
            if (add == null) {
                break;
            }
            currentPnr.getAddresses().add(createAddress(add));
        }

        for (;;) {
            TKT tkt = getConditionalSegment(TKT.class);
            if (tkt == null) {
                break;
            }
            processGroup3(tkt);
        }
    }

    /**
     * Repeats for each ticket associated with a passenger.
     * Not currently using this info.
     */
    private void processGroup3(TKT tkt) throws ParseException {
        getConditionalSegment(MON.class);
        getConditionalSegment(PTK.class);

        for (;;) {
            TXD txd = getConditionalSegment(TXD.class);
            if (txd == null) {
                break;
            }
        }

        getConditionalSegment(DAT_G1.class, "DAT");

        FOP fop = getConditionalSegment(FOP.class);
        processGroup4(fop);
    }

    /**
     * Form of payment info
     */
    private void processGroup4(FOP fop) throws ParseException {
        boolean ccCreated = false;
        CreditCardVo cc = new CreditCardVo();
        if (fop.isCreditCard()) {
            cc.setCardType(fop.getVendorCode());
            cc.setExpiration(fop.getExpirationDate());
            cc.setNumber(fop.getAccountNumber());
            currentPnr.getCreditCards().add(cc);
            ccCreated = true;
        }

        IFT ift = getConditionalSegment(IFT.class);
        if (ift != null) {
            if (ccCreated && ift.isSponsorInfo()) {
                List<String> msgs = ift.getMessages();
                if (msgs.size() >= 1) {
                    cc.setAccountHolder(msgs.get(0));                
                }
            }
        }
        
        ADD add = getConditionalSegment(ADD.class);
        if (add != null) {
            currentPnr.getAddresses().add(createAddress(add));
        }
    }

    /**
     * Flight info
     */
    private void processGroup5(TVL tvl) throws ParseException {
        FlightVo f = new FlightVo();
        f.setCarrier(tvl.getCarrier());
        f.setDestination(tvl.getDestination());
        f.setOrigin(tvl.getOrigin());
        f.setEta(tvl.getEta());
        f.setEtd(tvl.getEtd());
        f.setFlightNumber(tvl.getFlightNumber());
        f.setFlightDate(tvl.getEtd(), tvl.getEta(), parsedMessage.getTransmissionDate());
        currentPnr.getFlights().add(f);
        
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
    
    /**
     * the agent info that checked-in the passenger
     */
    private void processGroup6(DAT_G6 dat) throws ParseException {
        ORG org = getConditionalSegment(ORG.class, "ORG");

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
    
    /**
     * boarding, seat number and checked bag info
     */
    private void processGroup7(TRI tri) throws ParseException {
        TIF tif = getConditionalSegment(TIF.class);
        SSD ssd = getConditionalSegment(SSD.class);
        TBD tbd = getConditionalSegment(TBD.class);
    }
    
    private void processGroup8(EQN eqn) throws ParseException {
        RCI rci = getMandatorySegment(RCI.class);
    }

    /**
     * non-air segments: car, hotel, rail
     */
    private void processGroup9(MSG msg) throws ParseException {
        for (;;) {
            TVL tvl = getConditionalSegment(TVL.class);
            if (tvl == null) {
                break;
            }
        }
    }

    private void processGroup10(ABI abi) throws ParseException {
        DAT_G10 dat = getConditionalSegment(DAT_G10.class, "DAT");
        for (;;) {
            SAC sac = getConditionalSegment(SAC.class);
            if (sac == null) {
                break;
            }
            processGroup11(sac);
        }        
    }

    /**
     * history information
     */
    private void processGroup11(SAC sac) throws ParseException {
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

    private void processGroup12(TVL tvl) throws ParseException {
        RPI rpi = getConditionalSegment(RPI.class);
    }
    
    private AddressVo createAddress(ADD add) {
        AddressVo rv = new AddressVo();
        rv.setType(add.getAddressType());
        rv.setLine1(add.getStreetNumberAndName());
        rv.setCity(add.getCity());
        rv.setState(add.getStateOrProvinceCode());
        rv.setCountry(add.getCountryCode());
        rv.setPostalCode(add.getPostalCode());
        rv.setPhoneNumber(ParseUtils.prepTelephoneNumber(add.getTelephone()));
        return rv;
    }
    
    private PhoneVo createPhone(String number) {
        PhoneVo rv = new PhoneVo();
        rv.setNumber(ParseUtils.prepTelephoneNumber(number));
        return rv;
    }
}
