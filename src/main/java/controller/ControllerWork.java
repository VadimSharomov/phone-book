package controller;

import entity.Contact;
import entity.CustomUser;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import services.ConstantsRegexPattern;
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
public class ControllerWork {
    private final static Logger logger = getLogger(ControllerWork.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ContactService contactService;

    @RequestMapping("/test")
    public String greeting(
            @RequestParam(value = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "Greeting";
    }

    @RequestMapping("/accessdenied")
    public String accessDenied(Model model) {
        model.addAttribute("warningMessage", "Access denied!");
        return "Login";
    }

    @RequestMapping("/logout")
    public String logout() {
        User user = (User) SecurityContextHolder.getContext();
        logger.info("Logout user with id: '" + user.getUsername() + "'");
        return "Login";
    }


    @RequestMapping("/find")
    private String findContacts(
            @RequestParam(value = "lastname", required = false) String lastName,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "mobilePhone", required = false) String mobilePhone, Model model) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CustomUser dbUser = userService.getUserByLogin(user.getUsername());

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
        CustomUser dbUser = userService.getUserByLogin(user.getUsername());

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

        CustomUser dbUser = userService.getUserByLogin(user.getUsername());
        if ((idContact != null) && (lastName != null) && (middleName != null) && (mobilePhone != null)) {
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
            Pattern p = Pattern.compile(ConstantsRegexPattern.getPatternMobilePhoneUkr());
            if (!p.matcher(mobilePhone).matches()) {
                model.addAttribute("warningMessage", "Mobile phone is incorrect for Ukraine!");
                return "CreateContact";
            }
            if ((homePhone != null) && (!"".equals(homePhone))) {
                p = Pattern.compile(ConstantsRegexPattern.getPatternStationaryPhoneUkr());
                if (!p.matcher(homePhone).matches()) {
                    model.addAttribute("warningMessage", "Home phone is incorrect for Ukraine!");
                    return "CreateContact";
                }
            }
            if ((email != null) && (!"".equals(email))) {
                p = Pattern.compile(ConstantsRegexPattern.getPatternEmail(), Pattern.CASE_INSENSITIVE);
                if (!p.matcher(email).matches()) {
                    model.addAttribute("warningMessage", "Email is incorrect!");
                    return "CreateContact";
                }
            }
            if ("".equals(idContact)) {
                Contact contact = new Contact(dbUser.getId(), lastName, name, middleName, mobilePhone, homePhone, address, email);
                contactService.create(contact);
            } else {
                Contact contact = new Contact(dbUser.getId(), Long.parseLong(idContact), lastName, name, middleName, mobilePhone, homePhone, address, email);
                contactService.update(contact);
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
        model.addAttribute("titleUpdateContact", "Update contact");
        model.addAttribute("userLogin", dbUser.getLogin());
        model.addAttribute("contacts", contacts);
        return "redirect:/";
    }
}