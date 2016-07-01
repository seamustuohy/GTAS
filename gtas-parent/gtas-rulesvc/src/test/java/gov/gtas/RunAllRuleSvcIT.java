/*
 * All GTAS code is Copyright 2016, Unisys Corporation.
 * 
 * Please see LICENSE.txt for details.
 */
package gov.gtas;

import gov.gtas.rule.RuleRepositoryIT;
import gov.gtas.rule.RuleUtilsIT;
import gov.gtas.svc.TargetingServiceIT;
import gov.gtas.svc.TargetingServicePnrIT;
import gov.gtas.svc.WatchlistServiceIT;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ RuleRepositoryIT.class, RuleUtilsIT.class,
        TargetingServiceIT.class, TargetingServicePnrIT.class,
        WatchlistServiceIT.class })
public class RunAllRuleSvcIT {
}
