package tcp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketTcp implements Runnable {
	private Integer port;
	private String ip;
	private Boolean connected;
	private Socket socket;
	private Thread thread;
	private ObjectOutputStream outData;
	private ObjectInputStream inData;
	private OnMessageRecive listener;
	public SocketTcp(String ip, Integer port) {
		this.port = port;
		this.ip = ip;
		this.connected = false;
	}
	public void emmit(PackageData data, OnMessageRecive listener) {
		this.listener = listener;
		try {
			outData.writeObject(data);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void join(String room) {
		
	}
	public void connected()  {
		 try {
			//Creando el socket de conexión 
			System.out.println("CONEXION AL SOCKET");
			socket = new  Socket(this.ip, this.port);
			outData = new ObjectOutputStream(socket.getOutputStream());
			inData = new ObjectInputStream(socket.getInputStream());
			this.connected = true;
			thread = new Thread(this);
			thread.start();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//Escuchamos al servidor
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (this.connected) {
			try {
				PackageData data =  (PackageData)inData.readObject();
				if (data != null) {
					if (data.getMsn().equals(":quit")) {
						this.connected = false;
					}
					this.listener.OnMessage(data);
				}
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		dissConnect();
	}
	public void dissConnect() {
		try {
			this.socket.close();
			this.connected = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
