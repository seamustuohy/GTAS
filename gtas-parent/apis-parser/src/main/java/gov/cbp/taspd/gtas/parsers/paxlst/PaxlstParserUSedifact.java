package gov.cbp.taspd.gtas.parsers.paxlst;

import gov.cbp.taspd.gtas.model.Pax;
import gov.cbp.taspd.gtas.model.ReportingParty;
import gov.cbp.taspd.gtas.parsers.edifact.Segment;
import gov.cbp.taspd.gtas.parsers.paxlst.usedifact.CTA;
import gov.cbp.taspd.gtas.parsers.paxlst.usedifact.PDT;
import gov.cbp.taspd.gtas.parsers.paxlst.usedifact.UNB;

import java.util.ListIterator;

public class PaxlstParserUSedifact extends PaxlstParser {
    public PaxlstParserUSedifact(String filePath) {
        super(filePath, UNB.class.getPackage().getName());
    }
    
    public void parseSegments() {
        currentGroup = 0;
        
        for (ListIterator<Segment> i=segments.listIterator(); i.hasNext(); ) {
            Segment s = i.next();
            System.out.println(s);
            switch (s.getName()) {
            case "CTA":
                processReportingParty(s);
                break;
            case "DTM":
                processFlight(s);
                currentGroup = 1;
                break;
            case "UNS":
                currentGroup = 2;
                break;
            case "PDT":
                processPax(s);
                break;
            case "UNZ":
                currentGroup = 0;
                break;
            }
        }        
    }

    private void processFlight(Segment s) {        
    }

    private void processPax(Segment s) {
        Pax p = new Pax();
        passengers.add(p);

        PDT pdt = (PDT)s;
        p.setFirstName(pdt.getLastName());
        p.setLastName(pdt.getLastName());
        p.setMiddleName(pdt.getC_middleNameOrInitial());
        p.setDob(pdt.getDob());
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
}
