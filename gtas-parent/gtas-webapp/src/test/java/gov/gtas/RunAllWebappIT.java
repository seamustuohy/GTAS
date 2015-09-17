package gov.gtas;

import gov.gtas.controller.UdrBuilderControllerIT;
import gov.gtas.controller.WatchlistManagementControllerIT;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ UdrBuilderControllerIT.class, WatchlistManagementControllerIT.class
		 })
public class RunAllWebappIT {
}
