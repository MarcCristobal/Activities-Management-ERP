/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package erp.dao;

import erp.domain.Customer;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author oscar
 */
public interface CustomerDao extends GenericDao<Customer, Long> {

    @Query("SELECT c FROM Customer c WHERE c.name LIKE %:name% OR c.surnames LIKE %:name% OR CONCAT(c.name, ' ', c.surnames) LIKE %:name%")
    List<Customer> findCustomersByName(@Param("name") String name);

    @Query("SELECT c FROM Customer c JOIN c.activities a WHERE a.id = :activityId")
    List<Customer> findCustomerByActivityId(@Param("activityId") Long activityId);

    @Modifying
    @Query(value = "DELETE FROM customer_activity WHERE customer_id IN :customerIds AND activity_id = :activityId", nativeQuery = true)
    void removeCustomersFromActivity(@Param("activityId") Long activityId, @Param("customerIds") List<Long> customerIds);

}
