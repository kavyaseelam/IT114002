import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings({ "restriction", "serial" })
public class clientUI extends JFrame implements OnReceive{
	
	static Client client;
	static JButton toggle;
	static JButton clickit;
	static JTextArea chat;

   public clientUI()  {
    	super("Client");
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
		JFrame frame = new JFrame("Client");
		frame.setLayout(new BorderLayout());
		
		final clientUI window = new clientUI();
		window.setLayout(new BorderLayout());
		
		// create connection elements
		JPanel connectionDetails = new JPanel();
		JLabel u = new JLabel("username: ");
		final JTextField user = new JTextField();
		user.setPreferredSize(new Dimension(100,20));
		user.setText("");
		
		JLabel h = new JLabel("host: ");
		final JTextField host = new JTextField();
		host.setPreferredSize(new Dimension(100,20));
		host.setText("127.0.0.1");
		
		JLabel p = new JLabel("port: ");
		final JTextField port = new JTextField();
		port.setPreferredSize(new Dimension(50,20));
		port.setText("3002");
		final JButton connect = new JButton();
		connect.setText("Connect");
		
		// add elements to the panel
		connectionDetails.add(u);
		connectionDetails.add(user);
		connectionDetails.add(h);
		connectionDetails.add(host);
		connectionDetails.add(p);
		connectionDetails.add(port);
		connectionDetails.add(connect);
		JPanel area = new JPanel();
		area.setLayout(new BorderLayout());
		
		window.add(connectionDetails, BorderLayout.NORTH);
		
		//create panel for chat
		JPanel mainArea = new JPanel();
		mainArea.setPreferredSize(new Dimension(800,500));
		mainArea.setLayout(new BorderLayout());
		
		//create panel to hold multiple controls
		final JPanel chatArea = new JPanel();
		final JPanel gameArea = new JPanel();
		chatArea.setLayout(new BorderLayout());
		gameArea.setLayout(new BorderLayout());
		
		String magicWord = hang.getWord();
		
		
		drawHangman draw = new drawHangman();
		draw.wordLength(magicWord);
		int frameWidth = ((magicWord.length() *30)+ 800);
		gameArea.add(draw, BorderLayout.CENTER);
		
		//create text area for messages
		final JTextArea t1 = new JTextArea();
		chat = t1;
		
		Font font1 = new Font("SansSerif", Font.BOLD, 20);
		
		t1.setFont(font1);
		
		t1.append("the word is: " + magicWord);
		
		t1.setEditable(false);
		chatArea.add(t1);
		chatArea.setBorder(BorderFactory.createLineBorder(Color.black));
		gameArea.setBorder(BorderFactory.createLineBorder(Color.black));
		
		JSplitPane sl = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, chatArea, gameArea); 
		sl.setOneTouchExpandable(true);
		sl.setDividerLocation(300);
		
		mainArea.add(sl, BorderLayout.CENTER);
				
		
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
					t1.append("\n" + textField.getText());
					textField.setText("");
				}
				
				// send message to server
				//client = new Client();
				client.sendMessage(message);
			}
			
		});
		
		// add action listener for connect button
		
		connect.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	//client = new Client();
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
			    	//client.registerListeners(window);
			    	//METHOD 2 Lamba Expression (unnamed function to handle callback)
			    	/*client.registerListener(()->{	
			    		if(UISample.toggle != null) {
			    			UISample.toggle.setText("OFF");
			    			UISample.toggle.setBackground(Color.RED);
			    		}
			    	});*/
			    	
			    	
			    	//trigger any one-time data after client connects
			    	
			    	//register our history/message listener
			    	client.registerListeners(window);
			    	client.postConnectionData();
			    	connect.setEnabled(false);
			    	
			    	t1.setText("player is connected");
			    	//click.setEnabled(true);
			    	
			    	
		    	}
		    }
		});
		
		userInput.add(l);
		userInput.add(textField);
		userInput.add(b);
		//add panel to mainArea panel
		chatArea.add(userInput, BorderLayout.SOUTH);
		//add mainArea panel to frame
		window.add(mainArea, BorderLayout.CENTER);
		
		window.setPreferredSize(new Dimension(frameWidth,500));
		window.pack();
		window.setVisible(true);
		
		
	}


	@Override
	public void onReceivedSwitch(boolean isOn) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onReceivedMessage(String msg) {
		System.out.println("test");
		chat.append(msg);
		chat.append(System.lineSeparator());
	}
}

