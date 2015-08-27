package gov.gtas.testdatagen;

import java.text.ParseException;

import gov.gtas.model.Address;
import gov.gtas.model.Agency;
import gov.gtas.model.CreditCard;
import gov.gtas.model.Document;
import gov.gtas.model.Email;
import gov.gtas.model.Flight;
import gov.gtas.model.FrequentFlyer;
import gov.gtas.model.Passenger;
import gov.gtas.model.Phone;
import gov.gtas.model.Pnr;
import gov.gtas.model.PnrMessage;
import gov.gtas.util.DateCalendarUtils;

public class PnrDataGenerator {
	public static final long PNR_ID1 = 221L;
	public static final String PNR_ATTR_CARRIER1 = "NZ";
	public static final String PNR_ATTR_RECORD_LOCATOR1 = "VYZ32901123";
	public static final long PNR_ID2 = 251L;
	public static final String PNR_ATTR_CARRIER2 = "AA";
	public static final String PNR_ATTR_RECORD_LOCATOR2 = "MNP32556191";
	
	public static final String PNR_PASSENGER1 = "C,Actius,Ghoulish,Boozer,CAN,2012-05-01";//pnr1
	public static final String PNR_PASSENGER2 = "P,Bilbo,,Baggins,CAN,2013-06-30";//pnr1
	public static final String PNR_PASSENGER3 = "P,Kilmer,Gaunt,Baggins,CAN,2014-01-30";//pnr1
	public static final String PNR_PASSENGER4 = "P,Crisco,Slick,Masterson,USA,2013-06-30";//pnr1
	public static final String PNR_PASSENGER5 = "P,Donald,Hair,Trump,GBR,2014-06-30";//pnr2
	public static final String PNR_PASSENGER6 = "C,Kadmil,K,Zamboni,GBR,2013-06-30";//pnr2
	
	public static final String PNR_FLIGHT1 = "IAD,CCU";
	public static final String PNR_FLIGHT2 = "LHR,JFK";
	
	public static final String PNR_PHONE1 = "+1 555-765-9087";
	public static final String PNR_PHONE2 = "+1 456-231-8944";

	public static final String PNR_ADDRESS1 = "CAN,Toronto,12345,1 Nowhere Street";
	public static final String PNR_ADDRESS2 = "CAN,Montreal,98765,21 Rue Blanc";
	public static final String PNR_ADDRESS3 = "GBR,London,NKZ215,33 Ilkeston Blvd";

	public static final String PNR_EMAIL1 = "bilbo.baggins@fall.home";
	public static final String PNR_EMAIL2 = "the.donald@allmine.com";

	public static final String PNR_FREQUENT_FLYER1 = "CO,16675981";
	public static final String PNR_FREQUENT_FLYER2 = "AA,99834512";
	
	public static final String PNR_CREDIT_CARD1= "1234567890123456";
	public static final String PNR_CREDIT_CARD2 = "1231456823941111";

	public static final String PNR_AGENCY1 = "USA,Alexandria,Amnesia Travels,1234567890123456";
	public static final String PNR_AGENCY2 = "GBR,Leeds,Island Tours,1231456823941111";

    public static PnrMessage createTestPnrmessage(long id) {
        PnrMessage msg = new PnrMessage();
        try {
            msg.setId(id);
            Pnr pnr = createPnr(PNR_ID1, PNR_ATTR_CARRIER1, PNR_ATTR_RECORD_LOCATOR1);
            addAddress(pnr, 1);
            addAddress(pnr, 2);
            addEmail(pnr, 1);
            addPhone(pnr, 1);
            addAgency(pnr, 1);
            addCreditCard(pnr, 1);
            addFrequentFlyer(pnr, 1);

            Flight flight = addFlight(pnr, 1);
            addPassenger(pnr, 1, flight);
            addPassenger(pnr, 2, flight);
            addPassenger(pnr, 3, flight);
            addPassenger(pnr, 4, flight);
            msg.setPnr(pnr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return msg;
    }

    public static PnrMessage createTestPnrmessage2(long id) {
        PnrMessage msg = new PnrMessage();
        try {
            msg.setId(id);
            Pnr pnr = createPnr(PNR_ID2, PNR_ATTR_CARRIER2, PNR_ATTR_RECORD_LOCATOR2);
            msg.setPnr(pnr);
            addAddress(pnr, 3);
            addEmail(pnr, 2);
            addPhone(pnr, 2);
            addAgency(pnr, 2);
            addCreditCard(pnr, 2);
            addFrequentFlyer(pnr, 2);

            Flight flight = addFlight(pnr, 2);
            addPassenger(pnr, 5, flight);
            addPassenger(pnr, 6, flight);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return msg;
    }	
	
    private static Pnr createPnr(long id, String carrier, String recLocator) {
        Pnr pnr = new Pnr();
        pnr.setCarrier(carrier);
        pnr.setId(id);
        pnr.setRecordLocator(recLocator);
        return pnr;
    }

    private static void addAddress(Pnr pnr, int indx) {
        switch (indx) {
        case 1:
            pnr.addAddress(createAdddress(1L, PNR_ADDRESS1));
            break;
        case 2:
            pnr.addAddress(createAdddress(2L, PNR_ADDRESS2));
            break;
        case 3:
            pnr.addAddress(createAdddress(3L, PNR_ADDRESS3));
            break;
        }
    }

    private static Address createAdddress(long id, String address) {
        Address addr = new Address();
        String[] params = address.split(",");
        addr.setCountry(params[0]);
        addr.setCity(params[1]);
        addr.setPostalCode(params[2]);
        addr.setLine1(params[3]);
        addr.setId(id);
        return addr;
    }

    private static void addEmail(Pnr pnr, int indx) {
        switch (indx) {
        case 1:
            Email email = new Email();
            String[] params = PNR_EMAIL1.split("@");
            email.setAddress(PNR_EMAIL1);
            email.setDomain(params[1]);
            email.setId(1L);
            pnr.addEmail(email);
            break;
        case 2:
            email = new Email();
            params = PNR_EMAIL2.split("@");
            email.setAddress(PNR_EMAIL2);
            email.setDomain(params[1]);
            email.setId(2L);
            pnr.addEmail(email);
            break;
        }
    }

    private static void addAgency(Pnr pnr, int indx) {
        switch (indx) {
        case 1:
            Agency agency = new Agency();
            String[] params = PNR_AGENCY1.split(",");
            agency.setAgencyCountry(params[0]);
            agency.setAgencyCity(params[1]);
            agency.setAgencyName(params[2]);
            agency.setAgencyIdentifier(params[3]);
            agency.setId(1L);
            pnr.setAgency(agency);
            break;
        case 2:
            agency = new Agency();
            params = PNR_AGENCY2.split(",");
            agency.setAgencyCountry(params[0]);
            agency.setAgencyCity(params[1]);
            agency.setAgencyName(params[2]);
            agency.setAgencyIdentifier(params[3]);
            agency.setId(2L);
            pnr.setAgency(agency);
            break;
        }
    }

    private static void addCreditCard(Pnr pnr, int indx) {
        switch (indx) {
        case 1:
            CreditCard creditCard = new CreditCard();
            creditCard.setNumber(PNR_CREDIT_CARD1);
            creditCard.setId(1L);
            pnr.addCreditCard(creditCard);
            break;
        case 2:
            creditCard = new CreditCard();
            creditCard.setNumber(PNR_CREDIT_CARD2);
            creditCard.setId(2L);
            pnr.addCreditCard(creditCard);
            break;
        }
    }

    private static void addPhone(Pnr pnr, int indx) {
        switch (indx) {
        case 1:
            Phone phone = new Phone();
            phone.setNumber(PNR_PHONE1);
            phone.setId(1L);
            pnr.addPhone(phone);
            break;
        case 2:
            phone = new Phone();
            phone.setNumber(PNR_PHONE1);
            phone.setId(2L);
            pnr.addPhone(phone);
            break;
        }
    }

    private static void addFrequentFlyer(Pnr pnr, int indx) {
        switch (indx) {
        case 1:
            FrequentFlyer ff = new FrequentFlyer();
            String[] params = PNR_FREQUENT_FLYER1.split(",");
            ff.setAirlineCode(params[0]);
            ff.setFrequentFlyerNumber(params[1]);
            ff.setId(1L);
            pnr.addFrequentFlyer(ff);
            break;
        case 2:
            ff = new FrequentFlyer();
            params = PNR_FREQUENT_FLYER2.split(",");
            ff.setAirlineCode(params[0]);
            ff.setFrequentFlyerNumber(params[1]);
            ff.setId(2L);
            pnr.addFrequentFlyer(ff);
            break;
        }
    }

    private static void addPassenger(Pnr pnr, int indx, Flight flight) throws ParseException {
        switch (indx) {
        case 1:
            Passenger p = new Passenger();
            String[] params = PNR_PASSENGER1.split(",");
            p.setPassengerType(params[0]);
            p.setFirstName(params[1]);
            p.setMiddleName(params[2]);
            p.setLastName(params[3]);
            p.setId(1L);
            addDocumentToPassenger(p, params[4], params[5]);
            pnr.getPassengers().add(p);
            flight.getPassengers().add(p);
            break;
        case 2:
            p = new Passenger();
            params = PNR_PASSENGER2.split(",");
            p.setPassengerType(params[0]);
            p.setFirstName(params[1]);
            p.setMiddleName(params[2]);
            p.setLastName(params[3]);
            p.setId(2L);
            addDocumentToPassenger(p, params[4], params[5]);
            pnr.getPassengers().add(p);
            flight.getPassengers().add(p);
            break;
        case 3:
            p = new Passenger();
            params = PNR_PASSENGER3.split(",");
            p.setPassengerType(params[0]);
            p.setFirstName(params[1]);
            p.setMiddleName(params[2]);
            p.setLastName(params[3]);
            p.setId(3L);
            addDocumentToPassenger(p, params[4], params[5]);
            pnr.getPassengers().add(p);
            flight.getPassengers().add(p);
            break;
        case 4:
            p = new Passenger();
            params = PNR_PASSENGER4.split(",");
            p.setPassengerType(params[0]);
            p.setFirstName(params[1]);
            p.setMiddleName(params[2]);
            p.setLastName(params[3]);
            p.setId(4L);
            addDocumentToPassenger(p, params[4], params[5]);
            pnr.getPassengers().add(p);
            flight.getPassengers().add(p);
            break;
        case 5:
            p = new Passenger();
            params = PNR_PASSENGER5.split(",");
            p.setPassengerType(params[0]);
            p.setFirstName(params[1]);
            p.setMiddleName(params[2]);
            p.setLastName(params[3]);
            p.setId(5L);
            addDocumentToPassenger(p, params[4], params[5]);
            pnr.getPassengers().add(p);
            flight.getPassengers().add(p);
            break;
        case 6:
            p = new Passenger();
            params = PNR_PASSENGER6.split(",");
            p.setPassengerType(params[0]);
            p.setFirstName(params[1]);
            p.setMiddleName(params[2]);
            p.setLastName(params[3]);
            p.setId(6L);
            addDocumentToPassenger(p, params[4], params[5]);
            pnr.getPassengers().add(p);
            flight.getPassengers().add(p);
            break;
        }
    }

    private static void addDocumentToPassenger(Passenger pass, String country, String date) throws ParseException {
        Document doc = new Document();
        doc.setId(pass.getId());
        doc.setIssuanceCountry(country);
        doc.setIssuanceDate(DateCalendarUtils.parseJsonDate(date));
        pass.addDocument(doc);
    }

    private static Flight addFlight(Pnr pnr, int indx) {
        Flight flight = null;
        switch (indx) {
        case 1:
            flight = new Flight();
            String[] params = PNR_FLIGHT1.split(",");
            flight.setOrigin(params[0]);
            flight.setDestination(params[1]);
            flight.setId(1L);
            pnr.getFlights().add(flight);
            break;
        case 2:
            flight = new Flight();
            params = PNR_FLIGHT2.split(",");
            flight.setOrigin(params[0]);
            flight.setDestination(params[1]);
            flight.setId(2L);
            pnr.getFlights().add(flight);
            break;
        }
        return flight;
    }
}
