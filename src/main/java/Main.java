import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
  public static void main(String[] args) {
    try {
      ServerSocket serverSocket = new ServerSocket(4221);
      serverSocket.setReuseAddress(true);

      Socket socket = serverSocket.accept(); // Wait for connection from client.
      System.out.println("accepted new connection");

      socket.getOutputStream().write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }
  }
}
