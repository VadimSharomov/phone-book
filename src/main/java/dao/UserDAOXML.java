package dao;

import entity.User;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author by Vadim Sharomov
 */
public class UserDAOXML extends AbstractDAO {
    private final static Logger logger = getLogger(UserDAOXML.class);
    private File inputFile;
    private SAXReader reader;
    private Document document;
    private String pathToNode;

    private UserDAOXML() {
        reader = new SAXReader();
    }

    private static class SingleToneHelper {
        private static final UserDAOXML INSTANCE = new UserDAOXML();
    }

    public static UserDAOXML getInstance() {
        return SingleToneHelper.INSTANCE;
    }

    @Override
    public void setTypeDB(String pathToFileDB) {
        this.inputFile = new File(pathToFileDB + "users.xml");
        this.pathToNode = "/class/user";
        File file = new File(pathToFileDB + "users.xml");
        if (!file.exists()) {
            Document newDocument = DocumentHelper.createDocument();
            Element root = newDocument.addElement("class");
            saveDocument(newDocument, inputFile);
        }
        try {
            document = reader.read(inputFile);
        } catch (DocumentException e) {
            logger.error("Can't access to file DB in setTypeDB: '" + pathToFileDB + "users.xml" + "'", e.getMessage());
        }
    }


    @Override
    public void create(String fullName, String login, String password) {
        long maxId = getMaxId(document, pathToNode);

        Element classElement = document.getRootElement();
        Element contact = classElement.addElement("user").addAttribute("id", String.valueOf(maxId + 1));
        contact.addElement("idsession").addText("1");
        contact.addElement("fullname").addText(fullName);
        contact.addElement("login").addText(login);
        contact.addElement("password").addText(password);
        saveDocument(document, inputFile);
    }

    @Override
    public List<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        Element classElement = document.getRootElement();
        List<Node> nodes = document.selectNodes(pathToNode);
        for (Node node : nodes) {
            users.add(getUserFromNode(node));
        }
        return users;
    }

    @Override
    public User getByLogin(String login) {
        Element classElement = document.getRootElement();
        List<Node> nodes = document.selectNodes(pathToNode);
        for (Node node : nodes) {
            if (login.equals(node.selectSingleNode("login").getText())) {
                return getUserFromNode(node);
            }
        }
        return null;
    }

    @Override
    public void updateIdSession(long id, long idSession) {
        Element classElement = document.getRootElement();
        List<Node> nodes = document.selectNodes(pathToNode + "[@id='" + id + "']");
        for (Node node : nodes) {
            node.selectSingleNode("idsession").setText(String.valueOf(idSession));
            saveDocument(document, inputFile);
        }
    }

    @Override
    public User getUserById(String id) {
        Element classElement = document.getRootElement();
        List<Node> nodes = document.selectNodes(pathToNode);
        for (Node node : nodes) {
            if (id.equals(node.valueOf("@id"))) {
                return getUserFromNode(node);
            }
        }
        return null;
    }

    private User getUserFromNode(Node node) {
        User user = new User();
        user.setLogin(node.selectSingleNode("login").getText());
        user.setId(Long.parseLong(node.valueOf("@id")));
        user.setIdSession(Long.parseLong(node.selectSingleNode("idsession").getText()));
        user.setFullName(node.selectSingleNode("fullname").getText());
        user.setPassword(node.selectSingleNode("password").getText());
        return user;
    }

}
