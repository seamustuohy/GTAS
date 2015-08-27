package gov.gtas.model.watchlist.util;

import gov.gtas.enumtype.EntityEnum;
import gov.gtas.enumtype.WatchlistEditEnum;
import gov.gtas.model.watchlist.json.Watchlist;
import gov.gtas.model.watchlist.json.WatchlistItem;
import gov.gtas.model.watchlist.json.WatchlistTerm;
import gov.gtas.querybuilder.mappings.PassengerMapping;

/**
 * A builder pattern object for creating watch list objects programmatically.
 * 
 * @author GTAS3
 *
 */
public class WatchlistBuilder {

	/**
	 * Creates a sample UDR specification JSON object. (This is used for
	 * testing.)
	 * 
	 * @param userId
	 * @param title
	 * @param description
	 * @return
	 */
	public static Watchlist createSampleWatchlist() {
		Watchlist ret = new Watchlist(EntityEnum.PASSENGER.getEntityName().toUpperCase());
		ret.addWatchlistItem(
				new WatchlistItem(WatchlistEditEnum.C.getOperationName(),
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
				new WatchlistItem(WatchlistEditEnum.U.getOperationName(),
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
				new WatchlistItem(WatchlistEditEnum.D.getOperationName(),
				new WatchlistTerm[]{
				    new WatchlistTerm( 
				      EntityEnum.PASSENGER.getEntityName().toUpperCase(),
				      PassengerMapping.FIRST_NAME.getFieldName(), 
				      PassengerMapping.FIRST_NAME.getFieldType(), "The"),				      
					new WatchlistTerm( 
				      EntityEnum.PASSENGER.getEntityName().toUpperCase(),
				      PassengerMapping.LAST_NAME.getFieldName(), 
				      PassengerMapping.LAST_NAME.getFieldType(), "Donald"),
					new WatchlistTerm( 
				      EntityEnum.PASSENGER.getEntityName().toUpperCase(),
				      PassengerMapping.DOB.getFieldName(), 
				      PassengerMapping.DOB.getFieldType(), "1957-04-01")
				}));
		return ret;
	}

}
