package services;

import dao.ContactDAOJSON;
import dao.ContactDAOXML;
import dao.ContactDAOmySQL;
import entity.Contact;
import interfaces.ContactDAO;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by Vadim on 18.04.2016.
 */
public class ContactService {
    private ContactDAO dao;

    private ContactService() {
    }

    private static class SingleToneHelper {
        private static final ContactService INSTANCE = new ContactService();
    }

    public static ContactService getInstance() {
        return SingleToneHelper.INSTANCE;
    }

    public void setDataSource(DataSource dataSource, String typeDB, String pathToFileDB) {
        if ("mysql".equals(typeDB.toLowerCase())) {
            this.dao = ContactDAOmySQL.getInstance();
            this.dao.setDataSource(dataSource);
        } else if ("xml".equals(typeDB.toLowerCase())) {
            ContactDAOXML.getInstance().setTypeDB(pathToFileDB);
            this.dao = ContactDAOXML.getInstance();
        } else if ("json".equals(typeDB.toLowerCase())) {
            ContactDAOJSON.getInstance().setTypeDB(pathToFileDB);
            this.dao = ContactDAOJSON.getInstance();
        } else {
            System.out.println("\nClass " + this.getClass().getName() + ": Type data base is not known: " + typeDB + "! Refer parameter 'typeDB' in config file\n");
            System.exit(1);
        }
    }

    public void create(String idUser, String lastName, String name, String middleName, String mobilePhone, String homePhone, String address, String email) {
        dao.create(idUser, lastName, name, middleName, mobilePhone, homePhone, address, email);
    }

    public Contact getById(String idContact) {
        return dao.getById(idContact);
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
