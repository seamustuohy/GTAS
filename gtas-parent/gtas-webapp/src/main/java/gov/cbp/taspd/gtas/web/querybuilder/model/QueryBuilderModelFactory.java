package gov.cbp.taspd.gtas.web.querybuilder.model;

import gov.cbp.taspd.gtas.constants.Constants;

public class QueryBuilderModelFactory {

	public IQueryBuilderModel getQueryBuilderModel(String modelType){
	      if(modelType == null){
	         return null;
	      }		
	      
	      if(modelType.equalsIgnoreCase(Constants.ADDRESS)) {
	         return new Address(Constants.ADDRESS);
	      } 
	      else if(modelType.equalsIgnoreCase(Constants.API)) {
	         return new API(Constants.API);
	      } 
	      else if(modelType.equalsIgnoreCase(Constants.CREDIT_CARD)) {
	         return new CreditCard(Constants.CREDIT_CARD);
	      }
	      else if(modelType.equalsIgnoreCase(Constants.DOCUMENT)) {
	    	  return new Document(Constants.DOCUMENT);
	      }
	      else if(modelType.equalsIgnoreCase(Constants.EMAIL)) {
	    	  return new Email(Constants.EMAIL);
	      }
	      else if(modelType.equalsIgnoreCase(Constants.FLIGHT)) {
	    	  return new Flight(Constants.FLIGHT);
	      }
	      else if(modelType.equalsIgnoreCase(Constants.FREQUENT_FLIER)) {
	    	  return new FrequentFlier(Constants.FREQUENT_FLIER);
	      }
	      else if(modelType.equalsIgnoreCase(Constants.HITS)) {
	    	  return new Hits(Constants.HITS);
	      }
	      else if(modelType.equalsIgnoreCase(Constants.NAME_ORIGIN)) {
	    	  return new NameOrigin(Constants.NAME_ORIGIN);
	      }
	      else if(modelType.equalsIgnoreCase(Constants.PASSENGER)) {
	    	  return new Passenger(Constants.PASSENGER);
	      }
	      else if(modelType.equalsIgnoreCase(Constants.PHONE)) {
	    	  return new Phone(Constants.PHONE);
	      }
	      else if(modelType.equalsIgnoreCase(Constants.PNR)) {
	    	  return new PNR(Constants.PNR);
	      }
	      else if(modelType.equalsIgnoreCase(Constants.TRAVEL_AGENCY)) {
	    	  return new TravelAgency(Constants.TRAVEL_AGENCY);
	      }
	      
	      return null;
	   }

}
