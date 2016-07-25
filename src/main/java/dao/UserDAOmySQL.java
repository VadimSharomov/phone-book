package dao;

import entity.User;
import interfaces.UserDAO;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Vadim Sharomov
 */
@Service("usermysql")
public class UserDAOmySQL extends AbstractDAO implements UserDAO{
    private final static Logger logger = getLogger(UserDAOmySQL.class);
    private String table;
    private JdbcTemplate jdbcTemplateObject;

    private UserDAOmySQL() {
        this.table = "users";
    }

    @Override
    public void setTypeDB(String pathToFileDB) {

    }

    @Override
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    @Override
    public List<User> getAllUsers() {
        String SQL = "SELECT * FROM " + table;
        try {
            return new ArrayList<>(jdbcTemplateObject.query(SQL, new UserMapper()));
        } catch (IncorrectResultSizeDataAccessException e) {
            logger.error("Incorrect result size data access exception in getAllUsers", e.getMessage());
            return null;
        }
    }

    @Override
    public User getByLogin(String login) {
        String SQL = "SELECT * FROM " + table + " WHERE login = ?";
        try {
            return jdbcTemplateObject.queryForObject(SQL,
                    new Object[]{login}, new UserMapper());
        } catch (IncorrectResultSizeDataAccessException e) {
            logger.error("Incorrect result size data access exception in getByLogin", e.getMessage());
            return null;
        }
    }

    @Override
    public User getUserById(String id) {
        String SQL = "SELECT * FROM " + table + " WHERE id = ?";
        try {
            return jdbcTemplateObject.queryForObject(SQL,
                    new Object[]{id}, new UserMapper());
        } catch (IncorrectResultSizeDataAccessException e) {
            logger.error("Incorrect result size data access exception in getUserById", e.getMessage());
            return null;
        }
    }

    @Override
    public void create(String fullName, String login, String password) {
        String SQL = "INSERT INTO " + table + " (fullName, login, password) VALUES (?, ?, ?)";
        jdbcTemplateObject.update(SQL, fullName, login, password);
    }

    @Override
    public void updateIdSession(long id, long idSession) {
        String SQL = "update " + table + " set idsession = ? where id = ?";
        try {
            jdbcTemplateObject.update(SQL, idSession, id);
        } catch (DataAccessException e) {
            logger.error("Data access exception in updateIdSession", e.getMessage());
        }
    }
}
