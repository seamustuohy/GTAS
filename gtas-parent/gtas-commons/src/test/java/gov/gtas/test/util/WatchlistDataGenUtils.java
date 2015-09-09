package gov.gtas.test.util;

import gov.gtas.model.Role;
import gov.gtas.model.User;
import gov.gtas.model.watchlist.WatchlistItem;
import gov.gtas.services.UserService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Generates test data for rules domain objects.
 * 
 * @author GTAS3 (AB)
 *
 */
public class WatchlistDataGenUtils {
	public static final String TEST_WATCHLIST_PREFIX = "TestWL";

	public static final int TEST_ROLE1_ID = 1;
	public static final String TEST_ROLE1_DESCRIPTION = "admin";
	public static final String TEST_USER1_ID = "jpjones";

	public static final int TEST_ROLE2_ID = 99;
	public static final String TEST_ROLE2_DESCRIPTION = "readonly";
	public static final String TEST_USER2_ID = "pawnX";

	private UserService userService;

	public WatchlistDataGenUtils(UserService usrSvc) {
		this.userService = usrSvc;
	}

	public void initUserData() {
		try {
			Role role = new Role();
			User user = new User();

			role.setRoleDescription(TEST_ROLE1_DESCRIPTION);
			role.setRoleId(TEST_ROLE1_ID);
			user.setFirstName("JP");
			user.setLastName("Jones");
			user.setUserId(TEST_USER1_ID);
			user.setPassword("passsword");
			userService.create(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<WatchlistItem> createWatchlistItems(String[] jsonArray) {
		List<WatchlistItem> ret = new LinkedList<WatchlistItem>();
		for(String json:jsonArray){
			WatchlistItem item = new WatchlistItem();
			item.setItemData(json);
			ret.add(item);
		}
		return ret;
	}

}
