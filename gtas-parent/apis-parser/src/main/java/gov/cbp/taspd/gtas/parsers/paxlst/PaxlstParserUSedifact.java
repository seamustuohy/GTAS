package gov.cbp.taspd.gtas.parsers.paxlst;

import gov.cbp.taspd.gtas.parsers.edifact.Segment;
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
        }        
    }
}
