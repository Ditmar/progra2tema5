package example1;

import java.util.Scanner;

import udp.SocketUdp;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("APP SOCKET!");
		SocketUdp socket = new SocketUdp("localhost", 3000);
		socket.connect();
		Boolean terminate = false;
		Scanner readData = new Scanner(System.in);
		while (!terminate) {
			String dataketboard = readData.nextLine();
			socket.emmit(dataketboard, (String responseServer) -> {
				System.out.println(responseServer);
			});
			if (dataketboard.equals("exit")) {
				terminate = true;
			}
		}
		socket.disconnect();
	}

}
