package entity;

import java.util.ArrayList;

/**
 * Created by Vadim on 14.04.2016.
 *
 */
public class User {
    private long id;
    private long idSession;
    private String fullName;
    private String login;
    private String password;
    private ArrayList<Contact> contacts;

    public User(String fullName, String login, String password) {
        this.fullName = fullName;
        this.login = login;
        this.password = password;
    }

    public User() {

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
        return "User: " + fullName + " " + login + " idSession " + idSession;
    }
}
