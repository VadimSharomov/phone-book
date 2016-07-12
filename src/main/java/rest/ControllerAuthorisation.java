package main.java.rest;

import main.java.entity.Contact;
import main.java.entity.User;
import main.java.services.Constants;
import main.java.services.ContactService;
import main.java.services.UserService;
import main.java.services.UtilsRest;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Vadim
 * 12.07.2016.
 */
@Controller
public class ControllerAuthorisation {
    private final static Logger logger = getLogger(StartController.class);
    private UserService userService = UserService.getInstance();

    @RequestMapping("/login")
    public String loginPage(
            @RequestParam(value = "idsession", required = false) String idSession,
            @RequestParam(value = "login", required = false) String login,
            @RequestParam(value = "password", required = false) String password, Model model) {

        model.addAttribute("myipaddress", Constants.getMyIP());
        model.addAttribute("idSessionValue", String.valueOf(UtilsRest.generateIdSession()));
        if (!Constants.getInitIdSessionList().contains(Long.parseLong(idSession))) {
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
        logger.info("Login user: '" + user + "'");
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
                model.addAttribute("myipaddress", Constants.getMyIP());
                model.addAttribute("idSessionValue", String.valueOf(UtilsRest.generateIdSession()));
                return "RegistrationPage";
            }
        }

        if ((fullName == null) || (fullName.length() < 5)) {
            model.addAttribute("myipaddress", Constants.getMyIP());
            model.addAttribute("idSessionValue", String.valueOf(UtilsRest.generateIdSession()));
            model.addAttribute("warningMessage", "Full name is to short!");
            return "RegistrationPage";
        }
        if ((login == null) || (login.length() < 3)) {
            model.addAttribute("myipaddress", Constants.getMyIP());
            model.addAttribute("idSessionValue", String.valueOf(UtilsRest.generateIdSession()));
            model.addAttribute("warningMessage", "Login is to short!");
            return "RegistrationPage";
        }

        Pattern p = Pattern.compile(Constants.getPatternLogin());
        if (!p.matcher(login).matches()) {
            model.addAttribute("myipaddress", Constants.getMyIP());
            model.addAttribute("idSessionValue", String.valueOf(UtilsRest.generateIdSession()));
            model.addAttribute("warningMessage", "Incorrect characters in login!");
            return "RegistrationPage";
        }

        if ((password == null) || (password.length() < 5)) {
            model.addAttribute("myipaddress", Constants.getMyIP());
            model.addAttribute("idSessionValue", String.valueOf(UtilsRest.generateIdSession()));
            model.addAttribute("warningMessage", "Password is to short!");
            return "RegistrationPage";
        }

        User user = userService.getByLogin(login);
        if (user != null) {
            model.addAttribute("myipaddress", Constants.getMyIP());
            model.addAttribute("idSessionValue", String.valueOf(UtilsRest.generateIdSession()));
            model.addAttribute("warningMessage", "This login already exists!");
            return "RegistrationPage";
        }
        userService.create(fullName, login, password);
        user = userService.getByLogin(login);
        if (idSession != null) {
            user.setIdSession(Long.parseLong(idSession));
        } else {
            user.setIdSession(UtilsRest.generateIdSession());
        }
        userService.openSession(user.getId(), String.valueOf(user.getIdSession()));
        logger.info("Registration user: '" + user + "'");
        return viewContacts(String.valueOf(user.getId()), idSession, null, model);
    }

    @RequestMapping("/view")
    public String viewContacts(
            @RequestParam(value = "iduser", required = false) String idUser,
            @RequestParam(value = "idsession", required = false) String idSession,
            @RequestParam(value = "contacts", required = false) List<Contact> contacts, Model model) {

        if (!idUser.matches("[0-9]+")) {
            model.addAttribute("myipaddress", Constants.getMyIP());
            model.addAttribute("idSessionValue", String.valueOf(UtilsRest.generateIdSession()));
            model.addAttribute("warningMessage", "Session is over, you need to login!");
            return "Home";
        }
        User user = userService.getById(idUser);
        if (UtilsRest.isSessionOver(user, idSession)) {
            model.addAttribute("myipaddress", Constants.getMyIP());
            model.addAttribute("idSessionValue", String.valueOf(UtilsRest.generateIdSession()));
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
        model.addAttribute("myipaddress", Constants.getMyIP());
        model.addAttribute("idSessionValue", idSession);
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
            model.addAttribute("myipaddress", Constants.getMyIP());
            model.addAttribute("idSessionValue", idSession);
            model.addAttribute("warningMessage", "Session is over, you need to login!");
            return "Home";
        }

        User user = userService.getById(idUser);
        if (UtilsRest.isSessionOver(user, idSession)) {
            model.addAttribute("myipaddress", Constants.getMyIP());
            model.addAttribute("idSessionValue", idSession);
            model.addAttribute("warningMessage", "Session is over, you need to login!");
            return "Home";
        }

        String[] idContacts = idContact.split(",");
        if ("Delete".equals(isDelete)) {
            for (String idCont : idContacts) {
                ContactService.getInstance().delete(idCont);
            }
            return viewContacts(idUser, idSession, null, model);
        } else {
            Contact contact = ContactService.getInstance().getById(idContacts[0]); //only first checked is selecting
            model.addAttribute("myipaddress", Constants.getMyIP());
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

            logger.info("User: '" + user + "' edits contact '" + contact.getId() + "'");
            return "CreateContact";
        }
    }

}