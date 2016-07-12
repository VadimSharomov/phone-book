package main.java.dao;

import main.java.entity.Contact;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Vadim on 18.04.2016.
 *
 */
class ContactMapper implements RowMapper<Contact> {

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
            e.printStackTrace();
        }
        return contact;
    }
}
