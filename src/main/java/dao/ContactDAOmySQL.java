package dao;

import entity.Contact;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import repository.ContactRepository;

import javax.annotation.Resource;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Vadim Sharomov
 */
@Service("contactmysql")
public class ContactDAOmySQL extends AbstractDAO implements ContactDAO {
    private final static Logger logger = getLogger(ContactDAOmySQL.class);

    @Resource
    private ContactRepository contactRepository;

    @Override
    public void setTypeDB(String pathToFileDB) {

    }

    @Override
    public Contact getContactById(String id) {
        return contactRepository.getContactById(Long.valueOf(id));
    }

    @Override
    public List<Contact> getByIdUser(String idUser) {
        return contactRepository.findContactByIdUser(Long.valueOf(idUser));
    }

    @Override
    public List<Contact> getByIdUserAndName(String idUser, String lastName, String name, String mobilePhone) {
        return contactRepository.getByIdUserAndName(Long.valueOf(idUser), lastName, name, mobilePhone);
    }

    @Override
    public void create(String idUser, String lastName, String name, String middleName, String mobilePhone, String homePhone, String address, String email) {
        contactRepository.save(new Contact(Long.valueOf(idUser), lastName, name, middleName, mobilePhone, homePhone, address, email));
    }

    @Override
    public void update(String id, String lastName, String name, String middleName, String mobilePhone, String homePhone, String address, String email) {
        contactRepository.updateContact(lastName, name, middleName, mobilePhone, homePhone, address, email, Long.valueOf(id));
    }

    @Override
    public void delete(String id) {
        contactRepository.deleteById(Long.valueOf(id));
    }
}
