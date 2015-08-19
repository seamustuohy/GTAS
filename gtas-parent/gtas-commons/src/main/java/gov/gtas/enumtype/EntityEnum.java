package gov.gtas.enumtype;


public enum EntityEnum {
	
		ADDRESS ("ADDRESS", "Address", "a"),
		CREDIT_CARD ("CREDIT CARD", "CreditCard", "cc"),
		DOCUMENT ("DOCUMENT", "Document", "d"),
		EMAIL ("EMAIL", "Email", "e"),
		FLIGHT ("FLIGHT", "Flight", "f"),
		FREQUENT_FLYER ("FREQUENT FLYER", "FrequentFlyer", "ff"),
		HITS ("HITS", "Hits", "h"),
		PASSENGER("PASSENGER", "Passenger", "p"),
		PHONE ("PHONE", "Phone", "ph"),
		PNR ("PNR", "Pnr", "pnr"),
		TRAVEL_AGENCY ("TRAVEL AGENCY", "TravelAgency", "ta");
		
		private String friendlyName;
		private String entityName;
		private String alias;
		
		private EntityEnum(String friendlyName, String entityName, String alias) {
			this.friendlyName = friendlyName;
			this.entityName = entityName;
			this.alias = alias;
		}

		public String getFriendlyName() {
			return friendlyName;
		}

		public String getEntityName() {
			return entityName;
		}

		public String getAlias() {
			return alias;
		}
		
		public static EntityEnum getEnum(String value) {
			
			for (EntityEnum entityEnum : EntityEnum.values()) {
			     if(entityEnum.name().equalsIgnoreCase(value)) {
			    	 return entityEnum;
			     }
			 }
			
	        throw new IllegalArgumentException();
	    }
}
