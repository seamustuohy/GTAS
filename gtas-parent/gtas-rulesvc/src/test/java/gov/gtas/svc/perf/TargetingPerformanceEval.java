package gov.gtas.svc.perf;

import gov.gtas.config.RuleServiceConfig;
import gov.gtas.enumtype.EntityEnum;
import gov.gtas.model.MessageStatus;
import gov.gtas.model.watchlist.json.WatchlistItemSpec;
import gov.gtas.model.watchlist.json.WatchlistSpec;
import gov.gtas.model.watchlist.json.WatchlistTerm;
import gov.gtas.querybuilder.mappings.DocumentMapping;
import gov.gtas.querybuilder.mappings.PassengerMapping;
import gov.gtas.repository.ApisMessageRepository;
import gov.gtas.services.watchlist.WatchlistPersistenceService;
import gov.gtas.svc.TargetingService;
import gov.gtas.svc.WatchlistService;
import gov.gtas.svc.util.RuleExecutionContext;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * Load tests for the Rule Engine using Watch list rules.
 * 
 * @author GTAS3 (AB)
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RuleServiceConfig.class)
@TransactionConfiguration(defaultRollback = false)
public class TargetingPerformanceEval {
	public static final String WL_AUTHOR = "bstygar";
	public static final String PASSENGER_WL_NAME = "PerfTest Passenger WL";
	public static final String DOCUMENT_WL_NAME = "PerfTest Document WL";
	
	public static final String[][] WL_DATA = {
		{"Jonathon", "Smith", "1964-07-12", "123456789"},
		{"Williamnevell", "Jones", "1964-07-12", "123456789"},
		{"Johnalva", "Wayne", "2012-07-12", "123456789"},
		{"Garrywilliam", "Cooper", "2015-07-12", "987654321"},
		{"Jonthon", "Smith", "1964-07-12", "548721687"},
		{"Jothon", "Smith", "1964-07-12", "159264375"},
		{"Jonaon", "Smith", "1964-07-12", "12356789"},
		{"Jothon", "Smith", "1964-07-12", "12346789"},
		{"Jonahon", "Smith", "1964-07-12", "12356789"},
		{"Jonathon", "Smith", "1964-07-12", "12346789"},
		{"Jonatn", "Smith", "1964-07-12", "12345689"},
		{"Jonaton", "Smith", "1964-07-12", "12345689"},
		{"Jonaton", "Smith", "1964-07-12", "12345789"},
		{"Jonaton", "Smith", "1964-07-12", "12345789"},
		{"Jonaon", "Smith", "1964-07-12", "12345789"}
		
	};

	@Autowired
	TargetingService targetingService;

	@Autowired
	WatchlistService watchlistService;

	@Autowired
	WatchlistPersistenceService watchlistPersistenceService;

	@Resource
	private ApisMessageRepository apisMessageRepository;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	private void generateWlRules(String wlName, EntityEnum entity,
			String[][] dataArr, int count) {
		for(int i = 0; i < count; ++i) {
			WatchlistSpec spec = new WatchlistSpec(wlName, entity.getEntityName());
			for (String[] data : dataArr) {
				spec.addWatchlistItem(createItem(entity, null, "create", data));
			}
			watchlistService.createOrUpdateWatchlist(WL_AUTHOR, spec);
		}
	}

	private WatchlistItemSpec createItem(EntityEnum entity, Long id,
			String action, String[] data) {
		WatchlistTerm[] terms = null;
		switch (entity) {
		case PASSENGER:
			terms = new WatchlistTerm[3];
			terms[0] = new WatchlistTerm(entity.getEntityName(),
					PassengerMapping.FIRST_NAME.getFieldName(),
					PassengerMapping.FIRST_NAME.getFieldType(), data[0]);
			terms[1] = new WatchlistTerm(entity.getEntityName(),
					PassengerMapping.LAST_NAME.getFieldName(),
					PassengerMapping.LAST_NAME.getFieldType(), data[1]);
			terms[2] = new WatchlistTerm(entity.getEntityName(),
					PassengerMapping.DOB.getFieldName(),
					PassengerMapping.DOB.getFieldType(), data[2]);
			break;
		case DOCUMENT:
			terms = new WatchlistTerm[1];
			terms[0] = new WatchlistTerm(entity.getEntityName(),
					DocumentMapping.DOCUMENT_NUMBER.getFieldName(),
					DocumentMapping.DOCUMENT_NUMBER.getFieldType(), data[3]);
		default:
			break;
		}
		return new WatchlistItemSpec(id, action, terms);
	}
	
	

	//@Test
	@Transactional
	public void genPerformanceData() {
		generateWlRules(PASSENGER_WL_NAME, EntityEnum.PASSENGER, WL_DATA, 100);
		generateWlRules(DOCUMENT_WL_NAME, EntityEnum.DOCUMENT, WL_DATA, 20);
		System.out.println("*****************************************************************");
		System.out.println("********************   GENERATION COMPLETE  *********************");
		System.out.println("*****************************************************************");
	}
    //@Test
	public void activatePerformanceData() {
		watchlistService.activateAllWatchlists();
		System.out.println("*****************************************************************");
		System.out.println("********************   ACTIVATION COMPLETE  *********************");
		System.out.println("*****************************************************************");
	}
	//@Test
	@Transactional
	public void runPerformance() {
		long start = System.currentTimeMillis();
		RuleExecutionContext ctx = targetingService.analyzeLoadedMessages(MessageStatus.LOADED, MessageStatus.LOADED, false);
		long elapsed = System.currentTimeMillis() - start;
		System.out.println(String.format("******* result count = %d, elapsed millis = %d", ctx.getTargetingResult().size(), elapsed));
	}
}
