package server;

import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import server.models.RegistrationForm;

public class Server {
    /**
     * Commande pour enregistrer un étudiant
     */
    public final static String REGISTER_COMMAND = "INSCRIRE";
    /**
     * Commande pour charger les cours
     */
    public final static String LOAD_COMMAND = "CHARGER";
    /**
     * Commande pour quitter le serveur
     */
    private final ServerSocket server;
    /**
     * Socket du client
     */
    private Socket client;
    /**
     * Flux d'entrée
     */
    private ObjectInputStream objectInputStream;
    /**
     * Flux de sortie
     */
    private ObjectOutputStream objectOutputStream;
    /**
     * Liste des gestionnaires d'événements
     */
    private final ArrayList<EventHandler> handlers;

    /**
     * Constructeur du serveur
     * @param port le port sur lequel le serveur écoute
     */
    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
    }
    /**
     * Ajouter un gestionnaire d'événements
     * @param h le gestionnaire d'événements
     */
    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }
    /**
     * Appeler les gestionnaires d'événements
     * @param cmd la commande
     * @param arg les arguments
     */
    private void alertHandlers(String cmd, String arg) {
        for (EventHandler h : this.handlers) {
            h.handle(cmd, arg);
        }
    }
    
    /**
     * Lancer le serveur
     */
    public void run() {
        while (true) {
            try {
                client = server.accept();
                System.out.println("Connecté au client: " + client);
                objectInputStream = new ObjectInputStream(client.getInputStream());
                objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                listen();
                disconnect();
                System.out.println("Client déconnecté!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Ecouter les commandes envoyées par le client
     */
    public void listen() throws IOException, ClassNotFoundException {
        String line;
        if ((line = this.objectInputStream.readObject().toString()) != null) {
            Pair<String, String> parts = processCommandLine(line);
            String cmd = parts.getKey();
            String arg = parts.getValue();
            this.alertHandlers(cmd, arg);
        }
    }
    /**
     * Traiter la commande envoyée par le client
     * @param line la commande 
     * @return une paire contenant la commande et les arguments
     */
    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }
    /**
     * Déconnecter le client
     */
    public void disconnect() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }
    /**
     * Gérer les événements
     * @param cmd la commande
     * @param arg les arguments
     */
    public void handleEvents(String cmd, String arg) {
        if (cmd.equals(REGISTER_COMMAND)) {
            handleRegistration();
        } else if (cmd.equals(LOAD_COMMAND)) {
            handleLoadCourses(arg);
        }
    }

    /**
     Lire un fichier texte contenant des informations sur les cours et les transofmer en liste d'objets 'Course'.
     La méthode filtre les cours par la session spécifiée en argument.
     Ensuite, elle renvoie la liste des cours pour une session au client en utilisant l'objet 'objectOutputStream'.
     La méthode gère les exceptions si une erreur se produit lors de la lecture du fichier ou de l'écriture de l'objet dans le flux.
     @param arg la session pour laquelle on veut récupérer la liste des cours
     @throws IOException
     */
    public void handleLoadCourses(String arg) {
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/server/data/cours.txt"))) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }

            List<String> filteredLines = new ArrayList<>();
            for (String l : lines) {
                String[] parts = l.split("\t");
                if (parts.length >= 3 && parts[2].equals(arg)) {
                    filteredLines.add(l);
                }
            }
            System.out.println("Sending courses for session " + arg + " to client");
            System.out.println(filteredLines);
            objectOutputStream.writeObject(filteredLines);
        } catch (IOException e) {
            System.err.println("Error reading file or writing object to stream: " + e.getMessage());
        }
    }
    
    /**
     Récupérer l'objet 'RegistrationForm' envoyé par le client en utilisant 'objectInputStream', l'enregistrer dans un fichier texte
     et renvoyer un message de confirmation au client.
     La méthode gére les exceptions si une erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier ou dans le flux de sortie.
     @throws IOException
     @throws ClassNotFoundException
    */
    public void handleRegistration() {
        try {
            RegistrationForm form = (RegistrationForm) objectInputStream.readObject();
            System.out.println("Received registration form from client");
            System.out.println(form);
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/java/server/data/inscriptions.txt", true))) {
                bw.write(form.toString());
                bw.newLine();
                bw.flush();
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }
            objectOutputStream.writeObject("Inscription réussie!");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading object from stream: " + e.getMessage());
        }
    }
}

