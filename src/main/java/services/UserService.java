package services;

import dao.UserDAO;
import entity.CustomUser;
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
        this.dao = daoServices.get("user" + typeDB);
        this.dao.setTypeDB(pathToFileDB);
        if (!Constants.getPossibleTypesDB().contains(typeDB)) {
            logger.error("Type data base is not known: '" + typeDB + "'. Refer parameter 'typeDB' in config file.");
            System.exit(1);
        }
    }

    public void create(CustomUser user) {
        dao.create(user);
    }

    public List<CustomUser> getAllUsers() {
        return dao.getAllUsers();
    }

    public CustomUser getUserByLogin(String login) {
        return dao.getByLogin(login);
    }

    public CustomUser getById(String idUser) {
        return dao.getUserById(idUser);
    }

}


