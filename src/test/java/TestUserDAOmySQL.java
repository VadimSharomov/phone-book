import dao.UserDAOmySQL;
import entity.User;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


/**
 * Created by Vadim
 */
public class TestUserDAOmySQL {
    @Test
    public void testUserDAOmySQLCreate() {
        UserDAOmySQL mock = mock(UserDAOmySQL.class);
        doThrow(new RuntimeException()).when(mock).create("fullName", "login", "password");
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
}
