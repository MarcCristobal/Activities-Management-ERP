/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package erp.dao;

import erp.domain.Activity;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author oscar
 */
public interface ActivityDao extends GenericDao<Activity, Long> {

    @Query("SELECT a FROM Activity a WHERE a.name LIKE %:name%")
    List<Activity> findActivitiesByName(@Param("name") String name);

    @Query("SELECT a FROM Activity a WHERE a.startDate >= :date")
    List<Activity> findActivitiesByDate(@Param("date") Date date);

    @Query("SELECT a FROM Activity a WHERE a.name = :name")
    Activity findActivityByNameExact(@Param("name") String name);

    @Query("SELECT a FROM Activity a WHERE a.monitor.id = :monitorId")
    List<Activity> findActivitiesByMonitorId(@Param("monitorId") Long monitorId);

    @Query("SELECT a FROM Activity a WHERE a.name LIKE %:name% AND a.monitor.id = :monitorId")
    List<Activity> findActivitiesByNameAndMonitor(@Param("name") String name, @Param("monitorId") Long monitorId);

    @Query("SELECT a FROM Activity a WHERE a.startDate >= :date AND a.monitor.id = :monitorId")
    List<Activity> findActivitiesByDateAndMonitor(@Param("date") Date date, @Param("monitorId") Long monitorId);

}
