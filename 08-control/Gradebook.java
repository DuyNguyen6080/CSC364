package control;

public class Gradebook {
	private int deliveredCount = 0;
	
	// synchronized = critical section protecting shared mutable state
	public synchronized void markDelivered(int deliveryId) {
		deliveredCount++;
		System.out.printf("[%s] 📒 Gradebook updated: delivery #%d recorded (total=%d)%n",
			Thread.currentThread().getName(), deliveryId, deliveredCount);
	}
	
	public synchronized int totalDelivered() {
		return deliveredCount;
	}
}