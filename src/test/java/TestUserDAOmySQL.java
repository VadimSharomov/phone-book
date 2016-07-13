import dao.UserDAOmySQL;
import entity.User;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

/**
 * Created by Vadim
 */
public class TestUserDAOmySQL {
    /**
     * TestREST User DAO mySQL
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
}
