package control;

public class DeliveryTask implements Runnable {
	
	private final int deliveryId;
	private final int pagesToPrint;
	private final SchoolResources resources;
	
	public DeliveryTask(SchoolResources resources, int pagesToPrint) {
		this.resources = resources;
		this.deliveryId = resources.nextDeliveryId.getAndIncrement();
		this.pagesToPrint = pagesToPrint;
	}
	
	@Override
	public void run() {
		boolean acquiredCart = false;
		try {
			// 1) Semaphore: must acquire a cart to start delivering
			System.out.printf("[%s] 🚗 Waiting for a cart for delivery #%d...%n",
				Thread.currentThread().getName(), deliveryId);
			
			resources.carts.acquire();
			acquiredCart = true;
			
			System.out.printf("[%s] 🚗 Got a cart! Start delivery #%d%n",
				Thread.currentThread().getName(), deliveryId);
			
			// 2) Lock: must use the printer exclusively to print a label/cover sheet
			resources.printerLock.lock();
			try {
				System.out.printf("[%s] 🖨️ Printing %d pages for delivery #%d%n",
					Thread.currentThread().getName(), pagesToPrint, deliveryId);
				Thread.sleep(80L * pagesToPrint);
				System.out.printf("[%s] 🖨️ Done printing for delivery #%d%n",
					Thread.currentThread().getName(), deliveryId);
			} finally {
				resources.printerLock.unlock();
			}
			
			// 3) Simulate "walking the package" across campus
			System.out.printf("[%s] 🏫 Delivering package #%d...%n",
				Thread.currentThread().getName(), deliveryId);
			Thread.sleep(200);
			System.out.printf("[%s] ✅ Delivered package #%d%n",
				Thread.currentThread().getName(), deliveryId);
			
			// 4) synchronized: update shared gradebook safely
			resources.gradebook.markDelivered(deliveryId);
			
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} finally {
			// 5) Release semaphore permit: cart is returned
			if (acquiredCart) {
				resources.carts.release();
				System.out.printf("[%s] 🚗 Cart returned (delivery #%d complete)%n",
					Thread.currentThread().getName(), deliveryId);
			}
		}
	}
}