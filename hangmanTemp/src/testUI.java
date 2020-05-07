import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class testUI extends JFrame{

	public static void main(String[] args) {

		JFrame frame = new JFrame("Test");
		frame.setLayout(new BorderLayout());
		
		JPanel main = new JPanel();
		main.setPreferredSize(new Dimension(800,400));
		main.setLayout(new BorderLayout());
		frame.add(main, BorderLayout.CENTER);
		
		drawHangman draw = new drawHangman();
		
		draw.nextNumMisses();
		frame.add(draw);
		
		frame.pack();
		frame.setVisible(true);
		
		
	}
}
