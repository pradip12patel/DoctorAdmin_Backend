package DoctorAdminBackend.AdminBackend.controller;


import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;

import DoctorAdminBackend.AdminBackend.Model.Doctor;
import DoctorAdminBackend.AdminBackend.Model.PatientModel;
import DoctorAdminBackend.AdminBackend.Reposotiry.DoctorRepository;
import DoctorAdminBackend.AdminBackend.Reposotiry.PatientRepository;
import DoctorAdminBackend.AdminBackend.ServiceIMPL.DoctorDetailData;
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
    private final DoctorDetailData doctorDetailData;

    @Autowired
    public DoctorController(
        DoctorService doctorService, 
        PatientServiceIMPL patientService,
        FileStorageService fileStorageService, 
        DoctorRepository doctorRepository, 
        PatientRepository patientRepository,
        DoctorDetailData doctorDetailData
    ) {
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.fileStorageService = fileStorageService;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.doctorDetailData= doctorDetailData;
    }

    
    @GetMapping("/doctor-with-patients")
    public ResponseEntity<Map<String, Object>> getDoctorsWithPatients() {
        List<Map<String, Object>> doctorsWithPatients = doctorDetailData.getDoctorsWithPatients();
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("message", "Doctors and their associated patients retrieved successfully");
        response.put("data", doctorsWithPatients);
    
        savedatajson(response);
    
        // Return the response
        return ResponseEntity.ok(response);
    }
    
    void savedatajson(Map<String, Object> JsonData) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); 
    
        try {
            File savefile = new File("jsondata.json");
            mapper.writeValue(savefile, JsonData);
            System.out.println("-----JSON data saved successfully-----");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    

    @GetMapping("/doctor/{id}")
    public ResponseEntity<Map<String, Object>> getDoctorById(@PathVariable UUID id) {
    
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
        System.out.println("Error: " + ex.getMessage()); 

        response.put("message", "Doctor not found with ID: " + id);

        return ResponseEntity.status(404).body(response); 
    }
}

   @PostMapping(value = "/doctor")
   public ResponseEntity<Map<String, Object>> addDoctorWithImage(
        @RequestParam("file") MultipartFile file,
        @RequestParam("doctorName") String doctorName,
        @RequestParam("specialization") String specialization,
        @RequestParam("status") Boolean status,
        @RequestParam("experience_years") int experienceYears,
        @RequestParam("earnings") Double earnings,
        @RequestParam("memberSince") String memberSince,
        @RequestParam("isFeature") Boolean isFeature,
        @RequestParam("about") String about,
        @RequestParam("certification") String certification,
        @RequestParam("email") String email,
        @RequestParam("address") String addressjson) {


    // Create Doctor entity
    Doctor doctor = new Doctor();
    doctor.setDoctorName(doctorName);
    doctor.setSpecialization(specialization);
    doctor.setStatus(status);
    doctor.setEmail(email);
    doctor.setEarnings(earnings);
    doctor.setExperienceYears(experienceYears);
    doctor.setabout(about);
    doctor.setcertification(certification);
    doctor.setImageURL(fileStorageService.storeFile(file));
    doctor.setFeature(isFeature);
    doctor.setAddress(addressjson);

    try {
        doctor.setMemberSince(LocalDateTime.parse(memberSince));
    } catch (DateTimeParseException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Invalid date format for memberSince. Use 'yyyy-MM-ddTHH:mm:ss' format."));
    }

    // Save the doctor entity
    Doctor savedDoctor = doctorService.savedoctor(doctor);

    Map<String, Object> response = new LinkedHashMap<>();
    response.put("status", 201);
    response.put("message", "Doctor created successfully with image");
    response.put("data", savedDoctor);

    return new ResponseEntity<>(response, HttpStatus.CREATED);
}


    
    // 7. POST - Book an appointment (set appointment slot for a patient)
    @PostMapping("/set-appointment")
    public ResponseEntity<PatientModel> setAppointmentSlot(@RequestBody PatientModel request) {
        PatientModel updatedPatient = patientService.setAppointmentSlot(request);
        return ResponseEntity.ok(updatedPatient);
    }

    // 8. POST - Upload image for doctor or patient
    @PostMapping("/upload-image/{type}/{id}")
    public ResponseEntity<Map<String, Object>> uploadImage(
            @PathVariable String type, 
            @PathVariable UUID id, 
            @RequestParam("file") MultipartFile file) {

        String imageUrl = fileStorageService.storeFile(file);

        if ("doctor".equalsIgnoreCase(type)) {
            Doctor doctor = doctorRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + id));
            doctor.setImageURL(imageUrl);
            doctorRepository.save(doctor);
        } else if ("patient".equalsIgnoreCase(type)) {
            PatientModel patient = patientRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + id));
            patient.setImageURL(imageUrl);
            patientRepository.save(patient);
        } else {
            throw new IllegalArgumentException("Invalid type specified. Must be either 'doctor' or 'patient'.");
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("message", "Image uploaded successfully");
        response.put("imageUrl", imageUrl);

        return ResponseEntity.ok(response);
    }

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