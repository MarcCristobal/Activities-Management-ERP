/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package erp.controllers;

import erp.domain.User;
import erp.domain.UserRole;
import erp.services.ActivityService;
import erp.services.JsonConversionService;
import erp.services.UserService;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 *
 * @author wilso
 */
@WebMvcTest(ActivitiesController.class)
public class ActivitiesControllerTest {

          @Autowired
          private MockMvc mockMvc;

          @MockBean
          private ActivityService activityService;

          @MockBean
          private JsonConversionService jsonConversionService;

          @MockBean
          private UserService userService;

          @Test
          public void testListActivitiesView() throws Exception {
                    // Crea un User simulado
                    User user = new User();

                    user.setEmail("test@test.com");
                    user.setId(1L);
                    user.setName("Test User");
                    user.setPhotoPath("usuario2.png");
                    user.setRole(UserRole.MONITOR); // Asegúrate de establecer el rol del usuario

                    // Simula el comportamiento de userService.findUserByEmail(...)
                    when(userService.findUserByEmail(user.getEmail())).thenReturn(user);
                    // Crea un Authentication con los detalles necesarios
                    Map<String, Object> details = new HashMap<>();
                    details.put("id", user.getId());
                    details.put("name", user.getName());
                    details.put("photoPath", user.getPhotoPath());
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user.getEmail(), null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
                    auth.setDetails(details);

                    // Establece el Authentication en el SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(auth);

                    // Realiza la solicitud GET y verifica la respuesta
                    mockMvc.perform(get("/activities"))
                            .andExpect(status().isOk())
                            .andExpect(view().name("activities"));
          }

          @Test
          public void testShowActivityFormView() throws Exception {
                    // Crea un Authentication simulado con los detalles necesarios
                    Map<String, Object> details = new HashMap<>();
                    details.put("name", "Test User");
                    Authentication auth = new TestingAuthenticationToken("test@test.com", "password", "ROLE_ADMIN");
                    ((TestingAuthenticationToken) auth).setDetails(details);

                    // Establece el Authentication en el SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(auth);

                    // Realiza la solicitud GET y verifica la respuesta
                    mockMvc.perform(get("/activities/create-activity"))
                            .andExpect(status().isOk())
                            .andExpect(view().name("activityForm"));
          }

          // Añade aquí más pruebas para otros métodos del controlador...
}
