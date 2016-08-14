import dao.UserDAOJSON;
import entity.User;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by Vadim
 */
public class TestUserDAOJSON {
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
}
