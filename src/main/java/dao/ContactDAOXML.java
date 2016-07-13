package dao;

import entity.Contact;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author by Vadim Sharomov
 *
 */
public class ContactDAOXML extends AbstractDAO {
    private final static Logger logger = getLogger(ContactDAOXML.class);
    private File inputFile;
    private SAXReader reader;
    private Document document;
    private String pathToNode;

    private ContactDAOXML() {
        reader = new SAXReader();
    }

    private static class SingleToneHelper {
        private static final ContactDAOXML INSTANCE = new ContactDAOXML();
    }

    public static ContactDAOXML getInstance() {
        return SingleToneHelper.INSTANCE;
    }

    @Override
    public void setTypeDB(String pathToFileDB) {
        File file = new File(pathToFileDB + "contacts.xml");
        this.inputFile = new File(pathToFileDB + "contacts.xml");
        this.pathToNode = "/class/contact";
        if (!file.exists()) {
            Document newDocument = DocumentHelper.createDocument();
            Element root = newDocument.addElement("class");
            saveDocument(newDocument, inputFile);
        }
        try {
            document = reader.read(inputFile);
        } catch (DocumentException e) {
            logger.error("Can't access to file DB in setTypeDB: '" + pathToFileDB + "contacts.xml" + "'", e.getMessage());
        }
    }


    @Override
    public void create(String userId, String lastName, String name, String middleName, String mobilePhone, String homePhone, String address, String email) {
        long maxId = getMaxId(document, pathToNode);

        Element classElement = document.getRootElement();
        Element contact = classElement.addElement("contact").addAttribute("id", String.valueOf(maxId + 1));
        contact.addElement("iduser").addText(userId);
        contact.addElement("lastname").addText(lastName);
        contact.addElement("name").addText(name);
        contact.addElement("middlename").addText(middleName);
        contact.addElement("mobilephone").addText(mobilePhone);
        contact.addElement("homephone").addText(homePhone);
        contact.addElement("address").addText(address);
        contact.addElement("email").addText(email);
        saveDocument(document, inputFile);
    }

    @Override
    public List<Contact> getByIdUser(String idUser) {
        List<Contact> contacts = new ArrayList<>();
        Element classElement = document.getRootElement();
        List<Node> nodes = document.selectNodes(pathToNode);
        for (Node node : nodes) {
            if (idUser.equals(node.selectSingleNode("iduser").getText())) {
                contacts.add(getContactFromNode(node));
            }
        }
        return contacts;
    }

    @Override
    public List<Contact> getByIdUserAndName(String idUser, String lastName, String name, String mobilePhone) {
        List<Contact> contacts = new ArrayList<>();
        Element classElement = document.getRootElement();
        List<Node> nodes = document.selectNodes(pathToNode);
        for (Node node : nodes) {
            if (node.selectSingleNode("iduser").getText().toLowerCase().contains(idUser.toLowerCase()) &&
                    node.selectSingleNode("lastname").getText().toLowerCase().contains(lastName.toLowerCase()) &&
                    node.selectSingleNode("name").getText().toLowerCase().contains(name.toLowerCase()) &&
                    node.selectSingleNode("mobilephone").getText().contains(mobilePhone)) {
                contacts.add(getContactFromNode(node));
            }
        }
        return contacts;
    }

    @Override
    public Contact getContactById(String id) {
        Element classElement = document.getRootElement();
        List<Node> nodes = document.selectNodes(pathToNode);
        for (Node node : nodes) {
            if (id.equals(node.valueOf("@id"))) {
                return getContactFromNode(node);
            }
        }
        return null;
    }

    @Override
    public void update(String id, String lastName, String name, String middleName, String mobilePhone, String homePhone, String address, String email) {
        Element classElement = document.getRootElement();
        List<Node> nodes = document.selectNodes(pathToNode + "[@id='" + id + "']");
        for (Node node : nodes) {
            node.selectSingleNode("lastname").setText(lastName);
            node.selectSingleNode("name").setText(name);
            node.selectSingleNode("middlename").setText(middleName);
            node.selectSingleNode("mobilephone").setText(mobilePhone);
            node.selectSingleNode("homephone").setText(homePhone);
            node.selectSingleNode("address").setText(address);
            node.selectSingleNode("email").setText(email);
        }
        saveDocument(document, inputFile);
    }

    @Override
    public void delete(String id) {
        Element classElement = document.getRootElement();
        List<Node> nodes = document.selectNodes(pathToNode + "[@id='" + id + "']");
        for (Node node : nodes) {
            node.detach();
        }
        saveDocument(document, inputFile);
    }

    private Contact getContactFromNode(Node node) {
        Contact contact = new Contact();
        contact.setId(Long.parseLong(node.valueOf("@id")));
        contact.setUserId(Long.parseLong(node.selectSingleNode("iduser").getText()));
        contact.setLastName(node.selectSingleNode("lastname").getText());
        contact.setName(node.selectSingleNode("name").getText());
        contact.setMiddleName(node.selectSingleNode("middlename").getText());
        contact.setMobilePhone(node.selectSingleNode("mobilephone").getText());
        contact.setHomePhone(node.selectSingleNode("homephone").getText());
        contact.setAddress(node.selectSingleNode("address").getText());
        contact.setEmail(node.selectSingleNode("email").getText());
        return contact;
    }
}
