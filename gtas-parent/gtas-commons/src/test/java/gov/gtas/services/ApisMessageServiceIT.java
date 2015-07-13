package gov.gtas.services;

import static org.junit.Assert.assertNotNull;
import gov.gtas.config.CommonServicesConfig;
import gov.gtas.parsers.paxlst.vo.ApisMessageVo;
import gov.gtas.parsers.util.FileUtils;

import java.io.File;
import java.text.ParseException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

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
        byte[] raw = FileUtils.readSmallFile(this.apisFilePath);
        ApisMessageVo msg = svc.parseApisMessage(raw);
        assertNotNull(msg);
    }
}
