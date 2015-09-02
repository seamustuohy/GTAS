package gov.gtas.util;

import gov.gtas.enumtype.EntityEnum;
import gov.gtas.enumtype.WatchlistEditEnum;
import gov.gtas.model.watchlist.json.WatchlistItemSpec;
import gov.gtas.model.watchlist.json.WatchlistSpec;
import gov.gtas.model.watchlist.json.WatchlistTerm;
import gov.gtas.querybuilder.mappings.PassengerMapping;

/**
 * A builder pattern object for creating watch list objects programmatically.
 * 
 * @author GTAS3
 *
 */
public class SampleDataGenerator {
	/**
	 * Creates a sample watch list JSON object. (This is used for testing.)
	 * 
	 * @return watch list JSON object.
	 */
	public static WatchlistSpec createSampleWatchlist(String wlName) {
		WatchlistSpec ret = new WatchlistSpec(wlName,
				EntityEnum.PASSENGER.getEntityName().toUpperCase());
		ret.addWatchlistItem(new WatchlistItemSpec(null, WatchlistEditEnum.C
				.getOperationName(), new WatchlistTerm[] {
				new WatchlistTerm(EntityEnum.PASSENGER.getEntityName()
						.toUpperCase(), PassengerMapping.FIRST_NAME
						.getFieldName(), PassengerMapping.FIRST_NAME
						.getFieldType(), "John"),
				new WatchlistTerm(EntityEnum.PASSENGER.getEntityName()
						.toUpperCase(), PassengerMapping.LAST_NAME
						.getFieldName(), PassengerMapping.LAST_NAME
						.getFieldType(), "Jones"),
				new WatchlistTerm(EntityEnum.PASSENGER.getEntityName()
						.toUpperCase(), PassengerMapping.DOB.getFieldName(),
						PassengerMapping.DOB.getFieldType(), "1747-07-06") }));
		ret.addWatchlistItem(new WatchlistItemSpec(32L, WatchlistEditEnum.U
				.getOperationName(), new WatchlistTerm[] {
				new WatchlistTerm(EntityEnum.PASSENGER.getEntityName()
						.toUpperCase(), PassengerMapping.FIRST_NAME
						.getFieldName(), PassengerMapping.FIRST_NAME
						.getFieldType(), "Julius"),
				new WatchlistTerm(EntityEnum.PASSENGER.getEntityName()
						.toUpperCase(), PassengerMapping.LAST_NAME
						.getFieldName(), PassengerMapping.LAST_NAME
						.getFieldType(), "Seizure"),
				new WatchlistTerm(EntityEnum.PASSENGER.getEntityName()
						.toUpperCase(), PassengerMapping.DOB.getFieldName(),
						PassengerMapping.DOB.getFieldType(), "1966-09-13") }));
		ret.addWatchlistItem(new WatchlistItemSpec(25L, WatchlistEditEnum.D
				.getOperationName(), null));
		return ret;
	}
	public static WatchlistSpec newWlWith2Items(String wlName) {
		WatchlistSpec ret = new WatchlistSpec(wlName,
				EntityEnum.PASSENGER.getEntityName().toUpperCase());
		ret.addWatchlistItem(new WatchlistItemSpec(null, WatchlistEditEnum.C
				.getOperationName(), new WatchlistTerm[] {
				new WatchlistTerm(EntityEnum.PASSENGER.getEntityName()
						.toUpperCase(), PassengerMapping.FIRST_NAME
						.getFieldName(), PassengerMapping.FIRST_NAME
						.getFieldType(), "John"),
				new WatchlistTerm(EntityEnum.PASSENGER.getEntityName()
						.toUpperCase(), PassengerMapping.LAST_NAME
						.getFieldName(), PassengerMapping.LAST_NAME
						.getFieldType(), "Jones"),
				new WatchlistTerm(EntityEnum.PASSENGER.getEntityName()
						.toUpperCase(), PassengerMapping.DOB.getFieldName(),
						PassengerMapping.DOB.getFieldType(), "1747-07-06") }));
		ret.addWatchlistItem(new WatchlistItemSpec(32L, WatchlistEditEnum.C
				.getOperationName(), new WatchlistTerm[] {
				new WatchlistTerm(EntityEnum.PASSENGER.getEntityName()
						.toUpperCase(), PassengerMapping.FIRST_NAME
						.getFieldName(), PassengerMapping.FIRST_NAME
						.getFieldType(), "Julius"),
				new WatchlistTerm(EntityEnum.PASSENGER.getEntityName()
						.toUpperCase(), PassengerMapping.LAST_NAME
						.getFieldName(), PassengerMapping.LAST_NAME
						.getFieldType(), "Seizure"),
				new WatchlistTerm(EntityEnum.PASSENGER.getEntityName()
						.toUpperCase(), PassengerMapping.DOB.getFieldName(),
						PassengerMapping.DOB.getFieldType(), "1966-09-13") }));
		return ret;
	}

}
