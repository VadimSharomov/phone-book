package rest;

import entity.Contact;
import entity.User;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import services.Constants;
import services.ContactService;
import services.UserService;
import services.UtilsRest;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Vadim Sharomov
 */
@Controller
public class StartController {
    private final static Logger logger = getLogger(StartController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ContactService contactService;

    @Component
    public class MyBean {
        @Autowired
        public MyBean(ApplicationArguments args) {
            List<String> listArgs = args.getNonOptionArgs();

            if (listArgs.size() == 0) {
                logger.error("In arguments JVM not found the path to file.properties: 'application.properties'!");
                System.exit(1);
            }
            String pathToConfigFile = listArgs.get(0);

            try {
                InputStream input = new FileInputStream(pathToConfigFile);
                Properties properties = new Properties();
                properties.load(input);
                logger.info("*** Config file has read '" + pathToConfigFile + "'");
                Constants.setPathToDBFiles(properties.getProperty("pathToDBFiles"));
                Constants.setTypeDB(properties.getProperty("typeDB"));
                userService.setDataSource(Constants.getTypeDB(), Constants.getPathToDBFiles());
                contactService.setDataSource(Constants.getTypeDB(), Constants.getPathToDBFiles());
                input.close();
                logger.info("*** All constants have initialised");
            } catch (IOException e) {
                logger.error("File properties not found in this path: '" + pathToConfigFile + "'", e.getMessage());
                System.exit(1);
            }
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
        model.addAttribute("myipaddress", Constants.getMyIP());
        model.addAttribute("idSessionValue", String.valueOf(UtilsRest.generateIdSession()));
        return "Home";
    }

    @RequestMapping("/logout")
    public String logout(
            @RequestParam(value = "iduser", required = false) String idUser,
            @RequestParam(value = "idsession", required = false) String idSession, Model model) {
        if ((idUser != null) && (idSession) != null) {
            UtilsRest.closeSession(userService, idUser, idSession);
        }
        model.addAttribute("myipaddress", Constants.getMyIP());
        model.addAttribute("idSessionValue", String.valueOf(UtilsRest.generateIdSession()));
        logger.info("Logout user with id: '" + idUser + "'");
        return "Home";
    }


    @RequestMapping("/find")
    private String findContacts(
            @RequestParam(value = "iduser", required = false) String idUser,
            @RequestParam(value = "idsession", required = false) String idSession,
            @RequestParam(value = "lastname", required = false) String lastName,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "mobilePhone", required = false) String mobilePhone, Model model) {

        model.addAttribute("myipaddress", Constants.getMyIP());
        model.addAttribute("idSessionValue", idSession);
        if ((idUser == null) || (idSession == null)) {
            model.addAttribute("warningMessage", "Session is over, you need to login!");
            return "Home";
        }
        User user = userService.getUserById(idUser);
        if (UtilsRest.isSessionOver(user, idSession)) {
            model.addAttribute("warningMessage", "Session is over, you need to login!");
            return "Home";
        }
        List<Contact> contacts = contactService.getByIdUserAndName(idUser, lastName, name, mobilePhone);
        contacts.sort(new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                int resCompare = String.CASE_INSENSITIVE_ORDER.compare(o1.getLastName(), o2.getLastName());
                return (resCompare != 0) ? resCompare : o1.getLastName().compareTo(o2.getLastName());
            }
        });
        model.addAttribute("idUser", idUser);
        model.addAttribute("userLogin", user.getLogin());
        model.addAttribute("contacts", contacts);
        return "ViewContact";
    }

    @RequestMapping("/create")
    private String createContact(
            @RequestParam(value = "iduser", required = false) String idUser,
            @RequestParam(value = "idsession", required = false) String idSession,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "login", required = false) String login,
            @RequestParam(value = "password", required = false) String password, Model model) {

        User user = userService.getUserById(idUser);
        model.addAttribute("myipaddress", Constants.getMyIP());
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

        model.addAttribute("myipaddress", Constants.getMyIP());
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
                Pattern p = Pattern.compile(Constants.getPatternMobilePhoneUkr());
                if (!p.matcher(mobilePhone).matches()) {
                    model.addAttribute("warningMessage", "Mobile phone is incorrect for Ukraine!");
                    return "CreateContact";
                }
                if ((homePhone != null) && (!"".equals(homePhone))) {
                    p = Pattern.compile(Constants.getPatternStationaryPhoneUkr());
                    if (!p.matcher(homePhone).matches()) {
                        model.addAttribute("warningMessage", "Home phone is incorrect for Ukraine!");
                        return "CreateContact";
                    }
                }
                if ((email != null) && (!"".equals(email))) {
                    p = Pattern.compile(Constants.getPatternEmail(), Pattern.CASE_INSENSITIVE);
                    if (!p.matcher(email).matches()) {
                        model.addAttribute("warningMessage", "Email is incorrect!");
                        return "CreateContact";
                    }
                }
                contactService.create(idUser, lastName, name, middleName, mobilePhone, homePhone, address, email);
            } else {
                contactService.update(idUser, idContact, lastName, name, middleName, mobilePhone, homePhone, address, email);
            }
        } else if ((idUser == null) || (idSession == null)) {
            model.addAttribute("warningMessage", "Session is over, you need to login!");
            return "Home";
        }
        User user = userService.getUserById(idUser);
        List<Contact> contacts = contactService.getByIdUser(idUser);
        contacts.sort(new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                int resCompare = String.CASE_INSENSITIVE_ORDER.compare(o1.getLastName(), o2.getLastName());
                return (resCompare != 0) ? resCompare : o1.getLastName().compareTo(o2.getLastName());
            }
        });
        model.addAttribute("userLogin", user.getLogin());
        model.addAttribute("contacts", contacts);
        return "ViewContact";
    }
}