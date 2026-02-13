import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame implements ActionListener {

	Control controlBar;
	Blackboard blackboard;
	public Main() {
		setLayout(new BorderLayout());
		blackboard = new Blackboard(10,10);
		controlBar = new Control(blackboard);
		controlBar.addListener(this);
		controlBar.addStartListener(this);
		controlBar.addEndListener(this);
		controlBar.addResetListener(this);

		add(controlBar, BorderLayout.NORTH);
		add(blackboard, BorderLayout.CENTER);

	}


	public static void main(String[] args) {
		Main frame = new Main();
		frame.setTitle("Threads + GUI");
		frame.setSize(400, 400);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);


	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		// Check if it's the start button
		if (source == controlBar.start) {
			System.out.println("Start button clicked! Running BFS and Dijkstra in parallel...");
			blackboard.findPathMultiThread();
		}
		// Check if it's the end button
		else if (source == controlBar.end) {
			System.out.println("Stopping search...");
			blackboard.stopSearch();
		}
		// Check if it's the reset button
		else if (source == controlBar.reset) {
			System.out.println("Reset button clicked! Resetting grid...");
			blackboard.reset(blackboard.getRows());
		}
		// Check if it's the combo box
		else if (source instanceof JComboBox) {
			JComboBox<Integer> cb = (JComboBox) source;
			Integer selectedValue = (Integer) cb.getSelectedItem();
			blackboard.reset(selectedValue);
			System.out.println("Grid size changed to: " + selectedValue);
		}
	}
}