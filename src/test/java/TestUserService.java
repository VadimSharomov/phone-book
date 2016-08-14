import dao.UserDAO;
import entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rest.Application;
import services.UserService;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Vadim Sharomov
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestUserService {

    @Autowired
    private Map<String, UserDAO> daoServices;

    @Autowired
    private UserService userService;

    @Test
    public void testUserServiceGetByLogin() {
        UserService mock = mock(UserService.class);
        User user = new User();
        when(mock.getByLogin("1")).thenReturn(user);
        when(mock.getByLogin("aaa")).thenReturn(null);
        assertEquals(user, mock.getByLogin("1"));
        assertEquals(null, mock.getByLogin("aaa"));
    }

    @Test
    public void testUserServiceGetById() {
        UserService mock = mock(UserService.class);
        User user = new User();
        when(mock.getUserById("1")).thenReturn(user);
        when(mock.getUserById("aaa")).thenReturn(null);
        assertEquals(user, mock.getUserById("1"));
        assertEquals(null, mock.getUserById("aaa"));
    }

    @Test
    public void testUserServiceCreate() {
        UserService mock = mock(UserService.class);
        doThrow(new RuntimeException()).when(mock).create("fullName", "login", "password");
    }

    @Test
    public void testUserServiceGetAllUsers() {
        UserService mock = mock(UserService.class);
        ArrayList<User> users = new ArrayList<>();
        when(mock.getAllUsers()).thenReturn(users);
        assertEquals(users, mock.getAllUsers());
    }
}
