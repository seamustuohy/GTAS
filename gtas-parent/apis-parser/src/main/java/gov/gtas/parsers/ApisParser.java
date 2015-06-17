package gov.gtas.parsers;

import gov.gtas.model.ApisMessage;
import gov.gtas.model.Flight;
import gov.gtas.parsers.paxlst.PaxlstParser;
import gov.gtas.parsers.paxlst.PaxlstParserUNedifact;
import gov.gtas.parsers.paxlst.PaxlstParserUSedifact;
import gov.gtas.util.FileUtils;

import java.nio.charset.StandardCharsets;

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
