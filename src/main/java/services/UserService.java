package services;

import entity.User;
import dao.UserDAO;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Vadim Sharomov
 */
@Service
public class UserService {
    private final static Logger logger = getLogger(UserService.class);

    private UserDAO dao;

    @Resource
    private Map<String, UserDAO> daoServices;

    public void setDataSource(String typeDB, String pathToFileDB) {
        this.dao = daoServices.get("user" + typeDB.toLowerCase());
        if ("mysql".equals(typeDB.toLowerCase()) || "xml".equals(typeDB.toLowerCase()) || "json".equals(typeDB.toLowerCase())) {
            this.dao.setTypeDB(pathToFileDB);
        } else {
            logger.error("Type data base is not known: '" + typeDB + "'. Refer parameter 'typeDB' in config file.");
            System.exit(1);
        }
    }

    public void create(String fullName, String login, String password) {
        dao.create(fullName, login, password);
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
}


