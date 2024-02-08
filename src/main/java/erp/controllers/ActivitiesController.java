/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package erp.controllers;

import erp.domain.Activity;
import erp.services.ActivityService;
import erp.services.JsonConversionService;
import jakarta.transaction.Transactional;
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

    private final ActivityService activityService;
    private final JsonConversionService jsonConversionService;

    @Autowired
    public ActivitiesController(ActivityService activityService, JsonConversionService jsonConversionService) {
        this.activityService = activityService;
        this.jsonConversionService = jsonConversionService;
    }

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

    @Transactional
    @PostMapping("/activities/create-activity")
    public String createActivity(@ModelAttribute Activity activity, Model model,
            @RequestParam("resourceListHidden") String resourceJson, @RequestParam("requirementListHidden") String requirementJson) {

        boolean isAValidDate = activityService.validateDates(activity.getStartDate(), activity.getEndDate());
        boolean isAValidPaymentValue = activityService.validatePaymentValues(activity.getPricePerPerson(), activity.getNumberOfPayments());
        boolean isAValidParticipantValue = activity.getIsLimited() ? activityService.validateParticipantLimit(activity.getParticipantLimit()) : true;

        if (isAValidDate && isAValidPaymentValue && isAValidParticipantValue) {
            activityService.saveOrUpdateActivity(activity, resourceJson, requirementJson);
            return "redirect:/activities";
        } else {
            // Aquí almacenamos los recursos y requisitos en el modelo para que se vuelvan a mostrar en la vista
            activity.setResources(jsonConversionService.toList(resourceJson));
            activity.setRequirements(jsonConversionService.toList(requirementJson));

            model.addAttribute("activity", activity);
            model.addAttribute("incorrectDate", !isAValidDate);
            model.addAttribute("incorrectPaymentValue", !isAValidPaymentValue);
            model.addAttribute("incorrectParticipantValue", !isAValidParticipantValue);

            return "activityForm";
        }
    }

    @Transactional
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

    @Transactional
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

    @GetMapping("/activities-board")
    public String showActivitiesBoard(@RequestParam(required = false, name = "id") Long activityId, Model model) {
        List<Activity> activities = activityService.getAllActivities();
        model.addAttribute("activities", activities);

        if (activityId != null) {
            Activity activity = activityService.findActivityById(activityId);
            System.out.println(activityId);
            model.addAttribute("selectedActivity", activity);
        }

        return "activitiesBoard";
    }

}
