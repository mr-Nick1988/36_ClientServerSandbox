package telran;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalTime;


public class Client {
    public static final String SERVER_HOST = "127.0.0.1";
    public static final int SERVER_PORT = 9000;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             var writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             var consoleReader = new BufferedReader(new InputStreamReader(System.in));

        ) {
            customClientMessage("Connected to server: " + SERVER_HOST + ":" + SERVER_PORT);
            String message;
            System.out.println("Enter your message or 'exit' to quit");
            while (!"exit".equalsIgnoreCase(message = consoleReader.readLine())) {
                writer.println(message);
                customClientMessage("Sent: " + message);

                String response = reader.readLine();
                if (response != null) {
                    customClientMessage("Received response: " + response);
                } else {
                    customClientMessage("Server disconnected.");
                    break;
                }
                System.out.println("Enter your message or 'exit' to quit");
            }
            customClientMessage("Disconnected from server.");

        } catch (UnknownHostException e) {
            System.out.println("Unknown host ERROR");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void customClientMessage(String message) {
        String formattedMessage = String.format("[%s] \u001B[35m[CLIENT]\u001B[0m %s", LocalTime.now(), message);
        System.out.println(formattedMessage);
    }
}

