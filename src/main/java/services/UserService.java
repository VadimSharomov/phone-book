package services;

import entity.User;
import interfaces.UserDAO;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
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

    public void setDataSource(DataSource dataSource, String typeDB, String pathToFileDB) {
        this.dao = daoServices.get("user" + typeDB.toLowerCase());
        logger.info("*** Inject UserDAO in UserService: " + dao);
        if ("mysql".equals(typeDB.toLowerCase())) {
            this.dao.setDataSource(dataSource);
        } else if ("xml".equals(typeDB.toLowerCase())) {
            this.dao.setTypeDB(pathToFileDB);
        } else if ("json".equals(typeDB.toLowerCase())) {
            this.dao.setTypeDB(pathToFileDB);
        } else {
            logger.error("*** Class " + this.getClass().getName() + ": Type data base is not known: " + typeDB + ". Refer parameter 'typeDB' in config file.");
            System.exit(1);
        }
    }

    public List<User> getAllUsers() {
        return dao.getAllUsers();
    }

    public User getByLogin(String login) {
        return dao.getByLogin(login);
    }

    public User getUserById(String idUser) {
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


