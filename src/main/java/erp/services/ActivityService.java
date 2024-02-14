package erp.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import erp.dao.ActivityDao;
import erp.dao.CustomerDao;
import erp.domain.Activity;
import erp.domain.Customer;
import erp.domain.User;
import erp.domain.UserRole;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

/**
 *
 * @author oscar
 */
@Service
public class ActivityService {

    private final ActivityDao activityDao;
    private final CustomerDao customerDao;

    @Autowired
    public ActivityService(ActivityDao activitiesDao, CustomerDao customerDao) {
        this.activityDao = activitiesDao;
        this.customerDao = customerDao;
    }

    @Autowired
    private JsonConversionService jsonConversionService;

    public Activity saveOrUpdateActivity(Activity activity, String resourceJson, String requirementJson) {
        if (activity.getId() != null) {
            Activity existingActivity = activityDao.findById(activity.getId()).orElse(null);
            existingActivity.setName(activity.getName());
            existingActivity.setPlace(activity.getPlace());
            existingActivity.setStartDate(activity.getStartDate());
            existingActivity.setEndDate(activity.getEndDate());
            existingActivity.setIsFree(activity.getIsFree());
            existingActivity.setMonitor(activity.getMonitor());
            existingActivity.setIsLimited(activity.getIsLimited());
            existingActivity.setResources(jsonConversionService.toList(resourceJson));
            existingActivity.setRequirements(jsonConversionService.toList(requirementJson));
            if (existingActivity.getIsLimited()) {
                existingActivity.setParticipantLimit(activity.getParticipantLimit());
            } else {
                existingActivity.setParticipantLimit(0);
            }
            if (existingActivity.getIsFree()) {
                existingActivity.setPricePerPerson(activity.getPricePerPerson());
                existingActivity.setNumberOfPayments(activity.getNumberOfPayments());
            } else {
                existingActivity.setPricePerPerson(0);
                existingActivity.setNumberOfPayments(0);
            }

            return activityDao.save(existingActivity);
        }
        activity.setResources(jsonConversionService.toList(resourceJson));
        activity.setRequirements(jsonConversionService.toList(requirementJson));

        return activityDao.save(activity);
    }

    public void deleteActivity(Long id) {
        Activity activity = activityDao.findById(id).orElse(null);
        // Para cada cliente asociado a la actividad
        for (Customer customer : activity.getCustomers()) {
            // Elimina la actividad de la lista de actividades del cliente
            customer.getActivities().remove(activity);
            // Guarda el cliente con la lista de actividades actualizada
            customerDao.save(customer);
        }
        // Ahora puedes eliminar la actividad
        activityDao.delete(activity);
    }

    public Activity findActivityById(Long id) {
        return activityDao.findById(id).orElse(null);
    }

    public List<Activity> getAllActivities() {
        return activityDao.findAll();
    }

    public List<Activity> findActivitiesByName(String name, User user) {
        if (user.getRole().equals(UserRole.MONITOR)) {
            return activityDao.findActivitiesByNameAndMonitor(name, user.getId());
        } else {
            return activityDao.findActivitiesByName(name);
        }
    }

    public List<Activity> findActivitiesByDate(String dateString, User user) {
        if (dateString == null || dateString.isEmpty()) {
            return getAllActivities();
        } else {
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
                if (user.getRole().equals(UserRole.MONITOR)) {
                    return activityDao.findActivitiesByDateAndMonitor(date, user.getId());
                } else {
                    return activityDao.findActivitiesByDate(date);
                }
            } catch (ParseException pe) {
                return Collections.emptyList();
            }
        }
    }

    public boolean validateDates(Date startDate, Date endDate) {
        return startDate.compareTo(endDate) <= 0;
    }

    public boolean validatePaymentValues(double pricePerPerson, int numberOfPayments) {
        return !(pricePerPerson < 0 || numberOfPayments < 0);
    }

    public boolean validateParticipantLimit(int participantLimit) {
        return !(participantLimit < 1);
    }

    public List<Activity> getActivitiesByMonitor(User user) {
        return activityDao.findActivitiesByMonitorId(user.getId());

    }

}
