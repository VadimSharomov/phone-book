package dao;

import entity.Contact;
import interfaces.ContactDAO;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vadim on 20.04.2016.
 */
public class ContactDAOXML implements ContactDAO {
    private String pathToFileDB;
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

    public void setTypeDB(String pathToFileDB) {
        this.pathToFileDB = pathToFileDB;
        File file = new File(pathToFileDB + "contacts.xml");
        if (!file.exists()) {
            Document newDocument = DocumentHelper.createDocument();
            Element root = newDocument.addElement("class");
            saveDocument(newDocument);
        }
        this.inputFile = new File(pathToFileDB + "contacts.xml");
        this.pathToNode = "/class/contact";
        try {
            document = reader.read(inputFile);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setDataSource(DataSource dataSource) {

    }

    @Override
    public void create(String userId, String lastName, String name, String middleName, String mobilePhone, String homePhone, String address, String email) {
        long maxId = getMaxId();

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
        saveDocument(document);
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
            if (node.selectSingleNode("iduser").getText().toLowerCase().contains(idUser.toLowerCase()) && node.selectSingleNode("lastname").getText().toLowerCase().contains(lastName.toLowerCase()) && node.selectSingleNode("name").getText().toLowerCase().contains(name.toLowerCase()) && node.selectSingleNode("mobilephone").getText().toLowerCase().contains(mobilePhone.toLowerCase())) {
                contacts.add(getContactFromNode(node));
            }
        }
        return contacts;
    }

    @Override
    public Contact getById(String id) {
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
        saveDocument(document);
    }

    @Override
    public void delete(String id) {
        Element classElement = document.getRootElement();
        List<Node> nodes = document.selectNodes(pathToNode + "[@id='" + id + "']");
        for (Node node : nodes) {
            node.detach();
        }
        saveDocument(document);
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

    private long getMaxId() {
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

    private void saveDocument(Document document) {
        try {
            FileWriter fileWriter = new FileWriter(inputFile);
            XMLWriter output = new XMLWriter(fileWriter);
            output.write(document);
            output.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
