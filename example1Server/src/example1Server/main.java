package example1Server;

import java.util.Scanner;

import udp.Server;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Server server = new Server(3000);
		server.startServer();
		Scanner readkeyboard = new Scanner(System.in);
		String data = readkeyboard.nextLine();
		if (data.equals("q")) {
			server.stopServer();
			return;
		}
	}

}
