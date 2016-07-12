package test.java;

import main.java.rest.StartController;
import main.java.dao.*;
import main.java.entity.Contact;
import main.java.entity.User;
import main.java.services.ContactService;
import main.java.services.UserService;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by Vadim on 22.04.2016.
 *
 */

public class Tests {

    /**
     * Tests User DAO MXL
     */
    @SuppressWarnings("Duplicates")
    @Test
    public void testUserDAOXMLgetById() {
        UserDAOXML mock = mock(UserDAOXML.class);
        User user = new User();
        when(mock.getUserById("1")).thenReturn(user);
        when(mock.getUserById("a")).thenReturn(null);
        assertEquals(user, mock.getUserById("1"));
        assertEquals(null, mock.getUserById("a"));
    }

    @Test
    public void testUserDAOXMLcreate() {
        UserDAOXML mock = mock(UserDAOXML.class);
        doThrow(new RuntimeException()).when(mock).create("fullName", "login", "password");
    }

    @Test
    public void testUserDAOXMLupdateIdSession() {
        UserDAOXML mock = mock(UserDAOXML.class);
        doThrow(new RuntimeException()).when(mock).updateIdSession(1, 1);
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void testUserDAOXMLgetByLogin() {
        UserDAOXML mock = mock(UserDAOXML.class);
        User user = new User();
        when(mock.getByLogin("login")).thenReturn(user);
        when(mock.getByLogin("111")).thenReturn(null);
        assertEquals(user, mock.getByLogin("login"));
        assertEquals(null, mock.getByLogin("111"));
    }

    @Test
    public void testUserDAOXMLGetAllUsers() {
        UserDAOXML mock = mock(UserDAOXML.class);
        ArrayList<User> users = new ArrayList<>();
        when(mock.getAllUsers()).thenReturn(users);
        assertEquals(users, mock.getAllUsers());
    }

    /**
     * Tests User DAO mySQL
     */
    @Test
    public void testUserDAOmySQLCreate() {
        UserDAOmySQL mock = mock(UserDAOmySQL.class);
        doThrow(new RuntimeException()).when(mock).create("fullName", "login", "password");
    }

    @Test
    public void testUserDAOmySQLUpdateIdSession() {
        UserDAOmySQL mock = mock(UserDAOmySQL.class);
        doThrow(new RuntimeException()).when(mock).updateIdSession(1, 1);
    }

    @Test
    public void testUserDAOmySQLSetDataSource() {
        UserDAOmySQL mock = mock(UserDAOmySQL.class);
        doThrow(new RuntimeException()).when(mock).setDataSource(new DataSource());
    }

    @Test
    public void testUserDAOmySQLGetById() {
        UserDAOmySQL mock = mock(UserDAOmySQL.class);
        User user = new User();
        when(mock.getUserById("1")).thenReturn(user);
        when(mock.getUserById("a")).thenReturn(null);
        assertEquals(user, mock.getUserById("1"));
        assertEquals(null, mock.getUserById("a"));
    }

    @Test
    public void testUserDAOmySQLGetByLogin() {
        UserDAOmySQL mock = mock(UserDAOmySQL.class);
        User user = new User();
        when(mock.getByLogin("login")).thenReturn(user);
        when(mock.getByLogin("111")).thenReturn(null);
        assertEquals(user, mock.getByLogin("login"));
        assertEquals(null, mock.getByLogin("111"));
    }

    @Test
    public void testUserDAOmySQLGetAllUsers() {
        UserDAOmySQL mock = mock(UserDAOmySQL.class);
        ArrayList<User> users = new ArrayList<>();
        when(mock.getAllUsers()).thenReturn(users);
        assertEquals(users, mock.getAllUsers());
    }

    /**
     * Tests User DAO JSON
     */
    @SuppressWarnings("Duplicates")
    @Test
    public void testUserDAOJSONGetById() {
        UserDAOJSON mock = mock(UserDAOJSON.class);
        User user = new User();
        when(mock.getUserById("1")).thenReturn(user);
        when(mock.getUserById("a")).thenReturn(null);
        assertEquals(user, mock.getUserById("1"));
        assertEquals(null, mock.getUserById("a"));
    }

    @Test
    public void testUserDAOJSONCreate() {
        UserDAOJSON mock = mock(UserDAOJSON.class);
        doThrow(new RuntimeException()).when(mock).create("fullName", "login", "password");
    }

    @Test
    public void testUserDAOJSONUpdateIdSession() {
        UserDAOJSON mock = mock(UserDAOJSON.class);
        doThrow(new RuntimeException()).when(mock).updateIdSession(1, 1);
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void testUserDAOJSONGetByLogin() {
        UserDAOJSON mock = mock(UserDAOJSON.class);
        User user = new User();
        when(mock.getByLogin("login")).thenReturn(user);
        when(mock.getByLogin("111")).thenReturn(null);
        assertEquals(user, mock.getByLogin("login"));
        assertEquals(null, mock.getByLogin("111"));
    }

    @Test
    public void testUserDAOJSONGetAllUsers() {
        UserDAOJSON mock = mock(UserDAOJSON.class);
        ArrayList<User> users = new ArrayList<>();
        when(mock.getAllUsers()).thenReturn(users);
        assertEquals(users, mock.getAllUsers());
    }

    /**
     * Tests Contact DAO XML
     */
    @Test
    public void testContactDAOXMLcreate() {
        ContactDAOXML mock = mock(ContactDAOXML.class);
        doThrow(new RuntimeException()).when(mock).create("userId", "lastName", "name", "middleName", "mobilePhone", "homePhone", "address", "email");
    }

    @Test
    public void testContactDAOXMLupdate() {
        ContactDAOXML mock = mock(ContactDAOXML.class);
        doThrow(new RuntimeException()).when(mock).update("userId", "lastName", "name", "middleName", "mobilePhone", "homePhone", "address", "email");
    }

    @Test
    public void testContactDAOXMLdelete() {
        ContactDAOXML mock = mock(ContactDAOXML.class);
        doThrow(new RuntimeException()).when(mock).delete("id");
    }

    @Test
    public void testContactDAOXMLgetById() {
        ContactDAOXML mock = mock(ContactDAOXML.class);
        Contact contact = new Contact();
        when(mock.getContactById("1")).thenReturn(contact);
        when(mock.getContactById("a")).thenReturn(null);
        assertEquals(contact, mock.getContactById("1"));
        assertEquals(null, mock.getContactById("a"));
    }

    @Test
    public void testContactDAOXMLgetByIdUser() {
        ContactDAOXML mock = mock(ContactDAOXML.class);
        List<Contact> contacts = new ArrayList<>();
        when(mock.getByIdUser("1")).thenReturn(contacts);
        when(mock.getByIdUser("a")).thenReturn(contacts);
        assertEquals(contacts, mock.getByIdUser("1"));
        assertEquals(contacts, mock.getByIdUser("a"));
    }

    @Test
    public void testContactDAOXMLgetByIdUserAndName() {
        ContactDAOXML mock = mock(ContactDAOXML.class);
        List<Contact> contacts = new ArrayList<>();
        when(mock.getByIdUserAndName("1", "lastName", "name", "567")).thenReturn(contacts);
        when(mock.getByIdUserAndName("a", "lastName", "name", "567")).thenReturn(contacts);
        assertEquals(contacts, mock.getByIdUserAndName("1", "lastName", "name", "567"));
        assertEquals(contacts, mock.getByIdUserAndName("a", "lastName", "name", "567"));
    }

    /**
     * Tests Contact DAO mySQL
     */

    @Test
    public void testContactDAOmySQLcreate() {
        ContactDAOmySQL mock = mock(ContactDAOmySQL.class);
        doThrow(new RuntimeException()).when(mock).create("userId", "lastName", "name", "middleName", "mobilePhone", "homePhone", "address", "email");
    }

    @Test
    public void testContactDAOmySQLupdate() {
        ContactDAOmySQL mock = mock(ContactDAOmySQL.class);
        doThrow(new RuntimeException()).when(mock).update("userId", "lastName", "name", "middleName", "mobilePhone", "homePhone", "address", "email");
    }

    @Test
    public void testContactDAOmySQLdelete() {
        ContactDAOmySQL mock = mock(ContactDAOmySQL.class);
        doThrow(new RuntimeException()).when(mock).delete("id");
    }

    @Test
    public void testContactDAOmySQLsetDataSource() {
        ContactDAOmySQL mock = mock(ContactDAOmySQL.class);
        doThrow(new RuntimeException()).when(mock).setDataSource(new DataSource());
    }

    @Test
    public void testContactDAOmySQLgetById() {
        ContactDAOmySQL mock = mock(ContactDAOmySQL.class);
        Contact contact = new Contact();
        when(mock.getContactById("1")).thenReturn(contact);
        when(mock.getContactById("a")).thenReturn(null);
        assertEquals(contact, mock.getContactById("1"));
        assertEquals(null, mock.getContactById("a"));
    }

    @Test
    public void testContactDAOmySQLgetByIdUser() {
        ContactDAOmySQL mock = mock(ContactDAOmySQL.class);
        List<Contact> contacts = new ArrayList<>();
        when(mock.getByIdUser("1")).thenReturn(contacts);
        when(mock.getByIdUser("a")).thenReturn(contacts);
        assertEquals(contacts, mock.getByIdUser("1"));
        assertEquals(contacts, mock.getByIdUser("a"));
    }

    @Test
    public void testContactDAOmySQLgetByIdUserAndName() {
        ContactDAOmySQL mock = mock(ContactDAOmySQL.class);
        List<Contact> contacts = new ArrayList<>();
        when(mock.getByIdUserAndName("1", "aaa", "bbb", "567")).thenReturn(contacts);
        when(mock.getByIdUserAndName("a", "aaa", "bbb", "567")).thenReturn(contacts);
        assertEquals(contacts, mock.getByIdUserAndName("1", "aaa", "bbb", "567"));
        assertEquals(contacts, mock.getByIdUserAndName("a", "aaa", "bbb", "567"));
    }

    /**
     * Tests Contact DAO JSON
     */
    @Test
    public void testContactDAOJSONCreate() {
        ContactDAOJSON mock = mock(ContactDAOJSON.class);
        doThrow(new RuntimeException()).when(mock).create("userId", "lastName", "name", "middleName", "mobilePhone", "homePhone", "address", "email");
    }

    @Test
    public void testContactDAOJSONUpdate() {
        ContactDAOJSON mock = mock(ContactDAOJSON.class);
        doThrow(new RuntimeException()).when(mock).update("userId", "lastName", "name", "middleName", "mobilePhone", "homePhone", "address", "email");
    }

    @Test
    public void testContactDAOJSONDelete() {
        ContactDAOJSON mock = mock(ContactDAOJSON.class);
        doThrow(new RuntimeException()).when(mock).delete("id");
    }

    @Test
    public void testContactDAOJSONGetById() {
        ContactDAOJSON mock = mock(ContactDAOJSON.class);
        Contact contact = new Contact();
        when(mock.getContactById("1")).thenReturn(contact);
        when(mock.getContactById("a")).thenReturn(null);
        assertEquals(contact, mock.getContactById("1"));
        assertEquals(null, mock.getContactById("a"));
    }

    @Test
    public void testContactDAOJSONGetByIdUser() {
        ContactDAOJSON mock = mock(ContactDAOJSON.class);
        List<Contact> contacts = new ArrayList<>();
        when(mock.getByIdUser("1")).thenReturn(contacts);
        when(mock.getByIdUser("a")).thenReturn(contacts);
        assertEquals(contacts, mock.getByIdUser("1"));
        assertEquals(contacts, mock.getByIdUser("a"));
    }

    @Test
    public void testContactDAOJSONGetByIdUserAndName() {
        ContactDAOJSON mock = mock(ContactDAOJSON.class);
        List<Contact> contacts = new ArrayList<>();
        when(mock.getByIdUserAndName("1", "lastName", "name", "567")).thenReturn(contacts);
        when(mock.getByIdUserAndName("a", "lastName", "name", "567")).thenReturn(contacts);
        assertEquals(contacts, mock.getByIdUserAndName("1", "lastName", "name", "567"));
        assertEquals(contacts, mock.getByIdUserAndName("a", "lastName", "name", "567"));
    }

    /**
     * Tests User service
     */
    @SuppressWarnings("Duplicates")
    @Test
    public void testUserServiceGetByLogin() {
        UserService mock = mock(UserService.class);
        User user = new User();
        when(mock.getByLogin("login")).thenReturn(user);
        when(mock.getByLogin("111")).thenReturn(null);
        assertEquals(user, mock.getByLogin("login"));
        assertEquals(null, mock.getByLogin("111"));
    }

    @Test
    public void testUserServiceGetById() {
        UserService mock = mock(UserService.class);
        User user = new User();
        when(mock.getById("1")).thenReturn(user);
        when(mock.getById("aaa")).thenReturn(null);
        assertEquals(user, mock.getById("1"));
        assertEquals(null, mock.getById("aaa"));
    }

    @Test
    public void testUserServiceCreate() {
        UserService mock = mock(UserService.class);
        doThrow(new RuntimeException()).when(mock).create("fullName", "login", "password");
    }

    @Test
    public void testUserServiceSetDataSource() {
        UserService mock = mock(UserService.class);
        doThrow(new RuntimeException()).when(mock).setDataSource(new DataSource(), "typeDB", "pathToFileDB");
    }

    @Test
    public void testUserServiceOpenSession() {
        UserService mock = mock(UserService.class);
        doThrow(new RuntimeException()).when(mock).openSession(1, "11111111");
    }

    @Test
    public void testUserServiceCloseSession() {
        UserService mock = mock(UserService.class);
        doThrow(new RuntimeException()).when(mock).closeSession(0);
    }

    @Test
    public void testUserServiceGetAllUsers() {
        UserService mock = mock(UserService.class);
        ArrayList<User> users = new ArrayList<>();
        when(mock.getAllUsers()).thenReturn(users);
        assertEquals(users, mock.getAllUsers());
    }

    /**
     * Tests Contact service
     */
    @Test
    public void testContactServiceSetDataSource() {
        ContactService mock = mock(ContactService.class);
        doThrow(new RuntimeException()).when(mock).setDataSource(new DataSource(), "typeDB", "pathToFileDB");
    }

    @Test
    public void testContactServiceCreate() {
        ContactService mock = mock(ContactService.class);
        doThrow(new RuntimeException()).when(mock).create("idUser", "lastName", "name", "middleName", "mobilePhone", "homePhone", "address", "email");
    }

    @Test
    public void testContactServiceUpdate() {
        ContactService mock = mock(ContactService.class);
        doThrow(new RuntimeException()).when(mock).update("idUser", "idContact", "lastName", "name", "middleName", "mobilePhone", "homePhone", "address", "email");
    }

    @Test
    public void testContactServiceDelete() {
        ContactService mock = mock(ContactService.class);
        doThrow(new RuntimeException()).when(mock).delete("idContact");
    }

    @Test
    public void testContactServiceGetById() {
        ContactService mock = mock(ContactService.class);
        Contact contact = new Contact();
        when(mock.getById("1")).thenReturn(contact);
        when(mock.getById("a")).thenReturn(null);
        assertEquals(contact, mock.getById("1"));
        assertEquals(null, mock.getById("a"));
    }

    @Test
    public void testContactServiceGetByIdUser() {
        ContactService mock = mock(ContactService.class);
        List<Contact> contacts = new ArrayList<>();
        when(mock.getByIdUser("1")).thenReturn(contacts);
        when(mock.getByIdUser("a")).thenReturn(contacts);
        assertEquals(contacts, mock.getByIdUser("1"));
        assertEquals(contacts, mock.getByIdUser("a"));
    }

    @Test
    public void testContactServiceGetByIdUserAndName() {
        ContactService mock = mock(ContactService.class);
        List<Contact> contacts = new ArrayList<>();
        when(mock.getByIdUserAndName("1", "lastName", "name", "567")).thenReturn(contacts);
        when(mock.getByIdUserAndName("a", "lastName", "name", "567")).thenReturn(contacts);
        assertEquals(contacts, mock.getByIdUserAndName("1", "lastName", "name", "567"));
        assertEquals(contacts, mock.getByIdUserAndName("a", "lastName", "name", "567"));
    }

    /**
     * Tests REST main.java.rest
     */

    @Test
    public void testRESTControlGetMySQLDriverManagerDatasource() {
        StartController.MyBean mock = mock(StartController.MyBean.class);
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        when(mock.getMySQLDriverManagerDatasource()).thenReturn(driverManagerDataSource);
        assertEquals(driverManagerDataSource, mock.getMySQLDriverManagerDatasource());
    }
}
