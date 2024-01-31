package erp.services;

import erp.dao.UserDao;
import erp.domain.User;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
        private final PasswordGenerator passwordGenerator;
        private final MailSenderService javaMailSender;

        @Autowired
        public UserService(UserDao userDao, @Lazy PasswordEncoder passwordEncoder, PasswordGenerator passwordGenerator, MailSenderService javaMailSender) {
                this.userDao = userDao;
                this.passwordEncoder = passwordEncoder;
                this.passwordGenerator = passwordGenerator;
                this.javaMailSender = javaMailSender;
        }

        public User saveOrUpdateUser(User user) {
                if (user.getId() != null) {
                        // Si el usuario ya existe, actualiza sus datos
                        User existingUser = userDao.findById(user.getId()).orElse(null);
                        existingUser.setName(user.getName());
                        existingUser.setSurname(user.getSurname());
                        existingUser.setEmail(user.getEmail());
                        existingUser.setRole(user.getRole());
                        existingUser.setPhotoPath(user.getPhotoPath());

                        // Si el campo de contraseña no está vacío, significa que el usuario quiere cambiar su contraseña
                        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                                String encodedPassword = passwordEncoder.encode(user.getPassword());
                                existingUser.setPassword(encodedPassword);
                        }

                        return userDao.save(existingUser);
                } else {
                        if (!hasUsers()) {
                                String randomPassword = passwordGenerator.generateRandomPassword(8);
                                //javaMailSender.sendEmail(user.getEmail(), "Cuenta Activities ERP", "Aqui tiene sus credenciales nuevas y el link para regenerar la contraseña:" + randomPassword);

                                // Codifica la contraseña y la guarda en el usuario
                                String encodedPassword = passwordEncoder.encode(randomPassword);
                                user.setPassword(encodedPassword);
                        } else {
                                String encodedPassword = passwordEncoder.encode(user.getPassword());
                                user.setPassword(encodedPassword);
                        }
                        // Si el usuario es nuevo, genera una contraseña aleatoria y envíala por correo

                        return userDao.save(user);
                }
        }

        public void unLockUser(User user) {
                userDao.save(user);
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

        public User findPersonByName(String name) {
                return userDao.findByName(name);
        }

        public boolean hasUsers() {
                return userDao.count() == 0;
        }

        public String savePhoto(MultipartFile photo, User user) throws IOException {
                // Generamos un nombre de archivo único
                String filename;
                if (user.getPhotoPath() != null && !user.getPhotoPath().equals("/images/usuario2.png")) {
                        // Si el usuario ya tiene una imagen asignada que no es la imagen por defecto,
                        // usamos el mismo nombre de archivo para sobrescribir la imagen anterior
                        filename = user.getPhotoPath();
                } else {
                        // Si el usuario no tiene una imagen asignada o si tiene la imagen por defecto,
                        // generamos un nuevo nombre de archivo
                        filename = UUID.randomUUID().toString() + ".jpg";
                }

                // Obtenemos la ruta absoluta del directorio del proyecto
                String projectDirectory = new File(".").getAbsolutePath();

                // Creamos la ruta completa al archivo
                Path filePath = Paths.get(projectDirectory, "/src/main/resources/static/images/userImages/", filename);

                // Guardamos la imagen en el archivo, sobrescribiendo el archivo existente si existe
                Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Devolvemos solo el nombre del archivo, no la ruta completa
                return filename;
        }

        public List<User> getLockedUsers() {
                return userDao.findByAccountNonLockedFalse();
        }

        public List<User> findUsersByName(String name) {
                return userDao.findUsersByName(name);
        }

}
