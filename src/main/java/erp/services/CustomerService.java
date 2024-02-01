package erp.services;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import erp.dao.ActivityDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import erp.dao.CustomerDao;
import erp.domain.Activity;
import erp.domain.Customer;
import jakarta.transaction.Transactional;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author oscar
 */
@Service
public class CustomerService {

    private final CustomerDao customerDao;
    private final ActivityDao activityDao;

    @Autowired
    public CustomerService(CustomerDao customerDao, ActivityDao activityDao) {
        this.customerDao = customerDao;
        this.activityDao = activityDao;
    }

    public Customer saveOrUpdateCustomer(Customer customer) {
        if (customer.getId() != null) {
            Customer existingCustomer = customerDao.findById(customer.getId()).orElse(null);
            existingCustomer.setName(customer.getName());
            existingCustomer.setSurnames(customer.getSurnames());
            existingCustomer.setEmail(customer.getEmail());
            existingCustomer.setBirthDate(customer.getBirthDate());
            existingCustomer.setDni(customer.getDni());
            existingCustomer.setPhotoPath(customer.getPhotoPath());
            existingCustomer.setParentName(customer.getParentName());
            existingCustomer.setPhone(customer.getPhone());
            existingCustomer.setCourse(customer.getCourse());
            existingCustomer.setInterests(customer.getInterests());

            // Añadir actividades a la lista de actividades del cliente
            if (customer.getActivityNamesString() != null) {
                String[] activityNames = customer.getActivityNamesString().split(",");
                for (String activityName : activityNames) {
                    Activity activity = activityDao.findActivityByNameExact(activityName.trim());
                    if (activity != null) {
                        existingCustomer.getActivities().add(activity);
                    }
                }
            }

            return customerDao.save(existingCustomer);
        } else {
            // Este es un nuevo Customer, así que debes añadir las actividades
            if (customer.getActivityNamesString() != null) {
                String[] activityNames = customer.getActivityNamesString().split(",");
                for (String activityName : activityNames) {
                    Activity activity = activityDao.findActivityByNameExact(activityName.trim());
                    if (activity != null) {
                        customer.getActivities().add(activity);
                    }
                }
            }

        }
        return customerDao.save(customer);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        customerDao.deleteById(id);
    }

    public List<Customer> getAllCustomers() {
        return customerDao.findAll();
    }

    public Customer findCustomerById(Long id) {
        return customerDao.findById(id).orElse(null);
    }

        public String savePhoto(MultipartFile photo, Customer customer) throws IOException {
                // Generamos un nombre de archivo único
                String filename;
                if (photo != null && !photo.isEmpty()) {
                        if (customer.getPhotoPath() != null && !customer.getPhotoPath().equals("usuario2.png")) {
                                // Si el usuario ya tiene una imagen asignada que no es la imagen por defecto,
                                // usamos el mismo nombre de archivo para sobrescribir la imagen anterior
                                filename = customer.getPhotoPath();
                                // Obtenemos la ruta absoluta del directorio del proyecto
                                String projectDirectory = new File(".").getAbsolutePath();
                                // Creamos la ruta completa al archivo
                                Path filePath = Paths.get(projectDirectory, "/src/main/resources/static/images/userImages/", filename);
                                // Guardamos la imagen en el archivo, sobrescribiendo el archivo existente si existe
                                Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                        } else {
                                // Si el usuario no tiene una imagen asignada o si tiene la imagen por defecto,
                                // generamos un nuevo nombre de archivo
                                filename = UUID.randomUUID().toString() + ".jpg";
                                // Obtenemos la ruta absoluta del directorio del proyecto
                                String projectDirectory = new File(".").getAbsolutePath();
                                // Creamos la ruta completa al archivo
                                Path filePath = Paths.get(projectDirectory, "/src/main/resources/static/images/userImages/", filename);
                                // Guardamos la imagen en el archivo, sobrescribiendo el archivo existente si existe
                                Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                        }
                } else if (customer.getPhotoPath() != null) {
                        // Si no se sube una nueva foto, usa la foto actual del usuario
                        filename = customer.getPhotoPath();
                } else {
                        // Si no hay foto actual y no se sube una nueva foto, usa la imagen por defecto
                        filename = "usuario2.png";
                }
                // Devolvemos solo el nombre del archivo, no la ruta completa
                return filename;
        }

    public Queue<Customer> loadCustomersFromCsv(MultipartFile file) {
        System.out.println("Entre al metodo ");
        Queue<Customer> customers = new LinkedList<>();
        try ( Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            HeaderColumnNameMappingStrategy<Customer> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(Customer.class);

            CsvToBean<Customer> csvToBean = new CsvToBeanBuilder<Customer>(reader)
                    .withMappingStrategy(strategy)
                    .withSkipLines(0)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            Iterator<Customer> iterator = csvToBean.iterator();

            while (iterator.hasNext()) {
                Customer customer = iterator.next();
                System.out.println(customer); // Imprime el objeto Customer
                customers.add(customer);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return customers;
    }

    public List<Customer> findCustomersByName(String name) {
        return customerDao.findCustomersByName(name);
    }
    
    public List<Customer> getActivityCustomers(Long id){
        return customerDao.findCustomerByActivityId(id);
    }
    
    @Transactional
    public void removeCustomersFromActivity(Long activityId, List<Long> customerIds) {
        customerDao.removeCustomersFromActivity(activityId, customerIds);
    }

}
