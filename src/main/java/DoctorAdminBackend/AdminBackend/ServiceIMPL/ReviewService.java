package DoctorAdminBackend.AdminBackend.ServiceIMPL;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import DoctorAdminBackend.AdminBackend.Model.Doctor;
import DoctorAdminBackend.AdminBackend.Model.PatientModel;
import DoctorAdminBackend.AdminBackend.Model.Review;
import DoctorAdminBackend.AdminBackend.Reposotiry.DoctorRepository;
import DoctorAdminBackend.AdminBackend.Reposotiry.PatientRepository;
import DoctorAdminBackend.AdminBackend.Reposotiry.ReviewRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository,
                         DoctorRepository doctorRepository,
                         PatientRepository patientRepository) {
        this.reviewRepository = reviewRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

  // Save review with Doctor and Patient objects
  public Review saveReview(Doctor doctor, PatientModel patient, String description, double rating) {

    // Create a new Review and set the details
    Review review = new Review();
    review.setDoctor(doctor);  // Set the provided Doctor
    review.setPatient(patient);  // Set the provided Patient
    review.setReviewDateTime(LocalDateTime.now());  // Set the current date and time
    review.setDescription(description);  // Set the description
    review.setRating(rating);  // Set the rating

    // Save the review to the database
    return reviewRepository.save(review);
}
    

    // Get review by ID
    public Review getReviewById(UUID id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with ID: " + id));
    }

    // Get all reviews
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    // Update review
    public Review updateReview(UUID id, Review review) {
        Review existingReview = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with ID: " + id));

        existingReview.setRating(review.getRating());
        existingReview.setDescription(review.getDescription());
        existingReview.setReviewDateTime(LocalDateTime.now());
        existingReview.setDoctor(review.getDoctor());
        existingReview.setPatient(review.getPatient());

        return reviewRepository.save(existingReview);
    }

    // Delete review
    public void deleteReview(UUID id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with ID: " + id));
        reviewRepository.delete(review);
    }
}
