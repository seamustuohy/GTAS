package gov.gtas.svc;

import gov.gtas.model.udr.json.JsonServiceResponse;
import gov.gtas.model.watchlist.json.Watchlist;

/**
 * The service interface for managing User Defined Rules (UDR).<br>
 * 1. CRUD on UDR.<br>
 * 2. Generation of Drools Rules and creation of versioned Knowledge Base.
 * @author GTAS3 (AB)
 *
 */
public interface WatchlistService {
	/**
	 * Retrieves the UDR domain object from the DB and converts it to the corresponding JSON object.
	 * @param userId the userId of the author.
	 * @param entity the title of the UDR.
	 * @return the JSON UDR object.
	 */
   Watchlist fetchWatchlist(String userId, String entity);
  /**
   * Creates a new UDR object in the database and returns it in JSON object format.
   * @param userId the userId of the author.
   * @param udrToCreate the JSON UDR object to be inserted into tte DB.
   * @return the service response JSON format.
   */
  JsonServiceResponse createWatchlist(String userId, Watchlist wlToCreate);
  /**
   * Updates the UDR by replacing the UDR object in the DB with the same ID.
   * @param userId the userId of the author.
   * @param udrToUpdate the updated object image to use for replacing the DB object.
   * @return the updated object.
   */
  JsonServiceResponse updateWatchlist(String userId, Watchlist wlToUpdate);
}
