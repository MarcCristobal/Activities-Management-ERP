/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package erp.controllers;

import erp.domain.Activity;
import erp.services.ActivityService;
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

    @GetMapping("/activities")
    public String listActivities(Model model) {
        List<Activity> activities = activityService.getAllActivities();
        model.addAttribute("activities", activities);
        return "activities";
    }

    @GetMapping("/activities/create-activity")
    public String showActivityForm(@ModelAttribute Activity activity) {
        return "activityForm";
    }

    @PostMapping("/activities/create-activity")
    public String createActivity(@ModelAttribute Activity activity, Model model,
            @RequestParam("resourceListHidden") String resourceJson, @RequestParam("requirementListHidden") String requirementJson) {

        boolean isAValidDate = activityService.validateDates(activity.getStartDate(), activity.getEndDate());
        boolean isAValidPaymentValue = activityService.validatePaymentValues(activity.getPricePerPerson(), activity.getNumberOfPayments());
        boolean isAValidParticipantValue = activity.getIsLimited() ? activityService.validateParticipantLimit(activity.getParticipantLimit()) : true;

        if (isAValidDate && isAValidPaymentValue && isAValidParticipantValue) {
            activityService.saveOrUpdateActivity(activity, resourceJson, requirementJson);
            return "redirect:/activities";
        } else if (!isAValidDate) {
            model.addAttribute("incorrectDate", true);
        } else if (!isAValidPaymentValue) {
            model.addAttribute("incorrectPaymentValue", true);
        } else if (!isAValidParticipantValue) {
            model.addAttribute("incorrectParticipantValue", true);
        }
        return "activityForm";
    }

    @GetMapping("/activities/edit-activity/{id}")
    public String editActivity(@PathVariable("id") long id, Model model) {
        if (id > 0) {
            Activity activity = activityService.findActivityById(id);
            model.addAttribute("activity", activity);
        }
        return "activityForm";
    }

    @GetMapping("/activities/{id}")
    public String showActivity(@PathVariable("id") long id, Model model) {
        Activity activity = activityService.findActivityById(id);
        model.addAttribute("activity", activity);
        return "activitiesOverview";
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
                List<Activity> filteredActivities = activityService.findActivitiesByName(name);
                model.addAttribute("activities", filteredActivities);
                return "activities";
        }

    @GetMapping("/filtered-activities-by-date")
    public String filterActivitiesByDate(@RequestParam("date") String dateString, Model model) {
        List<Activity> filteredActivities = activityService.findActivitiesByDate(dateString);
        model.addAttribute("activities", filteredActivities);
        return "activities";
    }
}
