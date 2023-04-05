package server;

/**
 * Cette classe est en charge d'amorcer le serveur.
 */
public class ServerLauncher {
    /**
     * Port où le serveur se connecte
     */
    public final static int PORT = 1337;

    /**
     * Cette fonction est en charge de créer le serveur et de le démarrer
     * @param args argument du terminal
     */
    public static void main(String[] args) {
        Server server;
        try {
            server = new Server(PORT);
            System.out.println("Server is running...");
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}