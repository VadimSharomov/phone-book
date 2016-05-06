package dao;

import entity.Contact;
import interfaces.ContactDAO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.sql.DataSource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vadim on 06.05.2016.
 *
 */
public class ContactDAOJSON implements ContactDAO {
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

    public void setTypeDB(String pathToFileDB) {
        this.pathToFileDB = pathToFileDB;
        this.nameFile = "contacts.json";
        File f = new File(pathToFileDB + nameFile);
        if (!f.exists()) {
            try {
                JSONObject obj = new JSONObject();
                FileWriter file = new FileWriter(f);
                JSONArray list = new JSONArray();
                obj.put(nameTableContacts, list);

                file.write(obj.toJSONString());
                file.flush();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void setDataSource(DataSource dataSource) {

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

            saveFile(jsonObject);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Contact getById(String id) {
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
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
        } catch (FileNotFoundException | ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
        } catch (FileNotFoundException | ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contacts;
    }

    @Override
    public void update(String id, String lastName, String name, String middleName, String mobilePhone, String homePhone, String address, String email) {
        List<Contact> contacts = new ArrayList<>();
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
            saveFile(jObject);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        List<Contact> contacts = new ArrayList<>();
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
            saveFile(jsonObject);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
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
        } catch (FileNotFoundException | ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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

    private long getMaxId(JSONArray jContactsArray) {
        long maxId = 0;
        for (Object o : jContactsArray) {
            JSONObject jo = (JSONObject) o;
            if (maxId < Long.parseLong(jo.get("id").toString())) {
                maxId = Long.parseLong(jo.get("id").toString());
            }
        }
        return maxId;
    }

    private void saveFile(JSONObject jsonObject) {
        try {
            FileWriter file = new FileWriter(pathToFileDB + nameFile);
            file.write(jsonObject.toJSONString());
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
