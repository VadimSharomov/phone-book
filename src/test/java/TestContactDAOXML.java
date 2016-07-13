import dao.ContactDAOXML;
import entity.Contact;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

/**
 * Created by Vadim
 */
public class TestContactDAOXML {
    /**
     * TestREST Contact DAO XML
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
}
