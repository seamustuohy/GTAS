package gov.gtas.model.watchlist.util;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.gtas.enumtype.EntityEnum;
import gov.gtas.enumtype.WatchlistEditEnum;
import gov.gtas.model.watchlist.Watchlist;
import gov.gtas.model.watchlist.WatchlistItem;
import gov.gtas.model.watchlist.json.WatchlistSpec;
import gov.gtas.model.watchlist.json.WatchlistItemSpec;
import gov.gtas.model.watchlist.json.WatchlistTerm;
import gov.gtas.querybuilder.mappings.IEntityMapping;
import gov.gtas.querybuilder.mappings.PassengerMapping;

/**
 * A builder pattern object for creating watch list objects programmatically.
 * 
 * @author GTAS3
 *
 */
public class WatchlistBuilder {
	private String name;
	private EntityEnum entity;
	private List<WatchlistItemSpec> items;
	private List<WatchlistItem> deleteList;
	private List<WatchlistItem> createUpdateList;
	private ObjectMapper mapper;
	
	public WatchlistBuilder(final WatchlistSpec spec){
		this.mapper = new ObjectMapper();
		if(spec != null){
			this.name = spec.getName();
			this.entity = EntityEnum.getEnum(spec.getEntity());
			this.items = spec.getWatchlistItems();
		}
	}
	
	public WatchlistBuilder(final Watchlist watchlist, List<WatchlistItem> wlitems){
		this.mapper = new ObjectMapper();
		if(watchlist != null){
			this.name = watchlist.getWatchlistName();
			this.entity = watchlist.getWatchlistEntity();
			this.createUpdateList = wlitems;
		}
	}
	public WatchlistSpec buildWatchlistSpec()  throws IOException{
		WatchlistSpec ret = new WatchlistSpec(this.name, this.entity.getEntityName());
		for(WatchlistItem item:createUpdateList){
			WatchlistItemSpec itemSpec = mapper.readValue(item.getItemData(), WatchlistItemSpec.class);
			itemSpec.setId(item.getId());
			ret.addWatchlistItem(itemSpec);
		}
		return ret;
	}

	public void buildPersistenceLists()  throws JsonProcessingException{
		if(items != null && items.size() > 0){
			deleteList = new LinkedList<WatchlistItem>();
			createUpdateList = new LinkedList<WatchlistItem>();
			for(WatchlistItemSpec itemSpec:items){
				WatchlistEditEnum op = WatchlistEditEnum.getEditEnumForOperationName(itemSpec.getAction());
				WatchlistItem item = new WatchlistItem();
				switch(op){
					case U:
						item.setId(itemSpec.getId());
					case C:
						String json = mapper.writeValueAsString(itemSpec);
						item.setItemData(json);
						this.createUpdateList.add(item);
						break;
					case D:
						item.setId(itemSpec.getId());
						this.deleteList.add(item);
						break;
				}
			}
			if(deleteList.size() == 0){
				this.deleteList = null;
			}
			if(createUpdateList.size() == 0){
				this.createUpdateList = null;
			}
		}		
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the entity
	 */
	public EntityEnum getEntity() {
		return entity;
	}

	/**
	 * @return the deleteList
	 */
	public List<WatchlistItem> getDeleteList() {
		return deleteList;
	}

	/**
	 * @return the createUpdateList
	 */
	public List<WatchlistItem> getCreateUpdateList() {
		return createUpdateList;
	}

	/**
	 * Creates a sample UDR specification JSON object. (This is used for
	 * testing.)
	 * 
	 * @param userId
	 * @param title
	 * @param description
	 * @return
	 */
	public static WatchlistSpec createSampleWatchlist() {
		WatchlistSpec ret = new WatchlistSpec("Passenger Watch List 1", EntityEnum.PASSENGER.getEntityName().toUpperCase());
		ret.addWatchlistItem(
				new WatchlistItemSpec(null, WatchlistEditEnum.C.getOperationName(),
				new WatchlistTerm[]{
				    new WatchlistTerm( 
				      EntityEnum.PASSENGER.getEntityName().toUpperCase(),
				      PassengerMapping.FIRST_NAME.getFieldName(), 
				      PassengerMapping.FIRST_NAME.getFieldType(), "John"),				      
					new WatchlistTerm( 
				      EntityEnum.PASSENGER.getEntityName().toUpperCase(),
				      PassengerMapping.LAST_NAME.getFieldName(), 
				      PassengerMapping.LAST_NAME.getFieldType(), "Jones"),
					new WatchlistTerm( 
				      EntityEnum.PASSENGER.getEntityName().toUpperCase(),
				      PassengerMapping.DOB.getFieldName(), 
				      PassengerMapping.DOB.getFieldType(), "1747-07-06")
				}));
		ret.addWatchlistItem(
				new WatchlistItemSpec(32L, WatchlistEditEnum.U.getOperationName(),
				new WatchlistTerm[]{
				    new WatchlistTerm( 
				      EntityEnum.PASSENGER.getEntityName().toUpperCase(),
				      PassengerMapping.FIRST_NAME.getFieldName(), 
				      PassengerMapping.FIRST_NAME.getFieldType(), "Julius"),				      
					new WatchlistTerm( 
				      EntityEnum.PASSENGER.getEntityName().toUpperCase(),
				      PassengerMapping.LAST_NAME.getFieldName(), 
				      PassengerMapping.LAST_NAME.getFieldType(), "Seizure"),
					new WatchlistTerm( 
				      EntityEnum.PASSENGER.getEntityName().toUpperCase(),
				      PassengerMapping.DOB.getFieldName(), 
				      PassengerMapping.DOB.getFieldType(), "1966-09-13")
				}));
		ret.addWatchlistItem(
				new WatchlistItemSpec(25L, WatchlistEditEnum.D.getOperationName(), null));
		return ret;
	}

}
