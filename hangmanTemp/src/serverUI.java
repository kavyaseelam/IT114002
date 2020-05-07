import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.*; 

import java.awt.*; 

import javax.swing.*; 

public class serverUI {
	protected static Server server;
	static hang hang;

	public static void main(String[] args) {
		//create frame
		final
		JFrame frame = new JFrame("Server");
		frame.setLayout(new BorderLayout());
		
		//create panel
		JPanel serverFrame = new JPanel();
		serverFrame.setPreferredSize(new Dimension(800,400));
		serverFrame.setLayout(new BorderLayout());
		
		//create panel to hold multiple controls
		JPanel chatArea = new JPanel();
		JPanel gameArea = new JPanel();
		
		//create text area for messages
		final JTextArea t1 = new JTextArea();
		Font font1 = new Font("SansSerif", Font.BOLD, 20);
		t1.setFont(font1);
		
		
		//don't let the user edit this directly
		t1.setEditable(false);
		t1.setText("");
		
		
		chatArea.setLayout(new BorderLayout());
		gameArea.setLayout(new BorderLayout());
		
		//add text area to chat area
		chatArea.add(t1);
		chatArea.setBorder(BorderFactory.createLineBorder(Color.black));
		gameArea.setBorder(BorderFactory.createLineBorder(Color.black));
		
		// generating word, drawing hangman & blank lines
		String magicWord = hang.getWord();
		t1.append("the word is: " + magicWord);
		
		drawHangman draw = new drawHangman();
		draw.wordLength(magicWord);
		int frameWidth = ((magicWord.length() *30)+ 800);
		gameArea.add(draw, BorderLayout.CENTER);
		
		// split pane
		JSplitPane sl = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, chatArea, gameArea); 
		sl.setOneTouchExpandable(true);
		sl.setDividerLocation(300);
		  
		//add chat area to panel
		serverFrame.add(sl, BorderLayout.CENTER);
		
		//create panel to hold multiple controls
		JPanel userInput = new JPanel();
		
		//setup textfield
		final JTextField textField = new JTextField();
		textField.setPreferredSize(new Dimension(100,30));
		
		//setup submit button
		JButton b = new JButton();
		b.setPreferredSize(new Dimension(100,30));
		b.setText("Send");
		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String message = textField.getText();
				if(message.length() > 0) {
					//append a newline and the text from the textfield
					//to that textarea (simulate simple chatroom)
					t1.append("\n" + textField.getText());
					textField.setText("");
					//server = new Server();
					long id = 1;
					server.broadcast(message, id);
				}
				
			}
		});
		
		userInput.add(textField);
		userInput.add(b);
		//add panel to serverFrame panel
		chatArea.add(userInput, BorderLayout.SOUTH);
		
		serverFrame.setPreferredSize(new Dimension(frameWidth,400));
		//add serverFrame panel to frame
		frame.add(serverFrame, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		Thread serverThread = new Thread() {
			@Override
			public void run() {
				server = new Server();
				server.start(3005);
			}
		};
		serverThread.start();
		
	}
}

