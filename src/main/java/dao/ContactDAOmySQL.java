package main.java.dao;

import main.java.entity.Contact;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Vadim on 18.04.2016.
 *
 */
public class ContactDAOmySQL extends AbstractDAO {
    private final static Logger logger = getLogger(ContactDAOmySQL.class);
    private String table;
    private JdbcTemplate jdbcTemplateObject;

    private ContactDAOmySQL() {
        this.table = "contacts";
    }

    private static class SingleToneHelper {
        private static final ContactDAOmySQL INSTANCE = new ContactDAOmySQL();
    }

    public static ContactDAOmySQL getInstance() {
        return ContactDAOmySQL.SingleToneHelper.INSTANCE;
    }

    @Override
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    @Override
    public Contact getContactById(String id) {
        String SQL = "SELECT * FROM " + table + " WHERE id = ?";
        try {
            return jdbcTemplateObject.queryForObject(SQL,
                    new Object[]{id}, new ContactMapper());
        } catch (IncorrectResultSizeDataAccessException e) {
            logger.error("Incorrect result size data access exception in getContactById", e.getMessage());
            return null;
        }
    }

    @Override
    public List<Contact> getByIdUser(String idUser) {
        String SQL = "SELECT * FROM " + table + " WHERE iduser = ?";
        try {
            return jdbcTemplateObject.query(SQL,
                    new Object[]{idUser}, new ContactMapper());
        } catch (DataAccessException e) {
            logger.error("Data access exception in getByIdUser", e.getMessage());
            return null;
        }
    }

    @Override
    public List<Contact> getByIdUserAndName(String idUser, String lastName, String name, String mobilePhone) {
        String SQL = "SELECT * FROM " + table + " WHERE lastName LIKE ? AND name like ? AND mobilePhone like ? AND iduser = ?";
        try {
            return jdbcTemplateObject.query(SQL,
                    new Object[]{"%" + lastName + "%", "%" + name + "%", "%" + mobilePhone + "%", idUser}, new ContactMapper());
        } catch (DataAccessException e) {
            logger.error("Data access exception in getByIdUserAndName", e.getMessage());
            return null;
        }
    }

    @Override
    public void create(String iduser, String lastName, String name, String middleName, String mobilePhone, String homePhone, String address, String email) {
        String SQL = "INSERT INTO " + table + " (iduser, lastName, name, middleName, mobilePhone, homePhone, address, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            jdbcTemplateObject.update(SQL, iduser, lastName, name, middleName, mobilePhone, homePhone, address, email);
        } catch (DataAccessException e) {
            logger.error("Data access exception in create contact", e.getMessage());
        }
    }

    @Override
    public void update(String id, String lastName, String name, String middleName, String mobilePhone, String homePhone, String address, String email) {
        String SQL = "update " + table + " set lastName = ?, name = ?, middleName = ?, mobilePhone = ?, homePhone = ?, address = ?, email = ? where id = ?";
        try {
            jdbcTemplateObject.update(SQL, lastName, name, middleName, mobilePhone, homePhone, address, email, id);
        } catch (DataAccessException e) {
            logger.error("Data access exception in update contact", e.getMessage());
        }
    }

    @Override
    public void delete(String id) {
        String SQL = "delete from " + table + " where id = ?";
        try {
            jdbcTemplateObject.update(SQL, id);
        } catch (DataAccessException e) {
            logger.error("Data access exception in delete contact", e.getMessage());
        }
    }
}
