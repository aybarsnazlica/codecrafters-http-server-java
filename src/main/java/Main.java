import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(4221)) {
            serverSocket.setReuseAddress(true);

            Socket socket = serverSocket.accept();
            System.out.println("Accepted new connection");

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = reader.readLine();

            if (line != null) {
                sendResponse(line, reader, socket);
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

    private static void sendResponse(String line, BufferedReader reader, Socket socket) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        String[] tokens = line.split(" ");
        String httpMethod = tokens[0];
        String requestTarget = tokens[1];
        String httpVersion = tokens[2];
        String response;

        if ("GET".equals(httpMethod)) {
            if ("/".equals(requestTarget)) {
                response = String.format("%s 200 OK\r\n\r\n", httpVersion);
            } else if (requestTarget.startsWith("/user-agent")) {
                reader.readLine(); // Read and ignore the second line
                String line3 = reader.readLine();
                String[] tokens3 = line3.split(" ");
                String responseBody = tokens3[1];
                response = String.format("%s 200 OK\r\nContent-Type: text/plain\r\nContent-Length: %d\r\n\r\n%s", httpVersion, responseBody.length(), responseBody);
            } else if (requestTarget.startsWith("/echo")) {
                String msg = requestTarget.split("/")[2];
                response = String.format("%s 200 OK\r\nContent-Type: text/plain\r\nContent-Length: %d\r\n\r\n%s", httpVersion, msg.length(), msg);
            } else {
                response = String.format("%s 404 Not Found\r\n\r\n", httpVersion);
            }
            outputStream.write(response.getBytes());
            outputStream.flush();
        }
    }
}