package gov.gtas.web.querybuilder.model;

import gov.gtas.querybuilder.util.EntityEnum;

public class QueryBuilderModelFactory {

	public IQueryBuilderModel getQueryBuilderModel(String modelType) {
	      if(modelType == null){
	         return null;
	      }		
	      
	      if(modelType.equalsIgnoreCase(EntityEnum.ADDRESS.toString())) {
	         return new Address();
	      } 
	      else if(modelType.equalsIgnoreCase(EntityEnum.API.toString())) {
	         return new API();
	      } 
	      else if(modelType.equalsIgnoreCase(EntityEnum.CREDIT_CARD.toString())) {
	         return new CreditCard();
	      }
	      else if(modelType.equalsIgnoreCase(EntityEnum.DOCUMENT.toString())) {
	    	  return new Document();
	      }
	      else if(modelType.equalsIgnoreCase(EntityEnum.EMAIL.toString())) {
	    	  return new Email();
	      }
	      else if(modelType.equalsIgnoreCase(EntityEnum.FLIGHT.toString())) {
	    	  return new Flight();
	      }
	      else if(modelType.equalsIgnoreCase(EntityEnum.FREQUENT_FLYER.toString())) {
	    	  return new FrequentFlyer();
	      }
	      else if(modelType.equalsIgnoreCase(EntityEnum.HITS.toString())) {
	    	  return new Hits();
	      }
	      else if(modelType.equalsIgnoreCase(EntityEnum.NAME_ORIGIN.toString())) {
	    	  return new NameOrigin();
	      }
	      else if(modelType.equalsIgnoreCase(EntityEnum.PASSENGER.toString())) {
	    	  return new Passenger();
	      }
	      else if(modelType.equalsIgnoreCase(EntityEnum.PHONE.toString())) {
	    	  return new Phone();
	      }
	      else if(modelType.equalsIgnoreCase(EntityEnum.PNR.toString())) {
	    	  return new PNR();
	      }
	      else if(modelType.equalsIgnoreCase(EntityEnum.TRAVEL_AGENCY.toString())) {
	    	  return new TravelAgency();
	      }
	      
	      return null;
	   }

}
