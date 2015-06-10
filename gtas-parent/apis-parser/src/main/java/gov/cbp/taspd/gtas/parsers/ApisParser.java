package gov.cbp.taspd.gtas.parsers;

import gov.cbp.taspd.gtas.model.ApisMessage;
import gov.cbp.taspd.gtas.parsers.paxlst.PaxlstParser;
import gov.cbp.taspd.gtas.parsers.paxlst.PaxlstParserUNedifact;

public class ApisParser {
    public void parseApisFile(String filePath) {
        // TODO: determine type of file: unedifact, usedifact
        PaxlstParser parser = new PaxlstParserUNedifact(filePath);
        ApisMessage m = parser.parse();             
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
