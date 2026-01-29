package threading;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class View extends JPanel implements PropertyChangeListener {
	
	private JLabel labels[] = new JLabel[5];
	
	public View() {
		setLayout(new GridLayout(1, 5));
		for (int i = 0; i < 5; i++) {
			labels[i] = new JLabel();
			labels[i].setOpaque(true);
			add(labels[i]);
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		for (int i = 0; i < 5; i++) {
			int number = Blackboard.getInstance().getNumber(i);
			labels[i].setBackground(new Color((int) (number * 1234567L) | 0xFF000000));
			labels[i].setText(Integer.toString(number));
			repaint();
		}
	}
}


