package gov.gtas.querybuilder.mappings;

import gov.gtas.querybuilder.enums.EntityEnum;

import java.util.ArrayList;
import java.util.List;

public class QueryBuilderMappingFactory {

	public QueryBuilderMapping getQueryBuilderMapping(EntityEnum modelType) {
	      if(modelType == null){
	         return null;
	      }		
	      
	      if(modelType == EntityEnum.ADDRESS) {
	         return createQueryBuilderMapping(EntityEnum.ADDRESS, AddressMapping.values());
	      } 
	      else if(modelType == EntityEnum.API) {
	         return createQueryBuilderMapping(EntityEnum.API, APIMapping.values());
	      } 
	      else if(modelType == EntityEnum.CREDIT_CARD) {
	         return createQueryBuilderMapping(EntityEnum.CREDIT_CARD, CreditCardMapping.values());
	      }
	      else if(modelType == EntityEnum.DOCUMENT) {
	    	  return createQueryBuilderMapping(EntityEnum.DOCUMENT, DocumentMapping.values());
	      }
	      else if(modelType == EntityEnum.EMAIL) {
	    	  return createQueryBuilderMapping(EntityEnum.EMAIL, EmailMapping.values());
	      }
	      else if(modelType == EntityEnum.FLIGHT) {
	    	  return createQueryBuilderMapping(EntityEnum.FLIGHT, FlightMapping.values());
	      }
	      else if(modelType == EntityEnum.FREQUENT_FLYER) {
	    	  return createQueryBuilderMapping(EntityEnum.FREQUENT_FLYER, FrequentFlyerMapping.values());
	      }
	      else if(modelType == EntityEnum.HITS) {
	    	  return createQueryBuilderMapping(EntityEnum.HITS, HitsMapping.values());
	      }
	      else if(modelType == EntityEnum.NAME_ORIGIN) {
	    	  return createQueryBuilderMapping(EntityEnum.NAME_ORIGIN, NameOriginMapping.values());
	      }
	      else if(modelType == EntityEnum.PAX) {
	    	  return createQueryBuilderMapping(EntityEnum.PAX, PassengerMapping.values());
	      }
	      else if(modelType == EntityEnum.PHONE) {
	    	  return createQueryBuilderMapping(EntityEnum.PHONE, PhoneMapping.values());
	      }
	      else if(modelType == EntityEnum.PNR) {
	    	  return createQueryBuilderMapping(EntityEnum.PNR, PNRMapping.values());
	      }
	      else if(modelType == EntityEnum.TRAVEL_AGENCY) {
	    	  return createQueryBuilderMapping(EntityEnum.TRAVEL_AGENCY, TravelAgencyMapping.values());
	      }
	      
	      return null;
	   }

	private QueryBuilderMapping createQueryBuilderMapping(EntityEnum entityEnum, IEntityMapping[] entityMapping) {
		QueryBuilderMapping model = new QueryBuilderMapping();
		List<Column> columns = new ArrayList<>();
		
		model.setLabel(entityEnum.getFriendlyName());
		for(IEntityMapping iem : entityMapping) {
			Column col = new Column(iem.getFieldName(), iem.getFriendlyName(), iem.getFieldType());
			columns.add(col);
		}
		
		model.setColumns(columns);
		
		return model;
	}
	
}
