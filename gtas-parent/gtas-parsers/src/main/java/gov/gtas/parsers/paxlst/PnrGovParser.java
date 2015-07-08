package gov.gtas.parsers.paxlst;

import gov.gtas.parsers.paxlst.unedifact.UNB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PnrGovParser {

	private static final Logger logger = LoggerFactory.getLogger(PnrGovParser.class);
    private String filePath;
    private String segmentPackageName;
    
    public PnrGovParser(String filePath){
        this.filePath = filePath;
        this.segmentPackageName = UNB.class.getPackage().getName();
    }
	
    protected enum GROUP {
        NONE,
        HEADER,
        REPORTING_PARTY,
        FLIGHT,
        PAX
    }


}
