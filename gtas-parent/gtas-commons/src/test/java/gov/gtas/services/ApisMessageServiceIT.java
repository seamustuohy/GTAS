package gov.gtas.services;

import gov.gtas.config.CommonServicesConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CommonServicesConfig.class)
//@Transactional
public class ApisMessageServiceIT {
//extends AbstractTransactionalJUnit4SpringContextTests {
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

    public List<String> listFilesForFolder(final File folder) {
        List<String> rv = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                rv.add(fileEntry.getAbsolutePath());
            }
        }
        return rv;
    }
    
    public void testeverything() {
        final File folder = new File("c:/temp/APIS-test-files");
        List<String> files = listFilesForFolder(folder);
        for (String f : files) {
            System.out.println(f);
            svc.parseAndLoadApisFile(f);
        }
    }
}
