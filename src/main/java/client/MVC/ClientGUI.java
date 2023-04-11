package client.MVC;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import server.models.Course;

import java.util.ArrayList;

/**
 * Classe en charge de créer tout l'aspect visuel du GUI et de démarrer le client.
 */
public class ClientGUI extends Application {

    private Text textPrenom, textNom, textEmail, textMatricule;
    private TextField prenom, nom, email, matricule;
    private Label formulaireInscription, listeCours;
    private Button charge, envoyer;
    private HBox container, root;
    private VBox leftSide;
    private Pane rightSide, separator, selectionSession;
    private Scene scene;
    private TableView<Course> cours;
    private ChoiceBox<String> session;
    private Controleur controleur;

    /**
     * Cette fonction permet d'ajouter des cours à la liste de cours d'une session donnée.
     * @param listServer liste de cours d'une session donnée
     */
    public void addClass(ArrayList<Course> listServer){
        for (int i = 0; i < listServer.size(); i++) {
            this.cours.getItems().add(listServer.get(i));

        }
    }

    /**
     * Cette fonction permet de créer un message d'erreur.
     * @param message message qui s'affiche pour l'utilisateur
     */
    public void errorAlert(String message){
        Alert emptyField = new Alert(Alert.AlertType.ERROR);
        emptyField.setTitle("Error");
        emptyField.setHeaderText("Error");
        emptyField.setContentText(message);
        emptyField.showAndWait();
    }

    /**
     * Cette fonction permet de récupérer l'objet email.
     * @return l'objet TextField email
     */
    public TextField getEmail(){
        return this.email;
    }

    /**
     * Cette fonction permet de récupérer l'objet prenom
     * @return l'objet TextField prenom
     */
    public TextField getPrenom(){
        return this.prenom;
    }

    /**
     * Cette fonction permet de récupérer l'objet nom
     * @return l'objet TextField nom
     */
    public TextField getNom(){
        return this.nom;
    }

    /**
     * Cette fonction permet de récupérer l'objet matricule
     * @return l'objet TextField matricule
     */
    public TextField getMatricule(){
        return this.matricule;
    }

    /**
     * Cette fonction permet de récupérer le texte du prénom.
     * @return String prenom
     */
    public String getTextPrenom(){
        return this.prenom.getText();
    }

    /**
     * Cette fonction permet de récupérer le texte du nom.
     * @return String nom
     */
    public String getTextNom(){
        return this.nom.getText();
    }

    /**
     * Cette fonction permet de récupérer le texte du email.
     * @return String email
     */
    public String getTextEmail(){
        return this.email.getText();
    }

    /**
     * Cette fonction permet de récupérer le texte de la matricule.
     * @return String matricule
     */
    public String getTextMatricule(){
        return this.matricule.getText();
    }

    /**
     * Cette fonction permet de récupérer l'objet cours.
     * @return l'objet Tableview cours
     */
    public TableView getCours(){
        return this.cours;
    }

    /**
     * Cette fonction démarre l'application.
     * @param args argument du terminal
     */
    public static void main(String[] args){
        ClientGUI.launch();
    }

    /**
     * Cette fonction crée tout l'aspect visuel de l'application.
     * @param primaryStage fenêtre de l'application
     */
    public void start(Stage primaryStage){
        try {
            controleur = new Controleur(this);
            root       = new HBox();
            scene      = new Scene(root, 600, 400);
            rightSide  = new Pane();
            leftSide   = new VBox();
            separator  = new Pane();

            leftSide.setPrefWidth(scene.getWidth()/2);
            separator.setPrefWidth(8);
            rightSide.setPrefWidth(scene.getWidth()/2);
            leftSide.setStyle("-fx-background-color: #f3f4e7");
            rightSide.setStyle("-fx-background-color: #f3f4e7");

            //Côté gauche
            //Elements
            listeCours             = new Label("Liste des cours");
            cours                  = new TableView<>();
            container              = new HBox();
            charge                 = new Button("charger");
            session                = new ChoiceBox<>();
            selectionSession       = new Pane();

            TableColumn<Course, String> code = new TableColumn<>("Code");
            TableColumn<Course, String> name = new TableColumn<>("Cours");
            code.setCellValueFactory(new PropertyValueFactory<Course, String>("code"));
            name.setCellValueFactory(new PropertyValueFactory<Course, String>("name"));
            code.prefWidthProperty().bind(cours.widthProperty().multiply(0.3));
            name.prefWidthProperty().bind(cours.widthProperty().multiply(0.7));

            cours.getColumns().add(code);
            cours.getColumns().add(name);
            container.getChildren().add(cours);
            session.getItems().add("Hiver");
            session.getItems().add("Été");
            session.getItems().add("Automne");
            session.setValue("Hiver");
            selectionSession.getChildren().add(session);
            selectionSession.getChildren().add(charge);

            //Positionnement
            session.setLayoutY(13);
            session.setLayoutX(30);
            charge.setLayoutY(13);
            charge.setLayoutX(150);
            //Couleur de fond
            container.setStyle("-fx-background-color: #f3f4e7");
            listeCours.setStyle("-fx-background-color:#f3f4e7");
            selectionSession.setStyle("-fx-background-color: #f3f4e7");
            //Alignement
            container.setAlignment(Pos.BASELINE_CENTER);
            listeCours.setAlignment(Pos.CENTER);
            //Taille
            listeCours.setPrefWidth(300);
            selectionSession.setPrefHeight(50);
            container.setPrefHeight(310);
            cours.setPrefWidth(275);
            cours.setPrefHeight(300);
            listeCours.setPrefHeight(40);
            //Format texte
            listeCours.setFont(Font.font("arial", 20));
            //Composition Côté gauche
            leftSide.getChildren().add(listeCours);
            leftSide.getChildren().add(container);
            leftSide.getChildren().add(selectionSession);

            //Côté droit
            //Éléments
            formulaireInscription  = new Label("Formulaire d'inscription");
            envoyer                = new Button("envoyer");
            prenom                 = new TextField();
            nom                    = new TextField();
            email                  = new TextField();
            matricule              = new TextField();
            textPrenom             = new Text("Prenom");
            textNom                = new Text("Nom");
            textEmail              = new Text("Email");
            textMatricule          = new Text("Matricule");

            rightSide.getChildren().add(prenom);
            rightSide.getChildren().add(nom);
            rightSide.getChildren().add(email);
            rightSide.getChildren().add(matricule);
            rightSide.getChildren().add(textPrenom);
            rightSide.getChildren().add(textMatricule);
            rightSide.getChildren().add(textEmail);
            rightSide.getChildren().add(textNom);
            rightSide.getChildren().add(envoyer);
            rightSide.getChildren().add(formulaireInscription);
            //Format
            formulaireInscription.setFont(Font.font("arial", 20));
            //Taille
            formulaireInscription.setPrefWidth(300);
            envoyer.setPrefWidth(95);
            //Alignement
            formulaireInscription.setAlignment(Pos.CENTER);
            //Positionnement
            prenom.setLayoutY(80);
            textPrenom.setLayoutY(95);
            nom.setLayoutY(80+40);
            textNom.setLayoutY(95+40);
            email.setLayoutY(80+2*40);
            textEmail.setLayoutY(95+2*40);
            matricule.setLayoutY(80+3*40);
            textMatricule.setLayoutY(95+3*40);
            envoyer.setLayoutY(245);
            formulaireInscription.setLayoutY(10);
            prenom.setLayoutX(80);
            textPrenom.setLayoutX(20);
            nom.setLayoutX(80);
            textNom.setLayoutX(20);
            email.setLayoutX(80);
            textEmail.setLayoutX(20);
            matricule.setLayoutX(80);
            textMatricule.setLayoutX(20);
            envoyer.setLayoutX(105);

            //Composition finale
            root.getChildren().add(leftSide);
            root.getChildren().add(separator);
            root.getChildren().add(rightSide);

            //event button charge
            charge.setOnAction((event) -> {
                cours.getItems().setAll();
                controleur.loadClasses(session.getValue());
            });
            //event button envoyer
            envoyer.setOnAction((event) -> {
                controleur.formulaireValide();
            });

            primaryStage.setTitle("Inscription UdeM");
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
