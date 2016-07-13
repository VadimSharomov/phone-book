import entity.User;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.testng.annotations.Test;
import services.UserService;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by Vadim
 */
public class TestUserService {
    /**
     * TestREST User service
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
}
