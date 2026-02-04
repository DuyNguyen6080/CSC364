package control;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Shared resources (singletons by convention: one campus).
 */
public class SchoolResources {
	
	// 🚗 Only N carts available to carry deliveries at once
	public final Semaphore carts;
	
	// 🖨️ Only one printer exists (exclusive hardware)
	public final ReentrantLock printerLock;
	
	// 📒 Shared gradebook (shared mutable state)
	public final Gradebook gradebook;
	
	// Unique delivery IDs
	public final AtomicInteger nextDeliveryId;
	
	public SchoolResources(int cartPermits) {
		this.carts = new Semaphore(cartPermits);
		this.printerLock = new ReentrantLock(true); // fair lock for nicer output
		this.gradebook = new Gradebook();
		this.nextDeliveryId = new AtomicInteger(1);
	}
}