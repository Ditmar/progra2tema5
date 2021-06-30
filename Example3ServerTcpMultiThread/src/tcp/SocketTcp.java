package tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class SocketTcp {
	private Integer port;
	private ServerSocket serverSocket;
	private Boolean isStart = false;
	private HashMap<String, ArrayList<ServerClientSocket>> channelList;
	public SocketTcp(Integer port) {
		this.port = port;
		channelList = new HashMap<>();
	}
	public void start() {
		try {
			serverSocket = new ServerSocket(this.port);
			System.out.println("---> Server running in " + this.port);
			this.isStart = true;
			while(this.isStart) {
				Socket clientSocket = serverSocket.accept();
				ServerClientSocket client = new ServerClientSocket(clientSocket, this);
				client.start();
				client.addEventListener((String name, ServerClientSocket clientJoin) -> {
					ArrayList<ServerClientSocket> checkCannel = channelList.get(name);
					if (checkCannel == null) {
						checkCannel = new ArrayList<ServerClientSocket>();
						checkCannel.add(clientJoin);
						channelList.put(name, checkCannel);
					} else {
						checkCannel.add(clientJoin);
					}
				});
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void sendAllmessages(String room, PackageData sendPackage) {
		ArrayList<ServerClientSocket> listRoomChannel = channelList.get(room);
		if (listRoomChannel == null) {
			System.out.println("La sala no fue creada cree una sala con el comando :join <nombre de la sala>");
			return;
		}
		for (Integer i = 0; i < listRoomChannel.size(); i++) {
			listRoomChannel.get(i).sendMenssage(sendPackage);
		}
	}
	public void stop() {
		try {
			serverSocket.close();
			this.isStart = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public HashMap<String, ArrayList<ServerClientSocket>> getChannelList() {
		return channelList;
	}
	public void setChannelList(HashMap<String, ArrayList<ServerClientSocket>> channelList) {
		this.channelList = channelList;
	}
	public ServerSocket getServerSocket() {
		return serverSocket;
	}
	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
}
