package gov.gtas.services.watchlist;

import gov.gtas.enumtype.EntityEnum;
import gov.gtas.model.watchlist.Watchlist;
import gov.gtas.model.watchlist.WatchlistItem;

import java.util.List;

/**
 * The Persistence Layer service for Watch lists.
 * 
 * @author GTAS3 (AB)
 *
 */
public interface WatchlistPersistenceService {
	/**
	 * Creates or Updates a Watch List.
	 * 
	 * @param wlName
	 *            the name of the watch list object to persist in the DB.
	 * @param entity
	 *            the entity (e.g., PASSENGER) for the watch list.
	 * @param createUpdateList
	 *            the list of watch list items to be added or updated.
	 * @param updateList
	 *            the list of watch list items to be updated.
	 * @param deleteList
	 *            the list of watch list items to be deleted.
	 * @param user
	 *            the user persisting the rule (usually also the WL author.)
	 * @return the persisted watch list.
	 */
	public Watchlist createOrUpdate(String wlName, EntityEnum entity,
			List<WatchlistItem> createUpdateList, List<WatchlistItem> deleteList,
			String userId);

	/**
	 * Find and return the list of all watch lists.
	 * 
	 * @return list of all watch lists.
	 */
	public List<Watchlist> findAllSummary();

	/**
	 * Find and return the list of all watch list items.
	 * 
	 * @param watchlistName
	 *            the name of the watch list
	 * @return list of all watch list items.
	 */
	public List<WatchlistItem> findWatchlistItems(String watchlistName);

	/**
	 * Find and return the list of all watch list items for all watch lists.
	 * 
	 * @return list of all watch list items.
	 */
	public Iterable<WatchlistItem> findAllWatchlistItems();

	/**
	 * Find and return the list of all non-compiled watch lists.
	 * (Either the compile time stamp is null, or it is before the edit time stamp.)
	 * 
	 * @return list of all non-compiled watch lists.
	 */
	public List<Watchlist> findUncompiledWatchlists();

	/**
	 * Fetches a Watch list by its name. 
	 * 
	 * @param name
	 *            the name of the watch list to fetch.
	 * @return the fetched watch list or null.
	 */
	public Watchlist findByName(String name);

	/**
	 * Deletes a Watch list by its name. 
	 * (Note: this operation will throw an exception if the watch list contains items.)
	 * @param name
	 *            the name of the watch list to delete.
	 * @return the deleted watch list or null, if the watchlist could not be found.
	 */
	public Watchlist deleteWatchlist(String name);

}
