package controller;

import entity.Contact;
import entity.CustomUser;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import services.Constants;
import services.ContactService;
import services.UserService;

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
public class ControllerWork {
    private final static Logger logger = getLogger(ControllerWork.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ContactService contactService;

    @Component
    public class MyBean {
        @Autowired
        public MyBean(ApplicationArguments args) {
            List<String> listArgs = args.getNonOptionArgs();

            String pathToConfigFile = "src/main/resources/application.properties";
            if (listArgs.size() == 0) {
                logger.warn("In arguments JVM not found the path to file.properties: 'application.properties'!");
                logger.info("It will use as default the path: '" + pathToConfigFile + "'");
            } else {
                pathToConfigFile = listArgs.get(0);
            }

            try {
                InputStream input = new FileInputStream(pathToConfigFile);
                Properties properties = new Properties();
                properties.load(input);
                logger.info("*** Config file has read '" + pathToConfigFile + "'");
                Constants.setPathToDBFiles(properties.getProperty("pathToDBFiles"));
                Constants.setTypeDB(properties.getProperty("typeDB").toLowerCase());
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

    @RequestMapping("/accessdenied")
    public String accessDenied(Model model) {
        model.addAttribute("myipaddress", Constants.getMyIP());
        model.addAttribute("warningMessage", "Access denied!");

        return "Login";
    }

    @RequestMapping("/logout")
    public String logout(
            @RequestParam(value = "iduser", required = false) String idUser, Model model) {

        model.addAttribute("myipaddress", Constants.getMyIP());


        logger.info("Logout user with id: '" + idUser + "'");
        return "Login";
    }


    @RequestMapping("/find")
    private String findContacts(
            @RequestParam(value = "lastname", required = false) String lastName,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "mobilePhone", required = false) String mobilePhone, Model model) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String login = user.getUsername();

        CustomUser dbUser = userService.getUserByLogin(login);

        List<Contact> contacts = contactService.getByIdUserAndName(String.valueOf(dbUser.getId()), lastName, name, mobilePhone);
        contacts.sort(new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                int resCompare = String.CASE_INSENSITIVE_ORDER.compare(o1.getLastName(), o2.getLastName());
                return (resCompare != 0) ? resCompare : o1.getLastName().compareTo(o2.getLastName());
            }
        });
        model.addAttribute("userLogin", dbUser.getLogin());
        model.addAttribute("contacts", contacts);
        return "Index";
    }

    @RequestMapping("/create")
    private String createContact(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String login = user.getUsername();

        CustomUser dbUser = userService.getUserByLogin(login);

        model.addAttribute("titleUpdateContact", "Create contact");
        return "CreateContact";
    }

    @RequestMapping("/updatecontact")
    private String updateContact(
            @RequestParam(value = "idContact", required = false) String idContact,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "middleName", required = false) String middleName,
            @RequestParam(value = "mobilePhone", required = false) String mobilePhone,
            @RequestParam(value = "homePhone", required = false) String homePhone,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "email", required = false) String email, Model model) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String login = user.getUsername();

        CustomUser dbUser = userService.getUserByLogin(login);

        model.addAttribute("titleUpdateContact", "Update contact");

        if ((dbUser != null) && (idContact != null) && (lastName != null) && (middleName != null) && (mobilePhone != null)) {
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
                contactService.create(String.valueOf(dbUser.getId()), lastName, name, middleName, mobilePhone, homePhone, address, email);
            } else {
                contactService.update(String.valueOf(dbUser.getId()), idContact, lastName, name, middleName, mobilePhone, homePhone, address, email);
            }
        }

        List<Contact> contacts = contactService.getByIdUser(String.valueOf(dbUser.getId()));
        contacts.sort(new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                int resCompare = String.CASE_INSENSITIVE_ORDER.compare(o1.getLastName(), o2.getLastName());
                return (resCompare != 0) ? resCompare : o1.getLastName().compareTo(o2.getLastName());
            }
        });
        model.addAttribute("userLogin", dbUser.getLogin());
        model.addAttribute("contacts", contacts);
        return "Index";
    }
}