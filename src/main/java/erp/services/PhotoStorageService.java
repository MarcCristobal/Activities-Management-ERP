package erp.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import erp.domain.PhotoEntity;

@Service
public class PhotoStorageService {
    public String savePhoto(MultipartFile photo, PhotoEntity entity) throws IOException {
    // Generamos un nombre de archivo único
    String filename;
    if (photo != null && !photo.isEmpty()) {
        if (entity.getPhotoPath() != null && !entity.getPhotoPath().equals("usuario2.png")) {
            // Si la entidad ya tiene una imagen asignada que no es la imagen por defecto,
            // usamos el mismo nombre de archivo para sobrescribir la imagen anterior
            filename = entity.getPhotoPath();
            // Obtenemos la ruta absoluta del directorio del proyecto
            String projectDirectory = new File(".").getAbsolutePath();
            // Creamos la ruta completa al archivo
            Path filePath = Paths.get(projectDirectory, "./userImages/", filename);
            // Guardamos la imagen en el archivo, sobrescribiendo el archivo existente si existe
            Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } else {
            // Si la entidad no tiene una imagen asignada o si tiene la imagen por defecto,
            // generamos un nuevo nombre de archivo
            filename = UUID.randomUUID().toString() + ".jpg";
            // Obtenemos la ruta absoluta del directorio del proyecto
            String projectDirectory = new File(".").getAbsolutePath();
            // Creamos la ruta completa al archivo
            Path filePath = Paths.get(projectDirectory, "./userImages/", filename);
            // Guardamos la imagen en el archivo, sobrescribiendo el archivo existente si existe
            Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        }
    } else if (entity.getPhotoPath() != null) {
        // Si no se sube una nueva foto, usa la foto actual de la entidad
        filename = entity.getPhotoPath();
    } else {
        // Si no hay foto actual y no se sube una nueva foto, usa la imagen por defecto
        filename = "usuario2.png";
    }
    // Asignamos el nombre del archivo a la entidad
    entity.setPhotoPath(filename);
    // Devolvemos solo el nombre del archivo
    return filename;
}

}
