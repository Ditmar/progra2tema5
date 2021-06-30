import tcp.*;
public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SocketTcp server = new SocketTcp(3000);
		server.start();
	}

}
