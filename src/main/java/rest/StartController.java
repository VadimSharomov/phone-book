package main.java.rest;

import main.java.entity.Contact;
import main.java.entity.User;
import main.java.services.Constants;
import main.java.services.ContactService;
import main.java.services.UserService;
import main.java.services.UtilsRest;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Vadim
 * 18.04.2016.
 */
@Controller
public class StartController {
    private final static Logger logger = getLogger(StartController.class);

    @Component
    public class MyBean {
        @SuppressWarnings("SpringJavaAutowiringInspection")
        @Autowired
        public MyBean(ApplicationArguments args) {
            List<String> listArgs = args.getNonOptionArgs();

            if (listArgs.size() == 0) {
                logger.error("In arguments JVM not found the path to file.properties!");
                System.exit(1);
            }
            String pathToConfigFile = listArgs.get(0);

            try {
                InputStream input = new FileInputStream(pathToConfigFile);
                Properties properties = new Properties();
                properties.load(input);

                Constants.setHostDB(properties.getProperty("hostDB"));
                Constants.setPathToDBFiles(properties.getProperty("pathToDBFiles"));
                Constants.setTypeDB(properties.getProperty("typeDB"));
                Constants.setUserDB(properties.getProperty("userDB"));
                Constants.setUserPasswordDB(properties.getProperty("userPasswordDB"));
                input.close();
            } catch (IOException e) {
                logger.error("File properties not found in this path: '" + pathToConfigFile + "'", e.getMessage());
                System.exit(1);
            }
        }

        @Bean
        public DriverManagerDataSource getMySQLDriverManagerDatasource() {
            DriverManagerDataSource dataSource = new DriverManagerDataSource(Constants.getHostDB(), Constants.getUserDB(), Constants.getUserPasswordDB());
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            UserService.getInstance().setDataSource(dataSource, Constants.getTypeDB(), Constants.getPathToDBFiles());
            ContactService.getInstance().setDataSource(dataSource, Constants.getTypeDB(), Constants.getPathToDBFiles());
            logger.info("All constants are initialized");
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
        model.addAttribute("myipaddress", Constants.getMyIP());
        model.addAttribute("idSessionValue", String.valueOf(UtilsRest.generateIdSession()));
        return "Home";
    }

    @RequestMapping("/logout")
    public String logout(
            @RequestParam(value = "iduser", required = false) String idUser,
            @RequestParam(value = "idsession", required = false) String idSession, Model model) {
        if ((idUser != null) && (idSession) != null) {
            UtilsRest.closeSession(idUser, idSession);
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
        User user = UserService.getInstance().getById(idUser);
        if (UtilsRest.isSessionOver(user, idSession)) {
            model.addAttribute("warningMessage", "Session is over, you need to login!");
            return "Home";
        }
        List<Contact> contacts = ContactService.getInstance().getByIdUserAndName(idUser, lastName, name, mobilePhone);
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

        User user = UserService.getInstance().getById(idUser);
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
                ContactService.getInstance().create(idUser, lastName, name, middleName, mobilePhone, homePhone, address, email);
            } else {
                ContactService.getInstance().update(idUser, idContact, lastName, name, middleName, mobilePhone, homePhone, address, email);
            }
        } else if ((idUser == null) || (idSession == null)) {
            model.addAttribute("warningMessage", "Session is over, you need to login!");
            return "Home";
        }
        User user = UserService.getInstance().getById(idUser);
        List<Contact> contacts = ContactService.getInstance().getByIdUser(idUser);
        model.addAttribute("userLogin", user.getLogin());
        model.addAttribute("contacts", contacts);
        return "ViewContact";
    }
}