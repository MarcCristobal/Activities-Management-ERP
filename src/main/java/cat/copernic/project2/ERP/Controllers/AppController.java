/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Controller.java to edit this template
 */
package cat.copernic.project2.ERP.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Marc
 */
@Controller
public class AppController {

    @GetMapping("/index")
    public String showIndex() {
        return "login";
    }

    @GetMapping("/home")
    public String showHome() {
        return "base";
    }
}
