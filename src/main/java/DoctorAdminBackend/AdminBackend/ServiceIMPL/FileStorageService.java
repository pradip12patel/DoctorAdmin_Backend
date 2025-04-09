package DoctorAdminBackend.AdminBackend.ServiceIMPL;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {


    private final String uploadDir = "/uploads/"; 

    public FileStorageService() {
        // Ensure the upload directory exists
        Path uploadPath = Paths.get(uploadDir);
        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage directory", e);
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();

        // Ensure that the filename is clean and does not start with a slash
        if (fileName != null && fileName.startsWith("/")) {
            fileName = fileName.substring(1); 
        }

        String uniqueFileName = fileName;

        try {
            Path filePath = Paths.get(uploadDir).resolve(uniqueFileName);
    
            // Copy file, replace if it already exists
            Files.copy(file.getInputStream(), filePath);

            String imageUrl = filePath.toString().replace("\\", "/"); 
    
            // Return a URL-friendly path
            return "http://localhost:8086" + imageUrl;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + uniqueFileName, e);
        }
    }

}

