package gov.gtas.services;

import gov.gtas.config.CommonServicesConfig;
import gov.gtas.parsers.paxlst.vo.ApisMessageVo;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CommonServicesConfig.class)
@Transactional
public class ApisMessageServiceIT extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    private ApisMessageService svc;

    private String apisFilePath;
    @Before
    public void setUp() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("apis-messages/airline2.edi").getFile());
        this.apisFilePath = file.getAbsolutePath();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test()
    public void testRunService() throws ParseException {
        ApisMessageVo msg = svc.parseApisMessage(this.apisFilePath);
        assertNotNull(msg);
    }
}
