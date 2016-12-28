package services;

import entity.Contact;
import dao.ContactDAO;
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
public class ContactService {
    private final static Logger logger = getLogger(ContactService.class);

    private ContactDAO dao;

    @Resource
    private Map<String, ContactDAO> daoServices;

    public void setDataSource(String typeDB, String pathToFileDB) {
        this.dao = daoServices.get("contact" + typeDB);
        this.dao.setTypeDB(pathToFileDB);
        if (!Constants.getPosibleTypesDB().contains(typeDB)) {
            logger.error("Type data base is not known: '" + typeDB + "'. Refer parameter 'typeDB' in config file.");
            System.exit(1);
        }
    }

    public void create(String idUser, String lastName, String name, String middleName, String mobilePhone, String homePhone, String address, String email) {
        dao.create(idUser, lastName, name, middleName, mobilePhone, homePhone, address, email);
    }

    public Contact getById(String idContact) {
        return dao.getContactById(idContact);
    }

    public List<Contact> getByIdUser(String idUser) {
        return dao.getByIdUser(idUser);
    }

    public List<Contact> getByIdUserAndName(String idUser, String lastName, String name, String mobilePhone) {
        return dao.getByIdUserAndName(idUser, lastName, name, mobilePhone);
    }

    public void update(String idUser, String idContact, String lastName, String name, String middleName, String mobilePhone, String homePhone, String address, String email) {
        dao.update(idContact, lastName, name, middleName, mobilePhone, homePhone, address, email);
    }

    public void delete(String idContact) {
        dao.delete(idContact);
    }
}
