package interfaces;

import entity.User;

import javax.sql.DataSource;
import java.util.ArrayList;

/**
 * Created by Vadim on 14.04.2016.
 *
 */
public interface UserDAO {

    void setDataSource(DataSource dataSource);

    void create(String fullName, String login, String password);

    ArrayList<User> getAllUsers();

    User getUserById(String id);

    User getByLogin(String login);

    void updateIdSession(long id, long idSession);
}
