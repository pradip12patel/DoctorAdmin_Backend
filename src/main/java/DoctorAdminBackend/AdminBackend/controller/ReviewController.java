package DoctorAdminBackend.AdminBackend.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import DoctorAdminBackend.AdminBackend.Model.Doctor;
import DoctorAdminBackend.AdminBackend.Model.PatientModel;
import DoctorAdminBackend.AdminBackend.Model.Review;
import DoctorAdminBackend.AdminBackend.Reposotiry.DoctorRepository;
import DoctorAdminBackend.AdminBackend.Reposotiry.PatientRepository;
import DoctorAdminBackend.AdminBackend.ServiceIMPL.ReviewService;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

       private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public ReviewController(ReviewService reviewService, PatientRepository patientRepository,  DoctorRepository doctorRepository){
        this.reviewService = reviewService;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @PostMapping("/doctors/{doctorId}/patients/{patientId}")
    public ResponseEntity<Map<String, Object>> createReview(
            @PathVariable UUID doctorId,
            @PathVariable UUID patientId,
            @RequestBody Review review) {
    
        // Fetch the doctor and patient by UUID
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with ID: " + doctorId));
    
        PatientModel patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with ID: " + patientId));
    
        // Save the review using the service method
        Review savedReview = reviewService.saveReview(doctor, patient, review.getDescription(), review.getRating());
    
        // Create the response map
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Review created successfully");
    
        // Create a map for the review details
        Map<String, Object> reviewDetails = new LinkedHashMap<>();
        reviewDetails.put("id", savedReview.getId().toString());
        reviewDetails.put("description", savedReview.getDescription());
        reviewDetails.put("rating", savedReview.getRating());
        reviewDetails.put("reviewDateTime", savedReview.getReviewDateTime());
    
        // Add the doctor details
        Map<String, Object> doctorDetails = new LinkedHashMap<>();
        doctorDetails.put("doctorId", doctor.getId().toString());
        doctorDetails.put("doctorName", doctor.getDoctorName());
        reviewDetails.put("doctor", doctorDetails);
    
        // Add the patient details
        Map<String, Object> patientDetails = new LinkedHashMap<>();
        patientDetails.put("patientId", patient.getId().toString());
        patientDetails.put("patientName", patient.getPatientName());
        reviewDetails.put("patient", patientDetails);
    
        // Add the review details to the response
        response.put("data", reviewDetails);
    
        // Return the response with HTTP 201 status
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    


    // Get review by ID
    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable UUID id) {
        Review review = reviewService.getReviewById(id);
        return ResponseEntity.ok(review);
    }

    // Update review
    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(
            @PathVariable UUID id, 
            @RequestBody Review review) {
        Review updatedReview = reviewService.updateReview(id, review);
        return ResponseEntity.ok(updatedReview);
    }

    // Delete review
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable UUID id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}

