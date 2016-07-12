package main.java.services;

import main.java.entity.User;
import main.java.rest.StartController;
import org.slf4j.Logger;

import java.util.ArrayList;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Vadim
 * 12.07.2016.
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
            ArrayList<User> usersList = UserService.getInstance().getAllUsers();
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
