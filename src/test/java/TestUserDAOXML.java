import dao.UserDAOXML;
import entity.User;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by Vadim
 */
public class TestUserDAOXML {
    /**
     * TestREST User DAO MXL
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
}
