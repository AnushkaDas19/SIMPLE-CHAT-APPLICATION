import java.io.*;
import java.net.*;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server is listening on port " + port);

            // Accept client connection
            clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress());

            // Initialize streams
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Start a new thread to listen for messages from the client
            // new thread allow for asynchronous communication

            new Thread(() -> {
                try {
                    startListening();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Start sending messages from the server
            startSendingMessages();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stop();
        }
    }

    private void startListening() throws IOException {
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            System.out.println("Client: " + inputLine);
        }
    }

    private void startSendingMessages() throws IOException {
        BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
        String serverResponse;
        while (true) {
            System.out.print("You: ");
            serverResponse = consoleInput.readLine();
            out.println(serverResponse);
        }
    }

    private void stop() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null) clientSocket.close();
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start(6666); // Specify the port number
    }
}
