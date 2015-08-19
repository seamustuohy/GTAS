package gov.gtas.testdatagen;

import gov.gtas.model.Pnr;
import gov.gtas.model.PnrMessage;

public class PnrDataGenerator {
	public static final long PNR_ID1 = 221L;
	public static final String PNR_ATTR_CARRIER1 = "NZ";
	public static final String PNR_ATTR_RECORD_LOCATOR1 = "VYZ32901123";
	public static final long PNR_ID2 = 251L;
	public static final String PNR_ATTR_CARRIER2 = "AA";
	public static final String PNR_ATTR_RECORD_LOCATOR2 = "MNP32556191";
	
	public static final String PNR_PHONE1_1 = "+1 555-765-9087";
	public static final String PNR_PHONE2_1 = "+1 456-231-8944";

	public static final String PNR_ADDRESS1_1 = "USA,Atlanta,12345,1 Nowhere Street";
	public static final String PNR_ADDRESS1_2 = "CAN,Montreal,98765,21 Rue Blanc";

	public static final String PNR_EMAIL1_1 = "john.paul.jones@admiralty.gov";
	public static final String PNR_EMAIL1_2 = "the.donald@allmine.com";

   public static PnrMessage createTestPnrmessage(){
	   PnrMessage msg = new PnrMessage();
	   Pnr pnr = createPnr(PNR_ID1, PNR_ATTR_CARRIER1, PNR_ATTR_RECORD_LOCATOR1);
	   msg.addPnr(pnr);
	   
	   pnr = createPnr(PNR_ID2, PNR_ATTR_CARRIER2, PNR_ATTR_RECORD_LOCATOR2);
	   msg.addPnr(pnr);
	   return msg;
   }
   private static Pnr createPnr(long id, String carrier, String recLocator){
	   Pnr pnr = new Pnr();
	   pnr.setCarrier(carrier);
	   pnr.setId(id);
	   pnr.setRecordLocator(recLocator);
	   return pnr;	   
   }
}
