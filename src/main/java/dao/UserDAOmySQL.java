package dao;

import entity.User;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import repository.UserRepository;

import javax.annotation.Resource;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Vadim Sharomov
 */
@Service("usermysql")
public class UserDAOmySQL extends AbstractDAO implements UserDAO {
    private final static Logger logger = getLogger(UserDAOmySQL.class);

    @Resource
    private UserRepository userRepository;

    @Override
    public void setTypeDB(String pathToFileDB) {

    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getByLogin(String login) {
        return userRepository.getByLogin(login);
    }

    @Override
    public User getUserById(String id) {
        return userRepository.getUserById(Long.valueOf(id));
    }

    @Override
    public void create(String fullName, String login, String password) {
        userRepository.save(new User(fullName, login, password));
    }

    @Override
    public void updateIdSession(long id, long idSession) {
        userRepository.setFixedIdSessionFor(idSession, id);
    }
}
