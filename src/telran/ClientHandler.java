package telran;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalTime;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
                var inputStream = clientSocket.getInputStream();
                var outputStream = clientSocket.getOutputStream();
                var reader = new BufferedReader(new InputStreamReader(inputStream));
                var writer = new PrintWriter(outputStream, true);
        ) {
            String clientAddress = clientSocket.getInetAddress().toString();
            int clientPort = clientSocket.getPort();

            customClientMessage(clientAddress, clientPort, "Client connected.");

            String message;
            while ((message = reader.readLine()) != null) {
                customClientMessage(clientAddress, clientPort, "Received: " + message);
                String response = "Your message received: " + message + " at " + LocalTime.now();
                writer.println(response);
            }
            customClientMessage(clientAddress, clientPort, "Client disconnected.");
        } catch (IOException e) {
           throw new RuntimeException(e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                 throw new RuntimeException(e);
            }
        }
    }
    private void customClientMessage(String clientAddress, int port, String message) {
        String formattedMessage = String.format("[%s] \u001B[34m[CLIENT %s:%d]\u001B[0m %s",
                LocalTime.now(), clientAddress, port, message);
        System.out.println(formattedMessage);
    }
}

