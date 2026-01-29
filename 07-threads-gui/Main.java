package threading;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
	
	public Main() {
		View view = new View();
		add(view);
		Blackboard.getInstance().addPropertyChangeListener(view);
	}
	
	public static void main(String[] args) {
		Main frame = new Main();
		frame.setTitle("Threads + GUI");
		frame.setSize(400, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		for (int i = 0; i < 5; i++) {
			Thread generator = new Thread(new Generator(i, i*100, 100 + i * 1000));
			generator.start();
		}
	}
	
}

