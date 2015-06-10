package gov.cbp.taspd.gtas.parsers.paxlst;

import gov.cbp.taspd.gtas.parsers.paxlst.unedifact.UNB;

public class PaxlstParserUSedifact extends PaxlstParser {
    public PaxlstParserUSedifact(String filePath) {
        super(filePath, UNB.class.getPackage().getName());
    }
    
    public void parseSegments() {
    }
}
