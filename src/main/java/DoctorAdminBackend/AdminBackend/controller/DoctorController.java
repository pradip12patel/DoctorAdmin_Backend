package DoctorAdminBackend.AdminBackend.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CrossOrigin;
import DoctorAdminBackend.AdminBackend.Model.Doctor;
import DoctorAdminBackend.AdminBackend.Model.PatientModel;
import DoctorAdminBackend.AdminBackend.Reposotiry.DoctorRepository;
import DoctorAdminBackend.AdminBackend.Reposotiry.PatientRepository;
import DoctorAdminBackend.AdminBackend.ServiceIMPL.DoctorService;
import DoctorAdminBackend.AdminBackend.ServiceIMPL.FileStorageService;
import DoctorAdminBackend.AdminBackend.ServiceIMPL.PatientServiceIMPL;

@RestController
@RequestMapping("/api")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @CrossOrigin(origins = "http://localhost:8084")
    @GetMapping("/doctor-with-patients")
    public ResponseEntity<Map<String, Object>> getDoctorsWithPatients() {
        List<Map<String, Object>> doctorsWithPatients = doctorService.getDoctorsWithPatients();
    
        // Create the response map
        Map<String, Object> response = new LinkedHashMap<>(); // Preserve insertion order
        response.put("status", 200);
        response.put("message", "Doctors with associated patients retrieved successfully");
        response.put("data", doctorsWithPatients);
    
        return ResponseEntity.ok(response);
    }
        
     @Autowired
    private  PatientServiceIMPL patientService;
       
    public DoctorController(PatientServiceIMPL patientService)  {

          this.patientService = patientService;
    }

    
    
    @CrossOrigin(origins = "http://localhost:8084")
    @PostMapping("/set-apointment")
    public ResponseEntity<PatientModel> setAppointmentSlot(@RequestBody PatientModel request) {
        PatientModel updatedPatient = patientService.setAppointmentSlot(request);
        return ResponseEntity.ok(updatedPatient);
    }
       
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final FileStorageService fileStorageService;
    
    @Autowired
    public DoctorController(FileStorageService fileStorageService, 
                            DoctorRepository doctorRepository, 
                            PatientRepository patientRepository) {
        this.fileStorageService = fileStorageService;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }
    
    @CrossOrigin(origins = "http://localhost:8084")
    @PostMapping("/upload-image/{type}/{id}")
    public ResponseEntity<Map<String, Object>> uploadImage(
            @PathVariable String type, 
            @PathVariable UUID id, // Changed type to UUID
            @RequestParam("file") MultipartFile file) {
    
        // Store the file and get the URL
        String imageUrl = fileStorageService.storeFile(file); 
    
        // Handle doctor type
        if ("doctor".equalsIgnoreCase(type)) {
            Optional<Doctor> optionalDoctor = doctorRepository.findById(id); // Fetch doctor by ID
            if (optionalDoctor.isPresent()) { 
                Doctor doctor = optionalDoctor.get();
                doctor.setImageURL(imageUrl); // Set the image URL
                doctorRepository.save(doctor); // Save updated doctor
            } else {
                throw new RuntimeException("Doctor not found with ID: " + id); // Handle doctor not found
            }
        } 
        // Handle patient type
        else if ("patient".equalsIgnoreCase(type)) {
            Optional<PatientModel> optionalPatient = patientRepository.findById(id); // Fetch patient by ID
            if (optionalPatient.isPresent()) {
                PatientModel patient = optionalPatient.get();
                patient.setImageURL(imageUrl); // Set the image URL
                patientRepository.save(patient); // Save updated patient
            } else {
                throw new RuntimeException("Patient not found with ID: " + id); // Handle patient not found
            }
        } 
        // Handle invalid type
        else {
            throw new IllegalArgumentException("Invalid type specified. Must be either 'doctor' or 'patient'.");
        }
    
        // Create response map
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("message", "Image uploaded successfully");
        response.put("imageUrl", imageUrl);
    
        return ResponseEntity.ok(response);
    }
    


    
}

