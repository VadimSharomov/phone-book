package main.java.services;

/**
 * Created by Vadim
 * 12.07.2016.
 */
public class Constants {
    private static String typeDB;
    private static String hostDB;
    private static String userDB;
    private static String userPasswordDB;
    private static String pathToDBFiles;

    public static String getHostDB() {
        return hostDB;
    }

    public static void setHostDB(String hostDB) {
        Constants.hostDB = hostDB;
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

    public static String getUserDB() {
        return userDB;
    }

    public static void setUserDB(String userDB) {
        Constants.userDB = userDB;
    }

    public static String getUserPasswordDB() {
        return userPasswordDB;
    }

    public static void setUserPasswordDB(String userPasswordDB) {
        Constants.userPasswordDB = userPasswordDB;
    }
}
