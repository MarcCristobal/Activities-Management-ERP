/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package erp.dao;

import erp.domain.Customer;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author oscar
 */
public interface CustomerDao extends GenericDao<Customer, Long> {

    @Query("SELECT c FROM Customer c WHERE c.name LIKE %:name%")
    List<Customer> findCustomerByName(@Param("name") String name);

}
