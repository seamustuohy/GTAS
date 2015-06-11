package gov.cbp.taspd.gtas.parsers.paxlst;

import gov.cbp.taspd.gtas.model.Document;
import gov.cbp.taspd.gtas.model.Flight;
import gov.cbp.taspd.gtas.model.Pax;
import gov.cbp.taspd.gtas.model.ReportingParty;
import gov.cbp.taspd.gtas.parsers.edifact.Segment;
import gov.cbp.taspd.gtas.parsers.paxlst.usedifact.CTA;
import gov.cbp.taspd.gtas.parsers.paxlst.usedifact.PDT;
import gov.cbp.taspd.gtas.parsers.paxlst.usedifact.TDT;
import gov.cbp.taspd.gtas.parsers.paxlst.usedifact.UNB;

import java.util.ListIterator;

public class PaxlstParserUSedifact extends PaxlstParser {
    
    public PaxlstParserUSedifact(String filePath) {
        super(filePath, UNB.class.getPackage().getName());
    }
    
    public void parseSegments() {
        currentGroup = GROUP.NONE;
        
        for (ListIterator<Segment> i=segments.listIterator(); i.hasNext(); ) {
            Segment s = i.next();
            System.out.println(s);
            
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
                    processFlight(s);
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

    private void processFlight(Segment s) {
        TDT tdt = (TDT)s;
        Flight f = new Flight();
//        f.setCarrier();
    }

    private void processPax(Segment s) {
        Pax p = new Pax();
        passengers.add(p);

        PDT pdt = (PDT)s;
        p.setFirstName(pdt.getLastName());
        p.setLastName(pdt.getLastName());
        p.setMiddleName(pdt.getC_middleNameOrInitial());
        p.setDob(pdt.getDob());

        Document d = new Document();
        p.getDocuments().add(d);
//        d.setDocumentType(DocumentCode.);
        d.setNumber(pdt.getDocumentNumber());
        System.out.println("\t" + p);
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
