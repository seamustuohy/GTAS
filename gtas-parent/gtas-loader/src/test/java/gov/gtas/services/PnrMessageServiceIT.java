package gov.gtas.services;

import static org.junit.Assert.*;

import java.io.File;
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

import gov.gtas.config.CommonServicesConfig;
import gov.gtas.parsers.edifact.MessageVo;
import gov.gtas.parsers.exception.ParseException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CommonServicesConfig.class)
@Transactional
public class PnrMessageServiceIT extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    private PnrMessageService svc;

    private String filePath;
    
    @Before
    public void setUp() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("pnr-messages/2_pnrs_basic.edi").getFile());
        this.filePath = file.getAbsolutePath();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test()
    public void testRunService() throws ParseException {
        List<String> messages = svc.preprocess(this.filePath);
        assertEquals(2, messages.size());
        for (String m : messages) {
            MessageVo vo = svc.parse(m);
            assertNotNull(vo);
            svc.load(vo);
        }
    }
}
