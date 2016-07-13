package dao;

import entity.Contact;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author by Vadim Sharomov
 *
 */
public class ContactDAOJSON extends AbstractDAO {
    private final static Logger logger = getLogger(ContactDAOJSON.class);
    private String pathToFileDB;
    private String nameFile;
    private String nameTableContacts;
    private JSONParser parser;

    private ContactDAOJSON() {
        this.parser = new JSONParser();
        this.nameTableContacts = "contacts";
    }

    private static class SingleToneHelper {
        private static final ContactDAOJSON INSTANCE = new ContactDAOJSON();
    }

    public static ContactDAOJSON getInstance() {
        return SingleToneHelper.INSTANCE;
    }

    @Override
    public void setTypeDB(String pathToFileDB) {
        this.pathToFileDB = pathToFileDB;
        this.nameFile = "contacts.json";
        File fileDB = new File(pathToFileDB + nameFile);
        if (!fileDB.exists()) {
            try {
                JSONObject obj = new JSONObject();
                FileWriter file = new FileWriter(fileDB);
                JSONArray list = new JSONArray();
                obj.put(nameTableContacts, list);

                file.write(obj.toJSONString());
                file.flush();
                file.close();
            } catch (IOException e) {
                logger.error("Can't access to file DB: '" + fileDB + "'", e.getMessage());
            }
        }

    }

    @Override
    public void create(String userId, String lastName, String name, String middleName, String mobilePhone, String homePhone, String address, String email) {
        try {
            Object obj = parser.parse(new FileReader(pathToFileDB + nameFile));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray jContactsArray = (JSONArray) jsonObject.get(nameTableContacts);
            long maxId = getMaxId(jContactsArray);
            JSONObject jo = new JSONObject();
            jo.put("id", String.valueOf(maxId + 1));
            jo.put("iduser", String.valueOf(userId));
            jo.put("lastname", lastName);
            jo.put("name", name);
            jo.put("middlename", middleName);
            jo.put("mobilephone", mobilePhone);
            jo.put("homephone", homePhone);
            jo.put("address", address);
            jo.put("email", email);
            jContactsArray.add(jo);
            jsonObject.put(nameTableContacts, jContactsArray);

            saveFile(jsonObject, pathToFileDB + nameFile);
        } catch (FileNotFoundException e) {
            logger.error("Can't create contact in file DB: '" + pathToFileDB + nameFile + "'", e.getMessage());
        } catch (IOException e) {
            logger.error("Can't access create contact in file DB: '" + pathToFileDB + nameFile + "'", e.getMessage());
        } catch (ParseException e) {
            logger.error("Can't parse file DB for create contact: '" + pathToFileDB + nameFile + "'", e.getMessage());
        }
    }

    @Override
    public Contact getContactById(String id) {
        ArrayList<Contact> contacts = new ArrayList<>();
        try {
            Object obj = parser.parse(new FileReader(pathToFileDB + nameFile));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray jContactsArray = (JSONArray) jsonObject.get(nameTableContacts);
            for (Object o : jContactsArray) {
                JSONObject jo = (JSONObject) o;
                if (Long.parseLong(jo.get("id").toString()) == Long.parseLong(id)) {
                    return getContactFromJSONObject(jo);
                }
            }
        } catch (FileNotFoundException e) {
            logger.error("Can't getContactById from file DB: '" + pathToFileDB + nameFile + "'", e.getMessage());
        } catch (IOException e) {
            logger.error("Can't getContactById, can't access to file DB: '" + pathToFileDB + nameFile + "'", e.getMessage());
        } catch (ParseException e) {
            logger.error("Can't parse file DB for getContactById: '" + pathToFileDB + nameFile + "'", e.getMessage());
        }
        return null;
    }

    @Override
    public List<Contact> getByIdUser(String idUser) {
        List<Contact> contacts = new ArrayList<>();

        try {
            Object obj = parser.parse(new FileReader(pathToFileDB + nameFile));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray jContactsArray = (JSONArray) jsonObject.get(nameTableContacts);
            for (Object o : jContactsArray) {
                JSONObject jo = (JSONObject) o;
                if (Long.parseLong(jo.get("iduser").toString()) == Long.parseLong(idUser)) {
                    contacts.add(getContactFromJSONObject(jo));
                }
            }
        } catch (FileNotFoundException e) {
            logger.error("Can't getByIdUser from file DB: '" + pathToFileDB + nameFile + "'", e.getMessage());
        } catch (IOException e) {
            logger.error("Can't getByIdUser, can't access to file DB: '" + pathToFileDB + nameFile + "'", e.getMessage());
        } catch (ParseException e) {
            logger.error("Can't parse file DB for getByIdUser: '" + pathToFileDB + nameFile + "'", e.getMessage());
        }

        return contacts;
    }

    @Override
    public List<Contact> getByIdUserAndName(String idUser, String lastName, String name, String mobilePhone) {
        List<Contact> contacts = new ArrayList<>();

        try {
            Object obj = parser.parse(new FileReader(pathToFileDB + nameFile));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray jContactsArray = (JSONArray) jsonObject.get(nameTableContacts);
            for (Object o : jContactsArray) {
                JSONObject jo = (JSONObject) o;
                if ((Long.parseLong(jo.get("iduser").toString()) == Long.parseLong(idUser)) &&
                        jo.get("lastname").toString().toLowerCase().contains(lastName.toLowerCase()) &&
                        jo.get("name").toString().toLowerCase().contains(name.toLowerCase()) &&
                        jo.get("mobilephone").toString().contains(mobilePhone)) {
                    contacts.add(getContactFromJSONObject(jo));
                }
            }
        } catch (FileNotFoundException e) {
            logger.error("Can't getByIdUserAndName from file DB: '" + pathToFileDB + nameFile + "'", e.getMessage());
        } catch (ParseException e) {
            logger.error("Can't parse file DB for getByIdUserAndName: '" + pathToFileDB + nameFile + "'", e.getMessage());
        } catch (IOException e) {
            logger.error("Can't getByIdUserAndName, can't access to file DB: '" + pathToFileDB + nameFile + "'", e.getMessage());
        }

        return contacts;
    }

    @Override
    public void update(String id, String lastName, String name, String middleName, String mobilePhone, String homePhone, String address, String email) {
        List<Contact> contacts;
        try {
            Object obj = parser.parse(new FileReader(pathToFileDB + nameFile));
            JSONObject jObject = (JSONObject) obj;
            JSONArray jContactsArray = new JSONArray();
            contacts = getAllContacts();
            for (int i = 0; i < contacts.size(); i++) {
                if (contacts.get(i).getId() == Long.parseLong(id)) {
                    contacts.get(i).setLastName(lastName);
                    contacts.get(i).setName(name);
                    contacts.get(i).setMiddleName(middleName);
                    contacts.get(i).setMobilePhone(mobilePhone);
                    contacts.get(i).setHomePhone(homePhone);
                    contacts.get(i).setAddress(address);
                    contacts.get(i).setEmail(email);
                }
                JSONObject jo = new JSONObject();
                putContactToJSONObject(jo, contacts.get(i));

                jContactsArray.add(jo);

            }
            jObject.put(nameTableContacts, jContactsArray);
            saveFile(jObject, pathToFileDB + nameFile);
        } catch (FileNotFoundException e) {
            logger.error("Can't update contact in file DB: '" + pathToFileDB + nameFile + "'", e.getMessage());
        } catch (IOException e) {
            logger.error("Can't update contact, can't access to file DB: '" + pathToFileDB + nameFile + "'", e.getMessage());
        } catch (ParseException e) {
            logger.error("Can't parse file DB for update contact: '" + pathToFileDB + nameFile + "'", e.getMessage());
        }
    }

    @Override
    public void delete(String id) {
        List<Contact> contacts;
        try {
            Object obj = parser.parse(new FileReader(pathToFileDB + nameFile));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray jContactsArray = new JSONArray();
            contacts = getAllContacts();
            for (int i = 0; i < contacts.size(); i++) {
                if (contacts.get(i).getId() == Long.parseLong(id)) {
                    contacts.remove(i);
                    break;
                }
            }
            for (int i = 0; i < contacts.size(); i++) {
                JSONObject jo = new JSONObject();
                putContactToJSONObject(jo, contacts.get(i));

                jContactsArray.add(jo);
            }

            jsonObject.put(nameTableContacts, jContactsArray);
            saveFile(jsonObject, pathToFileDB + nameFile);
        } catch (FileNotFoundException e) {
            logger.error("Can't delete contact in file DB: '" + pathToFileDB + nameFile + "'", e.getMessage());
        } catch (IOException e) {
            logger.error("Can't delete contact, can't access to file DB: '" + pathToFileDB + nameFile + "'", e.getMessage());
        } catch (ParseException e) {
            logger.error("Can't parse file DB for delete contact: '" + pathToFileDB + nameFile + "'", e.getMessage());
        }
    }

    private List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();

        try {
            Object obj = parser.parse(new FileReader(pathToFileDB + nameFile));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray jContactsArray = (JSONArray) jsonObject.get(nameTableContacts);
            for (Object o : jContactsArray) {
                JSONObject jo = (JSONObject) o;
                contacts.add(getContactFromJSONObject(jo));
            }
        } catch (FileNotFoundException e) {
            logger.error("Can't getAllContacts in file DB: '" + pathToFileDB + nameFile + "'", e.getMessage());
        } catch (IOException e) {
            logger.error("Can't getAllContacts, can't access to file DB: '" + pathToFileDB + nameFile + "'", e.getMessage());
        } catch (ParseException e) {
            logger.error("Can't parse file DB for getAllContacts: '" + pathToFileDB + nameFile + "'", e.getMessage());
        }

        return contacts;
    }

    private void putContactToJSONObject(JSONObject jo, Contact contact) {
        jo.put("iduser", String.valueOf(contact.getUserId()));
        jo.put("id", contact.getId());
        jo.put("lastname", contact.getLastName());
        jo.put("name", contact.getName());
        jo.put("middlename", contact.getMiddleName());
        jo.put("mobilephone", contact.getMobilePhone());
        jo.put("homephone", contact.getHomePhone());
        jo.put("address", contact.getAddress());
        jo.put("email", contact.getEmail());
    }

    private Contact getContactFromJSONObject(JSONObject jo) {
        Contact contact = new Contact();
        contact.setId(Long.parseLong(jo.get("id").toString()));
        contact.setUserId(Long.parseLong(jo.get("iduser").toString()));
        contact.setLastName(jo.get("lastname").toString());
        contact.setName(jo.get("name").toString());
        contact.setMiddleName(jo.get("middlename").toString());
        contact.setMobilePhone(jo.get("mobilephone").toString());
        contact.setHomePhone(jo.get("homephone").toString());
        contact.setAddress(jo.get("address").toString());
        contact.setEmail(jo.get("email").toString());
        return contact;
    }
}
