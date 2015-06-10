package gov.cbp.taspd.gtas.parsers;

import gov.cbp.taspd.gtas.model.ApisMessage;
import gov.cbp.taspd.gtas.parsers.paxlst.PaxlstParser;
import gov.cbp.taspd.gtas.parsers.paxlst.PaxlstParserUNedifact;
import gov.cbp.taspd.gtas.parsers.paxlst.PaxlstParserUSedifact;

public class ApisParser {
    public void parseApisFile(String filePath) {
        // TODO: determine type of file: unedifact, usedifact
        PaxlstParser UNParser = new PaxlstParserUNedifact(filePath);
        PaxlstParser USParser = new PaxlstParserUSedifact(filePath);
        
        ApisMessage m = USParser.parse();             
    }
    
    public static void main(String[] argv) {        
        if (argv.length < 1) {
            System.out.println("usage: ApisParser [filename]");
            System.exit(0);
        }
        ApisParser parser = new ApisParser();
        parser.parseApisFile(argv[0]);
    }
}
