package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class SocketUdp implements Runnable{
	private DatagramSocket socket;
	private String ip;
	private Integer port;
	private Boolean connected;
	//HILO de procesamiento
	private Thread thread;
	private OnMessageRecive listener;
	public SocketUdp(String ip, Integer port) {
		this.ip = ip;
		this.port = port;
		this.connected = false;
	}
	public void connect() {
		
		try {
			socket = new DatagramSocket();
			this.connected = true;
			thread = new Thread(this);
			thread.start();
		} catch (SocketException e) {
			this.connected = false;
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void emmit(String msn, OnMessageRecive listenerMenssage) {
		this.listener = listenerMenssage;
		if (this.connected) {
			
			try {
				byte[] bytes = msn.getBytes();
				InetAddress host = InetAddress.getByName(this.ip);
				DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length, host, this.port);
				socket.send(datagramPacket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	public void disconnect() {
		this.connected = false;
		socket.close();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (this.connected) {
			byte[] buffer = new byte[1000];
			try {
				InetAddress host = InetAddress.getByName(this.ip);
				DatagramPacket response = new DatagramPacket(
						buffer, 
						buffer.length,
						host, this.port);
				socket.receive(response);
				if (response.getData() != null) {
					String msn = new String(response.getData());
					this.listener.OnMessage(msn);
				}
			} catch (IOException  e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
