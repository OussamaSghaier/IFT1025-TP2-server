package server;

@FunctionalInterface
public interface EventHandler {
    void handle(String cmd, String arg);
}
