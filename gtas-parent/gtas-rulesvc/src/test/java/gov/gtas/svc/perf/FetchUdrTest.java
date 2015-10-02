package gov.gtas.svc.perf;

import gov.gtas.model.udr.json.UdrSpecification;
import gov.gtas.svc.UdrService;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.context.ConfigurableApplicationContext;

public class FetchUdrTest implements PerformanceTestFactory, PerformanceTest {
	private UdrService udrService;
	private int parallelRequestCount = 1000;
	private int poolTimeoutSeconds = 500;

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.gtas.svc.perf.PerformanceTest#runTest()
	 */
	@Override
	public List<String> runTest() {
		ExecutorService exec = Executors.newWorkStealingPool(10);
		List<String> ret = new LinkedList<String>();
		
		long udrStart = System.currentTimeMillis();

		for(int i = 0; i < parallelRequestCount; i++){
		    exec.execute(createCommand((i%300)+1,udrService, ret));
		}
		try{
		  exec.shutdown();
		  exec.awaitTermination(poolTimeoutSeconds, TimeUnit.SECONDS);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		long udrElapsed = System.currentTimeMillis() - udrStart;
		ret.add("Total Time = " + udrElapsed
				+ ", Average Time = " + (udrElapsed / 300));
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.gtas.svc.perf.PerformanceTestFactory#createTest(org.springframework
	 * .context.ConfigurableApplicationContext)
	 */
	@Override
	public PerformanceTest createTest(ConfigurableApplicationContext ctx) {
		this.udrService = (UdrService) ctx.getBean("udrServiceImpl");
		return this;
	}

	private static Runnable createCommand(final int udrNum, final UdrService udrService, final List<String> ret){
		final String title = "PerfTestUdr"+udrNum;
		final String userId = "bstygar";
		return new Runnable(){

			/* (non-Javadoc)
			 * @see java.lang.Runnable#run()
			 */
			@Override
			public void run() {
				long start = System.currentTimeMillis();
				UdrSpecification spec = udrService.fetchUdr(userId, title);
				if(spec == null){
					ret.add(">>>>>>>> ERROR cannot find UDR:"+title);
				} else{
					long fin = System.currentTimeMillis() - start;
					ret.add("Elapsed time to fetch '"+title+"' ="+fin);
				}
			}
	    	
	    };
	}
	/**
	 * @param poolTimeoutSeconds
	 *            the poolTimeoutSeconds to set
	 */
	public void setPoolTimeoutSeconds(int poolTimeoutSeconds) {
		this.poolTimeoutSeconds = poolTimeoutSeconds;
	}

	/**
	 * @param parallelRequestCount the parallelRequestCount to set
	 */
	public void setParallelRequestCount(int parallelRequestCount) {
		this.parallelRequestCount = parallelRequestCount;
	}

}
