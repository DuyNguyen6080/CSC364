package control;

import java.util.concurrent.TimeUnit;

public class ProgressMonitor implements Runnable {
	
	private final Gradebook gradebook;
	
	public ProgressMonitor(Gradebook gradebook) {
		this.gradebook = gradebook;
	}
	
	@Override
	public void run() {
		System.out.printf("[monitor] 📊 totalDelivered=%d%n", gradebook.totalDelivered());
	}
}
