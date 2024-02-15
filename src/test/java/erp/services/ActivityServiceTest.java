/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package erp.services;

import erp.dao.ActivityDao;
import erp.dao.CustomerDao;
import erp.domain.Activity;
import java.util.ArrayList;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 *
 * @author wilso
 */

@ExtendWith(SpringExtension.class)
public class ActivityServiceTest {

    @Mock
    private ActivityDao activityDao;

    @Mock
    private CustomerDao customerDao;

    @Mock
    private JsonConversionService jsonConversionService;

    @InjectMocks
    private ActivityService activityService;
    
    @Test
    public void testSaveOrUpdateActivity() {
        // Crea un Activity simulado
        Activity activity = new Activity();
        activity.setName("test");
        // ... establece los otros campos de activity ...

        // Simula el comportamiento de activityDao.findById(...)
        when(activityDao.findById(activity.getId())).thenReturn(Optional.of(activity));

        // Simula el comportamiento de activityDao.save(...)
        when(activityDao.save(any(Activity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Simula el comportamiento de jsonConversionService.toList(...)
        when(jsonConversionService.toList(anyString())).thenReturn(new ArrayList<>());

        // Llama al método que quieres probar
        Activity result = activityService.saveOrUpdateActivity(activity, null, null);

        // Verifica que el resultado es el esperado
        assertEquals(activity, result);
    }
}

