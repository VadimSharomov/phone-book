package entity;

import org.slf4j.Logger;
import rest.StartController;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @autor Vadim Sharomov
 */
@Entity
public class User {
    private final static Logger logger = getLogger(StartController.class);
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long idSession;
    private String fullName;
    private String login;
    private String password;

    public User(){}

    public User(String fullName, String login, String password) {
        this.fullName = fullName;
        this.login = login;
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getIdSession() {
        return idSession;
    }

    public void setIdSession(long idSession) {
        this.idSession = idSession;
    }

    @Override
    public String toString() {
        return "User: " + fullName + " " + login;
    }
}
