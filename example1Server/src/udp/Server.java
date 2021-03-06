package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Server {
	private Integer port;
	private Boolean connected;
	private DatagramSocket socket;
	public Server(Integer port) {
		this.port = port;
		this.connected = false;
	}
	public void startServer() {
		try {
			socket = new DatagramSocket(this.port);
			this.connected = true;
			System.out.println("Server running in " + this.port);
			
			while(this.connected) {
				byte[] buffer = new byte[1000];
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				socket.receive(request);
				if (request.getData() != null) {
					String msn = new String(request.getData());
					msn = msn.trim();
					System.out.println("Cliente envio > " + msn);
					String responseMessage = new String(msn + " Server");
					byte[] bufferSend = responseMessage.getBytes();
					DatagramPacket reply = new DatagramPacket(
							bufferSend, 
							bufferSend.length, 
							request.getAddress(), 
							request.getPort());
					socket.send(reply);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void stopServer() {
		this.connected = false;
		socket.close();
		System.out.println("Close server");
	}
	
}
