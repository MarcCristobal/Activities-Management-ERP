/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.project2.ERP.Controllers;

import cat.copernic.project2.ERP.domain.Activity;
import cat.copernic.project2.ERP.services.ActivityService;
import cat.copernic.project2.ERP.services.JsonToListService;
import java.util.ArrayList;
import java.util.HashSet;
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
    private JsonToListService jsonToListService;

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
    public String createActivity(@ModelAttribute Activity activity,
            @RequestParam("resourceListHidden") String jsonResources,
            @RequestParam("requirementListHidden") String jsonRequirements) {

        ArrayList<String> resources = jsonToListService.toList(jsonResources);
        activity.setResources(resources);

        ArrayList<String> requirements = jsonToListService.toList(jsonRequirements);
        activity.setRequirements(requirements);

        activityService.saveOrUpdateActivity(activity);
        return "redirect:/activities";
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

}
