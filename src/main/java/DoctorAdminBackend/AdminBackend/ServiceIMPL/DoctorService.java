package DoctorAdminBackend.AdminBackend.ServiceIMPL;

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

    @Override
    public Doctor getDoctorbyId(UUID id) {
        // Use Optional's orElseThrow to simplify null check
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + id));
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
