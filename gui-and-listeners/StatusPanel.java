import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StatusPanel extends JPanel implements ActionListener {
	
	public StatusPanel() {
		JButton button1 = new JButton("Undo");
		JButton button2 = new JButton("Erase");
		add(button1);
		add(button2);
		button1.addActionListener(this);
		button2.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Status panel action: " + e.getActionCommand());
	}
	
}