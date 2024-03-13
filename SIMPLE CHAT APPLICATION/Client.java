import java.io.*;
import java.net.*;

public class Client {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public void start(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port);
            System.out.println("Connected to server: " + socket.getInetAddress());

            // Initialize streams
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Start a new thread to listen for messages from the server
            new Thread(() -> {
                try {
                    startListening();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Start sending messages from the client
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
            System.out.println("Server: " + inputLine);
        }
    }

    private void startSendingMessages() throws IOException {
        BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
        String clientResponse;
        while (true) {
            System.out.print("You: ");
            clientResponse = consoleInput.readLine();
            out.println(clientResponse);
        }
    }

    private void stop() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.start("192.168.29.168", 6666); // Specify the server's IP address and port number
    }
}
