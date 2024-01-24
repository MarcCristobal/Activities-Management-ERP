package erp.services;

import erp.dao.UserDao;
import erp.domain.User;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author oscar
 */
@Service
public class UserService {

          private final UserDao userDao;
          private final PasswordEncoder passwordEncoder;

          @Autowired
          public UserService(UserDao userDao, @Lazy PasswordEncoder passwordEncoder) {
                    this.userDao = userDao;
                    this.passwordEncoder = passwordEncoder;
          }

          public User saveOrUpdateUser(User user) {
                    String encodedPassword = passwordEncoder.encode(user.getPassword());
                    user.setPassword(encodedPassword);
                    if (user.getId() != null) {
                              User existingUser = userDao.findById(user.getId()).orElse(null);
                              existingUser.setName(user.getName());
                              existingUser.setSurname(user.getSurname());
                              existingUser.setEmail(user.getEmail());
                              existingUser.setPassword(encodedPassword); // Use the encoded password
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

          public User findUserByEmail(String email) {
                    return userDao.findByEmail(email);
          }

          public User findUserByName(String name) {
                    return userDao.findByName(name);
          }

          public boolean hasUsers() {
                    return userDao.count() == 0;
          }

          public String savePhoto(MultipartFile photo) throws IOException {
                    // Generamos un nombre de archivo único
                    String filename = UUID.randomUUID().toString() + ".jpg";

                    // Guardamos el archivo en el sistema de archivos
                    Files.copy(photo.getInputStream(), Paths.get("/images/userImages/" + filename));

                    return filename;
          }
}
