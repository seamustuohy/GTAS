package gov.gtas.web.querybuilder.model;

import gov.gtas.constants.TableNameEnum;

public class QueryBuilderModelFactory {

	public IQueryBuilderModel getQueryBuilderModel(String modelType) {
	      if(modelType == null){
	         return null;
	      }		
	      
	      if(modelType.equalsIgnoreCase(TableNameEnum.ADDRESS.toString())) {
	         return new Address();
	      } 
	      else if(modelType.equalsIgnoreCase(TableNameEnum.API.toString())) {
	         return new API();
	      } 
	      else if(modelType.equalsIgnoreCase(TableNameEnum.CREDIT_CARD.toString())) {
	         return new CreditCard();
	      }
	      else if(modelType.equalsIgnoreCase(TableNameEnum.DOCUMENT.toString())) {
	    	  return new Document();
	      }
	      else if(modelType.equalsIgnoreCase(TableNameEnum.EMAIL.toString())) {
	    	  return new Email();
	      }
	      else if(modelType.equalsIgnoreCase(TableNameEnum.FLIGHT.toString())) {
	    	  return new Flight();
	      }
	      else if(modelType.equalsIgnoreCase(TableNameEnum.FREQUENT_FLYER.toString())) {
	    	  return new FrequentFlyer();
	      }
	      else if(modelType.equalsIgnoreCase(TableNameEnum.HITS.toString())) {
	    	  return new Hits();
	      }
	      else if(modelType.equalsIgnoreCase(TableNameEnum.NAME_ORIGIN.toString())) {
	    	  return new NameOrigin();
	      }
	      else if(modelType.equalsIgnoreCase(TableNameEnum.PASSENGER.toString())) {
	    	  return new Passenger();
	      }
	      else if(modelType.equalsIgnoreCase(TableNameEnum.PHONE.toString())) {
	    	  return new Phone();
	      }
	      else if(modelType.equalsIgnoreCase(TableNameEnum.PNR.toString())) {
	    	  return new PNR();
	      }
	      else if(modelType.equalsIgnoreCase(TableNameEnum.TRAVEL_AGENCY.toString())) {
	    	  return new TravelAgency();
	      }
	      
	      return null;
	   }

}
