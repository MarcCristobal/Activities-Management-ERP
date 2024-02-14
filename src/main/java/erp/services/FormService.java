package erp.services;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import org.springframework.stereotype.Service;
import com.opencsv.CSVWriter;
import erp.dao.ActivityDao;
import erp.domain.Form;

@Service
public class FormService {

    private final ActivityDao activityDao;

    public FormService(ActivityDao activityDao) {
        this.activityDao = activityDao;
    }

    public void escribirformEnCsv(Form form) {
        String csvFile = "./inscriptionForm.csv";
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        String birthDateString = formato.format(form.getBirthDate());
        String typeName = form.getType().name();
        try (FileOutputStream fos = new FileOutputStream(csvFile, true);
                OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                CSVWriter csvWriter = new CSVWriter(osw,
                        CSVWriter.DEFAULT_SEPARATOR,
                        CSVWriter.NO_QUOTE_CHARACTER, // Cambia esto
                        CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                        CSVWriter.DEFAULT_LINE_END)) {

            String[] data = {
                    form.getName(), // "Nom"
                    form.getSurnames(), // "Cognoms"
                    form.getEmail(), // "Adreça electrònica"
                    birthDateString, // "Data de naixement"
                    form.getDni(), // "DNI"
                    typeName, // "Ets"
                    form.getPhotoPath(), // "Imatge de perfil"
                    form.getParentName(), // "Nom del pare/mare/tutor legal"
                    form.getPhone(), // "Telèfon"
                    form.getCourse(),
                    form.getInterests(), // "Curs (Només per estudiants)" // "Interessos"
                    form.getActivityNamesString() // "Activitat en què et vols inscriure"
            };

            csvWriter.writeNext(data);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Form initForm() {
        Form form = new Form();
        form.setActivities(activityDao.findAll());
        return form;
    }
}
