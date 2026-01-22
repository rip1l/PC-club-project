package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {
    protected int serverPort = 9090;
    protected ServerSocket serverSocket = null;
    protected boolean isStopped = false;
    private ExecutorService executorService;

    public Server(int port) {
        this.serverPort = port;
        this.executorService = Executors.newFixedThreadPool(10); // Пул потоков для обработки клиентов
    }

    @Override
    public void run() {
        openServerSocket();
        while (!isStopped()) {
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
                executorService.submit(new ClientHandler(clientSocket)); // Передаем обработку клиентского соединения в пул
                System.out.println("Клиент подключен.");
            } catch (IOException e) {
                if (isStopped()) {
                    System.out.println("Сервер остановлен.");
                    return;
                }
                System.err.println("Ошибка при принятии подключения клиента: " + e.getMessage());
                continue; // Игнорируем ошибку и продолжаем ожидание новых подключений
            }
        }
        executorService.shutdown(); // Закрытие пула потоков
        System.out.println("Сервер остановлен.");
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        System.out.println("Остановка сервера...");
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            System.err.println("Ошибка при закрытии сервера: " + e.getMessage());
            throw new RuntimeException("Ошибка при закрытии сервера", e);
        }
    }

    private void openServerSocket() {
        System.out.println("Открытие сокета сервера...");
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            System.err.println("Не удалось открыть порт " + this.serverPort + ": " + e.getMessage());
            throw new RuntimeException("Не удалось открыть порт " + this.serverPort, e);
        }
    }
}
