package controller;

import entity.Contact;
import entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import services.ContactService;
import services.UserService;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import static java.net.InetAddress.getLocalHost;

@RestController
public class RestControl {
    private String myIP;
    private String pathToHTMLFiles;
    private Properties properties = new Properties();
    private static final String PATTERN_LOGIN = "[a-z,A-Z]{3,}";
    private static final String PATTERN_MOBILE_PHONE_UKR = "[+][3][8][0][(][3569][0-9][)][0-9]{7}";
    private static final String PATTERN_STATIONARY_PHONE_UKR = "[+][3][8][0][(][3456][0-9][)][0-9]{7}";
    private static final String PATTERN_EMAIL = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    private ArrayList<Long> initIdSessionList = new ArrayList<>();

    {
        try {
            myIP = getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Component
    public class MyBean {
        @SuppressWarnings("SpringJavaAutowiringInspection")
        @Autowired
        public MyBean(ApplicationArguments args) {
            List<String> listArgs = args.getNonOptionArgs();
            try {
                if (listArgs.size() == 0) {
                    System.out.println("\nIn arguments JVM not found the path to file.properties!");
                    System.exit(1);
                }
                String pathToConfigFile = listArgs.get(0);
                InputStream input = new FileInputStream(pathToConfigFile);
                properties.load(input);
                pathToHTMLFiles = properties.getProperty("pathHTMLFiles");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Bean
        public DriverManagerDataSource getMySQLDriverManagerDatasource() {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUrl(properties.getProperty("hostDB"));
            dataSource.setUsername(properties.getProperty("userDB"));
            dataSource.setPassword(properties.getProperty("userPasswordDB"));

            UserService.getInstance().setDataSource(dataSource, properties.getProperty("typeDB"), properties.getProperty("pathToDBFiles"));
            ContactService.getInstance().setDataSource(dataSource, properties.getProperty("typeDB"), properties.getProperty("pathToDBFiles"));
            return dataSource;
        }
    }

    @RequestMapping("/test")
    public String greeting(
            @RequestParam(value = "name", required = false) String name) {
        return "Hello, World!";
    }

    @RequestMapping("/")
    public String homePage(
            @RequestParam(value = "name", required = false) String name) {
        return homePageHTML();
    }

    private String homePageHTML() {
        try {

            return new String(Files.readAllBytes(Paths.get(pathToHTMLFiles, "HomePage.html"))).replace("myipaddress", myIP).replace("idSessionValue", String.valueOf(generateIdSession()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "File not found: HomePage.html to path 'pathHTMLFiles' in file.properties: " + pathToHTMLFiles;
    }

    private long generateIdSession() {
        initIdSessionList.add(System.currentTimeMillis());
        return initIdSessionList.get(initIdSessionList.size() - 1);
    }

    @RequestMapping("/login")
    public String loginPage(
            @RequestParam(value = "idsession", required = false) String idSession,
            @RequestParam(value = "login", required = false) String login,
            @RequestParam(value = "password", required = false) String password) {

        if (!initIdSessionList.contains(Long.parseLong(idSession))) {
            return showWarning(homePageHTML(), "Session is over, you need to login!");
        }

        if ((login == null) || (login.length() < 3)) {
            return showWarning(homePageHTML(), "Login is to short!");
        }
        if ((password == null) || (password.length() < 5)) {
            return showWarning(homePageHTML(), "Password is to short!");
        }

        User user = UserService.getInstance().getByLogin(login);
        if (user == null) {
            return registrationPageHTML(idSession).replace("idSessionValue", String.valueOf(idSession));
        }

        if (!password.equals(user.getPassword())) {
            return showWarning(homePageHTML(), "Password is incorrect!");
        }
        UserService.getInstance().openSession(user.getId(), idSession);
        return viewContacts(String.valueOf(user.getId()), idSession, null).replace("idSessionValue", String.valueOf(idSession));
    }

    @RequestMapping("/logout")
    public String logout(
            @RequestParam(value = "iduser", required = false) String idUser,
            @RequestParam(value = "idsession", required = false) String idSession) {
        if ((idUser != null) && (idSession) != null) {
            closeSession(idUser, idSession);
        }
        return homePageHTML();
    }

    private void closeSession(String idUser, String idSession) {
        UserService.getInstance().closeSession(Long.parseLong(idUser));
        if (initIdSessionList.size() != 0) {
            if (initIdSessionList.indexOf(Long.parseLong(idSession)) >= 0) {
                initIdSessionList.remove(initIdSessionList.indexOf(Long.parseLong(idSession)));
            }
            ArrayList<User> usersList = UserService.getInstance().getAllUsers();
            boolean canClear = true;
            for (User usr : usersList) {
                if (initIdSessionList.contains(usr.getIdSession())) {
                    canClear = false;
                }
            }
            if (canClear) initIdSessionList.clear();
        }
    }

    @RequestMapping("/registration")
    public String authorization(
            @RequestParam(value = "name", required = false) String fullName,
            @RequestParam(value = "login", required = false) String login,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "idsession", required = false) String idSession) {

        if (fullName == null && login == null && password == null) {
            if (idSession == null) {
                return homePage(idSession);
            } else {
                return registrationPageHTML(idSession);
            }
        }

        if ((fullName == null) || (fullName.length() < 5)) {
            return showWarning(registrationPageHTML(idSession), "Full name is to short!");
        }
        if ((login == null) || (login.length() < 3)) {
            return showWarning(registrationPageHTML(idSession), "Login is to short!");
        }

        Pattern p = Pattern.compile(PATTERN_LOGIN);
        if (!p.matcher(login).matches()) {
            return showWarning(registrationPageHTML(idSession), "Incorrect characters in login!");
        }

        if ((password == null) || (password.length() < 5)) {
            return showWarning(registrationPageHTML(idSession), "Password is to short!");
        }

        User user = UserService.getInstance().getByLogin(login);
        if (user != null) {
            return showWarning(registrationPageHTML(idSession), "This login already exists!");
        }
        UserService.getInstance().create(fullName, login, password);
        user = UserService.getInstance().getByLogin(login);
        if (idSession != null) {
            user.setIdSession(Long.parseLong(idSession));
        } else {
            user.setIdSession(generateIdSession());
        }
        UserService.getInstance().openSession(user.getId(), String.valueOf(user.getIdSession()));

        return viewContacts(String.valueOf(user.getId()), idSession, null).replace("idSessionValue", String.valueOf(user.getIdSession()));
    }

    private String registrationPageHTML(String idSession) {
        try {
            return new String(Files.readAllBytes(Paths.get(pathToHTMLFiles, "RegistrationPage.html"))).replace("myipaddress", myIP).replace("idSessionValue", idSession);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "File not found: RegistrationPage.html in path: " + pathToHTMLFiles;
    }

    @RequestMapping("/view")
    private String viewContacts(
            @RequestParam(value = "iduser", required = false) String idUser,
            @RequestParam(value = "idsession", required = false) String idSession,
            @RequestParam(value = "contacts", required = false) List<Contact> contacts) {

        User user = UserService.getInstance().getById(idUser);
        if (contacts == null) {
            if (user.getIdSession() != Long.parseLong(idSession)) {
                return showWarning(homePageHTML(), "Session is over, you need to login!");
            }
            contacts = ContactService.getInstance().getByIdUser(idUser);

            contacts.sort(new Comparator<Contact>() {
                @Override
                public int compare(Contact o1, Contact o2) {
                    int resCompare = String.CASE_INSENSITIVE_ORDER.compare(o1.getLastName(), o2.getLastName());
                    return (resCompare != 0) ? resCompare : o1.getLastName().compareTo(o2.getLastName());
                }
            });
        }
        try {
            String page = new String(Files.readAllBytes(Paths.get(pathToHTMLFiles, "ViewContact.html"))).replace("myipaddress", myIP).replace("idUser", idUser).replace("userLogin", user.getLogin()).replace("idSessionValue", String.valueOf(user.getIdSession()));

            StringBuilder sb = new StringBuilder();
            for (Contact con : contacts) {
                sb.append("<tr onmouseenter=\"onmouse(this)\" onmouseleave=\"outmouse(this)\">");
                sb.append("<td class =\"vcenter\"> <input type=\"checkbox\" value=\"").append(con.getId()).append("\"></td>");
                sb.append("<td>").append(con.getLastName()).append("</td>");
                sb.append("<td>").append(con.getName()).append("</td>");
                sb.append("<td>").append(con.getMiddleName()).append("</td>");
                sb.append("<td>").append(con.getMobilePhone()).append("</td>");
                sb.append("<td>").append(con.getHomePhone()).append("</td>");
                sb.append("<td>").append(con.getAddress()).append("</td>");
                sb.append("<td>").append(con.getEmail()).append("</td>");
                sb.append("</tr>");
            }

            return page.replace("<!--Table_DoNotRemoveThis-->", sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "File not found: ViewContact.html in path: " + pathToHTMLFiles;
    }

    @RequestMapping("/find")
    private String findContacts(
            @RequestParam(value = "iduser", required = false) String idUser,
            @RequestParam(value = "idsession", required = false) String idSession,
            @RequestParam(value = "lastname", required = false) String lastName,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "mobilePhone", required = false) String mobilePhone) {

        if ((idUser == null) || (idSession == null)) {
            return showWarning(homePageHTML(), "Session is over, you need to login!");
        }
        User user = UserService.getInstance().getById(idUser);
        if (user.getIdSession() != Long.parseLong(idSession)) {
            return showWarning(homePageHTML(), "Session is over, you need to login!");
        }
        List<Contact> contacts = ContactService.getInstance().getByIdUserAndName(idUser, lastName, name, mobilePhone);

        return viewContacts(idUser, idSession, contacts).replace("name=\"lastname\"", "name=\"lastname\" value=\"" + lastName + "\"").replace("name=\"name\"", "name=\"name\" value=\"" + name + "\"").replace("name=\"mobilePhone\"", "name=\"mobilePhone\" value=\"" + mobilePhone + "\"");
    }


    @RequestMapping("/edit")
    private String editDeleteContact(@RequestParam(value = "iduser", required = false) String idUser,
                                     @RequestParam(value = "idsession", required = false) String idSession,
                                     @RequestParam(value = "sendcheckbox", required = false) String idContact,
                                     @RequestParam(value = "delete", required = false) String isDelete) {

        if ((idContact == null) || ("".equals(idContact)) || (idSession == null)) {
            return showWarning(homePageHTML(), "Session is over, you need to login!");
        }

        User user = UserService.getInstance().getById(idUser);
        if (user.getIdSession() != Long.parseLong(idSession)) {
            return showWarning(homePageHTML(), "Session is over, you need to login!");
        }

        String[] idContacts = idContact.split(",");
        StringBuilder page = new StringBuilder();
        if ("Delete".equals(isDelete)) {
            for (String idCont : idContacts) {
                ContactService.getInstance().delete(idCont);
            }
            return viewContacts(idUser, idSession, null);
        } else {
            Contact contact = ContactService.getInstance().getById(idContacts[0]);
            try {
                page.append(new String(Files.readAllBytes(Paths.get(pathToHTMLFiles, "CreateContact.html"))).replace("myipaddress", myIP).replace("idUser", idUser).replace("userLogin", user.getLogin()).replace("idSessionValue", String.valueOf(user.getIdSession())));

                page.replace(page.indexOf("name=\"lastName\""), page.indexOf("name=\"lastName\"") + "name=\"lastName\"".length(), "name=\"lastName\"".concat(" value=\"").concat(contact.getLastName()).concat("\""));
                page.replace(page.indexOf("name=\"name\""), page.indexOf("name=\"name\"") + "name=\"name\"".length(), "name=\"name\"".concat(" value=\"").concat(contact.getName()).concat("\""));
                page.replace(page.indexOf("name=\"middleName\""), page.indexOf("name=\"middleName\"") + "name=\"middleName\"".length(), "name=\"middleName\" value=\"".concat(contact.getMiddleName()).concat("\""));
                page.replace(page.indexOf("name=\"mobilePhone\""), page.indexOf("name=\"mobilePhone\"") + "name=\"mobilePhone\"".length(), "name=\"mobilePhone\" value=\"" + contact.getMobilePhone().concat("\""));
                page.replace(page.indexOf("name=\"homePhone\""), page.indexOf("name=\"homePhone\"") + "name=\"homePhone\"".length(), "name=\"homePhone\" value=\"" + contact.getHomePhone().concat("\""));
                page.replace(page.indexOf("name=\"address\""), page.indexOf("name=\"address\"") + "name=\"address\"".length(), "name=\"address\" value=\"" + contact.getAddress().concat("\""));
                page.replace(page.indexOf("name=\"email\""), page.indexOf("name=\"email\"") + "name=\"email\"".length(), "name=\"email\" value=\"" + contact.getEmail().concat("\""));
                page.replace(page.indexOf("value=\"idContact\""), page.indexOf("value=\"idContact\"") + "value=\"idContact\"".length(), "value=\"" + contact.getId() + "\"");
                page.replace(page.indexOf("Create"), page.indexOf("Create") + "Create".length(), "Update");


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return page.toString();
    }

    @RequestMapping("/create")
    private String createContact(
            @RequestParam(value = "iduser", required = false) String idUser,
            @RequestParam(value = "idsession", required = false) String idSession,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "login", required = false) String login,
            @RequestParam(value = "password", required = false) String password) {

        User user = UserService.getInstance().getById(idUser);
        try {
            return new String(Files.readAllBytes(Paths.get(pathToHTMLFiles, "CreateContact.html"))).replace("myipaddress", myIP).replace("idUser", idUser).replace("userLogin", user.getLogin()).replace("idSessionValue", String.valueOf(user.getIdSession()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "File not found: CreateContact.html in path: " + pathToHTMLFiles;
    }

    @RequestMapping("/updatecontact")
    private String updateContact(
            @RequestParam(value = "iduser", required = false) String idUser,
            @RequestParam(value = "idsession", required = false) String idSession,
            @RequestParam(value = "idContact", required = false) String idContact,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "middleName", required = false) String middleName,
            @RequestParam(value = "mobilePhone", required = false) String mobilePhone,
            @RequestParam(value = "homePhone", required = false) String homePhone,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "email", required = false) String email) {

        if ((idUser != null) && (idContact != null) && (lastName != null) && (middleName != null) && (mobilePhone != null)) {
            if ("idContact".equals(idContact)) {
                if (lastName.length() < 4) {
                    return showWarning(createContact(idUser, idSession, null, null, null), "Last name is to short!");
                }
                if (name.length() < 4) {
                    return showWarning(createContact(idUser, idSession, null, null, null), "Name is to short!");
                }
                if (middleName.length() < 4) {
                    return showWarning(createContact(idUser, idSession, null, null, null), "Middle name is to short!");
                }
                if (mobilePhone.length() < 15) {
                    return showWarning(createContact(idUser, idSession, null, null, null), "Mobile phone is to short!");
                }

                Pattern p = Pattern.compile(PATTERN_MOBILE_PHONE_UKR);
                if (!p.matcher(mobilePhone).matches()) {
                    return showWarning(createContact(idUser, idSession, null, null, null), "Mobile phone is incorrect for Ukraine!");
                }

                if ((homePhone != null) && (!"".equals(homePhone))) {
                    p = Pattern.compile(PATTERN_STATIONARY_PHONE_UKR);
                    if (!p.matcher(homePhone).matches()) {
                        return showWarning(createContact(idUser, idSession, null, null, null), "Home phone is incorrect for Ukraine!");
                    }
                }

                if ((email != null) && (!"".equals(email))) {
                    p = Pattern.compile(PATTERN_EMAIL, Pattern.CASE_INSENSITIVE);
                    if (!p.matcher(email).matches()) {
                        return showWarning(createContact(idUser, idSession, null, null, null), "Email is incorrect!");
                    }
                }

                ContactService.getInstance().create(idUser, lastName, name, middleName, mobilePhone, homePhone, address, email);
            } else {
                ContactService.getInstance().update(idUser, idContact, lastName, name, middleName, mobilePhone, homePhone, address, email);
            }
        } else if ((idUser == null) || (idSession == null)) {
            return homePageHTML();
        }

        return viewContacts(idUser, idSession, null);
    }


    private String showWarning(String page, String message) {
        return page.replace("<!--WarningMessage_DoNotRemoveThis-->", "<div class=\"alert alert-danger\">".concat(
                "<a href=\"\" class=\"close\" data-dismiss=\"alert\" aria-label=\"close\">&times;</a> <strong>".concat(message).concat("</strong></div>")));
    }


}
