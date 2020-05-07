import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class drawHangman extends JPanel {
	int numMisses=0;
	int wordLen=0;
	public void nextNumMisses()
	{
	    numMisses++;
	}
	
	public void wordLength(String magicWord) {
		wordLen = magicWord.length();
	}
	
	public int frameSize(int wordLen) {
		int size = 250 + (30*wordLen);
		return size;
	}

	public void paintComponent(Graphics g)
	{
	    Graphics2D g2 = (Graphics2D) g; 
	    
	    int x1 = 250;
	    int y1 = 300;
	    int x2 = 270;
	    int y2 = 300;

	    g2.setStroke(new BasicStroke(5,BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
	    g2.setColor(Color.BLACK); 
	    g.drawLine(50, 30, 50, 10);
	    g.drawLine(50, 10, 130, 10);
	    g.drawLine(130, 10, 130, 300);
	    g.drawLine(20, 300, 150, 300);
	    
	    for(int i=0; i < wordLen; i++) {
	    	g.drawLine(x1, y1, x2, y2);
	    	x1 = x1+30;
	    	x2 = x2+30;
	    }

	    if (numMisses==1)
	    {
	        //draws the head
	        g2.setStroke(new BasicStroke(3,BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
	        Ellipse2D.Double head = new Ellipse2D.Double(25, 30, 50, 50);
	        g2.draw(head); 
	    }

	    else if (numMisses==2)
	    {
	        //draws the body
	        g.drawLine(50, 80, 50, 180);
	    }
	    else if (numMisses==3)
	    {
	        //draws the left arm
	        g.drawLine(10, 150, 50, 100);
	    }
	    else if (numMisses==4)
	    {
	        //draws the right arm
	        g.drawLine(50, 100, 90, 150);
	    }
	    else if (numMisses==5)
	    {
	        //draws the left leg
	        g.drawLine(30, 250, 50, 180);
	    }
	    else if (numMisses==6)
	    {
	        //draws the right leg
	        g.drawLine(50, 180, 70, 250);
	    }
	}
}
