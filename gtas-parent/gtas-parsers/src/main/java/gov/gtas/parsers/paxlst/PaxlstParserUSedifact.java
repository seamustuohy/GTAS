package gov.gtas.parsers.paxlst;

import gov.gtas.parsers.edifact.Segment;
import gov.gtas.parsers.paxlst.usedifact.CTA;
import gov.gtas.parsers.paxlst.usedifact.DTM;
import gov.gtas.parsers.paxlst.usedifact.DTM.DtmCode;
import gov.gtas.parsers.paxlst.usedifact.LOC;
import gov.gtas.parsers.paxlst.usedifact.LOC.LocCode;
import gov.gtas.parsers.paxlst.usedifact.PDT;
import gov.gtas.parsers.paxlst.usedifact.PDT.DocType;
import gov.gtas.parsers.paxlst.usedifact.PDT.PersonStatus;
import gov.gtas.parsers.paxlst.usedifact.TDT;
import gov.gtas.parsers.paxlst.usedifact.UNB;
import gov.gtas.parsers.paxlst.vo.DocumentVo;
import gov.gtas.parsers.paxlst.vo.FlightVo;
import gov.gtas.parsers.paxlst.vo.PaxVo;
import gov.gtas.parsers.paxlst.vo.ReportingPartyVo;

import java.util.ListIterator;

public class PaxlstParserUSedifact extends PaxlstParser {
    
    public PaxlstParserUSedifact(String filePath) {
        super(filePath, UNB.class.getPackage().getName());
    }
    
    public void parseSegments() {
        currentGroup = GROUP.NONE;
        
        for (ListIterator<Segment> i=segments.listIterator(); i.hasNext(); ) {
            Segment s = i.next();
//            System.out.println(s);
            
            switch (s.getName()) {
            case "CTA":
                if (currentGroup == GROUP.NONE || currentGroup == GROUP.REPORTING_PARTY) {
                    currentGroup = GROUP.REPORTING_PARTY;
                    processReportingParty(s);
                } else {
                    handleUnexpectedSegment(s);
                    return;
                }
                break;

            case "TDT":
                if (currentGroup == GROUP.HEADER 
                    || currentGroup == GROUP.REPORTING_PARTY
                    || currentGroup == GROUP.FLIGHT) {
                    
                    currentGroup = GROUP.FLIGHT;
                    processFlight(s, i);
                } else {
                    handleUnexpectedSegment(s);
                    return;
                }
                break;
                
            case "UNS":
                currentGroup = GROUP.PAX;
                break;
            
            case "PDT":
                if (currentGroup == GROUP.PAX) {
                    processPax(s);
                } else {
                    // missing UNS segment
                    handleUnexpectedSegment(s);
                    return;
                }
                break;
                
            case "UNZ":
                currentGroup = GROUP.NONE;
                break;
            }
        }        
    }

    private void processFlight(Segment seg, ListIterator<Segment> i) {
        TDT tdt = (TDT)seg;
        FlightVo f = new FlightVo();
        message.addFlight(f);

        f.setFlightNumber(tdt.getC_flightNumber());
        f.setCarrier(tdt.getC_airlineCode());

        while (i.hasNext()) {
            Segment s = i.next();
//            System.out.println("\t" + s);
            switch (s.getName()) {
            case "LOC":
                LOC loc = (LOC)s;
                LocCode locCode = loc.getLocationCode();
                String country = loc.getIataCountryCode();
                String  airport = loc.getIataAirportCode();
                if (locCode == LocCode.DEPARTURE) {
                    f.setOriginCountry(country);
                    f.setOrigin(airport);
                } else if (locCode == LocCode.ARRIVAL) {
                    f.setDestinationCountry(country);
                    f.setDestination(airport);
                }
                break;
            
            case "DTM":
                DTM dtm = (DTM)s;
                DtmCode dtmCode = dtm.getDtmCode();
                if (dtmCode == DtmCode.DEPARTURE_DATETIME) {
                    f.setEtd(dtm.getC_dateTime());
                } else if (dtmCode == DtmCode.ARRIVAL_DATETIME) {
                    f.setEta(dtm.getC_dateTime());
                }
                break;
                
            default:
                i.previous();
                return;
            }
        }
    }

    private void processPax(Segment s) {
        PaxVo p = new PaxVo();
        message.addPax(p);

        PDT pdt = (PDT)s;
        p.setFirstName(pdt.getLastName());
        p.setLastName(pdt.getLastName());
        p.setMiddleName(pdt.getC_middleNameOrInitial());
        p.setDob(pdt.getDob());
        p.setGender(pdt.getGender());
        PersonStatus status = pdt.getPersonStatus();
        p.setPaxType(status.toString());
//        if (status == PersonStatus.PAX) {
//            p.setType(PaxType.PAX);
//        } else if (status == PersonStatus.CREW) {
//            p.setType(PaxType.CREW);
//        } else {
//            p.setType(PaxType.OTHER);
//        }

        DocumentVo d = new DocumentVo();
        p.addDocument(d);
        d.setDocumentNumber(pdt.getDocumentNumber());
        d.setExpirationDate(pdt.getC_dateOfExpiration());
        DocType docType = pdt.getDocumentType();
        d.setDocumentType(docType.toString());
//        if (docType == DocType.PASSPORT) {
//            d.setDocumentType(DocumentType.P);  
//        } else {
//            // TODO
//        }        
//        System.out.println("\t" + p);
    }

    private void processReportingParty(Segment s) {
        CTA cta = (CTA)s;
        ReportingPartyVo rp = new ReportingPartyVo();
        message.addReportingParty(rp);
        rp.setPartyName(cta.getName());
        rp.setTelephone(cta.getTelephoneNumber());
        rp.setFax(cta.getFaxNumber());
    }
    
    private void handleUnexpectedSegment(Segment s) {
        System.err.println("unexpected segment " + s);
    }    
}
