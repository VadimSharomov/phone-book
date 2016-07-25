package dao;

import entity.Contact;
import org.slf4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Vadim Sharomov
 */
class ContactMapper implements RowMapper<Contact> {
    private final static Logger logger = getLogger(ContactMapper.class);

    @Override
    public Contact mapRow(ResultSet rs, int rowNum) {
        Contact contact = new Contact();
        try {
            contact.setId(rs.getLong("id"));
            contact.setUserId(rs.getLong("iduser"));
            contact.setLastName(rs.getString("lastname"));
            contact.setName(rs.getString("name"));
            contact.setMiddleName(rs.getString("middlename"));
            contact.setMobilePhone(rs.getString("mobilephone"));
            contact.setHomePhone(rs.getString("homephone"));
            contact.setAddress(rs.getString("address"));
            contact.setEmail(rs.getString("email"));
        } catch (SQLException e) {
            logger.error("Error create contact from ResultSet in mapRow: '" + rs + "'", e.getMessage());
        }
        return contact;
    }
}
