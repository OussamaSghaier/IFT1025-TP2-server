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

    public void addClass(ArrayList<Course> listServer){
        for (int i = 0; i < listServer.size(); i++) {
            this.cours.getItems().add(listServer.get(i));

        }
    }
    public TextField getEmail(){
        return this.email;
    }
    public TextField getPrenom(){
        return this.prenom;
    }
    public TextField getNom(){
        return this.nom;
    }
    public TextField getMatricule(){
        return this.matricule;
    }
    public String getTextPrenom(){
        return this.prenom.getText();
    }
    public String getTextNom(){
        return this.nom.getText();
    }
    public String getTextEmail(){
        return this.email.getText();
    }
    public String getTextMatricule(){
        return this.matricule.getText();
    }
    public TableView getCours(){
        return this.cours;
    }


    public static void main(String[] args){
        ClientGUI.launch();
    }
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

            //leftSide

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

            session.setLayoutY(13);
            session.setLayoutX(30);
            charge.setLayoutY(13);
            charge.setLayoutX(150);

            container.setStyle("-fx-background-color: #f3f4e7");
            listeCours.setStyle("-fx-background-color:#f3f4e7");
            selectionSession.setStyle("-fx-background-color: #f3f4e7");

            container.setAlignment(Pos.BASELINE_CENTER);
            listeCours.setAlignment(Pos.CENTER);


            listeCours.setPrefWidth(300);
            selectionSession.setPrefHeight(50);
            container.setPrefHeight(310);
            cours.setPrefWidth(275);
            cours.setPrefHeight(300);
            listeCours.setPrefHeight(40);

            listeCours.setFont(Font.font("arial", 20));

            leftSide.getChildren().add(listeCours);
            leftSide.getChildren().add(container);
            leftSide.getChildren().add(selectionSession);

            //rightSide
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

            formulaireInscription.setFont(Font.font("arial", 20));

            formulaireInscription.setPrefWidth(300);
            envoyer.setPrefWidth(95);

            formulaireInscription.setAlignment(Pos.CENTER);

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

            root.getChildren().add(leftSide);
            root.getChildren().add(separator);
            root.getChildren().add(rightSide);



            charge.setOnAction((event) -> {
                cours.getItems().setAll();
                controleur.loadClasses(session.getValue());
            });

            envoyer.setOnAction((event) -> {
                controleur.formulaireValide();
            });




            primaryStage.setTitle("Inscription UdeM");
            primaryStage.setScene(scene);
            primaryStage.show(); // Important !
        }catch(Exception ex){
            ex.printStackTrace();
        }



    }

}
