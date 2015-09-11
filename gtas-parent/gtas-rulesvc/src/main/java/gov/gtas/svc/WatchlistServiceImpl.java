package gov.gtas.svc;

import gov.gtas.constant.WatchlistConstants;
import gov.gtas.enumtype.EntityEnum;
import gov.gtas.error.ErrorHandler;
import gov.gtas.error.ErrorHandlerFactory;
import gov.gtas.error.WatchlistServiceErrorHandler;
import gov.gtas.json.JsonServiceResponse;
import gov.gtas.model.udr.KnowledgeBase;
import gov.gtas.model.watchlist.Watchlist;
import gov.gtas.model.watchlist.WatchlistItem;
import gov.gtas.model.watchlist.json.WatchlistSpec;
import gov.gtas.model.watchlist.json.validation.WatchlistValidationAdapter;
import gov.gtas.services.watchlist.WatchlistPersistenceService;
import gov.gtas.svc.util.WatchlistBuilder;
import gov.gtas.svc.util.WatchlistServiceJsonResponseHelper;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
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

	@PostConstruct
	private void initializeErrorHandler() {
		ErrorHandler errorHandler = new WatchlistServiceErrorHandler();
		ErrorHandlerFactory.registerErrorHandler(errorHandler);
	}

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
		WatchlistValidationAdapter.validateWatchlistSpec(wlToCreateUpdate);
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
	@Transactional
	public JsonServiceResponse activateAllWatchlists(String knowledgeBaseName) {
		Iterable<WatchlistItem> items = watchlistPersistenceService.findAllWatchlistItems();
		if(StringUtils.isEmpty(knowledgeBaseName)){
			knowledgeBaseName = WatchlistConstants.WL_KNOWLEDGE_BASE_NAME;
		}
		KnowledgeBase kb = ruleManagementService.createKnowledgeBaseFromWatchlistItems(knowledgeBaseName, items);
		return WatchlistServiceJsonResponseHelper.createKnowledBaseResponse(kb, null);
	}

	/* (non-Javadoc)
	 * @see gov.gtas.svc.WatchlistService#activateAllWatchlists()
	 */
	@Override
	@Transactional
	public JsonServiceResponse activateAllWatchlists() {
		return activateAllWatchlists(WatchlistConstants.WL_KNOWLEDGE_BASE_NAME);
	}

	/* (non-Javadoc)
	 * @see gov.gtas.svc.WatchlistService#deleteWatchlist(java.lang.String)
	 */
	@Override
	public JsonServiceResponse deleteWatchlist(String wlName) {
		Watchlist wl = watchlistPersistenceService.deleteWatchlist(wlName);
		if(wl != null){
			return WatchlistServiceJsonResponseHelper.createResponse(true, WatchlistConstants.DELETE_OP_NAME,
					wl);
		} else {
			return WatchlistServiceJsonResponseHelper.createResponse(false, WatchlistConstants.DELETE_OP_NAME,
					wl, "since it does not exist or has been deleted previously");
		}
	}


}
