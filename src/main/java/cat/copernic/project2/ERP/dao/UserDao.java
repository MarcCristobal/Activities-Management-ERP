/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cat.copernic.project2.ERP.dao;

import cat.copernic.project2.ERP.domain.User;

/**
 *
 * @author brandon
 */
public interface UserDao extends GenericDao<User, Long> {

        User findByEmail(String email);
        User findByName(String name);
        
}
