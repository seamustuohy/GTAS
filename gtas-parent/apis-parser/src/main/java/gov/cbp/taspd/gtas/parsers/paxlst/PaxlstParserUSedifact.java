package gov.cbp.taspd.gtas.parsers.paxlst;

import gov.cbp.taspd.gtas.model.Carrier;
import gov.cbp.taspd.gtas.model.Document;
import gov.cbp.taspd.gtas.model.DocumentType;
import gov.cbp.taspd.gtas.model.Flight;
import gov.cbp.taspd.gtas.model.Gender;
import gov.cbp.taspd.gtas.model.Pax;
import gov.cbp.taspd.gtas.model.PaxType;
import gov.cbp.taspd.gtas.model.ReportingParty;
import gov.cbp.taspd.gtas.parsers.edifact.Segment;
import gov.cbp.taspd.gtas.parsers.paxlst.usedifact.CTA;
import gov.cbp.taspd.gtas.parsers.paxlst.usedifact.DTM;
import gov.cbp.taspd.gtas.parsers.paxlst.usedifact.DTM.DtmCode;
import gov.cbp.taspd.gtas.parsers.paxlst.usedifact.LOC;
import gov.cbp.taspd.gtas.parsers.paxlst.usedifact.LOC.LocCode;
import gov.cbp.taspd.gtas.parsers.paxlst.usedifact.PDT;
import gov.cbp.taspd.gtas.parsers.paxlst.usedifact.PDT.DocType;
import gov.cbp.taspd.gtas.parsers.paxlst.usedifact.PDT.PersonStatus;
import gov.cbp.taspd.gtas.parsers.paxlst.usedifact.TDT;
import gov.cbp.taspd.gtas.parsers.paxlst.usedifact.UNB;

import java.util.ListIterator;

import org.jadira.cdt.country.ISOCountryCode;

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
        Flight f = new Flight();
        this.flights.add(f);

        f.setFlightNumber(tdt.getC_flightNumber());
        String iataCarrier = tdt.getC_airlineCode();
        if (iataCarrier != null) {
            f.setCarrier(Carrier.getByIataCode(iataCarrier));    
        }

        while (i.hasNext()) {
            Segment s = i.next();
//            System.out.println("\t" + s);
            switch (s.getName()) {
            case "LOC":
                LOC loc = (LOC)s;
                LocCode locCode = loc.getLocationCode();
                if (locCode == LocCode.DEPARTURE) {
                    f.setOriginCountry(ISOCountryCode.getByAlpha2Code(loc.getIataCountryCode()));
                } else if (locCode == LocCode.ARRIVAL) {
                    f.setDestinationCountry(ISOCountryCode.getByAlpha2Code(loc.getIataCountryCode()));
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
        Pax p = new Pax();
        passengers.add(p);

        PDT pdt = (PDT)s;
        p.setFirstName(pdt.getLastName());
        p.setLastName(pdt.getLastName());
        p.setMiddleName(pdt.getC_middleNameOrInitial());
        p.setDob(pdt.getDob());
        p.setGender(Gender.valueOf(pdt.getGender()));
        PersonStatus status = pdt.getPersonStatus();
        if (status == PersonStatus.PAX) {
            p.setType(PaxType.PAX);
        } else if (status == PersonStatus.CREW) {
            p.setType(PaxType.CREW);
        } else {
            p.setType(PaxType.OTHER);
        }

        Document d = new Document();
        p.getDocuments().add(d);
        d.setNumber(pdt.getDocumentNumber());
        d.setExpirationDate(pdt.getC_dateOfExpiration());
        DocType docType = pdt.getDocumentType();
        if (docType == DocType.PASSPORT) {
            d.setDocumentType(DocumentType.P);  
        } else {
            // TODO
        }        
//        System.out.println("\t" + p);
    }

    private void processReportingParty(Segment s) {
        CTA cta = (CTA)s;
        ReportingParty rp = new ReportingParty();
        message.getReportingParties().add(rp);
        rp.setPartyName(cta.getName());
        rp.setTelephone(cta.getTelephoneNumber());
        rp.setFax(cta.getFaxNumber());
    }
    
    private void handleUnexpectedSegment(Segment s) {
        System.err.println("unexpected segment " + s);
    }    
}
