package DoctorAdminBackend.AdminBackend.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;

import DoctorAdminBackend.AdminBackend.Model.Doctor;
import DoctorAdminBackend.AdminBackend.Model.PatientModel;
import DoctorAdminBackend.AdminBackend.Reposotiry.DoctorRepository;
import DoctorAdminBackend.AdminBackend.Reposotiry.PatientRepository;
import DoctorAdminBackend.AdminBackend.ServiceIMPL.DoctorService;
import DoctorAdminBackend.AdminBackend.ServiceIMPL.FileStorageService;
import DoctorAdminBackend.AdminBackend.ServiceIMPL.PatientServiceIMPL;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8084")
public class DoctorController {

    private final DoctorService doctorService;
    private final PatientServiceIMPL patientService;
    private final FileStorageService fileStorageService;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public DoctorController(
        DoctorService doctorService, 
        PatientServiceIMPL patientService,
        FileStorageService fileStorageService, 
        DoctorRepository doctorRepository, 
        PatientRepository patientRepository
    ) {
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.fileStorageService = fileStorageService;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    // 1. GET - Retrieve all doctors with patients
    @GetMapping("/doctor-with-patients")
    public ResponseEntity<Map<String, Object>> getDoctorsWithPatients() {
        List<Map<String, Object>> doctorsWithPatients = doctorService.getDoctorsWithPatients();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("message", "Doctors and their associated patients retrieved successfully");
        response.put("data", doctorsWithPatients);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/doctor/{id}")
public ResponseEntity<Map<String, Object>> getDoctorById(@PathVariable UUID id) {
    // Log a message to indicate the method is being called
    System.out.println("Fetching doctor details for ID: " + id);

    Map<String, Object> response = new LinkedHashMap<>();

    try {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + id));

        // Add success message and doctor data to the response
        response.put("status", "200");
        response.put("message", "Doctor details fetched successfully.");
        response.put("doctor", doctor);

        return ResponseEntity.ok(response);
    } catch (RuntimeException ex) {
        System.out.println("Error: " + ex.getMessage()); // Log the error message

        // Add error message to the response
        response.put("message", "Doctor not found with ID: " + id);

        return ResponseEntity.status(404).body(response); // Return 404 with the message
    }
}


    @PostMapping("/doctor")
    public ResponseEntity<Map<String, Object>> addDoctorWithImage(
            @RequestParam("doctorName") String doctorName,
            @RequestParam("specialization") String specialization,
            @RequestParam("status") Boolean status,
            @RequestParam("earnings") Double earnings,
            @RequestParam("memberSince") String memberSince,
            @RequestParam("isFeature") Boolean isFeature,
            @RequestParam("file") MultipartFile file) {
    
        // Save the image and get its URL
        String imageUrl = fileStorageService.storeFile(file);
    
        // Create and set up the doctor entity
        Doctor doctor = new Doctor();
        doctor.setDoctorName(doctorName);
        doctor.setSpecialization(specialization);
        doctor.setStatus(status);
        doctor.setEarnings(earnings);
    
        // Convert memberSince string to LocalDateTime
        LocalDateTime memberSinceDate = LocalDateTime.parse(memberSince);
        doctor.setMemberSince(memberSinceDate);
    
        doctor.setFeature(isFeature);
        doctor.setImageURL(imageUrl);
    
        // Call the service method to save the doctor entity
        Doctor savedDoctor = doctorService.savedoctor(doctor);
    
        // Create the response map
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 201);
        response.put("message", "Doctor created successfully with image");
        response.put("data", savedDoctor);
    
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    
    
   


    @PutMapping("/doctor/{doctorId}")
    public ResponseEntity<Doctor> updateDoctorDetails(
            @PathVariable UUID doctorId, 
            @RequestBody Map<String, Object> updatedDetails) {
    
        // Fetch the doctor by ID
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with ID: " + doctorId));
    
        // Update doctor details if present in the request body
        if (updatedDetails.containsKey("doctorName")) {
            doctor.setDoctorName((String) updatedDetails.get("doctorName"));
        }
        if (updatedDetails.containsKey("specialization")) {
            doctor.setSpecialization((String) updatedDetails.get("specialization"));
        }
        if (updatedDetails.containsKey("status")) {
            doctor.setStatus((Boolean) updatedDetails.get("status"));
        }
        if (updatedDetails.containsKey("earnings")) {
            doctor.setEarnings(((Number) updatedDetails.get("earnings")).doubleValue());
        }
        if (updatedDetails.containsKey("isFeature")) {
            doctor.setFeature((Boolean) updatedDetails.get("isFeature"));
        }
        if (updatedDetails.containsKey("memberSince")) {
            doctor.setMemberSince((LocalDateTime) updatedDetails.get("memberSince"));
        }
        if (updatedDetails.containsKey("imageURL")) {
            doctor.setImageURL((String) updatedDetails.get("imageURL"));
        }
    
        // Save the updated doctor details
        doctorRepository.save(doctor);
    
        // Return the updated doctor details
        return ResponseEntity.ok(doctor);
    }
    
    // 7. POST - Book an appointment (set appointment slot for a patient)
    @PostMapping("/set-appointment")
    public ResponseEntity<PatientModel> setAppointmentSlot(@RequestBody PatientModel request) {
        PatientModel updatedPatient = patientService.setAppointmentSlot(request);
        return ResponseEntity.ok(updatedPatient);
    }

    // // 8. POST - Upload image for doctor or patient
    // @PostMapping("/upload-image/{type}/{id}")
    // public ResponseEntity<Map<String, Object>> uploadImage(
    //         @PathVariable String type, 
    //         @PathVariable UUID id, 
    //         @RequestParam("file") MultipartFile file) {

    //     String imageUrl = fileStorageService.storeFile(file);

    //     if ("doctor".equalsIgnoreCase(type)) {
    //         Doctor doctor = doctorRepository.findById(id)
    //                 .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + id));
    //         doctor.setImageURL(imageUrl);
    //         doctorRepository.save(doctor);
    //     } else if ("patient".equalsIgnoreCase(type)) {
    //         PatientModel patient = patientRepository.findById(id)
    //                 .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + id));
    //         patient.setImageURL(imageUrl);
    //         patientRepository.save(patient);
    //     } else {
    //         throw new IllegalArgumentException("Invalid type specified. Must be either 'doctor' or 'patient'.");
    //     }

    //     Map<String, Object> response = new LinkedHashMap<>();
    //     response.put("status", 200);
    //     response.put("message", "Image uploaded successfully");
    //     response.put("imageUrl", imageUrl);

    //     return ResponseEntity.ok(response);
    // }

    // 9. DELETE - Remove a doctor
    @DeleteMapping("/doctor/{doctorId}")
    public ResponseEntity<Map<String, String>> deleteDoctor(@PathVariable UUID doctorId) {
        doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with ID: " + doctorId));
        doctorRepository.deleteById(doctorId);

        Map<String, String> response = new LinkedHashMap<>();
        response.put("message", "Doctor deleted successfully");
        return ResponseEntity.ok(response);
    }
}