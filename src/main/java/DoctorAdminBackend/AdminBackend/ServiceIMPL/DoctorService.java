package DoctorAdminBackend.AdminBackend.ServiceIMPL;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import DoctorAdminBackend.AdminBackend.Model.Appointment;
import DoctorAdminBackend.AdminBackend.Model.Doctor;
import DoctorAdminBackend.AdminBackend.Model.PatientModel;
import DoctorAdminBackend.AdminBackend.Reposotiry.DoctorRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class DoctorService implements DoctorAdminBackend.AdminBackend.Service.DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public Doctor savedoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }


     public Doctor updateDoctorDetails(UUID doctorId, Map<String, Object> updatedDetails) {
        // Fetch the doctor by ID or throw an exception if not found
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with ID: " + doctorId));

        // Update doctor details dynamically based on the provided request body
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
            doctor.setMemberSince(LocalDateTime.parse((String) updatedDetails.get("memberSince"))); // Assuming ISO-8601 format
        }
        if (updatedDetails.containsKey("imageURL")) {
            doctor.setImageURL((String) updatedDetails.get("imageURL"));
        }
        if (updatedDetails.containsKey("about")) {
            doctor.setabout((String) updatedDetails.get("about"));
        }
        if (updatedDetails.containsKey("experience_years")) {
            doctor.setExperienceYears((int) updatedDetails.get("experience_years"));
        }
        if (updatedDetails.containsKey("certification")) {
            doctor.setcertification((String) updatedDetails.get("certification"));
        }

        // Save the updated doctor details
        return doctorRepository.save(doctor);
    }



    
    
    public Doctor updateStatus(UUID doctorId, boolean status) {
        // Find the doctor by their ID
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with ID: " + doctorId));

        // Update the doctor's status
        doctor.setStatus(status);

        // Save the updated doctor object to the repository and return it
        return doctorRepository.save(doctor);
    }

    public Doctor updateDoctorEarnings(UUID doctorId) {
        // Find the doctor by their ID
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with ID: " + doctorId));

        // Calculate total earnings for the doctor
        double totalEarnings = calculateTotalEarnings(doctor);

        // Update the doctor's earnings
        doctor.setEarnings(totalEarnings);

        // Save the updated doctor entity
        return doctorRepository.save(doctor);
    }

    private double calculateTotalEarnings(Doctor doctor) {
        double totalEarnings = 0.0;
    
        // Iterate through the doctor's appointments and sum the paid amounts from associated patients
        for (Appointment appointment : doctor.getAppointments()) {
            PatientModel patient = appointment.getPatient();
            if (patient != null) { // Ensure patient is not null
                totalEarnings += patient.getPaid();
            }
        }
    
        return totalEarnings;
    }
    

    public Doctor updateIsFeature(UUID doctorId, boolean isFeature) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with ID: " + doctorId));
        doctor.setFeature(isFeature);
        return doctorRepository.save(doctor);
    }
}
