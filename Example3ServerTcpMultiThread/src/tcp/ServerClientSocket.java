package tcp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerClientSocket extends Thread{
	private Socket clientSocket;
	private ObjectInputStream inDataServer;
	private ObjectOutputStream outDataServer;
	private Boolean isConnected;
	private SocketTcp  server;
	private onEventLister listener;
	private String room = "";
	public ServerClientSocket(Socket clientSocket, SocketTcp server) {
		this.clientSocket = clientSocket;
		this.server = server;
		try {
			outDataServer = new ObjectOutputStream(this.clientSocket.getOutputStream());
			inDataServer = new ObjectInputStream(this.clientSocket.getInputStream());
			this.isConnected = true;
			System.out.println("Cliente Conectado " + this.clientSocket.getLocalAddress() + " port " + this.clientSocket.getLocalPort());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void addEventListener(onEventLister listen) {
		this.listener = listen;
	}
	public void sendMenssage(PackageData sendPackage) {
		try {
			outDataServer.writeObject(sendPackage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		while(this.isConnected) {
			try {
				PackageData data =  (PackageData)inDataServer.readObject();
				System.out.println(data.getNick() + ">" + data.getMsn() + " " + data.getSenddate().toGMTString());
					if (data.getMsn().equals(":quit")) {
						PackageData quitMessage = new PackageData("Server",":quit", this.server.getServerSocket().getInetAddress().getHostName() );
						PackageData serverPackage = new PackageData("Server","Tu enviaste " + data.getMsn(), this.server.getServerSocket().getInetAddress().getHostName() );
						outDataServer.writeObject(serverPackage);

						this.isConnected = false;
					} else if (data.getMsn().contains(":join")) {
						String[] command = data.getMsn().split(" ");
						if (command.length == 2 && command[0].equals(":join")) {
							this.listener.join(command[1], this);
							room = command[1];
						}
					} 
					else {
						PackageData sendPacket = new PackageData(data.getNick(), data.getMsn(), this.server.getServerSocket().getInetAddress().getHostName() );
						this.server.sendAllmessages(room, sendPacket);
					}
				
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		disconnect();
	}
	public void disconnect() {
		try {
			this.clientSocket.close();
			this.isConnected = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
