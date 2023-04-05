package server;

/**
 * Interface fonctionnelle qui permet de définir les différents évènements possibles
 */
@FunctionalInterface
public interface EventHandler {
    void handle(String cmd, String arg);
}
