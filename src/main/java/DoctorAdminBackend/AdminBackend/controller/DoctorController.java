package DoctorAdminBackend.AdminBackend.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import DoctorAdminBackend.AdminBackend.Model.Doctor;
import DoctorAdminBackend.AdminBackend.ServiceIMPL.DoctorService;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @CrossOrigin(origins = "http://localhost:8084")
    @GetMapping("/with-patients")
    public ResponseEntity<Map<String, Object>> getDoctorsWithPatients() {
        List<Map<String, Object>> doctorsWithPatients = doctorService.getDoctorsWithPatients();
    
        // Create the response map
        Map<String, Object> response = new LinkedHashMap<>(); // Preserve insertion order
        response.put("status", 200);
        response.put("message", "Doctors with associated patients retrieved successfully");
        response.put("data", doctorsWithPatients);
    
        return ResponseEntity.ok(response);
    }


    
}

