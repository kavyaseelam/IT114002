import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Client {
	
	private Socket server;
	private OnReceive onReceiveListener;
	public void registerListeners(OnReceive listener) {
		this.onReceiveListener = listener;
	}
	private Queue<Payload> toServer = new LinkedList<Payload>();
	private Queue<Payload> fromServer = new LinkedList<Payload>();
	
	/*private OnReceive switchListener;
	public void registerSwitchListener(OnReceive listener) {
		this.switchListener = listener;
	}
	private OnReceive messageListener;
	public void registerMessageListener(OnReceive listener) {
		this.messageListener = listener;
	} */
	
	public static Client connect(String address, int port) {
		final Client client = new Client();
		client._connect(address, port);
		Thread clientThread =  new Thread() {
			@Override
			public void run() {
				try {
					client.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		clientThread.start();
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return client;
	}
	
	
	public void _connect(String address, int port) {
		try {
			server = new Socket(address, port);
			System.out.println("Client connected");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void start() throws IOException {
		if(server == null) {
			return;
		}
		System.out.println("Client Started");
		//listen to console, server in, and write to server out
		try(ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(server.getInputStream());){

			//Thread to listen for keyboard input so main thread isn't blocked
			Thread inputThread = new Thread() {
				@Override
				public void run() {
					try {
						while(!server.isClosed()) {
							//System.out.println("Waiting for input");
							//String line = si.nextLine();
							Payload p = toServer.poll();
							if(p != null) {
								//we can also default payloadtype in payload
								//to a desired value, though it's good to be clear
								//what we're sending
								p.setPayloadType(PayloadType.MESSAGE);
								out.writeObject(p);
							}
							else {
								System.out.println("Stopping input thread");
								//we're quitting so tell server we disconnected so it can broadcast
								p.setPayloadType(PayloadType.DISCONNECT);
								p.setMessage("bye");
								out.writeObject(p);
								break;
							}
						}
					}
					catch(Exception e) {
						System.out.println("Client shutdown");
					}
					finally {
						close();
					}
				}
			};
			inputThread.start();//start the thread
			
			//Thread to listen for responses from server so it doesn't block main thread
			Thread fromServerThread = new Thread() {
				@Override
				public void run() {
					try {
						Payload p;
						//while we're connected, listen for payloads from server
						while(!server.isClosed() && (p = (Payload)in.readObject()) != null) {
							System.out.println(fromServer);
							//processPayload(fromServer);
							fromServer.add(p);
						}
						System.out.println("Stopping server listen thread");
					}
					catch (Exception e) {
						if(!server.isClosed()) {
							e.printStackTrace();
							System.out.println("Server closed connection");
						}
						else {
							System.out.println("Connection closed");
						}
					}
					finally {
						close();
					}
				}
			};
			fromServerThread.start();//start the thread
			
			Thread payloadProcessor = new Thread(){
				@Override
				public void run() {
					while(!server.isClosed()) {
						Payload p = fromServer.poll();
						if(p != null) {
							processPayload(p);
						}
						else {
							try {
								Thread.sleep(8);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
			};
			
			payloadProcessor.start();
			
			//Keep main thread alive until the socket is closed
			//initialize/do everything before this line
			while(!server.isClosed()) {
				Thread.sleep(50);
			}
			System.out.println("Exited loop");
			System.exit(0);//force close
			//TODO implement cleaner closure when server stops
			//without this, it still waits for input before terminating
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			close();
		}
	}

	
	private void processPayload(Payload payload) {
		//System.out.println(payload);
		String msg ="";
		switch(payload.getPayloadType()) {
		case CONNECT:
			System.out.println(
					String.format("Client \"%s\" connected", payload.getMessage())
			);
			break;
		case DISCONNECT:
			System.out.println(
					String.format("Client \"%s\" disconnected", payload.getMessage())
			);
			break;
		case MESSAGE:
			System.out.println(
					String.format("%s", payload.getMessage())
			);
			if(onReceiveListener != null) {
				onReceiveListener.onReceivedMessage(msg);
			}
			break;
		default:
			System.out.println("Unhandled payload type: " + payload.getPayloadType().toString());
			break;
		}
	}
	private void close() {
		if(server != null) {
			try {
				server.close();
				System.out.println("Closed socket");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		Client client = new Client();
		client.connect("127.0.0.1", 3002);
		try {
			//if start is private, it's valid here since this main is part of the class
			client.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void postConnectionData() {
		Payload payload = new Payload();
		payload.setPayloadType(PayloadType.CONNECT);
		//payload.IsOn(isOn);
		toServer.add(payload);
		
	}
	
	public void sendMessage(String message) {
		System.out.println(message);
		Payload payload = new Payload();
		payload.setPayloadType(PayloadType.MESSAGE);
		payload.setMessage(message);
		toServer.add(payload);
	}

}

interface OnReceive {
	
	void onReceivedSwitch(boolean isOn);
	void onReceivedMessage(String msg);
}
