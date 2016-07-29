package repository;

import entity.Contact;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Vadim Sharomov
 */
public interface ContactRepository extends CrudRepository<Contact, Long> {

    @Query("select u from #{#entityName} u where u.id = ?1")
    Contact getContactById(Long id);

    @Query(value = "select * from #{#entityName} where iduser = ?1 order by lastname", nativeQuery = true)
    List<Contact> findContactByIdUser(Long idUser);

    @Query(value = "select * from #{#entityName} u where u.iduser = ?1 and u.lastname like %?2% and u.name like %?3% and u.mobilephone like %?4%", nativeQuery = true)
    List<Contact> getByIdUserAndName(Long idUser, String lastName, String name, String mobilePhone);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update #{#entityName} u set u.lastname = ?1, u.name = ?2, u.middlename = ?3, u.mobilephone = ?4, u.homephone = ?5, u.address = ?6, u.email = ?7 where u.id = ?8", nativeQuery = true)
    int updateContact(String lastName, String name, String middleName, String mobilePhone, String homePhone, String address, String email, Long id);

    @Transactional
    @Query("delete from #{#entityName} where id = ?1")
    @Modifying(clearAutomatically = true)
    int deleteById(Long id);
}
