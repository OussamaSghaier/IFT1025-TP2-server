package client.MVC;

import javafx.scene.control.Alert;
import server.models.Course;
import server.models.RegistrationForm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;


public class Controleur {
    private Course modeleCourse;
    private RegistrationForm modeleRegistration;
    private ClientGUI vue;

    public Controleur(ClientGUI vue){
        this.vue                = vue;
    }
    public void formulaireValide(){
        if(vue.getTextNom().equals("") || vue.getTextPrenom().equals("") ||
                vue.getTextEmail().equals("") || vue.getTextMatricule().equals("")){
            Alert emptyField = new Alert(Alert.AlertType.ERROR);
            emptyField.setTitle("Error");
            emptyField.setHeaderText("Error");
            emptyField.setContentText("Le formulaire est invalide."+"\n"+"Certains champs sont vides.");
            emptyField.showAndWait();
        }else if(!verifEmail(vue.getTextEmail()) && !verifMatricule(vue.getTextMatricule())){
            Alert emptyField = new Alert(Alert.AlertType.ERROR);
            emptyField.setTitle("Error");
            emptyField.setHeaderText("Error");
            emptyField.setContentText("Le formulaire est invalide."+"\n"+"L'addrese courriel fourni est invalide."
                    +"\n"+"La matricule fourni est invalide.");
            vue.getEmail().setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
            vue.getMatricule().setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
            emptyField.showAndWait();

        }else if(vue.getCours().getSelectionModel().isEmpty()){
            Alert emptyField = new Alert(Alert.AlertType.ERROR);
            emptyField.setTitle("Error");
            emptyField.setHeaderText("Error");
            emptyField.setContentText("Le formulaire est invalide."+"\n"+"Aucun cours sélectionné.");
            vue.getCours().setStyle("-fx-border-color: red");
            emptyField.showAndWait();
        }else if(!verifEmail(vue.getTextEmail())){
            Alert emptyField = new Alert(Alert.AlertType.ERROR);
            emptyField.setTitle("Error");
            emptyField.setHeaderText("Error");
            emptyField.setContentText("Le formulaire est invalide."+"\n"+"L'addrese courriel fourni est invalide.");
            vue.getEmail().setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
            emptyField.showAndWait();
        }else if(!verifMatricule(vue.getTextMatricule())){
            Alert emptyField = new Alert(Alert.AlertType.ERROR);
            emptyField.setTitle("Error");
            emptyField.setHeaderText("Error");
            emptyField.setContentText("Le formulaire est invalide."+"\n"+"La matricule fournie est invalide.");
            vue.getMatricule().setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
            emptyField.showAndWait();
        } else{
            try{
                Course coursChoisi = (Course) vue.getCours().getSelectionModel().getSelectedItem();
                String nomCours     = coursChoisi.getName();
                String code         = coursChoisi.getCode();
                String session      = coursChoisi.getSession();
                String nom          = vue.getTextNom();
                String prenom       = vue.getTextPrenom();
                String email        = vue.getTextEmail();
                String matricule    = vue.getTextMatricule();

                Socket clientSocket = new Socket("127.0.0.1", 1337);
                Course cours = new Course(nomCours, code, session);

                RegistrationForm ficheInscription = new RegistrationForm(prenom,nom, email,matricule, cours );
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
            }catch(IOException ex) {
                ex.printStackTrace();
            }catch (ClassNotFoundException ex){
                System.out.println("Erreur: Classe Introuvable");
            }
        }
    }
    public boolean verifMatricule(String matricule){
        return matricule.length() == 8 && matricule.matches("[0-9]+");
    }
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
