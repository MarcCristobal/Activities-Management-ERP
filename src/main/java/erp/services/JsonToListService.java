/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package erp.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

/**
 *
 * @author Marc
 */
@Service
public class JsonToListService {
    
    public ArrayList<String> toList(String json){
        
        ObjectMapper objectMapper = new ObjectMapper();
        
        ArrayList<String> list = new ArrayList<>();
        
        try {
            // Convierte la cadena JSON a un ArrayList<String>
            list = objectMapper.readValue(json, new TypeReference<ArrayList<String>>() {});

        } catch (IOException e) {
            return list;
        }
        
        return list;
    }
    
}
