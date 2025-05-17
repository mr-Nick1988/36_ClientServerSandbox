package telran;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static final int PORT = 9000;
    public static final int THREAD_POOL_SIZE = 10;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            customServerMessage("Server started on port " + PORT);
            while (true) {
                customServerMessage("Waiting for a new client...");
                Socket socket = serverSocket.accept();
                customServerMessage("Client connected : " + socket.getInetAddress() + ": " + socket.getPort());

                ClientHandler clientHandler = new ClientHandler(socket);
                executorService.execute(clientHandler);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            executorService.shutdown();
        }
    }
    public static void customServerMessage(String message) {
        String formattedMessage = String.format("[%s] \u001B[32m[SERVER]\u001B[0m %s", LocalTime.now(), message);
        System.out.println(formattedMessage);
    }

}
