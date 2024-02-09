package erp.services;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import erp.dao.ActivityDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import erp.dao.CustomerDao;
import erp.domain.Activity;
import erp.domain.CsvProcessingResult;
import erp.domain.Customer;
import jakarta.transaction.Transactional;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
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
            existingCustomer.setActivityNamesString(customer.getActivityNamesString());

            // Añadir actividades a la lista de actividades del cliente
            if (customer.getActivityNamesString() != null) {
                String[] activityNames = customer.getActivityNamesString().split(";");
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

    public CsvProcessingResult loadCustomersFromCsv() {

        System.out.println("Entre al metodo ");
        String file = "./inscriptionForm.csv";
        Queue<Customer> customers = new LinkedList<>();
        try ( Reader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
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
        CsvProcessingResult csvProcessingResult = new CsvProcessingResult();
        csvProcessingResult.setCustomers(customers);
        return csvProcessingResult;
    }

    
    

    public void writeUnprocessedLinesToCsv(List<String> unprocessedLines) {
        try (FileOutputStream fos = new FileOutputStream("inscriptionForm");
                OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                BufferedWriter writer = new BufferedWriter(osw)) {
            // Escribe las líneas no procesadas
            for (String line : unprocessedLines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            // Manejar la excepción
            System.out.println(e.getMessage());
        }
    }

    public List<Customer> findCustomersByName(String name) {
        return customerDao.findCustomersByName(name);
    }

    public Customer findCustomerByEmail(String email) {
        return customerDao.findByEmail(email);
    }

    public List<Customer> getActivityCustomers(Long id) {
        return customerDao.findCustomerByActivityId(id);
    }

    @Transactional
    public void removeCustomersFromActivity(Long activityId, List<Long> customerIds) {
        customerDao.removeCustomersFromActivity(activityId, customerIds);
    }

    public List<String> splitInterests(String interests) {
        List<String> interestList = new ArrayList<>();

        String[] splited = interests.split(";");

        for (int i = 0; i < splited.length; i++) {
            interestList.add(splited[i]);
        }

        return interestList;
    }

    public Map<String, Integer> getAllInterests() {
        Map<String, Integer> interestCounts = new HashMap<>();
        for (Customer customer : customerDao.findAll()) {
            for (String interest : splitInterests(customer.getInterests())) {
                interestCounts.put(interest, interestCounts.getOrDefault(interest, 0) + 1);
            }
        }
        
        return interestCounts;
    }

}
