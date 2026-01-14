import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {
	
	public static void main(String[] args) {
		final int port = 6666;
    int counter = 0;
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			System.out.println("Server listening on port " + port);
			while (true) {
				System.out.println("Waiting for client ");
				Socket socket = serverSocket.accept();
        OutputStream outputStream = socket.getOutputStream();
				DataOutputStream out = new DataOutputStream(outputStream);
				String msg = "hello-" + counter;
        counter++;
				out.writeUTF(msg);
				out.flush();
				System.out.println("Sent to " + socket.getRemoteSocketAddress() + ": " + msg);
			}
    // close streams and sockets
		} catch (Exception ex) {
        System.out.println("Something went wrong: " + ex);
    }
	}

}

