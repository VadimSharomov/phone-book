package controller;

import entity.Contact;
import entity.CustomUser;
import entity.UserRole;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import services.Constants;
import services.ContactService;
import services.UserService;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Vadim Sharomov
 */
@Controller
public class ControllerAuthorisation {
    private final static Logger logger = getLogger(ControllerAuthorisation.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ContactService contactService;

    @RequestMapping("/")
    public String indexPage(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String login = user.getUsername();

        CustomUser dbUser = userService.getUserByLogin(login);

        List<Contact> contacts = contactService.getByIdUser(String.valueOf(dbUser.getId()));
        contacts.sort(new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                int resCompare = String.CASE_INSENSITIVE_ORDER.compare(o1.getLastName(), o2.getLastName());
                return (resCompare != 0) ? resCompare : o1.getLastName().compareTo(o2.getLastName());
            }
        });

        logger.info("Login user: '" + dbUser + "'");
        model.addAttribute("userLogin", dbUser.getLogin());
        model.addAttribute("contacts", contacts);

        return "index";
    }

    @RequestMapping("/registration")
    public String authorization(
            @RequestParam(value = "name", required = false) String fullName,
            @RequestParam(value = "login", required = false) String login,
            @RequestParam(value = "password", required = false) String password,
            Model model) {

        if (fullName == null && login == null && password == null) {
            model.addAttribute("myipaddress", Constants.getMyIP());
            return "RegistrationPage";
        }

        if ((fullName == null) || (fullName.length() < 5)) {
            model.addAttribute("myipaddress", Constants.getMyIP());
            model.addAttribute("warningMessage", "Full name is to short: < 5 letters!");
            return "RegistrationPage";
        }
        if ((login == null) || (login.length() < 3)) {
            model.addAttribute("myipaddress", Constants.getMyIP());
            model.addAttribute("warningMessage", "Login is to short: < 3 letters!");
            return "RegistrationPage";
        }

        Pattern p = Pattern.compile(Constants.getPatternLogin());
        if (!p.matcher(login).matches()) {
            model.addAttribute("myipaddress", Constants.getMyIP());
            model.addAttribute("warningMessage", "Incorrect characters in login!");
            return "RegistrationPage";
        }

        if ((password == null) || (password.length() < 5)) {
            model.addAttribute("warningMessage", "Password is to short: < 5 letters!");
            return "RegistrationPage";
        }

        CustomUser customUser = userService.getUserByLogin(login);
        if (customUser != null) {
            model.addAttribute("warningMessage", "This login already exists!");
            return "RegistrationPage";
        }
        String encodedPassword = (new ShaPasswordEncoder()).encodePassword(password, null);
        userService.create(new CustomUser(fullName, login, encodedPassword, UserRole.USER));
        customUser = userService.getUserByLogin(login);
        logger.info("Registration customUser: '" + customUser + "'");
        return "index";
    }

    @RequestMapping("/view")
    public String viewContacts(Model model) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String login = user.getUsername();

        CustomUser dbUser = userService.getUserByLogin(login);

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

    @RequestMapping("/edit")
    private String editDeleteContact(@RequestParam(value = "sendcheckbox", required = false) String idContact,
                                     @RequestParam(value = "delete", required = false) String isDelete, Model model) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String login = user.getUsername();

        CustomUser dbUser = userService.getUserByLogin(login);

        if (idContact == null) {
            return "redirect:/";
        }

        String[] idContacts = idContact.split(",");
        if ("Delete".equals(isDelete)) {
            for (String idCont : idContacts) {
                contactService.delete(idCont);
            }
            return "index";
        } else {
            Contact contact = contactService.getById(idContacts[0]); //only first checked is selecting
            model.addAttribute("myipaddress", Constants.getMyIP());
            model.addAttribute("userLogin", dbUser.getLogin());
            model.addAttribute("titleUpdateContact", "Update contact");

            model.addAttribute("idContact", contact.getId());
            model.addAttribute("lastName", contact.getLastName());
            model.addAttribute("name", contact.getName());
            model.addAttribute("middleName", contact.getMiddleName());
            model.addAttribute("mobilePhone", contact.getMobilePhone());
            model.addAttribute("homePhone", contact.getHomePhone());
            model.addAttribute("address", contact.getAddress());
            model.addAttribute("email", contact.getEmail());

            logger.info("CustomUser edits contact : '" + dbUser + "' contact '" + contact.getId() + "'");
            return "CreateContact";
        }
    }

}