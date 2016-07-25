package interfaces;

import entity.Contact;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author Vadim Sharomov
 */
public interface ContactDAO {

    void setTypeDB(String pathToFileDB);

    void setDataSource(DataSource dataSource);

    void create(String userId, String lastName, String name, String middleName, String mobilePhone, String homePhone, String address, String email);

    Contact getContactById(String id);

    List<Contact> getByIdUser(String idUser);

    List<Contact> getByIdUserAndName(String idUser, String lastName, String name, String mobilePhone);

    void update(String id, String lastName, String name, String middleName, String mobilePhone, String homePhone, String address, String email);

    void delete(String id);

}
