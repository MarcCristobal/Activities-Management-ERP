/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package erp.controllers;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.IOException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 *
 * @author wilso
 */
@Component
public class StartupConfig {

          @PostConstruct
          public void init() {
                    createDirectoryAndCopyDefaultImageIfNotExists("./userImages/", "static/images/userImages/usuario2.png");
          }



          public void createDirectoryAndCopyDefaultImageIfNotExists(String directoryPath, String defaultImagePath) {
                    Path dirPath = Paths.get(directoryPath);
                    if (!Files.exists(dirPath)) {
                              try {
                                        Files.createDirectories(dirPath);

                                        // Copia la imagen por defecto al nuevo directorio
                                        Resource defaultImage = new ClassPathResource(defaultImagePath);
                                        Path targetPath = dirPath.resolve(defaultImage.getFilename());
                                        Files.copy(defaultImage.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                              } catch (IOException e) {
                                        // Manejar la excepción
                                        System.out.println(e.getMessage());
                              }
                    }
          }

}
