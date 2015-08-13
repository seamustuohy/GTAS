package gov.gtas.enumtype;


public enum EntityEnum {
	
		ADDRESS ("ADDRESS", "Address", "a", ".addresses"),
		CREDIT_CARD ("CREDIT CARD", "CreditCard", "cc", ".creditCard"),
		DOCUMENT ("DOCUMENT", "Document", "d", ".documents"),
		EMAIL ("EMAIL", "Email", "e", ".emails"),
		FLIGHT ("FLIGHT", "Flight", "f", ".flights"),
		FREQUENT_FLYER ("FREQUENT FLYER", "FrequentFlyer", "ff", ".frequentFlyer"),
		HITS ("HITS", "Hits", "h", ""),
		PASSENGER("PASSENGER", "Passenger", "p", ".passengers"),
		PHONE ("PHONE", "Phone", "ph", ".phones"),
		PNR ("PNR", "PNR", "pnr", ".pnrs"),
		TRAVEL_AGENCY ("TRAVEL AGENCY", "TravelAgency", "ta", ".agency");
		
		private String friendlyName;
		private String entityName;
		private String alias;
		private String entityReference;
		
		private EntityEnum(String friendlyName, String entityName, String alias, String entityReference) {
			this.friendlyName = friendlyName;
			this.entityName = entityName;
			this.alias = alias;
			this.entityReference = entityReference;
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
		
		public String getEntityReference() {
			return entityReference;
		}

		public static EntityEnum getEnum(String value) {
			
			for (EntityEnum entityEnum : EntityEnum.values()) {
			     if(entityEnum.getEntityName().equalsIgnoreCase(value)) {
			    	 return entityEnum;
			     }
			 }
			
	        throw new IllegalArgumentException();
	    }
}
