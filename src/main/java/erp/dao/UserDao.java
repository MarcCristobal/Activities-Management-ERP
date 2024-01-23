/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package erp.dao;

import erp.domain.User;

/**
 *
 * @author brandon
 */
public interface UserDao extends GenericDao<User, Long> {

        User findByEmail(String email);
        User findByName(String name);
        
}
