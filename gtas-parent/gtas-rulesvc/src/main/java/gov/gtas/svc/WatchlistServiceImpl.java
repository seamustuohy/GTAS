package gov.gtas.svc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.gtas.enumtype.EntityEnum;
import gov.gtas.model.udr.json.JsonServiceResponse;
import gov.gtas.model.watchlist.json.WatchlistSpec;
import gov.gtas.model.watchlist.json.WatchlistItemSpec;
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
		final String wlName = wlToCreateUpdate.getName();
		final EntityEnum entity = EntityEnum.getEnum(wlToCreateUpdate.getEntity());
		final List<WatchlistItemSpec> items = wlToCreateUpdate.getWatchlistItems();
		return null;
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
