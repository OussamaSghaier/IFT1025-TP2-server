package client.MVC;

import javafx.scene.control.Alert;
import server.models.Course;
import server.models.RegistrationForm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Cette classe est en charge de gérer les interactions entre la vue et les modèles.
 */
public class Controleur {
    private Course modeleCourse;
    private RegistrationForm modeleRegistration;
    private ClientGUI vue;

    public Controleur(ClientGUI vue){
        this.vue = vue;
    }

    /**
     * Cette fonction permet de vérifier si le formulaire est valide et de procéder à
     * l'inscription si c'est le cas.
     */
    public void formulaireValide() {

        int erreur = 0;
        try {
            if (vue.getTextNom().equals("") || vue.getTextPrenom().equals("") ||
                    vue.getTextEmail().equals("") || vue.getTextMatricule().equals("")) {
                erreur = 1;
                throw new IllegalArgumentException();
            } else if (!verifEmail(vue.getTextEmail()) && !verifMatricule(vue.getTextMatricule())) {
                erreur = 2;
                throw new IllegalArgumentException();
            } else if (vue.getCours().getSelectionModel().isEmpty()) {
                erreur = 3;
                throw new IllegalArgumentException();
            } else if (!verifEmail(vue.getTextEmail())) {
                erreur = 4;
                throw new IllegalArgumentException();
            } else if (!verifMatricule(vue.getTextMatricule())) {
                erreur = 5;
                throw new IllegalArgumentException();
            }
            Course coursChoisi = (Course) vue.getCours().getSelectionModel().getSelectedItem();
            String nomCours = coursChoisi.getName();
            String code = coursChoisi.getCode();
            String session = coursChoisi.getSession();
            String nom = vue.getTextNom();
            String prenom = vue.getTextPrenom();
            String email = vue.getTextEmail();
            String matricule = vue.getTextMatricule();

            Socket clientSocket = new Socket("127.0.0.1", 1337);
            Course cours = new Course(nomCours, code, session);

            RegistrationForm ficheInscription = new RegistrationForm(prenom, nom, email, matricule, cours);
            ObjectOutputStream writer = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream receiver = new ObjectInputStream(clientSocket.getInputStream());

            writer.writeObject("INSCRIRE");
            writer.flush();
            writer.writeObject(ficheInscription);
            writer.flush();

            String confirmation = receiver.readObject().toString();
            Alert emptyField = new Alert(Alert.AlertType.INFORMATION);
            emptyField.setTitle("Message");
            emptyField.setHeaderText("Message");
            emptyField.setContentText(confirmation);
            emptyField.showAndWait();
            vue.getCours().setStyle("-fx-border-color: null ");
            vue.getEmail().setStyle("-fx-focus-color: blue ;");
            vue.getMatricule().setStyle(" -fx-focus-color: blue ;");
            vue.getEmail().setText("");
            vue.getMatricule().setText("");
            vue.getNom().setText("");
            vue.getPrenom().setText("");


            writer.close();
            receiver.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            System.out.println("Erreur: Classe Introuvable");
        } catch (IllegalArgumentException ex) {
            switch (erreur) {
                case(1):
                    vue.errorAlert("Le formulaire est invalide." + "\n" + "Certains champs sont vides.");
                    break;
                case(2):
                    vue.errorAlert("Le formulaire est invalide." + "\n" + "L'addrese courriel fourni est invalide."
                            + "\n" + "La matricule fourni est invalide.");
                    vue.getEmail().setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    vue.getMatricule().setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    break;
                case(3):
                    vue.errorAlert("Le formulaire est invalide." + "\n" + "Aucun cours sélectionné.");
                    vue.getCours().setStyle("-fx-border-color: red");
                    break;
                case(4):
                    vue.errorAlert("Le formulaire est invalide." + "\n" + "L'addrese courriel fournie est invalide.");
                    vue.getEmail().setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
                    break;
                case(5):
                    vue.errorAlert("Le formulaire est invalide." + "\n" + "La matricule fournie est invalide.");
                    vue.getMatricule().setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");


            }
        }

    }

    /**
     * Cette fonction est en charge de vérifier que la matricuel est valide.
     * @param matricule matricule à vérifier
     * @return boolean true ou false
     */
    public boolean verifMatricule(String matricule){
        return matricule.length() == 8 && matricule.matches("[0-9]+");
    }

    /**
     * Cette fonction est en charge de vérifier que l'email est valide.
     * @param email email à vérifier
     * @return boolean true ou false
     */
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

    /**
     * Cette fonction est en charge de récupérer les cours de la session spécifié.
     * @param session session spécifié
     */
    public void loadClasses(String session){
        try{
            String command = null;
            if (session.equals("Automne")){
                command = "CHARGER Automne";
            }else if(session.equals("Hiver")){
                command = "CHARGER Hiver";
            }else if(session.equals("Été")){
                command = "CHARGER Ete";
            }

            Socket clientSocket = new Socket("127.0.0.1", 1337);
            ObjectOutputStream writer = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream receiver = new ObjectInputStream(clientSocket.getInputStream());

            writer.writeObject(command);
            writer.flush();

            ArrayList<Course> listServer = null;

            listServer = (ArrayList) receiver.readObject();
            vue.addClass(listServer);
            writer.close();
            receiver.close();


        }catch(IOException | IllegalArgumentException ex){
            ex.printStackTrace();
        }catch (ClassNotFoundException ex){
            System.out.println("Erreur: Classe Introuvable");
        }
    }
}
