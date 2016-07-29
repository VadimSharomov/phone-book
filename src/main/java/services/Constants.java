package services;

import org.slf4j.Logger;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static java.net.InetAddress.getLocalHost;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author by Vadim Sharomov
 */
public class Constants {
    private final static Logger logger = getLogger(Constants.class);
    private static String typeDB;
    private static String pathToDBFiles;
    private static String myIP;
    private static List<Long> initIdSessionList = new ArrayList<>();
    private static final String PATTERN_LOGIN = "[a-z,A-Z]{3,}";
    private static final String PATTERN_MOBILE_PHONE_UKR = "[+][3][8][0][(][3569][0-9][)][0-9]{7}";
    private static final String PATTERN_STATIONARY_PHONE_UKR = "[+][3][8][0][(][3456][0-9][)][0-9]{7}";
    private static final String PATTERN_EMAIL = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

    static {
        try {
            Constants.myIP = getLocalHost().getHostAddress();
            logger.info("Got ip address: '" + Constants.myIP + "'");
        } catch (UnknownHostException e) {
            logger.error("Not got ip address!", e.getMessage());
            System.exit(1);
        }
    }

    public static String getPathToDBFiles() {
        return pathToDBFiles;
    }

    public static void setPathToDBFiles(String pathToDBFiles) {
        Constants.pathToDBFiles = pathToDBFiles;
    }

    public static String getTypeDB() {
        return typeDB;
    }

    public static void setTypeDB(String typeDB) {
        Constants.typeDB = typeDB;
    }

    public static String getMyIP() {
        return myIP;
    }

    public static List<Long> getInitIdSessionList() {
        return initIdSessionList;
    }

    public static void addInitIdSession(Long idSession) {
        Constants.initIdSessionList.add(idSession);
    }

    public static String getPatternEmail() {
        return PATTERN_EMAIL;
    }

    public static String getPatternMobilePhoneUkr() {
        return PATTERN_MOBILE_PHONE_UKR;
    }

    public static String getPatternStationaryPhoneUkr() {
        return PATTERN_STATIONARY_PHONE_UKR;
    }

    public static String getPatternLogin() {
        return PATTERN_LOGIN;
    }
}
