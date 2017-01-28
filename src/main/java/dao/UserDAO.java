package dao;

import entity.CustomUser;
import entity.UserRole;

import java.util.List;

/**
 * @author Vadim Sharomov
 */
public interface UserDAO {

    void setTypeDB(String pathToFileDB);

    void create(String fullName, String login, String password, UserRole role);

    List<CustomUser> getAllUsers();

    CustomUser getUserById(String id);

    CustomUser getByLogin(String login);

}
