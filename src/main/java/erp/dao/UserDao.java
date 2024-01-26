/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package erp.dao;

import erp.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author brandon
 */
public interface UserDao extends GenericDao<User, Long> {

        User findByEmail(String email);
        User findByName(String name);
        @Query("SELECT a FROM User a WHERE a.name LIKE %:name%")
        List<User> findUsersByName(@Param("name") String name);
        List<User> findByAccountNonLockedFalse();
        
}


