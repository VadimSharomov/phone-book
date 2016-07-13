package services;

import dao.UserDAOJSON;
import dao.UserDAOXML;
import dao.UserDAOmySQL;
import entity.User;
import interfaces.UserDAO;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author by Vadim Sharomov
 */
public class UserService {
    private UserDAO dao;

    private UserService() {
    }

    private static class SingleToneHelper {
        private static final UserService INSTANCE = new UserService();
    }

    public static UserService getInstance() {
        return SingleToneHelper.INSTANCE;
    }

    public void setDataSource(DataSource dataSource, String typeDB, String pathToFileDB) {
        if ("mysql".equals(typeDB.toLowerCase())) {
            this.dao = UserDAOmySQL.getInstance();
            this.dao.setDataSource(dataSource);
        } else if ("xml".equals(typeDB.toLowerCase())) {
            UserDAOXML.getInstance().setTypeDB(pathToFileDB);
            this.dao = UserDAOXML.getInstance();
        } else if ("json".equals(typeDB.toLowerCase())) {
            UserDAOJSON.getInstance().setTypeDB(pathToFileDB);
            this.dao = UserDAOJSON.getInstance();
        } else {
            System.out.println("\nClass " + this.getClass().getName() + ": Type data base is not known: " + typeDB + ". Refer parameter 'typeDB' in config file.\n");
            System.exit(1);
        }
    }

    public List<User> getAllUsers() {
        return dao.getAllUsers();
    }

    public User getByLogin(String login) {
        return dao.getByLogin(login);
    }

    public User getById(String idUser) {
        return dao.getUserById(idUser);
    }

    public void openSession(long id, String idSession) {
        dao.updateIdSession(id, Long.parseLong(idSession));
    }

    public void closeSession(long id) {
        dao.updateIdSession(id, 0);
    }

    public void create(String fullName, String login, String password) {
        dao.create(fullName, login, password);
    }

}


