package interfaces;

import entity.User;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author Vadim Sharomov
 */
public interface UserDAO {

    void setTypeDB(String pathToFileDB);

    void setDataSource(DataSource dataSource);

    void create(String fullName, String login, String password);

    List<User> getAllUsers();

    User getUserById(String id);

    User getByLogin(String login);

    void updateIdSession(long id, long idSession);
}
