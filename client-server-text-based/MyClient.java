import java.io.DataInputStream;
import java.io.InputStream;
import java.net.Socket;

public class MyClient {
	
	public static void main(String[] args) {
		final String host = "localhost";
		final int port = 6666;
		try {
			Socket socket = new Socket(host, port);
			InputStream inputStream = socket.getInputStream();
			DataInputStream in = new DataInputStream(inputStream);
			String msg = in.readUTF();
			System.out.println("Received: " + msg);
		} catch (Exception ex) {
			System.out.println("Something went wrong: " + ex);
		}
	}

}

