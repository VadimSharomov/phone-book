package dao;

import entity.User;
import interfaces.UserDAO;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Vadim Sharomov
 */
public interface UserRepository extends UserDAO, CrudRepository<User, Long> {


}
