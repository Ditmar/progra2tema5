package tcp;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
public class SocketTcp implements Runnable {
	private Integer port;
	private ServerSocket sockerserver; 
	private Socket client;
	private ObjectInputStream inData;
	private ObjectOutputStream outData;
	private Thread thread;
	private Boolean running;
	public SocketTcp(Integer port) {
		try {
			this.port = port;
			this.running = false;
			initServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void initServer() throws IOException {
		sockerserver = new ServerSocket(this.port);
		System.out.println("Server running in " + this.port);
		this.running = true;
		client = sockerserver.accept();
		System.out.println("conexión Aceptada " + client.getLocalAddress() + ":" + client.getLocalPort());
		inData = new ObjectInputStream(client.getInputStream());
		outData = new ObjectOutputStream(client.getOutputStream());
		thread = new Thread(this);
		thread.start();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(this.running) {
			try {
				PackageData data = (PackageData)inData.readObject();
				System.out.println(data.getNick() + ">" + data.getMsn() + " " + data.getSenddate().toGMTString());
				
				PackageData serverPackage = new PackageData("Server","Tu enviaste " + data.getMsn(), sockerserver.getInetAddress().getHostName() );
				outData.writeObject(serverPackage);
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void dissConnect() {
		try {
			this.sockerserver.close();
			this.running = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
