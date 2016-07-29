package repository;

import entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Vadim Sharomov
 */
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findAll();

    @Query("select u from #{#entityName} u where u.id = ?1")
    User getUserById(Long id);

    @Query("select u from #{#entityName} u where u.login = ?1")
    User getByLogin(String login);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update #{#entityName} u set u.idsession = ?1 where u.id = ?2", nativeQuery = true)
    int setFixedIdSessionFor(Long idSession, Long id);


}
