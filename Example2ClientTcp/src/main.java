import java.util.Scanner;

import tcp.PackageData;
import tcp.SocketTcp;

public class main {
	static Boolean connect = true;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//localhost = 127.0.0.1
		Scanner read = new Scanner(System.in);
		
		System.out.println("Cual es su nickname");
		String nickname = read.nextLine();
		//localhost
		SocketTcp client = new SocketTcp("127.0.0.1", 3000);
		client.connected();
		connect = true;
		String msn = "";
		while (connect) {
			msn = read.nextLine();
			PackageData data = new PackageData(nickname, msn, "localhost");
			if (msn.equals(":quit")) {
				connect = false;
				break;
			}
			client.emmit(data, (response) -> {
				System.out.println(response.getNick() + ">" + response.getMsn() + " " + response.getSenddate().toGMTString());
				if (response.getMsn().equals(":quit")) {
					connect = false;
				}
			});
			
		}
		PackageData data = new PackageData("Ditmar", msn, "localhost");
		client.emmit(data, (response) -> {
			System.out.println(response.getNick() + ">" + response.getMsn() + " " + response.getSenddate().toGMTString());
			client.dissConnect();
		});
		
	}
}
