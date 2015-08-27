package gov.gtas.svc;

import org.springframework.stereotype.Service;

import gov.gtas.model.udr.json.JsonServiceResponse;
import gov.gtas.model.watchlist.json.Watchlist;
/**
 * The Watch list service implementation.
 * @author GTAS3 (AB)
 *
 */
@Service
public class WatchlistServiceImpl implements WatchlistService {

	@Override
	public Watchlist fetchWatchlist(String userId, String entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonServiceResponse createWatchlist(String userId,
			Watchlist wlToCreate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonServiceResponse updateWatchlist(String userId,
			Watchlist wlToUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

}
