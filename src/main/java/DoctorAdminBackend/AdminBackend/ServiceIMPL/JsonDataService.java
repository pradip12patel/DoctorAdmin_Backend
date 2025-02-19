package DoctorAdminBackend.AdminBackend.ServiceIMPL;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.util.Map;


@Service
public class JsonDataService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Object> readJsonData() {
        try {
            File file = new ClassPathResource("Data.json").getFile(); // Load JSON file
            
            if (file.length() == 0) { // Check if file is empty
                return Map.of("status", 500, "message", "Error: JSON file is empty");
            }

            return objectMapper.readValue(file, new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return Map.of("status", 500, "message", "Error reading data from file: " + e.getMessage());
        }
    }
}


