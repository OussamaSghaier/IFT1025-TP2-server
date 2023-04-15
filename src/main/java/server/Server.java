package server;

import javafx.util.Pair;
import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Cette classe est en charge de la création, la gestion et la communication du serveur.
 */
public class Server {
    /**
     * Commande pour s'inscrire
     */

    private final ServerSocket server;
    private Socket client;

    /**
     * Constructeur de la classe Server en charge d'initialiser le serveur
     *
     * @param port port spécifié
     * @throws IOException
     */
    public Server(int port) throws IOException {
        this.server = new ServerSocket(port);

    }


    /**
     * Fonction qui permet de démarrer le serveur.
     */
    public void run() {
        while (true) {
            try {
                client = server.accept();
                System.out.println("Connecté au client: " + client);
                ClientThreading clientThread = new ClientThreading(client);
                clientThread.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Cette classe interne permet de démarrer un nouveau thread.
     */
    public static class ClientThreading extends Thread{
        private final ArrayList<EventHandler> handlers;

        private Socket client;
        private ObjectInputStream objectInputStream;
        private ObjectOutputStream objectOutputStream;
        /**
         * Commande pour s'inscrire
         */
        public final static String REGISTER_COMMAND = "INSCRIRE";
        /**
         * Commande pour voir les cours disponibles
         */
        public final static String LOAD_COMMAND = "CHARGER";
        public ClientThreading(Socket client){

            this.client = client;
            this.handlers = new ArrayList<EventHandler>();
            this.addEventHandler(this::handleEvents);

        }
        /**
         * Fonction qui permet d'ajouter des EventHandler.
         *
         * @param h handler
         */
        public void addEventHandler(EventHandler h) {
            this.handlers.add(h);
        }

        /**
         *  Appelle la fonction `handle()` de tous les
         *  handlers définis avec la commande et l'argument spécifié.
         *
         * @param cmd commande spécifié
         * @param arg argument de la commande
         */
        private void alertHandlers(String cmd, String arg) {
            for (EventHandler h : this.handlers) {
                h.handle(cmd, arg);
            }
        }

        /**
         * Cette fonction agit en tant que boucle d'évènements qui attends que l'utilisateur
         * envoie une commande pour qu'elle soit traitée.
         *
         * @throws IOException
         * @throws ClassNotFoundException
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
         * Cette fonction permet de distinguer la commande de l'argument
         *
         * @param line ligne de commande entrée par l'utilisateur
         * @return une combinaison avec cmd comme clé et args comme valeur
         */
        public Pair<String, String> processCommandLine(String line) {
            String[] parts = line.split(" ");
            String cmd = parts[0];
            String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
            return new Pair<>(cmd, args);
        }

        /**
         * Cette fonction permet au client de se déconnecter.
         *
         * @throws IOException
         */
        public void disconnect() throws IOException {
            objectOutputStream.close();
            objectInputStream.close();
            client.close();
        }

        /**
         * Cette fonction fait appel aux méthodes handleRegistration ou handleLoadCourses si
         * la commande est équivalent à l'attribut REGISTER_COMMAND ou LOAD_COMMAND.
         *
         * @param cmd commande spécifié
         * @param arg argument de la commande
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
         */
        public void handleLoadCourses(String arg) {
            // TODO: implémenter cette méthode
            try {
                //Récupère les données du fichier cours.txt

                String fileName = "data/cours.txt";
                FileReader fr = new FileReader(fileName);
                BufferedReader reader = new BufferedReader(fr);
                String s;
                //Arraylist Contenant les cours disponibles
                ArrayList <Course> listeCours = new ArrayList<Course>();

                while ((s = reader.readLine()) != null) {
                    String[] infoCours = s.split("\t");
                    //Création des objets de chaque cours
                    String code = infoCours[0]; String name = infoCours[1];  String session = infoCours[2];
                    Course cours = new Course(name, code, session);
                    //filtre les cours spécifié par le client
                    if (cours.getSession().equals(arg)) {
                        listeCours.add(cours);
                    }
                }
                reader.close();
                //Envoi au client
                objectOutputStream.writeObject(listeCours);
                objectOutputStream.flush();

            } catch (FileNotFoundException ex) {
                System.out.println("Erreur à l'ouverture du fichier");
            } catch (IOException ex) {
                System.out.println("Erreur à l'écriture de l'objet");
            }


        }

        /**
         Récupérer l'objet 'RegistrationForm' envoyé par le client en utilisant 'objectInputStream', l'enregistrer dans un fichier texte
         et renvoyer un message de confirmation au client.tr
         La méthode gére les exceptions si une erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier ou dans le flux de sortie.
         */
        public void handleRegistration() {

            // TODO: implémenter cette méthode
            try {

                RegistrationForm inscription = null;
                //Attente de l'envoi du RegistrationForm
                while (inscription == null){
                    inscription = (RegistrationForm)objectInputStream.readObject();

                    //Envoi de la fiche d'inscription dans le fichier inscription.txt
                    String ficheInscription =   inscription.getCourse().getSession() +"\t"+
                            inscription.getCourse().getCode()+"\t"+
                            inscription.getMatricule()+"\t"+
                            inscription.getPrenom() +"\t"+
                            inscription.getNom()+"\t"+
                            inscription.getEmail() +"\n" ;
                    String fileName = "data/inscription.txt";
                    FileWriter fw = new FileWriter(fileName, true);
                    BufferedWriter writer = new BufferedWriter(fw);
                    writer.write(ficheInscription);
                    writer.close();

                    //Envoi message de confirmation
                    String confirmation = "Félicitations! Inscription réussie de "+
                            inscription.getPrenom() + " au cours de " +
                            inscription.getCourse().getCode() + ".";
                    objectOutputStream.writeObject(confirmation);
                    objectOutputStream.flush();
                }


            }catch(ClassNotFoundException ex){
                System.out.println("Erreur: La classe lue n'existe pas dans le programme");
            }catch(IOException ex){
                ex.printStackTrace();
                System.out.println("Erreur à la lecture du fichier");
            }
        }

        @Override
        public void run(){
            try{
                objectInputStream = new ObjectInputStream(client.getInputStream());
                objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                listen();
                disconnect();
                System.out.println("Client déconnecté!");

            }catch(Exception ex){
                ex.printStackTrace();

            }
        }
    }


}

