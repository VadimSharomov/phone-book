package services;

import entity.User;
import rest.StartController;
import org.slf4j.Logger;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author by Vadim Sharomov
 */
public class UtilsRest {
    private final static Logger logger = getLogger(StartController.class);
    public static boolean isSessionOver(User user, String idSession) {
        logger.info("User: '" + user + "' - session is over");
        return (user.getIdSession() != Long.parseLong(idSession));
    }

    public static void closeSession(String idUser, String idSession) {
        UserService.getInstance().closeSession(Long.parseLong(idUser));
        if (Constants.getInitIdSessionList().size() != 0) {
            if (Constants.getInitIdSessionList().indexOf(Long.parseLong(idSession)) >= 0) {
                Constants.getInitIdSessionList().remove(Constants.getInitIdSessionList().indexOf(Long.parseLong(idSession)));
            }
            List<User> usersList = UserService.getInstance().getAllUsers();
            boolean canClear = true;
            for (User usr : usersList) {
                if (Constants.getInitIdSessionList().contains(usr.getIdSession())) {
                    canClear = false;
                }
            }
            if (canClear) Constants.getInitIdSessionList().clear();
        }
    }

    public static long generateIdSession() {
        Constants.getInitIdSessionList().add(System.currentTimeMillis());
        return Constants.getInitIdSessionList().get(Constants.getInitIdSessionList().size() - 1);
    }
}
