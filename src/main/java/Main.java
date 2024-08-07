import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(4221)) {
            serverSocket.setReuseAddress(true);

            Socket socket = serverSocket.accept();
            System.out.println("Accepted new connection");

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream outputStream = socket.getOutputStream();

            String line = reader.readLine();

            if (line != null) {
                sendResponse(line, reader, outputStream, socket);
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

    private static void sendResponse(String line, BufferedReader reader, OutputStream outputStream, Socket socket) throws IOException {
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
            socket.close();
        }
    }
}