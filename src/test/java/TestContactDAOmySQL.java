import dao.ContactDAOmySQL;
import entity.Contact;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

/**
 * Created by Vadim
 */
public class TestContactDAOmySQL {
    /**
     * TestREST Contact DAO mySQL
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
}
