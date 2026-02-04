package control;

import java.util.concurrent.*;

/**
 *  - ExecutorService (execution pool)
 *  - Semaphore (limited carts/cars)
 *  - ReentrantLock (exclusive printer)
 *  - synchronized (shared gradebook update)
 */
public class Main {
	
	public static void main(String[] args) throws InterruptedException {
		
		// Change permits to demo throttling: 1,2,3,4...
		SchoolResources resources = new SchoolResources(3);
		
		// * availableProcessors
		int cores = Runtime.getRuntime().availableProcessors();
		int staff = Math.min(cores, 6);
		System.out.println("CPU cores: " + cores);
		System.out.println("Delivery staff (thread pool size): " + staff);
		System.out.println("Carts available (semaphore permits): " + resources.carts.availablePermits());
		System.out.println();
		
		// * ExecutorService
		ExecutorService pool = Executors.newFixedThreadPool(staff);
		
		// * Monitor thread: prints progress once per second
		ScheduledExecutorService monitorExec = Executors.newSingleThreadScheduledExecutor();
		monitorExec.scheduleAtFixedRate(
			new ProgressMonitor(resources.gradebook), 0, 1, TimeUnit.SECONDS);
		
		// * Submit many delivery tasks (tasks > threads)
		for (int i = 0; i < 12; i++) {
			int pages = 1 + (i % 3); // 1..3 pages
			pool.submit(new DeliveryTask(resources, pages));
		}
		
		// * Shutdown
		pool.shutdown();
		pool.awaitTermination(2, TimeUnit.MINUTES);
		monitorExec.shutdownNow();
		
		System.out.println("\n=== END OF DAY ===");
		System.out.println("Total deliveries recorded: " + resources.gradebook.totalDelivered());
	}

}