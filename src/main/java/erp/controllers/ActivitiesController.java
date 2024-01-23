/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package erp.controllers;

import erp.dao.ActivityDao;
import erp.domain.Activity;
import erp.services.ActivityService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author oscar
 */
@Controller
public class ActivitiesController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityDao activityDao;

    @GetMapping("/activities")
    public String listActivities(Model model) {
        List<Activity> activities = activityService.getAllActivities();
        model.addAttribute("activities", activities);
        return "activities";
    }

    @GetMapping("/activities/create-activity")
    public String showActivityForm(@ModelAttribute Activity activity) {
        return "activity-form";
    }

    @PostMapping("/activities/create-activity")
    public String createActivity(@ModelAttribute Activity activity, Model model) {
        boolean isValid = validateDates(activity.getStartDate(), activity.getEndDate());

        if (isValid) {
            activityService.saveOrUpdateActivity(activity);
            return "redirect:/activities";
        } else {
            model.addAttribute("dateError", true);
            return "activity-form";
        }
    }

    @GetMapping("/activities/edit-activity/{id}")
    public String editActivity(@PathVariable("id") long id, Model model) {
        if (id > 0) {
            Activity activity = activityService.findActivityById(id);
            model.addAttribute("activity", activity);
        }
        return "activity-form";
    }

    @GetMapping("/activities/{id}")
    public String showActivity(@PathVariable("id") long id, Model model) {
        Activity activity = activityService.findActivityById(id);
        model.addAttribute("activity", activity);
        return "activities-overview";
    }

    @PostMapping("/activities/delete-activity/{id}")
    public String deleteActivity(@PathVariable("id") long id, Model model) {
        activityService.deleteActivity(id);
        return "redirect:/activities";
    }

    @GetMapping("/activities/cancel")
    public String cancelAction() {
        return "redirect:/activities";
    }

    @GetMapping("/filtered-activities-by-name")
    public String filterActivitiesByName(@RequestParam("name") String name, Model model) {
        List<Activity> filteredActivities = activityDao.findActivitiesByName(name);
        model.addAttribute("activities", filteredActivities);
        return "activities";
    }

    @GetMapping("/filtered-activities-by-date")
    public String filterActivitiesByDate(@RequestParam("date") String dateString, Model model) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
            List<Activity> filteredActivities = activityDao.findActivitiesByDate(date);
            model.addAttribute("activities", filteredActivities);
            return "activities";
        } catch (ParseException pe) {
            return "activities";
        }
    }

    private boolean validateDates(Date startDate, Date endDate) {
        return startDate == null || endDate == null || startDate.before(endDate);
    }
}
