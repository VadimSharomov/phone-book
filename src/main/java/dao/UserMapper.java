package dao;

import entity.User;
import org.slf4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author by Vadim Sharomov
 */
class UserMapper implements RowMapper<User>{
    private final static Logger logger = getLogger(UserMapper.class);

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
            logger.error("Error create user from ResultSet in mapRow: '" + rs + "'", e.getMessage());
        }
        return user;
    }
}
