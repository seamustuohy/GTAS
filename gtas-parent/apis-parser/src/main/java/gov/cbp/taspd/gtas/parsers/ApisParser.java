package gov.cbp.taspd.gtas.parsers;

import java.nio.charset.StandardCharsets;

import gov.cbp.taspd.gtas.model.ApisMessage;
import gov.cbp.taspd.gtas.parsers.paxlst.PaxlstParser;
import gov.cbp.taspd.gtas.parsers.paxlst.PaxlstParserUNedifact;
import gov.cbp.taspd.gtas.parsers.paxlst.PaxlstParserUSedifact;
import gov.cbp.taspd.gtas.util.FileUtils;

public class ApisParser {
    public void parseApisFile(String filePath) {
        byte[] raw = FileUtils.readSmallFile(filePath);
        String msg = new String(raw, StandardCharsets.US_ASCII);

        PaxlstParser parser;
        if (isUSEdifactFile(msg)) {
            parser = new PaxlstParserUSedifact(filePath);
        } else {
            parser= new PaxlstParserUNedifact(filePath);
        }
        
        ApisMessage m = parser.parse();             
    }
    
    private boolean isUSEdifactFile(String msg) {
        return (msg.contains("CDT") || msg.contains("PDT"));
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
