import dao.UserDAO;
import dao.UserDAOXML;
import entity.User;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by Vadim
 */
public class TestUserDAOXML {
    @SuppressWarnings("Duplicates")
    @Test
    public void testUserDAOXMLgetById() {
        UserDAO userDAOXML = mock(UserDAOXML.class);
        User user = new User();
        when(userDAOXML.getUserById("1")).thenReturn(user);
        when(userDAOXML.getUserById("a")).thenReturn(null);
        assertEquals(user, userDAOXML.getUserById("1"));
        assertEquals(null, userDAOXML.getUserById("a"));
    }

    @Test
    public void testUserDAOXMLcreate() {
        UserDAOXML mock = mock(UserDAOXML.class);
        doThrow(new RuntimeException()).when(mock).create("fullName", "login", "password");
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
