package gov.gtas.web.querybuilder.model;

import gov.gtas.constants.Constants;

public class QueryBuilderModelFactory {

	public IQueryBuilderModel getQueryBuilderModel(String modelType) {
	      if(modelType == null){
	         return null;
	      }		
	      
	      if(modelType.equalsIgnoreCase(Constants.ADDRESS)) {
	         return new Address();
	      } 
	      else if(modelType.equalsIgnoreCase(Constants.API)) {
	         return new API();
	      } 
	      else if(modelType.equalsIgnoreCase(Constants.CREDIT_CARD)) {
	         return new CreditCard();
	      }
	      else if(modelType.equalsIgnoreCase(Constants.DOCUMENT)) {
	    	  return new Document();
	      }
	      else if(modelType.equalsIgnoreCase(Constants.EMAIL)) {
	    	  return new Email();
	      }
	      else if(modelType.equalsIgnoreCase(Constants.FLIGHT)) {
	    	  return new Flight();
	      }
	      else if(modelType.equalsIgnoreCase(Constants.FREQUENT_FLYER)) {
	    	  return new FrequentFlyer();
	      }
	      else if(modelType.equalsIgnoreCase(Constants.HITS)) {
	    	  return new Hits();
	      }
	      else if(modelType.equalsIgnoreCase(Constants.NAME_ORIGIN)) {
	    	  return new NameOrigin();
	      }
	      else if(modelType.equalsIgnoreCase(Constants.PASSENGER)) {
	    	  return new Passenger();
	      }
	      else if(modelType.equalsIgnoreCase(Constants.PHONE)) {
	    	  return new Phone();
	      }
	      else if(modelType.equalsIgnoreCase(Constants.PNR)) {
	    	  return new PNR();
	      }
	      else if(modelType.equalsIgnoreCase(Constants.TRAVEL_AGENCY)) {
	    	  return new TravelAgency();
	      }
	      
	      return null;
	   }

}
