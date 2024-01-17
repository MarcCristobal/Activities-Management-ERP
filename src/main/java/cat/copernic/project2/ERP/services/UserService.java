package cat.copernic.project2.ERP.services;

import cat.copernic.project2.ERP.dao.UserDao;
import cat.copernic.project2.ERP.domain.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author oscar
 */
@Service
public class UserService {

          private final UserDao userDao;

          @Autowired
          public UserService(UserDao userDao) {
                    this.userDao = userDao;
          }
          public User saveOrUpdatePerson(User user) {
                    if (user.getId() != null) {
                              User existingUser = userDao.findById(user.getId()).orElse(null);
                              existingUser.setName(user.getName());
                              existingUser.setSurname(user.getSurname());
                              existingUser.setEmail(user.getEmail());
                              existingUser.setPassword(user.getPassword());
                              existingUser.setRole(user.getRole());
                              existingUser.setPhotoPath(user.getPhotoPath());
                              return userDao.save(existingUser);
                    }
                    return userDao.save(user);
          }
          public void deleteUser(Long id) {
                    userDao.deleteById(id);
          }
          public User findById(Long id) {
                    return userDao.findById(id).orElse(null);
          }
          public List<User> getAllUsers() {
                    return userDao.findAll();
          }
          public List<User> findPersonsByIds(List<Long> ids) {
                    return userDao.findAllById(ids);
          }
          public User findPersonByEmail(String email) {
                    return userDao.findByEmail(email);
          }
          public User findPersonByName(String name){
                    return userDao.findByName(name);
          }
}
