package gov.gtas.svc.perf;

import gov.gtas.config.CommonServicesConfig;
import gov.gtas.config.RuleServiceConfig;
import gov.gtas.enumtype.EntityEnum;
import gov.gtas.model.MessageStatus;
import gov.gtas.model.udr.json.UdrSpecification;
import gov.gtas.services.udr.RulePersistenceService;
import gov.gtas.svc.TargetingService;
import gov.gtas.svc.UdrService;
import gov.gtas.svc.WatchlistService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * A Java application for performance/load testing of the Rule Engine and Targeting Service.
 * The following tests are implemented:
 * 1. fetchUdrTest() - 
 *
 */
public class PerfTestRunner {
	public static final String PASSENGER_WL_NAME = "PerfTest Passenger WL";
	public static final String DOCUMENT_WL_NAME = "PerfTest Document WL";

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = null;
		try {
			ctx = new AnnotationConfigApplicationContext(
					CommonServicesConfig.class, RuleServiceConfig.class);
			TargetingService targetingService = (TargetingService) ctx
					.getBean("targetingServiceImpl");
			int iterationCount = 0;
			if (args.length < 1) {
				System.out
						.println("The number of iterations must be provided!");
				System.exit(0);
			} else {
				if(args[0].equalsIgnoreCase("clean")){
					cleanupRuleData(ctx);
					System.exit(0);
				}else if(args[0].equalsIgnoreCase("udrtest")){
					fetchUdrTest(ctx,1000);
					System.exit(0);
				}

				iterationCount = Integer.parseInt(args[0]);
			}
			if (args.length > 1 && args[1].equalsIgnoreCase("runall")) {
				WatchlistService watchlistService = (WatchlistService) ctx
						.getBean("watchlistServiceImpl");
				UdrService udrService = (UdrService) ctx
						.getBean("udrServiceImpl");
				genPerformanceData(udrService, 100, watchlistService, 300, 40);
				watchlistService.activateAllWatchlists();
				System.out
						.println("*****************************************************************");
				System.out
						.println("********************   ACTIVATION COMPLETE  *********************");
				System.out
						.println("*****************************************************************");
			}
			long totalStart = System.currentTimeMillis();
			long minmax[] = runPerformance(targetingService, iterationCount);
			long totalElapsed = System.currentTimeMillis() - totalStart;
			System.out
					.println("******************************************************************");
			System.out.println(String.format(
					"Min Time = %d, Max Time = %d", minmax[0], minmax[1]));
			System.out.println("Total Time = " + totalElapsed
					+ ", Average Time = " + (totalElapsed / iterationCount));
			System.out
					.println("******************************************************************");
			ctx.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (ctx != null)
				ctx.close();
		}
		System.exit(0);
	}

	private static long[] runPerformance(TargetingService targetingService,
			int count) {
		long max = 0;
		long min = Long.MAX_VALUE;
		for (int i = 0; i < count; ++i) {
			long start = System.currentTimeMillis();
			targetingService.analyzeLoadedMessages(
					MessageStatus.LOADED, MessageStatus.LOADED, false);
			//Collection<TargetSummaryVo> res = ctx.getTargetingResult();
			long elapsed = System.currentTimeMillis() - start;
			if (elapsed > max)
				max = elapsed;
			if (elapsed < min)
				min = elapsed;
		}
		return new long[] { min, max};
	}
	private static void fetchUdrTest(ConfigurableApplicationContext ctx, int parallelRequestCount){
		FetchUdrTest testMaker = new FetchUdrTest();
		testMaker.setPoolTimeoutSeconds(400);
		testMaker.setParallelRequestCount(parallelRequestCount);
		PerformanceTest test = testMaker.createTest(ctx);
		List<String> result = test.runTest();
		System.out
				.println("******************************************************************");
		for(String line:result){
			System.out.println(line);
		}
		System.out
				.println("******************************************************************");		
	}
	private static void cleanupRuleData(ConfigurableApplicationContext ctx){
		RulePersistenceService rulePersistenceService = (RulePersistenceService) ctx
				.getBean("rulePersistenceServiceImpl");
		final EntityManager em = rulePersistenceService.getEntityManager();
		JpaTransactionManager transactionManager = (JpaTransactionManager)ctx.getBean("transactionManager");
		TransactionTemplate template = new TransactionTemplate(transactionManager);
		template.execute(new TransactionCallback<Integer>() {
		      // the code in this method executes in a transaction context
		      public Integer doInTransaction(TransactionStatus status) {
		  		Query q = em.createQuery("delete from WatchlistItem it where it.id > 0");
		  		int del = q.executeUpdate();
				System.out.println("Number wl item deleted = "+del);
		  		q = em.createQuery("delete from Watchlist wl where wl.id > 0");
		  		int del2 = q.executeUpdate();
				System.out.println("Number watch list deleted = "+del2);
		  		q = em.createQuery("delete from RuleMeta rm where rm.id > 0");
		  		int del3 = q.executeUpdate();
				System.out.println("Number RuleMeta deleted = "+del3);
		  		q = em.createQuery("delete from Rule r where r.id > 0");
		  		int del4 = q.executeUpdate();
				System.out.println("Number Rules deleted = "+del4);
		  		q = em.createQuery("delete from UdrRule u where u.id > 0");
		  		int del5 = q.executeUpdate();
				System.out.println("Number Udr deleted = "+del5);
		        return del+del2+del3+del4+del5;
		      }
		    });
	}

	private static void genPerformanceData(UdrService udrService, int udrCount,
			WatchlistService watchlistService, int passengerWlCount,
			int documentWlCount) {
		if (passengerWlCount > 0) {
			WatchlistRuleGenerator.generateWlRules(watchlistService,
					PASSENGER_WL_NAME, EntityEnum.PASSENGER, passengerWlCount);
		}
		if (documentWlCount > 0) {
			WatchlistRuleGenerator.generateWlRules(watchlistService,
					DOCUMENT_WL_NAME, EntityEnum.DOCUMENT, documentWlCount);
		}
		if (udrCount > 0) {
			UdrRuleGenerator.generateUdr(udrService, "PerfTestUdr", udrCount);
		}
		System.out
				.println("*****************************************************************");
		System.out
				.println("********************   GENERATION COMPLETE  *********************");
		System.out
				.println("*****************************************************************");
	}
}
