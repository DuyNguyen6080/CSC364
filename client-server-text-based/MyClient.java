import java.io.DataInputStream;
import java.net.Socket;

public class MyClient {
    public static void main(String[] args) {
        final String host = "localhost";
        final int port = 6666;
        try (Socket socket = new Socket(host, port);
             DataInputStream in = new DataInputStream(socket.getInputStream())) {
            String msg = in.readUTF();
            System.out.println("Received: " + msg);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
