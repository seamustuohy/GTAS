package gov.gtas.services;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import gov.gtas.config.CommonServicesConfig;
import gov.gtas.services.Filter.FilterData;
import gov.gtas.services.security.RoleData;
import gov.gtas.services.security.RoleService;
import gov.gtas.services.security.RoleServiceUtil;
import gov.gtas.services.security.UserData;
import gov.gtas.services.security.UserService;
import gov.gtas.services.security.UserServiceUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CommonServicesConfig.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceIT {

	private static final String USER_ID = "iTest";
	private static final String FIRST_NAME = "Integration";
	private static final String LAST_NAME = "test";
	private static final String PASSWORD = "$2a$10$VZaP2o9djsabv2x3DCjK.e8TRSNyjb972M9k4rtXlUAc4J0AEm7.C";
	private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	private static final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

	@Autowired
	UserService userService;

	@Autowired
	UserServiceUtil userServiceUtil;

	@Autowired
	RoleService roleService;

	@Autowired
	RoleServiceUtil roleServiceUtil;

	Set<RoleData> roles;

	@Before
	public void setUp() throws Exception {
		roles = roleService.findAll();

	}

	@After
	public void tearDown() throws Exception {
		// userService.delete(USER_ID);
	}

	@Test
	public void testGetAllUser() {
		// TBD
		List<UserData> users = userService.findAll();
		users.forEach(r -> r.getRoles().forEach(role -> System.out.println(role.getRoleDescription())));

	}

	@Test
	public void testGetSpecifUser() {

		UserData user = userService.findById("bstygar");
		user.getRoles().forEach(r -> System.out.println(r.getRoleDescription()));
	}

	@Test
	public void testCreateUserWithRoles() {
		// Arrange

		Stream<RoleData> streamRoles = roles.stream().filter(r -> r.getRoleId() == 2);
		Set<RoleData> authRoles = streamRoles.collect(Collectors.toSet());

		System.out.println(authRoles);
		UserData expectedUser = new UserData("iTest99", PASSWORD, "test", "99", 1, authRoles, null);

		UserData actualUser = null;
		// Act
		try {
			actualUser = userService.create(expectedUser);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Assert
		assertEquals(expectedUser, actualUser);
	}

	@Test
	public void testCreateUserWithRolesAndFilter() {
		// Arrange

		Stream<RoleData> streamRoles = roles.stream().filter(r -> r.getRoleId() == 2 || r.getRoleId() == 7);
		Set<RoleData> authRoles = streamRoles.collect(Collectors.toSet());

		Set<String> originAirports = new HashSet<String>();

		originAirports.add("HFN");
		originAirports.add("HZK");
		originAirports.add("IFJ");

		Set<String> destinationAirports = new HashSet<String>();
		destinationAirports.add("KEF");
		destinationAirports.add("PFJ");
		destinationAirports.add("RKV");
		Date etaStart = new Date();
		Date etaEnd = DateUtils.addDays(etaStart, 30);
		Date etdStart = DateUtils.addDays(etaStart, 0);
		Date etdEnd = DateUtils.addDays(etaStart, 15);

		FilterData filter = new FilterData("iTest99", "I", originAirports, destinationAirports, etaStart, etaEnd,
				etdStart, etdEnd);

		UserData expectedUser = new UserData("iTest99", PASSWORD, "test", "99", 1, authRoles, filter);

		UserData actualUser = null;
		// Act
		try {
			actualUser = userService.create(expectedUser);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Assert
		assertEquals(expectedUser, actualUser);
	}

	@Test
	public void testUpdateUserWithOutFilters() {
		// Arrange

		Stream<RoleData> streamRoles = roles.stream().filter(r -> r.getRoleId() == 3 || r.getRoleId() == 2);
		Set<RoleData> authRoles = streamRoles.collect(Collectors.toSet());

		System.out.println(authRoles);
		UserData expectedUser = new UserData("iTest99", PASSWORD, "test", "99", 1, authRoles, null);

		UserData actualUser = null;
		// Act
		try {
			actualUser = userService.update(expectedUser);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Assert
		assertEquals(expectedUser, actualUser);
	}

	@Test
	public void testFinduser() {
		// Arrange
		String userId = "iTest2";

		// Act
		UserData actualUser = userService.findById(userId);

		// Assert
	}

	@Test
	public void testUpdateUserWithFilters() {
		// Arrange

		Stream<RoleData> streamRoles = roles.stream().filter(r -> r.getRoleId() == 1 || r.getRoleId() == 2);
		Set<RoleData> authRoles = streamRoles.collect(Collectors.toSet());
		Set<String> originAirports = new HashSet<String>();

		originAirports.add("GKA");

		Set<String> destinationAirports = new HashSet<String>();
		destinationAirports.add("LAE");
		Date etaStart = new Date();
		Date etaEnd = DateUtils.addDays(etaStart, 3);
		Date etdStart = new Date();
		Date etdEnd = DateUtils.addDays(etaStart, 13);

		FilterData filterData = new FilterData("iTest99", "O", originAirports, destinationAirports, etaStart, etaEnd,
				etdStart, etdEnd);

		System.out.println(authRoles);
		UserData expectedUser = new UserData(USER_ID, PASSWORD, "test", "99", 1, authRoles, filterData);

		UserData actualUser = null;
		// Act
		try {
			actualUser = userService.update(expectedUser);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Assert
		assertEquals(expectedUser, actualUser);
	}

}
