import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {
    public static void main(String[] args) {
        final int port = 6666;
        final int maxClients = 5;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on port " + port + " (will accept " + maxClients + " clients)...");
            for (int i = 1; i <= maxClients; i++) {
                System.out.println("Waiting for client " + i + "/" + maxClients + "...");
                try (Socket socket = serverSocket.accept();
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
                    String msg = "hello-" + i;
                    out.writeUTF(msg);
                    out.flush();
                    System.out.println("Sent to " + socket.getRemoteSocketAddress() + ": " + msg);
                }
            }
            System.out.println("Done. Accepted " + maxClients + " clients. Server exiting.");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
