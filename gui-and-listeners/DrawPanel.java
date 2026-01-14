import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class DrawPanel extends JPanel implements MouseListener {
	
	public DrawPanel() {
		setBackground(new Color(176, 250, 192));
		addMouseListener(this);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("Mouse pressed at: " + e.getX() + ", " + e.getY());
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("Mouse released at: " + e.getX() + ", " + e.getY());
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
	}
	
}