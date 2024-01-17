/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.project2.ERP.Controllers;

import cat.copernic.project2.ERP.domain.Activity;
import cat.copernic.project2.ERP.services.ActivityService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author oscar
 */
@Controller
public class ActivitiesController {

    @Autowired
    private ActivityService activityService;

    @GetMapping("/activities")
    public String listACtivities(Model model) {
        List<Activity> activities = activityService.getAllActivities();
        model.addAttribute("activities", activities);
        return "activities";
    }

    @GetMapping("/activities/create-activity")
    public String showActivityForm(@ModelAttribute Activity activity) {
        return "activity-form";
    }

    @PostMapping("/activities/crate-activity")
    public String createActivity(@ModelAttribute Activity activity) {
        activityService.saveOrUpdateActivity(activity);
        return "redirect:/activities";
    }

}
