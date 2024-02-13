/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package erp.dao;

import erp.domain.User;
import erp.domain.UserRole;

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

        @Query("SELECT u FROM User u WHERE u.role = :role")
        List<User> findUsersByRole(@Param("role") UserRole role);

        @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email = :email")
        boolean existsByEmail(@Param("email") String email);

}
