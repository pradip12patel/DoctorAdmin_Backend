package DoctorAdminBackend.AdminBackend.ServiceIMPL;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import DoctorAdminBackend.AdminBackend.Model.Appointment;
import DoctorAdminBackend.AdminBackend.Model.Doctor;
import DoctorAdminBackend.AdminBackend.Model.PatientModel;
import DoctorAdminBackend.AdminBackend.Model.Review;
import DoctorAdminBackend.AdminBackend.Reposotiry.DoctorRepository;
import DoctorAdminBackend.AdminBackend.Reposotiry.ReviewRepository;

@Service
public class DoctorDetailData {

    private final DoctorRepository doctorRepository;
    private final ReviewRepository reviewRepository;

    public DoctorDetailData(DoctorRepository doctorRepository, ReviewRepository reviewRepository) {
        this.doctorRepository = doctorRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<Map<String, Object>> getDoctorsWithPatients() {
        List<Doctor> doctors = doctorRepository.findAll();

    List<Map<String, Object>> result = new ArrayList<>();
        for (Doctor doctor : doctors) {
            Map<String, Object> doctorData = new LinkedHashMap<>();
            doctorData.put("doctorId", doctor.getId().toString());
            doctorData.put("doctorName", doctor.getDoctorName());
            doctorData.put("specialization", doctor.getSpecialization());
            doctorData.put("memberSince", doctor.getMemberSince());
            doctorData.put("earnings", doctor.getEarnings());
            doctorData.put("address", doctor.getAddress());
            doctorData.put("status", doctor.getStatus());
            doctorData.put("email", doctor.getEmail());
            doctorData.put("certification", doctor.getcertification());
            doctorData.put("about", doctor.getabout());
            doctorData.put("experience_years", doctor.getExperienceYears());
            doctorData.put("ImageUrl", doctor.getImageURL());
            doctorData.put("isFeature", doctor.isFeature());

            // Map patients associated with the doctor through appointments
            List<Map<String, Object>> patients = new ArrayList<>();
            for (Appointment appointment : doctor.getAppointments()) {
                PatientModel patient = appointment.getPatient();

                if (patient != null) { // Ensure patient is not null
                    Map<String, Object> patientData = new LinkedHashMap<>();
                    patientData.put("patientId", patient.getId().toString());
                    patientData.put("patientName", patient.getPatientName());
                    patientData.put("age", patient.getAge());
                    patientData.put("address", patient.getAddress());
                    patientData.put("phone", patient.getPhone());
                    patientData.put("lastVisit", patient.getLastVisit());
                    patientData.put("paid", patient.getPaid());
                    patientData.put("ImageUrl", patient.getImageURL());

                    // Filter appointments for the specific doctor and patient
                    List<Map<String, Object>> filteredAppointments = new ArrayList<>();
                    for (Appointment patientAppointment : patient.getAppointments()) {
                        if (patientAppointment.getDoctor().getId().equals(doctor.getId())) {
                            Map<String, Object> appointmentData = new LinkedHashMap<>();
                            appointmentData.put("appointmentId", patientAppointment.getId().toString());
                            appointmentData.put("appointmentSlot", patientAppointment.getFormattedAppointmentDate());
                            filteredAppointments.add(appointmentData);
                        }
                    }

                    patientData.put("appointments", filteredAppointments);

                    // Fetch reviews for the specific doctor-patient relationship
                    List<Map<String, Object>> reviews = new ArrayList<>();
                    List<Review> doctorReviews = reviewRepository.findByDoctorAndPatient(doctor, patient);
                    for (Review review : doctorReviews) {
                        Map<String, Object> reviewData = new LinkedHashMap<>();
                        reviewData.put("id", review.getId().toString());
                        reviewData.put("Description", review.getDescription());
                        reviewData.put("rating", review.getRating());
                        reviews.add(reviewData);
                    }

                    patientData.put("reviews", reviews); // Add reviews to the patient data
                    patients.add(patientData);
                }
            }

            doctorData.put("patients", patients);
            result.add(doctorData);
        }

        return result;
    }
    
}
