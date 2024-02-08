/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/Controller.java to edit this template
 */
package erp.controllers;

import erp.domain.Activity;
import erp.domain.Customer;
import erp.domain.Form;
import erp.services.ActivityService;
import erp.services.CustomerService;
import erp.services.FormService;
import erp.services.PhotoStorageService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
        private final PhotoStorageService photoStorageService;
        private final ActivityService activityService;
        private final FormService formService;

        @Autowired
        public CustomerController(CustomerService customerService, ActivityService activityService,
                        FormService formService, PhotoStorageService photoStorageService) {
                this.customerService = customerService;
                this.activityService = activityService;
                this.formService = formService;
                this.photoStorageService = photoStorageService;
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
        public String updateCustomer(@Valid @ModelAttribute Customer customer, BindingResult result,
                        @RequestParam("photo") MultipartFile photo, Model model,
                        @RequestParam List<String> selectedActivities) {
                if (result.hasErrors()) {
                        // Aquí, puedes agregar los errores al modelo para que puedan ser mostrados en
                        // la vista
                        model.addAttribute("errors", result.getAllErrors());
                        // Añade los atributos necesarios al modelo antes de redirigir
                        List<Activity> activities = activityService.getAllActivities();
                        model.addAttribute("activities", activities);
                        model.addAttribute("selectedActivityNames", selectedActivities);
                        return "customerForm"; // Redirige de vuelta al formulario
                }

                String photoPath;
                Customer existingCustomer = null;

                if (customer.getId() != null) {
                        existingCustomer = customerService.findCustomerByEmail(customer.getEmail());
                        if (existingCustomer != null) {
                                customer.setId(existingCustomer.getId());
                                // Si el cliente existe, obtén las actividades seleccionadas previamente
                                List<String> oldActivityNames = new ArrayList<>(
                                                Arrays.asList(existingCustomer.getActivityNamesString().split(";")));
                                // Crea una copia de las actividades antiguas
                                List<String> copyOfOldActivityNames = new ArrayList<>(oldActivityNames);
                                // Retén solo las actividades que están presentes en ambas listas (las que no se
                                // han quitado)
                                oldActivityNames.retainAll(selectedActivities);
                                // Añade las nuevas actividades seleccionadas a la lista (las que se han
                                // añadido)
                                oldActivityNames.addAll(selectedActivities);
                                // Elimina duplicados
                                oldActivityNames = oldActivityNames.stream().distinct().collect(Collectors.toList());
                                // Actualiza las actividades del cliente
                                customer.setActivityNamesString(String.join(";", oldActivityNames));
                        } else {
                                // Combina las actividades seleccionadas con las existentes
                                customer.setActivityNamesString(String.join(";", selectedActivities));
                        }

                } else {
                        existingCustomer = customerService.findCustomerByEmail(customer.getEmail());
                        if (existingCustomer != null) {
                                customer.setId(existingCustomer.getId());
                                List<String> oldActivityNames = new ArrayList<>(
                                                Arrays.asList(existingCustomer.getActivityNamesString().split(";")));
                                // Añade las nuevas actividades seleccionadas a la lista
                                oldActivityNames.addAll(selectedActivities);
                                // Elimina duplicados
                                oldActivityNames = oldActivityNames.stream().distinct().collect(Collectors.toList());
                                // Actualiza las actividades del cliente
                                customer.setActivityNamesString(String.join(";", oldActivityNames));

                        } else {
                                customer.setActivityNamesString(String.join(";", selectedActivities));
                        }

                }

                try {
                        // Si no se proporciona una nueva foto, usa la foto actual del usuario
                        photoPath = photoStorageService.savePhoto(photo,
                                        existingCustomer != null ? existingCustomer : customer);
                        customer.setPhotoPath(photoPath);
                } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                }

                // Guardamos el usuario
                customerService.saveOrUpdateCustomer(customer);

                if (customersCSV != null && !customersCSV.isEmpty()) {
                        customersCSV.poll(); // Quita el cliente de la cola solo después de que se haya procesado con
                                             // éxito
                }

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
                        return "redirect:/home/customers"; // tu vista cuando todos los clientes han sido revisados
                } else {
                        Customer customer = customersCSV.peek(); // Usa peek en lugar de poll para no quitar el cliente
                                                                 // de la cola
                        if (customer.getPhotoPath() == null) {
                                customer.setPhotoPath("usuario2.png");
                        }
                        List<Activity> activities = activityService.getAllActivities();
                        model.addAttribute("activities", activities);

                        List<String> selectedActivityNames = Arrays
                                        .asList(customer.getActivityNamesString().split(";"));
                        model.addAttribute("selectedActivityNames", selectedActivityNames);

                        System.out.println(customer);
                        model.addAttribute("customer", customer);
                        return "customerForm"; // tu vista
                }
        }

        @GetMapping("/home/customers/update/{id}")
        public String showUpdateForm(@PathVariable("id") long id, Model model) {
                Customer customer = customerService.findCustomerById(id);
                List<Activity> activities = activityService.getAllActivities();
                model.addAttribute("activities", activities);
                List<String> selectedActivityNames = Arrays.asList(customer.getActivityNamesString().split(";"));
                model.addAttribute("selectedActivityNames", selectedActivityNames);
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
        public String filterActivitiesByName(@RequestParam("name") String name, Model model) {
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

        @GetMapping("/activities-board/show-inscription-form")
        public String showInscriptionForm(Model model) {
                Form form = formService.initForm();
                model.addAttribute("form", form);
                model.addAttribute("activities", form.getActivities());
                model.addAttribute("cursos", form.getCursos());
                model.addAttribute("intereses", form.getIntereses());
                return "inscriptionForm";
        }

        @PostMapping("/activities-board/show-inscription-form/form")
        public String recibirFormulario(@Valid @ModelAttribute Form form, BindingResult result,
                        @RequestParam("photo") MultipartFile photo,
                        Model model, @RequestParam List<String> selectedActivities) {
                String photoPath;
                if (result.hasErrors()) {
                        // Aquí, puedes agregar los errores al modelo para que puedan ser mostrados en
                        // la vista
                        model.addAttribute("errors", result.getAllErrors());
                        // Añade los atributos necesarios al modelo antes de redirigir
                        List<Activity> activities = activityService.getAllActivities();
                        model.addAttribute("form", form);
                        model.addAttribute("activities", activities);
                        model.addAttribute("selectedActivityNames", selectedActivities);
                        model.addAttribute("cursos", form.getCursos());
                        model.addAttribute("intereses", form.getIntereses());
                        return "inscriptionForm"; // Redirige de vuelta al formulario
                }
                try {
                        // Si se proporciona una nueva foto, guarda la foto y establece la ruta de la
                        // foto en el objeto Form
                        photoPath = photoStorageService.savePhoto(photo, form);
                        form.setPhotoPath(photoPath);
                        form.setActivityNamesString(String.join(";", selectedActivities));
                        // Escribe los datos del Customer en el CSV
                        formService.escribirformEnCsv(form);
                } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                }
                System.out.println(form.getInterests() + "dsa");
                // Redirige a una pantalla de confirmación
                return "redirect:/show-inscription-form";
        }
}
