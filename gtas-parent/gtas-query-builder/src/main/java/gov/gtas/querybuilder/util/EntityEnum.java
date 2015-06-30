package gov.gtas.querybuilder.util;

public enum EntityEnum {
	
		ADDRESS ("Address"),
		API ("API"),
		CREDIT_CARD ("CreditCard"),
		DOCUMENT ("Document"),
		EMAIL ("Email"),
		FLIGHT ("Flight"),
		FREQUENT_FLYER ("FrequentFlyer"),
		HITS ("Hits"),
		NAME_ORIGIN ("NameOrigin"),
		PASSENGER ("Pax"),
		PAX("Pax"),
		PHONE ("Phone"),
		PNR ("PNR"),
		TRAVEL_AGENCY ("TravelAgency");
		
		private String name;
		
		public String getName() {
			return name;
		}

		private EntityEnum(String name) {
			this.name = name;
		}
}
