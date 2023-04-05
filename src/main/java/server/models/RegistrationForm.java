package server.models;

import java.io.Serializable;

/**
 * Cette classe est en charge de créer et manipuler un formulaire d'inscription.
 */
public class RegistrationForm implements Serializable {
    private String prenom;
    private String nom;
    private String email;
    private String matricule;
    private Course course;

    /**
     * Constructeur de la classe RegistrationForm qui initialise les valeurs des attributs suivants: prénom,
     * nom, email, matricule, course.
     *
     * @param prenom    prénom de l'individu voulant s'inscrire
     * @param nom       prénom de l'individu voulant s'inscrire
     * @param email     email de l'individu voulant s'inscrire
     * @param matricule matricule de l'individu voulant s'inscrire
     * @param course    cours choisi par l'individu qui veut s'inscrire
     */
    public RegistrationForm(String prenom, String nom, String email, String matricule, Course course) {
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.matricule = matricule;
        this.course = course;
    }

    /**
     * Permet de récupérer le prénom de l'instance de RegistrationForm
     *
     * @return un prénom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Permet de modifier le prénom d'une instance de RegistrationForm
     *
     * @param prenom nouveau prénom
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Permet de récupérer le nom d'une instance de RegistrationForm.
     *
     * @return un nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Permet de modifier le nom d'une instance de RegistrationForm.
     *
     * @param nom nouveau nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Permet de récupérer le email d'une instance de RegistrationForm.
     *
     * @return un email
     */
    public String getEmail() {
        return email;
    }
    /**
     * Permet de modifier le email d'une instance de RegistrationForm.
     *
     * @param email nouveau email
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * Permet de récupérer la matricule d'une instance de RegistrationForm.
     *
     * @return une matricule
     */
    public String getMatricule() {
        return matricule;
    }
    /**
     * Permet de modifier la matricule d'une instance de RegistrationForm.
     *
     * @param matricule nouvelle matricule
     */
    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }
    /**
     * Permet de récupérer le cours d'une instance de RegistrationForm.
     *
     * @return un cours
     */
    public Course getCourse() {
        return course;
    }
    /**
     * Permet de modifier le cours d'une instance de RegistrationForm.
     *
     * @param course nouveau cours
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * Permet de bien formater les informations relatifs à une instance de RegistrationForm.
     *
     * @return String détaillant des valeurs des attributs d'une instance de RegistrationForm
     */
    @Override
    public String toString() {
        return "InscriptionForm{" + "prenom='" + prenom + '\'' + ", nom='" + nom + '\'' + ", email='" + email + '\'' + ", matricule='" + matricule + '\'' + ", course='" + course + '\'' + '}';
    }
}
