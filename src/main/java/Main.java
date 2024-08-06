import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Main {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(4221);
            serverSocket.setReuseAddress(true);

            Socket socket = serverSocket.accept(); // Wait for connection from client.
            System.out.println("accepted new connection");

            InputStream inputStream = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            OutputStream outputStream = socket.getOutputStream();

            String line = reader.readLine();
            String[] tokens = line.split(" ");

            String httpMethod = tokens[0];
            String requestTarget = tokens[1];
            String httpVersion = tokens[2];

            if (httpMethod.equals("GET")) {
                if (requestTarget.equals("/")) {
                    outputStream.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
                } else {
                    outputStream.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
                }
            }


        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}
