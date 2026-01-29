import java.beans.PropertyChangeSupport;

public class Blackboard extends PropertyChangeSupport {
	
	private static final Blackboard instance = new Blackboard();
	private int[] numbers = new int[5];
	
	private Blackboard() {
		super(new Object());
	}
	
	public static Blackboard getInstance() {
		return instance;
	}
	
	public void setNumber(int index, int value) {
		numbers[index] = value;
		firePropertyChange("numbers", null, value);
	}
	
	public int getNumber(int index) {
		return numbers[index];
	}
	
}

