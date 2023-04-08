package client;

import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class SimpleClient {
    public static void main(String[] args) {
        System.out.println("*** Bienvenue au portail d'inscription de cours de l'Udem ***");
        //Création du client
        SimpleClient client = new SimpleClient();
        client.messageAccueil();


        Scanner scanner = new Scanner(System.in);
        int etat = 0;
        String session = null;
        boolean run = true;
        while (run) {
            //Choix session
            if(etat == 0){
                int choix1 = Integer.parseInt(scanner.nextLine());
                if(choix1 == 1 || choix1 == 2 || choix1 == 3){
                    switch(choix1){
                        case 1: session = "Automne"; break;
                        case 2: session = "Hiver"  ; break;
                        case 3: session = "Ete"    ;
                    }
                    client.choixSession(choix1);
                    etat = 1;
                }else{
                    throw new IllegalArgumentException("Entrée invalide");
                }
            }
            //Choix inscription ou sélection d'une autre session
            if(etat == 1){
                int choix2 = Integer.parseInt(scanner.nextLine());
                if(choix2 == 1){
                    client.messageAccueil();
                    etat = 0;

                }else if(choix2 == 2){
                    etat = 2;
                }else{
                    throw new IllegalArgumentException("Entrée invalide");
                }
            }
            //Envoi de l'inscription
            if(etat == 2){

                System.out.print("\n"+"Veuiller saisir votre prénom: ");
                String prenom = scanner.nextLine();
                System.out.print("Veuiller saisir votre nom: ");
                String nom = scanner.nextLine();
                System.out.print("Veuiller saisir votre email: ");
                String email = scanner.nextLine();
                System.out.print("Veuiller saisir votre matricule: ");
                String matricule = scanner.nextLine();
                System.out.print("Veuiller saisir le code du cours: ");
                String code = scanner.nextLine();

                if(!client.verifEmail(email)){
                  throw new IllegalArgumentException("L'addrese courriel rentré est invalide");
                }
                if(!client.verifCodeCours(code, session)){
                    throw new IllegalArgumentException("Le code du cours rentré est invalide");
                }else{
                    client.inscription(nom, prenom, email, matricule, session, code);
                }
                run = false;
            }
        }
    }
    //Fonction qui affiche le message d'accueil
    public void messageAccueil(){

        System.out.println("Veuillez choisir la session pour laquelle vous voulez consulter la liste de cours");
        System.out.println("1.Automne" + "\n" + "2.Hiver" + "\n" + "3.Été");
        System.out.print("> Choix: ");

    }
    //Fonction en charge d'afficher les cours des différentes sessions
    public void choixSession(int choix){
        try{
            String saison = null;
            String session = null;
            if (choix == 1){
                saison = "automne";
                session = "CHARGER Automne";
            } else if (choix == 2) {
                saison = "hiver";
                session = "CHARGER Hiver";
            } else if (choix == 3) {
                saison = "été";
                session = "CHARGER Ete";
            }

            Socket clientSocket = new Socket("127.0.0.1", 1337);

            ObjectOutputStream writer = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream receiver = new ObjectInputStream(clientSocket.getInputStream());

            writer.writeObject(session);
            writer.flush();
            System.out.println("Les cours offerts pendant la session d'"+ saison +" sont:");

            ArrayList<Course> listServer = null;

            listServer = (ArrayList) receiver.readObject();
            for (int i = 0; i < listServer.size(); i++) {
                System.out.println((i+1)+ ". "+listServer.get(i).getCode()+"\t"+listServer.get(i).getName());
            }
            writer.close();
            receiver.close();

            System.out.println("> Choix: ");
            System.out.println("1. Consulter les cours offerts pour une autre session");
            System.out.println("2. Inscription à un cours");
            System.out.print("> Choix: ");



        }catch(IOException ex){
            ex.printStackTrace();
        }catch (ClassNotFoundException ex){
            System.out.println("Erreur: Classe Introuvable");
        }
    }
    //Fonction en charge de vérifier que le email fourni est valide
    public boolean verifEmail(String email){
        if (!email.contains("@")){
            return false;
        }else{
            String[] parts = email.split("@");

            if (parts[1].contains(".") && parts[0].length() > 0){
                String[] secondParts = parts[1].split("\\.");


                return secondParts[0].length() > 0 && secondParts[1].length() > 0;
            }else {
                return false;}
        }
    }
    //Fonction en charge de vérifier que le cours choisi correspond à la bonne session
    public boolean verifCodeCours(String code, String session){
        boolean check = false;
        try{
            session = "CHARGER "+ session;
            Socket clientSocket = new Socket("127.0.0.1", 1337);

            ObjectOutputStream writer = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream receiver = new ObjectInputStream(clientSocket.getInputStream());

            writer.writeObject(session);
            writer.flush();

            ArrayList<Course> listServer = null;

            listServer = (ArrayList) receiver.readObject();
            for (int i = 0; i < listServer.size(); i++) {
                if(listServer.get(i).getCode().equals(code)){
                    check = true;
                }
            }
            writer.close();
            receiver.close();


        }catch(IOException ex){
            ex.printStackTrace();
        }catch (ClassNotFoundException ex){
            System.out.println("Erreur: Classe Introuvable");
        }
        return check;
    }
    //Fonction en charge d'effectuer l'inscription
    public void inscription(String nom,String prenom, String email, String matricule, String session, String code){
        try{
            Socket clientSocket = new Socket("127.0.0.1", 1337);
            Course coursChoisi = new Course(nom, code, session);

            RegistrationForm ficheInscription = new RegistrationForm(prenom,nom, email,matricule, coursChoisi );
            ObjectOutputStream writer = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream receiver = new ObjectInputStream(clientSocket.getInputStream());

            writer.writeObject("INSCRIRE");
            writer.flush();
            writer.writeObject(ficheInscription);
            writer.flush();

            String confirmation = receiver.readObject().toString();

            System.out.println("\n" + confirmation);

            writer.close();
            receiver.close();

        }catch (IOException ex) {
            ex.printStackTrace();
        }catch (ClassNotFoundException ex){
            System.out.println("Erreur: Classe Introuvable");
        }
    }
}