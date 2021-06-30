package tcp;

import java.net.Socket;

public interface onEventLister {
	public void join(String channelName, ServerClientSocket client);
}
