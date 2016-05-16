package controller;

import entity.Contact;
import entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import services.ContactService;
import services.UserService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import static java.net.InetAddress.getLocalHost;

@Controller
public class RestControl {
    private String myIP;
    private Properties properties = new Properties();
    private static final String PATTERN_LOGIN = "[a-z,A-Z]{3,}";
    private static final String PATTERN_MOBILE_PHONE_UKR = "[+][3][8][0][(][3569][0-9][)][0-9]{7}";
    private static final String PATTERN_STATIONARY_PHONE_UKR = "[+][3][8][0][(][3456][0-9][)][0-9]{7}";
    private static final String PATTERN_EMAIL = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    private ArrayList<Long> initIdSessionList = new ArrayList<>();
    private UserService userService = UserService.getInstance();

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
                    System.out.println("\nIn arguments JVM not found the path to file.properties!\n");
                    System.exit(1);
                }
                String pathToConfigFile = listArgs.get(0);
                if (!(new File(pathToConfigFile)).exists()) {
                    System.out.println("\nIn arguments JVM is incorrect path to file.properties!\n");
                    System.exit(1);
                }

                InputStream input = new FileInputStream(pathToConfigFile);
                properties.load(input);
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

            userService.setDataSource(dataSource, properties.getProperty("typeDB"), properties.getProperty("pathToDBFiles"));
            ContactService.getInstance().setDataSource(dataSource, properties.getProperty("typeDB"), properties.getProperty("pathToDBFiles"));
            return dataSource;
        }
    }

    @RequestMapping("/test")
    public String greeting(
            @RequestParam(value = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "Greeting";
    }

    @RequestMapping("/")
    public String homePage(
            @RequestParam(value = "name", required = false) String name, Model model) {
        model.addAttribute("myipaddress", myIP);
        model.addAttribute("idSessionValue", String.valueOf(generateIdSession()));
        return "Home";
    }

    @RequestMapping("/logout")
    public String logout(
            @RequestParam(value = "iduser", required = false) String idUser,
            @RequestParam(value = "idsession", required = false) String idSession, Model model) {
        if ((idUser != null) && (idSession) != null) {
            closeSession(idUser, idSession);
        }
        model.addAttribute("myipaddress", myIP);
        model.addAttribute("idSessionValue", String.valueOf(generateIdSession()));
        return "Home";
    }

    @RequestMapping("/login")
    public String loginPage(
            @RequestParam(value = "idsession", required = false) String idSession,
            @RequestParam(value = "login", required = false) String login,
            @RequestParam(value = "password", required = false) String password, Model model) {

        model.addAttribute("myipaddress", myIP);
        model.addAttribute("idSessionValue", String.valueOf(generateIdSession()));
        if (!initIdSessionList.contains(Long.parseLong(idSession))) {
            model.addAttribute("warningMessage", "Session is over, you need to login!");
            return "Home";
        }

        if ((login == null) || (login.length() < 3)) {
            model.addAttribute("warningMessage", "Login is to short!");
            return "Home";
        }
        if ((password == null) || (password.length() < 5)) {
            model.addAttribute("warningMessage", "Password is to short!");
            return "Home";
        }

        User user = userService.getByLogin(login);
        if (user == null) {
            return "RegistrationPage";
        }

        if (!password.equals(user.getPassword())) {
            model.addAttribute("warningMessage", "Password is incorrect!");
            return "Home";
        }

        userService.openSession(user.getId(), idSession);
        return viewContacts(String.valueOf(user.getId()), idSession, null, model);
    }

    @RequestMapping("/registration")
    public String authorization(
            @RequestParam(value = "name", required = false) String fullName,
            @RequestParam(value = "login", required = false) String login,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "idsession", required = false) String idSession, Model model) {

        if (fullName == null && login == null && password == null) {
            if (idSession == null) {
                return "Home";
            } else {
                model.addAttribute("myipaddress", myIP);
                model.addAttribute("idSessionValue", String.valueOf(generateIdSession()));
                return "RegistrationPage";
            }
        }

        if ((fullName == null) || (fullName.length() < 5)) {
            model.addAttribute("myipaddress", myIP);
            model.addAttribute("idSessionValue", String.valueOf(generateIdSession()));
            model.addAttribute("warningMessage", "Full name is to short!");
            return "RegistrationPage";
        }
        if ((login == null) || (login.length() < 3)) {
            model.addAttribute("myipaddress", myIP);
            model.addAttribute("idSessionValue", String.valueOf(generateIdSession()));
            model.addAttribute("warningMessage", "Login is to short!");
            return "RegistrationPage";
        }

        Pattern p = Pattern.compile(PATTERN_LOGIN);
        if (!p.matcher(login).matches()) {
            model.addAttribute("myipaddress", myIP);
            model.addAttribute("idSessionValue", String.valueOf(generateIdSession()));
            model.addAttribute("warningMessage", "Incorrect characters in login!");
            return "RegistrationPage";
        }

        if ((password == null) || (password.length() < 5)) {
            model.addAttribute("myipaddress", myIP);
            model.addAttribute("idSessionValue", String.valueOf(generateIdSession()));
            model.addAttribute("warningMessage", "Password is to short!");
            return "RegistrationPage";
        }

        User user = userService.getByLogin(login);
        if (user != null) {
            model.addAttribute("myipaddress", myIP);
            model.addAttribute("idSessionValue", String.valueOf(generateIdSession()));
            model.addAttribute("warningMessage", "This login already exists!");
            return "RegistrationPage";
        }
        userService.create(fullName, login, password);
        user = userService.getByLogin(login);
        if (idSession != null) {
            user.setIdSession(Long.parseLong(idSession));
        } else {
            user.setIdSession(generateIdSession());
        }
        userService.openSession(user.getId(), String.valueOf(user.getIdSession()));

        return viewContacts(String.valueOf(user.getId()), idSession, null, model);
    }

    @RequestMapping("/view")
    private String viewContacts(
            @RequestParam(value = "iduser", required = false) String idUser,
            @RequestParam(value = "idsession", required = false) String idSession,
            @RequestParam(value = "contacts", required = false) List<Contact> contacts, Model model) {

        if (!idUser.matches("[0-9]+")) {
            model.addAttribute("myipaddress", myIP);
            model.addAttribute("idSessionValue", String.valueOf(generateIdSession()));
            model.addAttribute("warningMessage", "Session is over, you need to login!");
            return "Home";
        }
        User user = userService.getById(idUser);
        if (isSessionOver(user, idSession)) {
            model.addAttribute("myipaddress", myIP);
            model.addAttribute("idSessionValue", String.valueOf(generateIdSession()));
            model.addAttribute("warningMessage", "Session is over, you need to login!");
            return "Home";
        }
        if (contacts == null) {
            contacts = ContactService.getInstance().getByIdUser(idUser);
            contacts.sort(new Comparator<Contact>() {
                @Override
                public int compare(Contact o1, Contact o2) {
                    int resCompare = String.CASE_INSENSITIVE_ORDER.compare(o1.getLastName(), o2.getLastName());
                    return (resCompare != 0) ? resCompare : o1.getLastName().compareTo(o2.getLastName());
                }
            });
        }
        model.addAttribute("myipaddress", myIP);
        model.addAttribute("idSessionValue", idSession);
        model.addAttribute("idUser", idUser);
        model.addAttribute("userLogin", user.getLogin());
        model.addAttribute("contacts", contacts);

        return "ViewContact";
    }

    @RequestMapping("/find")
    private String findContacts(
            @RequestParam(value = "iduser", required = false) String idUser,
            @RequestParam(value = "idsession", required = false) String idSession,
            @RequestParam(value = "lastname", required = false) String lastName,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "mobilePhone", required = false) String mobilePhone, Model model) {

        model.addAttribute("myipaddress", myIP);
        model.addAttribute("idSessionValue", idSession);
        if ((idUser == null) || (idSession == null)) {
            model.addAttribute("warningMessage", "Session is over, you need to login!");
            return "Home";
        }
        User user = userService.getById(idUser);
        if (isSessionOver(user, idSession)) {
            model.addAttribute("warningMessage", "Session is over, you need to login!");
            return "Home";
        }
        List<Contact> contacts = ContactService.getInstance().getByIdUserAndName(idUser, lastName, name, mobilePhone);

        model.addAttribute("idUser", idUser);
        model.addAttribute("userLogin", user.getLogin());
        model.addAttribute("contacts", contacts);

        return "ViewContact";
    }

    @RequestMapping("/edit")
    private String editDeleteContact(@RequestParam(value = "iduser", required = false) String idUser,
                                     @RequestParam(value = "idsession", required = false) String idSession,
                                     @RequestParam(value = "sendcheckbox", required = false) String idContact,
                                     @RequestParam(value = "delete", required = false) String isDelete, Model model) {

        if ((idContact == null) || ("".equals(idContact)) || (idSession == null)) {
            model.addAttribute("myipaddress", myIP);
            model.addAttribute("idSessionValue", idSession);
            model.addAttribute("warningMessage", "Session is over, you need to login!");
            return "Home";
        }

        User user = userService.getById(idUser);
        if (isSessionOver(user, idSession)) {
            model.addAttribute("myipaddress", myIP);
            model.addAttribute("idSessionValue", idSession);
            model.addAttribute("warningMessage", "Session is over, you need to login!");
            return "Home";
        }

        String[] idContacts = idContact.split(",");
        StringBuilder page = new StringBuilder();
        if ("Delete".equals(isDelete)) {
            for (String idCont : idContacts) {
                ContactService.getInstance().delete(idCont);
            }
            return viewContacts(idUser, idSession, null, model);
        } else {
            Contact contact = ContactService.getInstance().getById(idContacts[0]); //only first checked is selecting
            model.addAttribute("myipaddress", myIP);
            model.addAttribute("idUser", idUser);
            model.addAttribute("userLogin", user.getLogin());
            model.addAttribute("idSessionValue", String.valueOf(user.getIdSession())); //idSession
            model.addAttribute("titleUpdateContact", "Update contact");

            model.addAttribute("idContact", contact.getId());
            model.addAttribute("lastName", contact.getLastName());
            model.addAttribute("name", contact.getName());
            model.addAttribute("middleName", contact.getMiddleName());
            model.addAttribute("mobilePhone", contact.getMobilePhone());
            model.addAttribute("homePhone", contact.getHomePhone());
            model.addAttribute("address", contact.getAddress());
            model.addAttribute("email", contact.getEmail());

            return "CreateContact";
        }
    }

    @RequestMapping("/create")
    private String createContact(
            @RequestParam(value = "iduser", required = false) String idUser,
            @RequestParam(value = "idsession", required = false) String idSession,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "login", required = false) String login,
            @RequestParam(value = "password", required = false) String password, Model model) {

        User user = userService.getById(idUser);

        model.addAttribute("myipaddress", myIP);
        model.addAttribute("idUser", idUser);
        model.addAttribute("idSessionValue", idSession);//String.valueOf(user.getIdSession())
        model.addAttribute("titleUpdateContact", "Create contact");

        return "CreateContact";
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
            @RequestParam(value = "email", required = false) String email, Model model) {

        model.addAttribute("myipaddress", myIP);
        model.addAttribute("idUser", idUser);
        model.addAttribute("idSessionValue", idSession);
        model.addAttribute("titleUpdateContact", "Update contact");

        if ((idUser != null) && (idContact != null) && (lastName != null) && (middleName != null) && (mobilePhone != null)) {
            if ("".equals(idContact)) {
                if (lastName.length() < 4) {
                    model.addAttribute("warningMessage", "Last name is to short!");
                    return "CreateContact";
                }
                if (name.length() < 4) {
                    model.addAttribute("warningMessage", "Name is to short!");
                    return "CreateContact";
                }
                if (middleName.length() < 4) {
                    model.addAttribute("warningMessage", "Middle name is to short!");
                    return "CreateContact";
                }
                if (mobilePhone.length() < 15) {
                    model.addAttribute("warningMessage", "Mobile phone is to short!");
                    return "CreateContact";
                }

                Pattern p = Pattern.compile(PATTERN_MOBILE_PHONE_UKR);
                if (!p.matcher(mobilePhone).matches()) {
                    model.addAttribute("warningMessage", "Mobile phone is incorrect for Ukraine!");
                    return "CreateContact";
                }

                if ((homePhone != null) && (!"".equals(homePhone))) {
                    p = Pattern.compile(PATTERN_STATIONARY_PHONE_UKR);
                    if (!p.matcher(homePhone).matches()) {
                        model.addAttribute("warningMessage", "Home phone is incorrect for Ukraine!");
                        return "CreateContact";
                    }
                }

                if ((email != null) && (!"".equals(email))) {
                    p = Pattern.compile(PATTERN_EMAIL, Pattern.CASE_INSENSITIVE);
                    if (!p.matcher(email).matches()) {
                        model.addAttribute("warningMessage", "Email is incorrect!");
                        return "CreateContact";
                    }
                }

                ContactService.getInstance().create(idUser, lastName, name, middleName, mobilePhone, homePhone, address, email);
            } else {
                ContactService.getInstance().update(idUser, idContact, lastName, name, middleName, mobilePhone, homePhone, address, email);
            }
        } else if ((idUser == null) || (idSession == null)) {
            model.addAttribute("warningMessage", "Session is over, you need to login!");
            return "Home";
        }

        User user = userService.getById(idUser);
        List<Contact> contacts = ContactService.getInstance().getByIdUser(idUser);

        model.addAttribute("userLogin", user.getLogin());
        model.addAttribute("contacts", contacts);
        return "ViewContact";
    }

    private long generateIdSession() {
        initIdSessionList.add(System.currentTimeMillis());
        return initIdSessionList.get(initIdSessionList.size() - 1);
    }

    private void closeSession(String idUser, String idSession) {
        userService.closeSession(Long.parseLong(idUser));
        if (initIdSessionList.size() != 0) {
            if (initIdSessionList.indexOf(Long.parseLong(idSession)) >= 0) {
                initIdSessionList.remove(initIdSessionList.indexOf(Long.parseLong(idSession)));
            }
            ArrayList<User> usersList = userService.getAllUsers();
            boolean canClear = true;
            for (User usr : usersList) {
                if (initIdSessionList.contains(usr.getIdSession())) {
                    canClear = false;
                }
            }
            if (canClear) initIdSessionList.clear();
        }
    }

    private boolean isSessionOver(User user, String idSession) {
        return (user.getIdSession() != Long.parseLong(idSession));
    }
}
