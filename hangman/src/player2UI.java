import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings({ "restriction", "serial" })
public class player2UI extends JFrame{
	
	static Client client;
	static JButton toggle;
	static JButton clickit;

   public player2UI()  {
    	super("Player 2");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// add a window listener
		this.addWindowListener(new WindowAdapter() {
			/* (non-Javadoc)
			 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
			 */
			@Override
			public void windowClosing(WindowEvent e) {
				// before we stop the JVM stop the example
				//client.isRunning = false;
				super.windowClosing(e);
			}
		});
    	
    }
	
	
	@SuppressWarnings("restriction")
	public static void main(String[] args) {
		//create frame
		@SuppressWarnings("restriction")
		JFrame frame = new JFrame("Player 2");
		frame.setLayout(new BorderLayout());
		
		player2UI window = new player2UI();
		window.setLayout(new BorderLayout());
		
		// create connection elements
		JPanel connectionDetails = new JPanel();
		final JTextField host = new JTextField();
		host.setText("127.0.0.1");
		final JTextField port = new JTextField();
		port.setText("3002");
		final JButton connect = new JButton();
		connect.setText("Connect");
		
		// add elements to the panel
		connectionDetails.add(host);
		connectionDetails.add(port);
		connectionDetails.add(connect);
		JPanel area = new JPanel();
		area.setLayout(new BorderLayout());
		
		window.add(connectionDetails, BorderLayout.NORTH);
		
		//create panel for chat
		JPanel simpleChat = new JPanel();
		simpleChat.setPreferredSize(new Dimension(400,400));
		simpleChat.setLayout(new BorderLayout());
		
		//create text area for messages
		final JTextArea textArea = new JTextArea();
		
		//don't let the user edit this directly
		textArea.setEditable(false);
		textArea.setText("");
		
		//create panel to hold multiple controls
		final JPanel chatArea = new JPanel();
		chatArea.setLayout(new BorderLayout());
		
		Font font1 = new Font("SansSerif", Font.BOLD, 20);
		
		//add text area to chat area
		chatArea.add(textArea, BorderLayout.CENTER);
		chatArea.setBorder(BorderFactory.createLineBorder(Color.black));
		textArea.setFont(font1);
		
		//add chat area to panel
		simpleChat.add(chatArea, BorderLayout.CENTER);
		
		
		//create panel to hold multiple controls
		JPanel userInput = new JPanel();
		
		//setup textfield
		
		JLabel l = new JLabel("enter guess: ");
		final JTextField textField = new JTextField();
		textField.setPreferredSize(new Dimension(100,30));
		textField.setFont(font1);
		
		//setup submit button
		JButton b = new JButton();
		b.setPreferredSize(new Dimension(100,30));
		b.setText("Send");
		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				// connect to the server
				
				
				String message = textField.getText();
				if(message.length() > 0) {
					//append a newline and the text from the textfield
					//to that textarea (simulate simple chatroom)
					textArea.append("\n" + textField.getText());
					textField.setText("");
				}
			}
			
		});
		
		// add action listener for connect button
		
		connect.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	client = new Client();
		    	int _port = -1;
		    	try {
		    		_port = Integer.parseInt(port.getText());
		    	}
		    	catch(Exception num) {
		    		System.out.println("Port not a number");
		    	}
		    	if(_port > -1) {
			    	client = Client.connect(host.getText(), _port);
			    	
			    	//METHOD 1 Using the interface
			    	//client.registerSwitchListener(window);
			    	//METHOD 2 Lamba Expression (unnamed function to handle callback)
			    	/*client.registerListener(()->{	
			    		if(UISample.toggle != null) {
			    			UISample.toggle.setText("OFF");
			    			UISample.toggle.setBackground(Color.RED);
			    		}
			    	});*/
			    	
			    	
			    	//trigger any one-time data after client connects
			    	
			    	//register our history/message listener
			    	//client.registerMessageListener(window);
			    	client.postConnectionData();
			    	connect.setEnabled(false);
			    	
			    	textArea.setText("player 2 is connected");
			    	//click.setEnabled(true);
			    	
			    	
		    	}
		    }
		});
		
		userInput.add(l);
		userInput.add(textField);
		userInput.add(b);
		//add panel to simpleChat panel
		simpleChat.add(userInput, BorderLayout.SOUTH);
		//add simpleChat panel to frame
		window.add(simpleChat, BorderLayout.CENTER);
		
		window.setPreferredSize(new Dimension(600,600));
		window.pack();
		window.setVisible(true);
		
	}
}

