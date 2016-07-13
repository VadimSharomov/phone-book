package main.java.dao;

import main.java.entity.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;

import java.io.*;
import java.util.ArrayList;

import static org.slf4j.LoggerFactory.getLogger;


/**
 * Created by Vadim on 30.04.2016.
 * Implementation JSON format
 */
public class UserDAOJSON extends AbstractDAO {
    private final static Logger logger = getLogger(UserDAOJSON.class);
    private String pathToFileDB;
    private String nameFile;
    private JSONParser parser;

    private UserDAOJSON() {
        this.parser = new JSONParser();
    }

    private static class SingleToneHelper {
        private static final UserDAOJSON INSTANCE = new UserDAOJSON();
    }

    public static UserDAOJSON getInstance() {
        return SingleToneHelper.INSTANCE;
    }

    @Override
    public void setTypeDB(String pathToFileDB) {
        this.pathToFileDB = pathToFileDB;
        this.nameFile = "users.json";
        File f = new File(pathToFileDB + nameFile);
        if (!f.exists()) {
            try {
                JSONObject obj = new JSONObject();
                FileWriter file = new FileWriter(f);
                JSONArray list = new JSONArray();
                obj.put("users", list);

                file.write(obj.toJSONString());
                file.flush();
                file.close();
            } catch (IOException e) {
                logger.error("Can't access to file DB in setTypeDB: '" + pathToFileDB + nameFile + "'", e.getMessage());
            }
        }

    }

    @Override
    public void create(String fullName, String login, String password) {
        try {
            Object obj = parser.parse(new FileReader(pathToFileDB + nameFile));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray jUsers = (JSONArray) jsonObject.get("users");
            long maxId = getMaxId(jUsers);
            JSONObject jo = new JSONObject();
            jo.put("id", String.valueOf(maxId + 1));
            jo.put("idsession", "1");
            jo.put("fullname", fullName);
            jo.put("login", login);
            jo.put("password", password);
            jUsers.add(jo);
            jsonObject.put("users", jUsers);

            saveFile(jsonObject, pathToFileDB + nameFile);
        } catch (FileNotFoundException e) {
            logger.error("Can't create user in file DB: '" + pathToFileDB + nameFile + "'", e.getMessage());
        } catch (IOException e) {
            logger.error("Can't access to file DB in create user: '" + pathToFileDB + nameFile + "'", e.getMessage());
        } catch (ParseException e) {
            logger.error("Can't parse file DB for create user: '" + pathToFileDB + nameFile + "'", e.getMessage());
        }
    }

    @Override
    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        try {
            Object obj = parser.parse(new FileReader(pathToFileDB + nameFile));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray jUsers = (JSONArray) jsonObject.get("users");
            for (Object o : jUsers) {
                JSONObject jo = (JSONObject) o;
                users.add(getUserFromJSONObject(jo));
            }
        } catch (FileNotFoundException e) {
            logger.error("Can't getAllUsers from file DB: '" + pathToFileDB + nameFile + "'", e.getMessage());
        } catch (IOException e) {
            logger.error("Can't access to file DB in getAllUsers: '" + pathToFileDB + nameFile + "'", e.getMessage());
        } catch (ParseException e) {
            logger.error("Can't parse file DB for getAllUsers: '" + pathToFileDB + nameFile + "'", e.getMessage());
        }
        return users;
    }

    @Override
    public User getUserById(String id) {
        ArrayList<User> users = new ArrayList<>();
        try {
            Object obj = parser.parse(new FileReader(pathToFileDB + nameFile));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray jUsers = (JSONArray) jsonObject.get("users");
            for (Object o : jUsers) {
                JSONObject jo = (JSONObject) o;
                if (Long.parseLong(jo.get("id").toString()) == Long.parseLong(id)) {
                    return getUserFromJSONObject(jo);
                }
            }
        } catch (FileNotFoundException e) {
            logger.error("Can't getUserById from file DB: '" + pathToFileDB + nameFile + "'", e.getMessage());
        } catch (IOException e) {
            logger.error("Can't access to file DB in getUserById: '" + pathToFileDB + nameFile + "'", e.getMessage());
        } catch (ParseException e) {
            logger.error("Can't parse file DB for getUserById: '" + pathToFileDB + nameFile + "'", e.getMessage());
        }
        return null;
    }

    @Override
    public User getByLogin(String login) {
        try {
            Object obj = parser.parse(new FileReader(pathToFileDB + nameFile));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray jUsers = (JSONArray) jsonObject.get("users");
            for (Object o : jUsers) {
                JSONObject jo = (JSONObject) o;
                if (jo.get("login").equals(login)) {
                    return getUserFromJSONObject(jo);
                }
            }
        } catch (FileNotFoundException e) {
            logger.error("Can't getByLogin from file DB: '" + pathToFileDB + nameFile + "'", e.getMessage());
        } catch (IOException e) {
            logger.error("Can't access to file DB in getByLogin: '" + pathToFileDB + nameFile + "'", e.getMessage());
        } catch (ParseException e) {
            logger.error("Can't parse file DB for getByLogin: '" + pathToFileDB + nameFile + "'", e.getMessage());
        }
        return null;
    }

    @Override
    public void updateIdSession(long id, long idSession) {
        ArrayList<User> users = new ArrayList<>();
        try {
            Object obj = parser.parse(new FileReader(pathToFileDB + nameFile));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray jUsers = new JSONArray();
            users = getAllUsers();
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getId() == id) {
                    users.get(i).setIdSession(idSession);
                }
                JSONObject jo = new JSONObject();
                jo.put("id", users.get(i).getId());
                jo.put("idsession", users.get(i).getIdSession());
                jo.put("fullname", users.get(i).getFullName());
                jo.put("login", users.get(i).getLogin());
                jo.put("password", users.get(i).getPassword());
                jUsers.add(jo);
            }
            jsonObject.put("users", jUsers);
            saveFile(jsonObject, pathToFileDB + nameFile);
        } catch (FileNotFoundException e) {
            logger.error("Can't updateIdSession from file DB: '" + pathToFileDB + nameFile + "'", e.getMessage());
        } catch (IOException e) {
            logger.error("Can't access to file DB in updateIdSession: '" + pathToFileDB + nameFile + "'", e.getMessage());
        } catch (ParseException e) {
            logger.error("Can't parse file DB for updateIdSession: '" + pathToFileDB + nameFile + "'", e.getMessage());
        }
    }

    private User getUserFromJSONObject(JSONObject jo) {
        User user = new User(jo.get("fullname").toString(), jo.get("login").toString(), jo.get("password").toString());
        user.setId(Long.parseLong(jo.get("id").toString()));
        user.setIdSession(Long.parseLong(jo.get("idsession").toString()));
        return user;
    }
}
