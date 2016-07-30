import entity.Contact;
import org.testng.annotations.Test;
import services.ContactService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

/**
 * Created by Vadim
 */
public class TestContactService {
    @Test
    public void testContactServiceSetDataSource() {
        ContactService mock = mock(ContactService.class);
        doThrow(new RuntimeException()).when(mock).setDataSource("typeDB", "pathToFileDB");
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
}
