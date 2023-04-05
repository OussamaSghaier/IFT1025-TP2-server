package server.models;

import java.io.Serializable;

/**
 * Cette classe permet de créer un cours avec un nom, un code et une session spécifique.
 *
 */
public class Course implements Serializable {

    private String name;
    private String code;
    private String session;

    /**
     *
     * Constructeur de la classe Course qui permet d'initialiser les valeurs des attributs(name, code, session)
     * de la classe Course.
     *
     * @param name nom du Course
     * @param code code du Course
     * @param session session du Course
     */
    public Course(String name, String code, String session) {
        this.name = name;
        this.code = code;
        this.session = session;
    }

    /**
     * Permet d'obtenir la valeur de l'attribut name.
     *
     * @return le nom du Course
     */
    public String getName() {
        return name;
    }

    /**
     * Permet de redéfinir le nom d'une instance de Course.
     *
     * @param name nouvelle valeur de l'attribut name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Permet d'obtenir la valeur de l'attribut code.
     *
     * @return le code de Course
     */
    public String getCode() {
        return code;
    }

    /**
     * Permet de redéfinir le code d'une instance de Course.
     *
     * @param code nouvelle valeur de l'attribut code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Permet d'obtenir la valeur de l'attribut session.
     *
     * @return la session de Course
     */
    public String getSession() {
        return session;
    }

    /**
     * Permet de redéfinir la session d'une instance de Course.
     *
     * @param session nouvelle valeur de l'attribut session
     */
    public void setSession(String session) {
        this.session = session;
    }

    /**
     * Permet de bien formater les informations relatifs à une instance de Course.
     *
     * @return String détaillant des valeurs des attributs d'une instance de Course
     */
    @Override
    public String toString() {
        return "Course{" +
                "name=" + name +
                ", code=" + code +
                ", session=" + session +
                '}';
    }
}
