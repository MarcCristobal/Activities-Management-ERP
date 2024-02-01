/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package erp.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 *
 * @author Marc
 */
@Service
public class JsonConversionService {

    private final ObjectMapper objectMapper;
    
    public JsonConversionService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<String> toList(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {
            });
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public String toJson(List<String> list) {
        try {
            return objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            return "{}";
        }
    }
}
