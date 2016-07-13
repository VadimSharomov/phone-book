package dao;

import entity.Contact;
import entity.User;
import interfaces.ContactDAO;
import interfaces.UserDAO;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.XMLWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author by Vadim Sharomov
 */
abstract class AbstractDAO implements UserDAO, ContactDAO {
    private final static Logger logger = getLogger(AbstractDAO.class);

    void saveFile(JSONObject jsonObject, String pathToFile) {
        try {
            FileWriter file = new FileWriter(pathToFile);
            file.write(jsonObject.toJSONString());
            file.flush();
            file.close();
        } catch (IOException e) {
            logger.error("Can't write in file DB as JSON format in saveFile: '" + pathToFile + "'", e.getMessage());
        }
    }

    void saveDocument(Document document, File inputFile) {
        try {
            FileWriter fileWriter = new FileWriter(inputFile);
            XMLWriter output = new XMLWriter(fileWriter);
            output.write(document);
            output.close();
            fileWriter.close();
        } catch (IOException e) {
            logger.error("Can't write in file DB as XML format in saveDocument: '" + inputFile + "'", e.getMessage());
        }
    }

    long getMaxId(Document document, String pathToNode) {
        long maxId = 0;
        Element classElement = document.getRootElement();
        List<Node> nodes = document.selectNodes(pathToNode);
        for (Node node : nodes) {
            if (maxId < Long.parseLong(node.valueOf("@id"))) {
                maxId = Long.parseLong(node.valueOf("@id"));
            }
        }
        return maxId;
    }

    long getMaxId(JSONArray jArray) {
        long maxId = 0;
        for (Object o : jArray) {
            JSONObject jo = (JSONObject) o;
            if (maxId < Long.parseLong(jo.get("id").toString())) {
                maxId = Long.parseLong(jo.get("id").toString());
            }
        }
        return maxId;
    }

    void setTypeDB(String pathToFileDB) {
    }

    @Override
    public void create(String userId, String lastName, String name, String middleName, String mobilePhone, String homePhone, String address, String email) {

    }

    @Override
    public Contact getContactById(String id) {
        return null;
    }

    @Override
    public List<Contact> getByIdUser(String idUser) {
        return null;
    }

    @Override
    public List<Contact> getByIdUserAndName(String idUser, String lastName, String name, String mobilePhone) {
        return null;
    }

    @Override
    public void update(String id, String lastName, String name, String middleName, String mobilePhone, String homePhone, String address, String email) {

    }

    @Override
    public void delete(String id) {

    }


    @Override
    public void setDataSource(DataSource dataSource) {

    }

    @Override
    public void create(String fullName, String login, String password) {

    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public User getUserById(String id) {
        return null;
    }

    @Override
    public User getByLogin(String login) {
        return null;
    }

    @Override
    public void updateIdSession(long id, long idSession) {

    }
}