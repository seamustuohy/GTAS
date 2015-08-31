package gov.gtas.svc;

import gov.gtas.enumtype.EntityEnum;
import gov.gtas.model.udr.KnowledgeBase;
import gov.gtas.model.udr.json.JsonServiceResponse;
import gov.gtas.model.watchlist.Watchlist;
import gov.gtas.model.watchlist.WatchlistItem;
import gov.gtas.model.watchlist.json.WatchlistSpec;
import gov.gtas.services.watchlist.WatchlistPersistenceService;
import gov.gtas.svc.util.WatchlistBuilder;
import gov.gtas.svc.util.WatchlistServiceJsonResponseHelper;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * The Watch list service implementation.
 * @author GTAS3 (AB)
 *
 */
@Service
public class WatchlistServiceImpl implements WatchlistService {
	
	@Autowired
	private WatchlistPersistenceService watchlistPersistenceService;
	@Autowired
	private RuleManagementService ruleManagementService;

	/* (non-Javadoc)
	 * @see gov.gtas.svc.WatchlistService#fetchWatchlist(java.lang.String)
	 */
	@Override
	public WatchlistSpec fetchWatchlist(String wlName) {
		WatchlistSpec ret = null;
		Watchlist wl = watchlistPersistenceService.findByName(wlName);
		if(wl != null){
		    List<WatchlistItem> items = watchlistPersistenceService.findWatchlistItems(wlName);
		    WatchlistBuilder bldr = new WatchlistBuilder(wl, items);
		    ret = bldr.buildWatchlistSpec();
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see gov.gtas.svc.WatchlistService#createOrUpdateWatchlist(java.lang.String, gov.gtas.model.watchlist.json.Watchlist)
	 */
	@Override
	public JsonServiceResponse createOrUpdateWatchlist(String userId,
			WatchlistSpec wlToCreateUpdate) {
		
		WatchlistBuilder bldr = new WatchlistBuilder(wlToCreateUpdate);
		bldr.buildPersistenceLists();
		
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
		List<Watchlist> summary = watchlistPersistenceService.findAllSummary();
		List<WatchlistSpec> ret = new LinkedList<WatchlistSpec>();
		for(Watchlist wl:summary){
			ret.add(new WatchlistSpec(wl.getWatchlistName(), wl.getWatchlistEntity().getEntityName()));
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see gov.gtas.svc.WatchlistService#activateAllWatchlists(java.lang.String)
	 */
	@Override
	public JsonServiceResponse activateAllWatchlists(String knowledgeBaseName) {
		Iterable<WatchlistItem> items = watchlistPersistenceService.findAllWatchlistItems();
		KnowledgeBase kb = ruleManagementService.createKnowledgeBaseFromWatchlistItems(knowledgeBaseName, items);
		return WatchlistServiceJsonResponseHelper.createKnowledBaseResponse(kb, null);
	}


}
