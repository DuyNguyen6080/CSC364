import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToolPanel extends JPanel implements ActionListener {
	
	public ToolPanel() {
		String[] colors = {
			"Black", "Red", "Blue", "Green", "Yellow", "Orange", "Pink"
		};
		JComboBox colorMenu = new JComboBox(colors);
		JRadioButton rectangleRadio = new JRadioButton("Rectangle");
		JRadioButton circleRadio = new JRadioButton("Circle");
		JRadioButton arcRadio = new JRadioButton("Arc");
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(rectangleRadio);
		buttonGroup.add(circleRadio);
		buttonGroup.add(arcRadio);
		setLayout(new GridLayout(7, 1));
		add(colorMenu);
		add(rectangleRadio);
		add(circleRadio);
		add(arcRadio);
		rectangleRadio.addActionListener(this);
		circleRadio.addActionListener(this);
		arcRadio.addActionListener(this);
		colorMenu.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Mouse pressed at: " + e.getActionCommand());
	}
	
}