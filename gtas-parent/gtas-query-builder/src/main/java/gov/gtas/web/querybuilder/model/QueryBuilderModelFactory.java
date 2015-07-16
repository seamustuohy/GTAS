package gov.gtas.web.querybuilder.model;

import gov.gtas.querybuilder.util.EntityEnum;

public class QueryBuilderModelFactory {

	public IQueryBuilderModel getQueryBuilderModel(EntityEnum modelType) {
	      if(modelType == null){
	         return null;
	      }		
	      
	      if(modelType == EntityEnum.ADDRESS) {
	         return new Address();
	      } 
	      else if(modelType == EntityEnum.API) {
	         return new API();
	      } 
	      else if(modelType == EntityEnum.CREDIT_CARD) {
	         return new CreditCard();
	      }
	      else if(modelType == EntityEnum.DOCUMENT) {
	    	  return new Document();
	      }
	      else if(modelType == EntityEnum.EMAIL) {
	    	  return new Email();
	      }
	      else if(modelType == EntityEnum.FLIGHT) {
	    	  return new Flight();
	      }
	      else if(modelType == EntityEnum.FREQUENT_FLYER) {
	    	  return new FrequentFlyer();
	      }
	      else if(modelType == EntityEnum.HITS) {
	    	  return new Hits();
	      }
	      else if(modelType == EntityEnum.NAME_ORIGIN) {
	    	  return new NameOrigin();
	      }
	      else if(modelType == EntityEnum.PAX) {
	    	  return new Passenger();
	      }
	      else if(modelType == EntityEnum.PHONE) {
	    	  return new Phone();
	      }
	      else if(modelType == EntityEnum.PNR) {
	    	  return new PNR();
	      }
	      else if(modelType == EntityEnum.TRAVEL_AGENCY) {
	    	  return new TravelAgency();
	      }
	      
	      return null;
	   }

}
