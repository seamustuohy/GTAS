package gov.gtas.services.watchlist;

import gov.gtas.constant.CommonErrorConstants;
import gov.gtas.constant.WatchlistConstants;
import gov.gtas.enumtype.EntityEnum;
import gov.gtas.enumtype.WatchlistEditEnum;
import gov.gtas.error.ErrorHandler;
import gov.gtas.error.ErrorHandlerFactory;
import gov.gtas.model.User;
import gov.gtas.model.watchlist.Watchlist;
import gov.gtas.model.watchlist.WatchlistEditLog;
import gov.gtas.model.watchlist.WatchlistItem;
import gov.gtas.repository.watchlist.WatchlistItemRepository;
import gov.gtas.repository.watchlist.WatchlistLogRepository;
import gov.gtas.repository.watchlist.WatchlistRepository;
import gov.gtas.services.UserService;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * The back-end service for persisting watch lists.
 * 
 * @author GTAS3 (AB)
 *
 */
@Service
public class WatchlistPersistenceServiceImpl implements
		WatchlistPersistenceService {

	/*
	 * The logger for the WatchlistPersistenceServiceImpl.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(WatchlistPersistenceServiceImpl.class);

	// private static final int UPDATE_BATCH_SIZE = 100;

	@PersistenceContext
	private EntityManager entityManager;

	@Resource
	private WatchlistRepository watchlistRepository;

	@Resource
	private WatchlistItemRepository watchlistItemRepository;

	@Resource
	private WatchlistLogRepository watchlistLogRepository;

	@Autowired
	private UserService userService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gtas.services.watchlist.WatchlistPersistenceService#createOrUpdate
	 * (java.lang.String, java.lang.String, java.util.List, java.util.List,
	 * java.util.List, java.lang.String)
	 */
	@Override
	@Transactional
	public Watchlist createOrUpdate(String wlName, EntityEnum entity,
			List<WatchlistItem> createUpdateList,
			List<WatchlistItem> deleteList, String userId) {
		final User user = fetchUser(userId);
		Watchlist watchlist = watchlistRepository.getWatchlistByName(wlName);
		if (watchlist == null) {
			watchlist = new Watchlist(wlName, entity);
		}
		// set the audit fields
		watchlist.setEditTimestamp(new Date());
		watchlist.setWatchListEditor(user);
		watchlist = watchlistRepository.save(watchlist);
//		if (createUpdateList != null && createUpdateList.size() > 0) {
//			for (WatchlistItem item : createUpdateList) {
//				item.setWatchlist(watchlist);
//			}
//			watchlistItemRepository.save(createUpdateList);
//		}
//		if (deleteList != null && deleteList.size() > 0) {
//			watchlistItemRepository.delete(deleteList);
//		}
		doCrudWithLogging(watchlist, user, createUpdateList, deleteList);
		return watchlist;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gtas.services.watchlist.WatchlistPersistenceService#findWatchlistItems
	 * (java.lang.String)
	 */
	@Override
	public List<WatchlistItem> findWatchlistItems(String watchlistName) {
		return watchlistItemRepository.getItemsByWatchlistName(watchlistName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gtas.services.watchlist.WatchlistPersistenceService#findAllWatchlistItems
	 * ()
	 */
	@Override
	public Iterable<WatchlistItem> findAllWatchlistItems() {
		return watchlistItemRepository.findAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.gtas.services.watchlist.WatchlistPersistenceService#findAll()
	 */
	@Override
	public List<Watchlist> findAllSummary() {
		List<Object[]> summaryList = watchlistRepository
				.fetchWatchlistSummary();
		List<Watchlist> ret = new LinkedList<Watchlist>();
		for (Object[] line : summaryList) {
			ret.add(new Watchlist(line[0].toString(), (EntityEnum) line[1]));
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.gtas.services.watchlist.WatchlistPersistenceService#
	 * findUncompiledWatchlists()
	 */
//	@Override
//	public List<Watchlist> findUncompiledWatchlists() {
//		return watchlistRepository.fetchUncompiledWatchlists();
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gtas.services.watchlist.WatchlistPersistenceService#findByName(java
	 * .lang.String)
	 */
	@Override
	public Watchlist findByName(String name) {
		Watchlist wl = watchlistRepository.getWatchlistByName(name);
		if (wl == null) {
			throw ErrorHandlerFactory.getErrorHandler().createException(
					CommonErrorConstants.QUERY_RESULT_EMPTY_ERROR_CODE,
					"Watchlist", name);
		}
		return wl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gtas.services.watchlist.WatchlistPersistenceService#deleteWatchlist
	 * (java.lang.String)
	 */
	@Override
	@Transactional
	public Watchlist deleteWatchlist(String name) {
		Watchlist wl = null;
		List<WatchlistItem> childItems = watchlistItemRepository
				.getItemsByWatchlistName(name);
		if (CollectionUtils.isEmpty(childItems)) {
			wl = watchlistRepository.getWatchlistByName(name);
			if (wl != null) {
				watchlistRepository.delete(wl);
			} else {
				logger.warn("WatchlistPersistenceServiceImpl.deleteWatchlist - cannot delete watchlist since it does not exist:"
						+ name);
			}
		} else {
			throw ErrorHandlerFactory
					.getErrorHandler()
					.createException(
							WatchlistConstants.CANNOT_DELETE_NONEMPTY_WATCHLIST_ERROR_CODE,
							name);
		}
		return wl;
	}

	/* (non-Javadoc)
	 * @see gov.gtas.services.watchlist.WatchlistPersistenceService#findLogEntriesForWatchlist(java.lang.String)
	 */
	@Override
	public List<WatchlistEditLog> findLogEntriesForWatchlist(String watchlistName) {
		return watchlistLogRepository.getLogByWatchlistName(watchlistName);
	}

	private Collection<WatchlistEditLog> doCrudWithLogging(Watchlist watchlist,
			User editUser, Collection<WatchlistItem> createUpdateItems,
			Collection<WatchlistItem> deleteItems) {
		List<WatchlistEditLog> ret = new LinkedList<WatchlistEditLog>();
		if (createUpdateItems != null && createUpdateItems.size() > 0) {
			for (WatchlistItem item : createUpdateItems) {
				ret.add(new WatchlistEditLog(editUser, watchlist,
						item.getId() == null ? WatchlistEditEnum.C
								: WatchlistEditEnum.U, item.getItemData()));
				item.setWatchlist(watchlist);
			}
			watchlistItemRepository.save(createUpdateItems);
		}
		if (deleteItems != null && deleteItems.size() > 0) {
			Iterable<Long> itr = deleteItems.stream().map(itm -> itm.getId())
					.collect(Collectors.toList());
			Iterable<WatchlistItem> delItems = watchlistItemRepository
					.findAll(itr);
			for (WatchlistItem item : delItems) {
				ret.add(new WatchlistEditLog(editUser, watchlist,
						WatchlistEditEnum.D, item.getItemData()));
			}
			watchlistItemRepository.delete(deleteItems);
		}
		if(ret.size() > 0){
			watchlistLogRepository.save(ret);
		}
		return ret;
	}

	/**
	 * Fetches the user object and throws an unchecked exception if the user
	 * cannot be found.
	 * 
	 * @param userId
	 *            the ID of the user to fetch.
	 * @return the user fetched from the DB.
	 */
	private User fetchUser(final String userId) {
		final User user = userService.findById(userId);
		if (user == null) {
			ErrorHandler errorHandler = ErrorHandlerFactory.getErrorHandler();
			throw errorHandler.createException(
					CommonErrorConstants.INVALID_USER_ID_ERROR_CODE, userId);
		}
		return user;
	}

}
