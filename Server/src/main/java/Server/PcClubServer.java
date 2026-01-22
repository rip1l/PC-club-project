package Server;

public class PcClubServer {
    public static final int PORT = 9090;

    public static void main(String[] args) {
        try {
            Server server = new Server(PORT);
            new Thread(server).start();
            // server.stop();
        } catch (Exception e) {
            System.err.println("Ошибка при запуске сервера: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
