/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Controller.java to edit this template
 */
package erp.controllers;

import erp.domain.Activity;
import erp.domain.Customer;
import erp.services.ActivityService;
import erp.services.CustomerService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Marc
 */
@Controller
public class CustomerController {

    private final CustomerService customerService;
    private Queue<Customer> customersCSV;
    private final ActivityService activityService;

    @Autowired
    public CustomerController(CustomerService customerService, ActivityService activityService) {
        this.customerService = customerService;
        this.activityService = activityService;
    }

    @GetMapping("/home/customers")
    public String showAllCustomers(Model model) {
        List<Customer> customers = customerService.getAllCustomers();
        customers.forEach(customer -> {
            if (customer.getPhotoPath() != null) {
                customer.setPhotoPath(customer.getPhotoPath());
            }
        });
        model.addAttribute("customers", customers);
        return "customers";
    }

    @GetMapping("/home/customers/{id}")
    public String showCustomerProfile(@PathVariable("id") Long id, Model model) {
        Customer customer = customerService.findCustomerById(id);
        model.addAttribute("customer", customer);
        return "customerOverview";
    }

    @Transactional
    @PostMapping("/home/customers/update")
    public String updateCustomer(@ModelAttribute Customer customer, @RequestParam("photo") MultipartFile photo, Model model) {
        String photoPath;
        Customer oldCustomer = null;

        // Comprueba si el usuario ya existe
        if (customer.getId() != null) {
            oldCustomer = customerService.findCustomerById(customer.getId());
        }

        try {
            // Si no se proporciona una nueva foto, usa la foto actual del usuario
            photoPath = customerService.savePhoto(photo, oldCustomer != null ? oldCustomer : customer);
            customer.setPhotoPath(photoPath);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        // Guardamos el usuario
        customerService.saveOrUpdateCustomer(customer);

        return "redirect:/editCustomer";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        System.out.println("Hola");
        customersCSV = customerService.loadCustomersFromCsv(file);
        System.out.println("No pete");
        return "redirect:/editCustomer";
    }

    @Transactional
    @GetMapping("/editCustomer")
    public String editCustomer(Model model) {
        if (customersCSV == null || customersCSV.isEmpty()) {
            return "redirect:/home/customers";  // tu vista cuando todos los clientes han sido revisados
        } else {
            Customer customer = customersCSV.poll();
            if (customer.getPhotoPath() == null) {
                customer.setPhotoPath("usuario2.png");
            }
            System.out.println(customer);
            model.addAttribute("customer", customer);
            return "customerForm";  // tu vista
        }
    }

    @GetMapping("/home/customers/update/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Customer customer = customerService.findCustomerById(id);
        model.addAttribute("customer", customer);
        return "customerForm";
    }

    @Transactional
    @PostMapping("/home/customers/delete/{id}")
    public String deleteCustomer(@PathVariable("id") long id) {
        customerService.deleteCustomer(id);
        return "redirect:/home/customers";
    }

    @GetMapping("/filtered-customers-by-name")
    public String filterCustomersByName(@RequestParam("name") String name, Model model) {
        List<Customer> filteredCustomers = customerService.findCustomersByName(name);
        model.addAttribute("customers", filteredCustomers);
        return "customers";
    }

    @GetMapping("/home/activities/show-participant-list/{id}")
    public String showParticipants(@PathVariable("id") Long activityId, HttpSession session, Model model) {
        if (activityId != null) {
            session.setAttribute("activityId", activityId);
            Activity activity = activityService.findActivityById(activityId);
            if (activity != null) {
                model.addAttribute("activity", activity);
                model.addAttribute("customers", customerService.getActivityCustomers(activityId));
                model.addAttribute("activity_id", activityId);
                return "participantList";
            }
        }
        return "partitipantList";
    }

    @Transactional
    @PostMapping("/update-activity-customers")
    public String updateActivityCustomers(HttpSession session,
            @RequestParam("selectedCustomers") List<Long> selectedCustomers, Model model) {
        Long activityId = (Long) session.getAttribute("activityId");
        Activity activity = activityService.findActivityById(activityId);

        if (activity != null) {
            customerService.removeCustomersFromActivity(activityId, selectedCustomers);
            model.addAttribute("activity", activity);
            model.addAttribute("customers", customerService.getActivityCustomers(activityId));
            model.addAttribute("activity_id", activityId);
        }

        return "participantList";
    }
}
