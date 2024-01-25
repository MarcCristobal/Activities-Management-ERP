package erp.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import erp.dao.CustomerDao;
import erp.domain.Customer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author oscar
 */
@Service
public class CustomerService {

    private final CustomerDao customerDao;

    @Autowired
    public CustomerService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public Customer saveOrUpdateCustomer(Customer customer) {
        if (customer.getId() != null) {
            Customer existingCustomer = customerDao.findById(customer.getId()).orElse(null);
            existingCustomer.setName(customer.getName());
            existingCustomer.setSurnames(customer.getSurnames());
            existingCustomer.setEmail(customer.getEmail());
            existingCustomer.setBirthDate(customer.getBirthDate());
            existingCustomer.setDni(customer.getDni());
            existingCustomer.setType(customer.getType());
            existingCustomer.setPhotoPath(customer.getPhotoPath());
            existingCustomer.setParentName(customer.getParentName());
            existingCustomer.setPhone(customer.getPhone());
            existingCustomer.setCourse(customer.getCourse());
            existingCustomer.setInterests(customer.getInterests());
            return customerDao.save(existingCustomer);
        }
        return customerDao.save(customer);
    }

    public void deleteCustomer(Long id) {
        customerDao.deleteById(id);
    }

    public Customer findById(Long id) {
        return customerDao.findById(id).orElse(null);
    }

    public List<Customer> getAllCustomers() {
        return customerDao.findAll();
    }

    public Customer findCustomerById(Long id) {
        return customerDao.findById(id).orElse(null);
    }

    public String savePhoto(MultipartFile photo) throws IOException {
        // Generamos un nombre de archivo único
        String filename = UUID.randomUUID().toString() + ".jpg";

        // Guardamos el archivo en el sistema de archivos
        Files.copy(photo.getInputStream(), Paths.get("/images/userImages/" + filename));

        return filename;
    }
}
