package gov.gtas.services.watchlist;

import static gov.gtas.constant.AuditLogConstants.WATCHLIST_LOG_CREATE_MESSAGE;
import static gov.gtas.constant.AuditLogConstants.WATCHLIST_LOG_DELETE_MESSAGE;
import static gov.gtas.constant.AuditLogConstants.WATCHLIST_LOG_TARGET_PREFIX;
import static gov.gtas.constant.AuditLogConstants.WATCHLIST_LOG_TARGET_SUFFIX;
import static gov.gtas.constant.AuditLogConstants.WATCHLIST_LOG_UPDATE_MESSAGE;
import gov.gtas.constant.CommonErrorConstants;
import gov.gtas.constant.WatchlistConstants;
import gov.gtas.enumtype.AuditActionType;
import gov.gtas.enumtype.EntityEnum;
import gov.gtas.enumtype.Status;
import gov.gtas.error.ErrorHandler;
import gov.gtas.error.ErrorHandlerFactory;
import gov.gtas.model.AuditRecord;
import gov.gtas.model.User;
import gov.gtas.model.watchlist.Watchlist;
import gov.gtas.model.watchlist.WatchlistItem;
import gov.gtas.repository.AuditRecordRepository;
import gov.gtas.repository.watchlist.WatchlistItemRepository;
import gov.gtas.repository.watchlist.WatchlistRepository;
import gov.gtas.services.security.UserData;
import gov.gtas.services.security.UserService;
import gov.gtas.services.security.UserServiceUtil;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
	private AuditRecordRepository auditRecordRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private UserServiceUtil userServiceUtil;

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
		} else if (entity != watchlist.getWatchlistEntity()) {
			// existing watch list has a different entity than that specified in
			// the create/update operation
			ErrorHandlerFactory.createAndThrowException(CommonErrorConstants.INVALID_ARGUMENT_ERROR_CODE, "entity", "Update Watchlist");
		}
		// set the audit fields
		watchlist.setEditTimestamp(new Date());
		watchlist.setWatchListEditor(user);
		watchlist = watchlistRepository.save(watchlist);
		doCrudWithLogging(watchlist, user, createUpdateList, deleteList);
		return watchlist;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.gtas.services.watchlist.WatchlistPersistenceService#
	 * findWatchlistItems (java.lang.String)
	 */
	@Override
	public List<WatchlistItem> findWatchlistItems(String watchlistName) {
		return watchlistItemRepository.getItemsByWatchlistName(watchlistName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.gtas.services.watchlist.WatchlistPersistenceService#
	 * findAllWatchlistItems ()
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
	 * @see
	 * gov.gtas.services.watchlist.WatchlistPersistenceService#findByName(java
	 * .lang.String)
	 */
	@Override
	public Watchlist findByName(String name) {
		Watchlist wl = watchlistRepository.getWatchlistByName(name);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.gtas.services.watchlist.WatchlistPersistenceService#
	 * findLogEntriesForWatchlist(java.lang.String)
	 */
	@Override
	public List<AuditRecord> findLogEntriesForWatchlist(String watchlistName) {
		return auditRecordRepository.findByTarget(WATCHLIST_LOG_TARGET_PREFIX
				+ watchlistName + WATCHLIST_LOG_TARGET_SUFFIX);
	}

	private Collection<AuditRecord> doCrudWithLogging(Watchlist watchlist,
			User editUser, Collection<WatchlistItem> createUpdateItems,
			Collection<WatchlistItem> deleteItems) {
		List<AuditRecord> ret = new LinkedList<AuditRecord>();
		Map<Long, WatchlistItem> updateDeleteItemMap = null;
		if (createUpdateItems != null && createUpdateItems.size() > 0) {
			List<WatchlistItem> updList = new LinkedList<WatchlistItem>();
			for (WatchlistItem item : createUpdateItems) {
				if (item.getId() != null) {
					ret.add(new AuditRecord(AuditActionType.UPDATE_WL,
							WATCHLIST_LOG_TARGET_PREFIX
									+ watchlist.getWatchlistName()
									+ WATCHLIST_LOG_TARGET_SUFFIX,
							Status.SUCCESS, WATCHLIST_LOG_UPDATE_MESSAGE, item
									.getItemData(), editUser));
					updList.add(item);
				} else {
					ret.add(new AuditRecord(AuditActionType.CREATE_WL,
							WATCHLIST_LOG_TARGET_PREFIX
									+ watchlist.getWatchlistName()
									+ WATCHLIST_LOG_TARGET_SUFFIX,
							Status.SUCCESS, WATCHLIST_LOG_CREATE_MESSAGE, item
									.getItemData(), editUser));
				}
				item.setWatchlist(watchlist);
			}
			// add the delete items and validate using one query.
			if (deleteItems != null) {
				updList.addAll(deleteItems);
			}
			updateDeleteItemMap = validateItemsPresentInDb(updList);
			watchlistItemRepository.save(createUpdateItems);
		} else {
			updateDeleteItemMap = validateItemsPresentInDb(deleteItems);
		}
		if (deleteItems != null && deleteItems.size() > 0) {
			for (WatchlistItem item : deleteItems) {
				WatchlistItem itemToDelete = updateDeleteItemMap.get(item
						.getId());
				ret.add(new AuditRecord(AuditActionType.DELETE_WL,
						WATCHLIST_LOG_TARGET_PREFIX
								+ watchlist.getWatchlistName()
								+ WATCHLIST_LOG_TARGET_SUFFIX, Status.SUCCESS,
						WATCHLIST_LOG_DELETE_MESSAGE, itemToDelete
								.getItemData(), editUser));
			}
			watchlistItemRepository.delete(deleteItems);
		}
		if (ret.size() > 0) {
			auditRecordRepository.save(ret);
		}
		return ret;
	}

	private Map<Long, WatchlistItem> validateItemsPresentInDb(
			Collection<WatchlistItem> targetItems) {
		Map<Long, WatchlistItem> ret = new HashMap<Long, WatchlistItem>();
		if (targetItems != null && targetItems.size() > 0) {
			List<Long> lst = targetItems.stream().map(itm -> itm.getId())
					.collect(Collectors.toList());
			Iterable<WatchlistItem> items = watchlistItemRepository
					.findAll(lst);
			int itemCount = 0;
			for (WatchlistItem itm : items) {
				ret.put(itm.getId(), itm);
				++itemCount;
			}
			if (targetItems.size() != itemCount) {
				throw ErrorHandlerFactory
						.getErrorHandler()
						.createException(
								WatchlistConstants.MISSING_DELETE_OR_UPDATE_ITEM_ERROR_CODE);
			}
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

		UserData userData = userService.findById(userId);
		final User user = userServiceUtil.mapUserEntityFromUserData(userData);
		if (user.getUserId() == null) {
			ErrorHandler errorHandler = ErrorHandlerFactory.getErrorHandler();
			throw errorHandler.createException(
					CommonErrorConstants.INVALID_USER_ID_ERROR_CODE, userId);
		}
		return user;
	}
}
