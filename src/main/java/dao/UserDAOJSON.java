package dao;

import entity.CustomUser;
import entity.UserRole;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Vadim Sharomov
 */
@Service("userjson")
public class UserDAOJSON extends AbstractDAO implements UserDAO{
    private final static Logger logger = getLogger(UserDAOJSON.class);
    private String pathToFileDB;
    private String nameFile;
    private JSONParser parser;

    private UserDAOJSON() {
        this.parser = new JSONParser();
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
    public void create(String fullName, String login, String password, UserRole role) {
        try {
            Object obj = parser.parse(new FileReader(pathToFileDB + nameFile));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray jUsers = (JSONArray) jsonObject.get("users");
            long maxId = getMaxId(jUsers);
            JSONObject jo = new JSONObject();
            jo.put("id", String.valueOf(maxId + 1));
            jo.put("fullname", fullName);
            jo.put("login", login);
            jo.put("password", password);
            jo.put("role", role.toString());
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
    public List<CustomUser> getAllUsers() {
        List<CustomUser> customUsers = new ArrayList<>();
        try {
            Object obj = parser.parse(new FileReader(pathToFileDB + nameFile));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray jUsers = (JSONArray) jsonObject.get("customUsers");
            for (Object o : jUsers) {
                JSONObject jo = (JSONObject) o;
                customUsers.add(getUserFromJSONObject(jo));
            }
        } catch (FileNotFoundException e) {
            logger.error("Can't getAllUsers from file DB: '" + pathToFileDB + nameFile + "'", e.getMessage());
        } catch (IOException e) {
            logger.error("Can't access to file DB in getAllUsers: '" + pathToFileDB + nameFile + "'", e.getMessage());
        } catch (ParseException e) {
            logger.error("Can't parse file DB for getAllUsers: '" + pathToFileDB + nameFile + "'", e.getMessage());
        }
        return customUsers;
    }

    @Override
    public CustomUser getUserById(String id) {
        List<CustomUser> customUsers = new ArrayList<>();
        try {
            Object obj = parser.parse(new FileReader(pathToFileDB + nameFile));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray jUsers = (JSONArray) jsonObject.get("customUsers");
            for (Object o : jUsers) {
                JSONObject jo = (JSONObject) o;
                if (Long.parseLong(jo.get("id").toString()) == Long.parseLong(id)) {
                    return getUserFromJSONObject(jo);
                }
            }
        } catch (FileNotFoundException e) {
            logger.error("Can't getContactById from file DB: '" + pathToFileDB + nameFile + "'", e.getMessage());
        } catch (IOException e) {
            logger.error("Can't access to file DB in getContactById: '" + pathToFileDB + nameFile + "'", e.getMessage());
        } catch (ParseException e) {
            logger.error("Can't parse file DB for getContactById: '" + pathToFileDB + nameFile + "'", e.getMessage());
        }
        return null;
    }

    @Override
    public CustomUser getByLogin(String login) {
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
            logger.error("Can't getUserByLogin from file DB: '" + pathToFileDB + nameFile + "'", e.getMessage());
        } catch (IOException e) {
            logger.error("Can't access to file DB in getUserByLogin: '" + pathToFileDB + nameFile + "'", e.getMessage());
        } catch (ParseException e) {
            logger.error("Can't parse file DB for getUserByLogin: '" + pathToFileDB + nameFile + "'", e.getMessage());
        }
        return null;
    }


    private CustomUser getUserFromJSONObject(JSONObject jo) {
        CustomUser customUser = new CustomUser(jo.get("fullname").toString(), jo.get("login").toString(), jo.get("password").toString(), UserRole.valueOf(jo.get("role").toString()));
        customUser.setId(Long.parseLong(jo.get("id").toString()));
        return customUser;
    }
}
