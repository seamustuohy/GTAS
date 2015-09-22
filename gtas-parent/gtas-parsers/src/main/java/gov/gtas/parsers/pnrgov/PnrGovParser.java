package gov.gtas.parsers.pnrgov;

import java.util.List;

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
import gov.gtas.parsers.pnrgov.segment.TBD.BagDetails;
import gov.gtas.parsers.pnrgov.segment.TIF;
import gov.gtas.parsers.pnrgov.segment.TIF.TravelerDetails;
import gov.gtas.parsers.pnrgov.segment.TKT;
import gov.gtas.parsers.pnrgov.segment.TRA;
import gov.gtas.parsers.pnrgov.segment.TRI;
import gov.gtas.parsers.pnrgov.segment.TVL;
import gov.gtas.parsers.pnrgov.segment.TVL_L0;
import gov.gtas.parsers.pnrgov.segment.TXD;
import gov.gtas.parsers.util.ParseUtils;
import gov.gtas.vo.PnrVo;
import gov.gtas.vo.passenger.AddressVo;
import gov.gtas.vo.passenger.CreditCardVo;
import gov.gtas.vo.passenger.FlightVo;
import gov.gtas.vo.passenger.FrequentFlyerVo;
import gov.gtas.vo.passenger.PassengerVo;
import gov.gtas.vo.passenger.PhoneVo;
import gov.gtas.vo.passenger.PnrReportingAgentVo;


public final class PnrGovParser extends EdifactParser<PnrVo> {
   
    public PnrGovParser() {
        this.parsedMessage = new PnrVo();
    }

    protected String getPayloadText(String message) throws ParseException {
        return EdifactLexer.getMessagePayload(message, "SRC", "UNT");
    }
    
    @Override
    public void parsePayload() throws ParseException {
        MSG msg = getMandatorySegment(MSG.class);
        if(msg != null && msg.getMessageTypeCode() != null){
        	parsedMessage.setMessageCode(msg.getMessageTypeCode().getCode());
        }
        
        getMandatorySegment(ORG.class);
        TVL_L0 tvl = getMandatorySegment(TVL_L0.class, "TVL");
        getMandatorySegment(EQN.class);
        getMandatorySegment(SRC.class);       
        processGroup1(tvl);
    }

    /**
     * start of a new PNR
     */
    private void processGroup1(TVL_L0 tvl_l0) throws ParseException {
        parsedMessage.setCarrier(tvl_l0.getCarrier());
        parsedMessage.setOrigin(tvl_l0.getOrigin());
        parsedMessage.setDepartureDate(tvl_l0.getEtd());
        
        RCI rci = getMandatorySegment(RCI.class);
        ReservationControlInfo controlInfo = rci.getReservations().get(0);
        parsedMessage.setRecordLocator(controlInfo.getReservationControlNumber());

        for (;;) {
            SSR ssr = getConditionalSegment(SSR.class, "SSR");
            if (ssr == null) {
                break;
            }
        }

        DAT_G1 dat = getConditionalSegment(DAT_G1.class, "DAT");
        if (dat != null) {
            parsedMessage.setDateBooked(dat.getTicketIssueDate());
        }

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
        parsedMessage.getReportingParties().add(agentVo);

        for (;;) {
            ADD add = getConditionalSegment(ADD.class);
            if (add == null) {
                break;
            }
            AddressVo address = PnrUtils.createAddress(add);
            parsedMessage.getAddresses().add(address);
            if (address.getPhoneNumber() != null) {
                PhoneVo p = PnrUtils.createPhone(address.getPhoneNumber());
                parsedMessage.getPhoneNumbers().add(p);
            }
        }

        for (;;) {
            // excess baggage information for all passengers
            EBD ebd = getConditionalSegment(EBD.class);
            if (ebd == null) {
                break;
            }
            processExcessBaggage(ebd);
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
        if (fti != null) {
            FrequentFlyerVo ffvo = new FrequentFlyerVo();
            FrequentFlierDetails ffdetails = fti.getFrequentFlierInfo().get(0);
            ffvo.setCarrier(ffdetails.getAirlineCode());
            ffvo.setNumber(ffdetails.getFreqTravelerNumber());
            parsedMessage.getFrequentFlyerDetails().add(ffvo);
        }
        
        for (;;) {
            IFT ift = getConditionalSegment(IFT.class);
            if (ift == null) {
                break;
            }
        }

        getConditionalSegment(REF.class);
        getConditionalSegment(EBD.class);

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
            String code = ssr.getTypeOfRequest();
            if (SSR.DOCS.equals(code)) {
                PassengerVo p = PnrUtils.createPassenger(ssr, tif);
                if (p != null) {
                    parsedMessage.getPassengers().add(p);
                    paxCreated = true;
                    parsedMessage.setPassengerCount(parsedMessage.getPassengerCount() + 1);
                }
            } else if (SSR.DOCA.equals(code)) {
                parsedMessage.getAddresses().add(PnrUtils.createAddress(ssr));
            }
        }

        if (!paxCreated) {
            // all we can do is create the passenger from the TIF segment
            PassengerVo p = PnrUtils.createPassenger(tif);
            if (p != null) {
                parsedMessage.getPassengers().add(p);
                parsedMessage.setPassengerCount(parsedMessage.getPassengerCount() + 1);
            }
        }
        
        for (;;) {
            ADD add = getConditionalSegment(ADD.class);
            if (add == null) {
                break;
            }
            parsedMessage.getAddresses().add(PnrUtils.createAddress(add));
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
     * Ticket cost info. Repeats for each ticket associated with a passenger.
     * Not currently using this.
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
     * Form of payment info: get credit card if exists
     */
    private void processGroup4(FOP fop) throws ParseException {
        boolean ccCreated = false;
        CreditCardVo cc = new CreditCardVo();

        if (fop != null) {
            parsedMessage.setFormOfPayment(fop.getPaymentType());
            if (fop.isCreditCard()) {
                cc.setCardType(fop.getVendorCode());
                cc.setExpiration(fop.getExpirationDate());
                cc.setNumber(fop.getAccountNumber());
                parsedMessage.getCreditCards().add(cc);
                ccCreated = true;
            }
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
            parsedMessage.getAddresses().add(PnrUtils.createAddress(add));
        }
    }

    /**
     * Flight info: repeats for each flight segment in the passenger record’s
     * itinerary.
     */
    private void processGroup5(TVL tvl) throws ParseException {
        FlightVo f = new FlightVo();
        f.setCarrier(tvl.getCarrier());
        f.setDestination(tvl.getDestination());
        f.setOrigin(tvl.getOrigin());
        f.setEta(tvl.getEta());
        f.setEtd(tvl.getEtd());
        f.setFlightNumber(ParseUtils.padFlightNumberWithZeroes(tvl.getFlightNumber()));
        f.setFlightDate(tvl.getEtd(), tvl.getEta(), parsedMessage.getTransmissionDate());
        parsedMessage.getFlights().add(f);
        
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
        PassengerVo thePax = null;
        String refNumber = tri.getTravelerReferenceNumber();
        if (refNumber != null) {
            for (PassengerVo pax : parsedMessage.getPassengers()) {
                if (refNumber.equals(pax.getTravelerReferenceNumber())) {
                    thePax = pax;
                    break;
                }
            }
        }
        
        TIF tif = getConditionalSegment(TIF.class);
        if (thePax == null && tif != null) {
            // try finding pax based on tif info
            String surname = tif.getTravelerSurname();
            List<TravelerDetails> td = tif.getTravelerDetails();
            if (td != null && td.size() > 0) {
                String firstName = td.get(0).getTravelerGivenName();
                for (PassengerVo pax : parsedMessage.getPassengers()) {
                    if (surname.equals(pax.getLastName()) && firstName.equals(pax.getFirstName())) {
                        thePax = pax;
                        break;
                    }
                }
            }
        }
        
        SSD ssd = getConditionalSegment(SSD.class);
        if (thePax != null && ssd != null) {
            thePax.setSeat(ssd.getSeatNumber());
        }
        
        TBD tbd = getConditionalSegment(TBD.class);
        if (tbd == null) {
            return;
        }
        
        Integer n = tbd.getNumBags();
        if (n != null) {
            parsedMessage.setBagCount(parsedMessage.getBagCount() + n);
        } else {
            for (BagDetails bd : tbd.getBagDetails()) {
                int tmp = bd.getNumConsecutiveTags();
                parsedMessage.setBagCount(parsedMessage.getBagCount() + tmp);                
            }
        }
    }
    
    private void processGroup8(EQN eqn) throws ParseException {
        getMandatorySegment(RCI.class);
    }

    /**
     * non-air segments: car, hotel, rail.  Not used.
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
    
    private void processExcessBaggage(EBD ebd) {
        if (ebd != null) {
            Integer n = ParseUtils.returnNumberOrNull(ebd.getNumberInExcess());
            if (n != null) {
                parsedMessage.setBagCount(parsedMessage.getBagCount() + n);
            }
        }
    }
}
