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

/**
 *
 * @author oscar
 */
@Controller
public class ActivitiesController {

    @Autowired
    private ActivityService activityService;

    @GetMapping("/activities")
    public String listPersons(Model model) {
        List<Activity> activities = activityService.getAllActivities();
        model.addAttribute("activities", activities);
        return "activities";
    }

}
