package threading;

public class Generator implements Runnable {
	
	private int counter;
	private int id;
	private int sleepTime;
	
	public Generator(int id, int initialCounter, int sleepTime) {
		this.id = id;
		this.counter = initialCounter;
		this.sleepTime = sleepTime;
	}
	
	@Override
	public void run() {
		while (true) {
			counter++;
			Blackboard.getInstance().setNumber(id, counter);
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
}

