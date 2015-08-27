package gov.gtas.svc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import gov.gtas.enumtype.EntityEnum;
import gov.gtas.model.udr.json.JsonServiceResponse;
import gov.gtas.model.watchlist.Watchlist;
import gov.gtas.model.watchlist.WatchlistItem;
import gov.gtas.model.watchlist.json.WatchlistSpec;
import gov.gtas.model.watchlist.json.WatchlistItemSpec;
import gov.gtas.model.watchlist.util.WatchlistBuilder;
import gov.gtas.services.watchlist.WatchlistPersistenceService;
/**
 * The Watch list service implementation.
 * @author GTAS3 (AB)
 *
 */
@Service
public class WatchlistServiceImpl implements WatchlistService {
	
	@Autowired
	private WatchlistPersistenceService watchlistPersistenceService;

	/* (non-Javadoc)
	 * @see gov.gtas.svc.WatchlistService#fetchWatchlist(java.lang.String)
	 */
	@Override
	public WatchlistSpec fetchWatchlist(String wlName) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see gov.gtas.svc.WatchlistService#createOrUpdateWatchlist(java.lang.String, gov.gtas.model.watchlist.json.Watchlist)
	 */
	@Override
	public JsonServiceResponse createOrUpdateWatchlist(String userId,
			WatchlistSpec wlToCreateUpdate) {
		
		WatchlistBuilder bldr = new WatchlistBuilder(wlToCreateUpdate);
		try{
		    bldr.buildPersistenceLists();
		} catch(JsonProcessingException jpe){
			jpe.printStackTrace();
			return null;
		}
		
		final String wlName = bldr.getName();
		final EntityEnum entity = bldr.getEntity();
		List<WatchlistItem> createUpdateList = bldr.getCreateUpdateList();
		List<WatchlistItem> deleteList = bldr.getDeleteList();
		Watchlist wl = watchlistPersistenceService.createOrUpdate(wlName, entity, createUpdateList, deleteList, userId);
		return WatchlistServiceJsonResponseHelper.createResponse(true, "Create/Update", wl);
	}

	/* (non-Javadoc)
	 * @see gov.gtas.svc.WatchlistService#fetchAllWatchlists()
	 */
	@Override
	public List<WatchlistSpec> fetchAllWatchlists() {
		// TODO Auto-generated method stub
		return null;
	}


}
