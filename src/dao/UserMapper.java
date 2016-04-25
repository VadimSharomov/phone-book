package dao;

import entity.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Vadim on 18.04.2016.
 *
 */
class UserMapper implements RowMapper<User>{

    @Override
    public User mapRow(ResultSet rs, int rowNum){
        User user = new User();
        try {
            user.setId(rs.getLong("id"));
            user.setIdSession(rs.getLong("idsession"));
            user.setFullName(rs.getString("fullname"));
            user.setLogin(rs.getString("login"));
            user.setPassword(rs.getString("password"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
