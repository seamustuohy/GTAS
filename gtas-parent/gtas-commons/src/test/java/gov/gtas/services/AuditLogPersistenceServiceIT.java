/*
 * All GTAS code is Copyright 2016, Unisys Corporation.
 * 
 * Please see LICENSE.txt for details.
 */
package gov.gtas.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import gov.gtas.config.CachingConfig;
import gov.gtas.config.CommonServicesConfig;
import gov.gtas.enumtype.AuditActionType;
import gov.gtas.enumtype.Status;
import gov.gtas.json.AuditActionData;
import gov.gtas.json.AuditActionTarget;
import gov.gtas.model.AuditRecord;
import gov.gtas.model.User;
import gov.gtas.services.security.UserService;
import gov.gtas.services.security.UserServiceUtil;
import gov.gtas.test.util.TestUtils;

import java.util.Date;
import java.util.List;

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
 * Persistence layer tests for Audit Logging.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CommonServicesConfig.class,
        CachingConfig.class })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class AuditLogPersistenceServiceIT {

    @Autowired
    private AuditLogPersistenceService testTarget;
    @Autowired
    private UserService userService;
    @Autowired
    private UserServiceUtil userServiceUtil;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Transactional
    @Test()
    public void testCreateFetchAuditLog() {
        TestUtils.insertAdminUser(userService, "jpjones", "password",
                "firstName", "lastName");
        User user = TestUtils
                .fetchUser(userService, userServiceUtil, "jpjones");
        testTarget.create(new AuditRecord(AuditActionType.CREATE_UDR,
                "UDR_TITLE", user));
        List<AuditRecord> recList = testTarget.findByUser("jpjones");
        assertNotNull(recList);
        assertEquals(1, recList.size());
        AuditRecord rec = recList.get(0);
        assertEquals(AuditActionType.CREATE_UDR, rec.getActionType());
        assertEquals(Status.SUCCESS, rec.getActionStatus());
        assertEquals("UDR_TITLE", rec.getTarget());
        assertNull(rec.getActionData());

    }

    @Transactional
    @Test()
    public void testCreateFetchAuditLogWithData() {
        TestUtils.insertAdminUser(userService, "jpjones", "password",
                "firstName", "lastName");
        User user = TestUtils
                .fetchUser(userService, userServiceUtil, "jpjones");
        testTarget.create(new AuditRecord(AuditActionType.CREATE_UDR,
                "UDR_TITLE", Status.SUCCESS, "Creating UDR",
                "{jhhgjghgkjhgkjh}", user));
        testTarget.create(new AuditRecord(AuditActionType.CREATE_WL, "WL_NAME",
                Status.SUCCESS_WITH_WARNING, "Creating WL", null, user));

        List<AuditRecord> recList = testTarget.findByUser("jpjones");
        assertNotNull(recList);
        assertEquals(2, recList.size());

        recList = testTarget.findByUserAndActionType(
                AuditActionType.CREATE_UDR, "jpjones");
        assertEquals(1, recList.size());
        AuditRecord rec = recList.get(0);
        assertEquals(AuditActionType.CREATE_UDR, rec.getActionType());
        assertEquals(Status.SUCCESS, rec.getActionStatus());
        assertEquals("UDR_TITLE", rec.getTarget());
        assertEquals("Creating UDR", rec.getMessage());
        assertEquals("{jhhgjghgkjhgkjh}", rec.getActionData());

        recList = testTarget.findByUserAndActionType(AuditActionType.CREATE_WL,
                "jpjones");
        assertEquals(1, recList.size());
        rec = recList.get(0);
        assertEquals(AuditActionType.CREATE_WL, rec.getActionType());
        assertEquals("Creating WL", rec.getMessage());
        assertEquals(Status.SUCCESS_WITH_WARNING, rec.getActionStatus());
        assertEquals("WL_NAME", rec.getTarget());
        assertNull(rec.getActionData());
    }

    @Transactional
    @Test()
    public void testCreateFetchAuditLogWithDataObjects() throws Exception {
        TestUtils.insertAdminUser(userService, "jpjones", "password",
                "firstName", "lastName");
        User user = TestUtils
                .fetchUser(userService, userServiceUtil, "jpjones");
        AuditActionTarget target = new AuditActionTarget(
                AuditActionType.CREATE_UDR, "UDR_TITLE", null);
        AuditActionData data = new AuditActionData();
        data.addProperty("foo", "bar");
        data.addProperty("foo2", "bar2");
        testTarget.create(AuditActionType.CREATE_UDR, target, data,
                "Creating UDR", user.getUserId());

        List<AuditRecord> recList = testTarget.findByUser("jpjones");
        assertNotNull(recList);
        assertEquals(1, recList.size());

        recList = testTarget.findByUserAndActionType(
                AuditActionType.CREATE_UDR, "jpjones");
        assertEquals(1, recList.size());
        AuditRecord rec = recList.get(0);
        assertEquals(AuditActionType.CREATE_UDR, rec.getActionType());
        assertEquals(Status.SUCCESS, rec.getActionStatus());
        assertEquals("{\"type\":\"UDR\",\"name\":\"UDR_TITLE\",\"id\":\"\"}",
                rec.getTarget());
        assertEquals("Creating UDR", rec.getMessage());
        String actionData = rec.getActionData();
        assertNotNull(actionData);
        assertEquals(
                "[{\"name\":\"foo\",\"value\":\"bar\"},{\"name\":\"foo2\",\"value\":\"bar2\"}]",
                actionData);
    }

    @Transactional
    @Test()
    public void testCreateFetchAuditLogWithQueryByActionType() {
        TestUtils.insertAdminUser(userService, "jpjones", "password",
                "firstName", "lastName");
        User user = TestUtils
                .fetchUser(userService, userServiceUtil, "jpjones");
        testTarget.create(new AuditRecord(AuditActionType.CREATE_UDR,
                "UDR_TITLE", user));
        testTarget.create(new AuditRecord(AuditActionType.CREATE_WL, "WL_NAME",
                Status.SUCCESS_WITH_WARNING, null, null, user));
        testTarget.create(new AuditRecord(AuditActionType.CREATE_WL,
                "WL_NAME2", Status.SUCCESS_WITH_WARNING, null, null, user));
        testTarget.create(new AuditRecord(AuditActionType.CREATE_WL,
                "WL_NAME3", Status.SUCCESS_WITH_WARNING, null, null, user));

        List<AuditRecord> recList = testTarget.findByUser("jpjones");
        assertNotNull(recList);
        assertEquals(4, recList.size());

        recList = testTarget.findByUserAndActionType(
                AuditActionType.CREATE_UDR, "jpjones");
        assertEquals(1, recList.size());
        AuditRecord rec = recList.get(0);
        assertEquals(AuditActionType.CREATE_UDR, rec.getActionType());
        assertEquals(Status.SUCCESS, rec.getActionStatus());
        assertEquals("UDR_TITLE", rec.getTarget());
        assertNull(rec.getActionData());

        recList = testTarget.findByUserAndActionType(AuditActionType.CREATE_WL,
                "jpjones");
        assertEquals(3, recList.size());
    }

    @Transactional
    @Test()
    public void testCreateFetchAuditLogByDate() {
        TestUtils.insertAdminUser(userService, "nimitz", "password",
                "firstName", "lastName");
        TestUtils.insertAdminUser(userService, "jpjones", "password",
                "firstName", "lastName");
        User user = TestUtils
                .fetchUser(userService, userServiceUtil, "jpjones");
        User user2 = TestUtils
                .fetchUser(userService, userServiceUtil, "nimitz");
        Date date1 = new Date();
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        testTarget.create(new AuditRecord(AuditActionType.CREATE_UDR,
                "UDR_TITLE", user2));
        testTarget.create(new AuditRecord(AuditActionType.CREATE_WL, "WL_NAME",
                Status.SUCCESS_WITH_WARNING, null, null, user2));
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        Date date2 = new Date();
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        testTarget.create(new AuditRecord(AuditActionType.CREATE_WL,
                "WL_NAME2", Status.SUCCESS_WITH_WARNING, null, null, user));
        testTarget.create(new AuditRecord(AuditActionType.CREATE_WL,
                "WL_NAME3", Status.SUCCESS_WITH_WARNING, null, null, user));

        List<AuditRecord> recList = testTarget.findByDateFrom(date1);
        assertNotNull(recList);
        assertEquals(4, recList.size());

        recList = testTarget.findByDateRange(date1, date2);
        assertEquals(2, recList.size());
        AuditRecord rec = recList.get(0);
        assertEquals("nimitz", rec.getUser().getUserId());
    }

    @Transactional
    @Test()
    public void testCreateFetchAuditLogByTarget() {
        TestUtils.insertAdminUser(userService, "nimitz", "password",
                "firstName", "lastName");
        TestUtils.insertAdminUser(userService, "jpjones", "password",
                "firstName", "lastName");
        User user = TestUtils
                .fetchUser(userService, userServiceUtil, "jpjones");
        User user2 = TestUtils
                .fetchUser(userService, userServiceUtil, "nimitz");
        testTarget.create(new AuditRecord(AuditActionType.CREATE_UDR,
                "UDR_TITLE", user2));
        testTarget.create(new AuditRecord(AuditActionType.CREATE_WL, "WL_NAME",
                Status.SUCCESS_WITH_WARNING, "{DocWatchList1}", null, user2));
        testTarget.create(new AuditRecord(AuditActionType.CREATE_WL,
                "WL_NAME2", Status.SUCCESS_WITH_WARNING, null, null, user));
        testTarget.create(new AuditRecord(AuditActionType.UPDATE_WL, "WL_NAME",
                Status.SUCCESS_WITH_WARNING, "{DocWatchList2}", null, user));

        List<AuditRecord> recList = testTarget.findByUserAndTarget("nimitz",
                "WL_NAME");
        assertNotNull(recList);
        assertEquals(1, recList.size());
        assertEquals("{DocWatchList1}", recList.get(0).getMessage());

        recList = testTarget.findByUserAndTarget("jpjones", "WL_NAME");
        assertNotNull(recList);
        assertEquals(1, recList.size());
        assertEquals("{DocWatchList2}", recList.get(0).getMessage());

        recList = testTarget.findByTarget("WL_NAME");
        assertEquals(2, recList.size());
    }

}
