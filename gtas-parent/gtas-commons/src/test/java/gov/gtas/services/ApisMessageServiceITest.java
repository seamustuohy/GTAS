package gov.gtas.services;

import gov.gtas.config.CommonServicesConfig;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CommonServicesConfig.class)
public class ApisMessageServiceITest {
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
    public void testRunService() {
        svc.parseAndLoadApisFile(this.apisFilePath);
    }
}
