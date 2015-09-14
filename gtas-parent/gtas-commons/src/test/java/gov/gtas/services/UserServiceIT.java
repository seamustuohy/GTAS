package gov.gtas.services;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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

	public static final String USER_ID = "iTest";
	public static final String FIRST_NAME = "Integration";
	public static final String LAST_NAME = "test";
	public static final String PASSWORD = "$2a$10$T5xL/wIet8ev/RhqM5iDTOrqRwIbO7IkQ/DWkDe592zaBaZy8RAxu";

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

		UserData user = userService.findById("VTAMMINENI");
		user.getRoles().forEach(r -> System.out.println(r.getRoleDescription()));
	}

	@Test
	public void testCreateUser() {
		// Arrange

		Stream<RoleData> streamRoles = roles.stream().filter(r -> r.getRoleId() == 2 || r.getRoleId() == 7);
		Set<RoleData> authRoles = streamRoles.collect(Collectors.toSet());

		System.out.println(authRoles);
		UserData expectedUser = new UserData(USER_ID, PASSWORD, FIRST_NAME, LAST_NAME, 1, authRoles);

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
	public void testUpdateUser() {
		// Arrange

		Stream<RoleData> streamRoles = roles.stream().filter(r -> r.getRoleId() == 7 || r.getRoleId() == 4);
		Set<RoleData> authRoles = streamRoles.collect(Collectors.toSet());

		System.out.println(authRoles);
		UserData expectedUser = new UserData(USER_ID, PASSWORD, FIRST_NAME, LAST_NAME, 1, authRoles);

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
