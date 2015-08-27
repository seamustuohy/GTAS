package gov.gtas.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import gov.gtas.config.CommonServicesConfig;
import gov.gtas.enumtype.EntityEnum;
import gov.gtas.model.User;
import gov.gtas.model.watchlist.Watchlist;
import gov.gtas.model.watchlist.WatchlistItem;
import gov.gtas.services.watchlist.WatchlistPersistenceService;
import gov.gtas.test.util.WatchlistDataGenUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
/**
 * Persistence layer tests for Watch list.
 * @author GTAS3 (AB)
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CommonServicesConfig.class)
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback = true)
public class WatchlistPersistenceServiceIT {
    private static final String TEST_WL_NAME = "Foo Knowledge Base";
    private static final EntityEnum TEST_WL_ENTITY = EntityEnum.PASSENGER;
    private static final String[] TEST_WL_ITEMS1 = new String[]{
    	"Test Item 1",
    	"Test Item 2",
    	"Test Item 3",
    	"Test Item 4"
    };
    private static final String[] TEST_WL_ITEMS2 = new String[]{
    	"Test Item 5",
    	"Test Item 6"
    };
 
	@Autowired
	private WatchlistPersistenceService testTarget;
	@Autowired
	private UserService userService;

	private WatchlistDataGenUtils testGenUtils;
	
	@Before
	public void setUp() throws Exception {
		testGenUtils = new WatchlistDataGenUtils(userService);
		testGenUtils.initUserData();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Transactional
	@Test()
	public void testCreateWatchlist() {
		List<WatchlistItem> createList = testGenUtils.createWatchlistItems(TEST_WL_ITEMS1);
		Watchlist wl = testTarget.createOrUpdate(TEST_WL_NAME, TEST_WL_ENTITY, createList, null, WatchlistDataGenUtils.TEST_USER1_ID);
		assertNotNull(wl);
		assertNotNull(wl.getId());
		assertNotNull(wl.getEditTimestamp());
		assertEquals(TEST_WL_NAME, wl.getWatchlistName());
		assertEquals(TEST_WL_ENTITY, wl.getWatchlistEntity());
		User editor = wl.getWatchListEditor();
		assertEquals(WatchlistDataGenUtils.TEST_USER1_ID, editor.getUserId());
		List<WatchlistItem> items = testTarget.findWatchlistItems(wl.getWatchlistName());
		assertNotNull(items);
		assertEquals(4, items.size());
		for(WatchlistItem item:items){
			assertNotNull(item.getId());
			assertNotNull(item.getItemData());
			assertNull(item.getItemRuleData());
			assertNotNull(item.getWatchlist());
		}
	}
	
	@Transactional
	@Test()
	public void testUpdateWatchlist() {
		List<WatchlistItem> createList = testGenUtils.createWatchlistItems(TEST_WL_ITEMS1);
		List<WatchlistItem> addList = testGenUtils.createWatchlistItems(TEST_WL_ITEMS2);
		Watchlist wl = testTarget.createOrUpdate(TEST_WL_NAME, TEST_WL_ENTITY, createList, null, WatchlistDataGenUtils.TEST_USER1_ID);
		assertNotNull(wl);
		List<WatchlistItem> items = testTarget.findWatchlistItems(wl.getWatchlistName());
		Map<String, Long> jsonMap = new HashMap<>();
		for(WatchlistItem item:items){
			String update = item.getItemData() + "-update";
			item.setItemData(update);
			jsonMap.put(update, item.getId());			
		}
		items.addAll(addList);
		testTarget.createOrUpdate(wl.getWatchlistName(), wl.getWatchlistEntity(), items, null, WatchlistDataGenUtils.TEST_USER1_ID);
		items = testTarget.findWatchlistItems(wl.getWatchlistName());
		assertNotNull(items);
		assertEquals(6, items.size());
		int updateCount = 0;
		for(WatchlistItem item:items){
			Long id = item.getId();
			assertNotNull(id);
			assertNotNull(item.getItemData());
			assertNull(item.getItemRuleData());
			assertNotNull(item.getWatchlist());
			//check updates
			Long upd = jsonMap.get(item.getItemData());
			if(upd != null){
				++updateCount;
			}
		}
		assertEquals(4, updateCount);	
	}
	@Transactional
	@Test()
	public void testDeleteWatchlist() {
		List<WatchlistItem> createList = testGenUtils.createWatchlistItems(TEST_WL_ITEMS1);
		Watchlist wl = testTarget.createOrUpdate(TEST_WL_NAME, TEST_WL_ENTITY, createList, null, WatchlistDataGenUtils.TEST_USER1_ID);
		assertNotNull(wl);
		List<WatchlistItem> items = testTarget.findWatchlistItems(wl.getWatchlistName());
		assertEquals(4, items.size());
		List<WatchlistItem> deleteItems = new LinkedList<WatchlistItem>();
		String deldata1 = addDeleteItem(deleteItems, items.get(0));
		String deldata2 = addDeleteItem(deleteItems, items.get(3));
		testTarget.createOrUpdate(wl.getWatchlistName(), wl.getWatchlistEntity(), null, deleteItems, WatchlistDataGenUtils.TEST_USER1_ID);
		items = testTarget.findWatchlistItems(wl.getWatchlistName());
		assertNotNull(items);
		assertEquals(2, items.size());
		WatchlistItem item = items.get(0);
		assertTrue(!item.getItemData().equalsIgnoreCase(deldata1) || !item.getItemData().equalsIgnoreCase(deldata2));
		item = items.get(1);
		assertTrue(!item.getItemData().equalsIgnoreCase(deldata1) || !item.getItemData().equalsIgnoreCase(deldata2));
	}

	@Transactional
	@Test()
	public void testWatchlistSummary() {
		List<WatchlistItem> createList = testGenUtils.createWatchlistItems(TEST_WL_ITEMS1);
		Watchlist wl = testTarget.createOrUpdate(TEST_WL_NAME, TEST_WL_ENTITY, createList, null, WatchlistDataGenUtils.TEST_USER1_ID);
		assertNotNull(wl);
		List<Watchlist> summaryList = testTarget.findAllSummary();
		assertEquals(1, summaryList.size());
		wl = summaryList.get(0);
		assertEquals(TEST_WL_NAME, wl.getWatchlistName());
		assertEquals(TEST_WL_ENTITY, wl.getWatchlistEntity());		
	}
private String addDeleteItem(List<WatchlistItem> delList, WatchlistItem item){
		String deldata = item.getItemData();
		WatchlistItem delItem = new WatchlistItem();
		delItem.setId(item.getId());
		delList.add(delItem);
		return deldata;
	}
}
;