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

    public List<Map<String, Object>> getDoctorsWithPatients() {
        List<Doctor> doctors = doctorRepository.findAll(); // Fetch all doctors with their patients
    
        List<Map<String, Object>> result = new ArrayList<>();
        for (Doctor doctor : doctors) {
            Map<String, Object> doctorData = new LinkedHashMap<>();
            doctorData.put("doctorId", doctor.getId().toString());
            doctorData.put("doctorName", doctor.getDoctorName());
            doctorData.put("specialization", doctor.getSpecialization());
            doctorData.put("memberSince", doctor.getMemberSince());
            doctorData.put("earnings", doctor.getEarnings());
            doctorData.put("status", doctor.getStatus());
            doctorData.put("experience_years", doctor.getExperienceYears());
            doctorData.put("ImageUrl", doctor.getImageURL());
            doctorData.put("isFeature", doctor.isFeature());
    
            // Map patients associated with the doctor
            List<Map<String, Object>> patients = new ArrayList<>();
            for (PatientModel patient : doctor.getPatients()) {
                Map<String, Object> patientData = new LinkedHashMap<>();
                patientData.put("patientId", patient.getId().toString());
                patientData.put("patientName", patient.getPatientName());
                patientData.put("age", patient.getAge());
                patientData.put("address", patient.getAddress());
                patientData.put("phone", patient.getPhone());
                patientData.put("lastVisit", patient.getLastVisit());
                patientData.put("paid", patient.getPaid());
                patientData.put("ImageUrl", patient.getImageURL());
    
                // Map associated appointments for each patient
                List<Map<String, Object>> appointments = new ArrayList<>();
                for (Appointment appointment : patient.getAppointments()) {
                    Map<String, Object> appointmentData = new LinkedHashMap<>();
                    appointmentData.put("appointmentId", appointment.getId().toString());
                appointmentData.put("AppointmentSlot", appointment.getFormattedAppointmentDate());
                    appointments.add(appointmentData);
                }
    
                // Add appointments to patient data
                patientData.put("appointments", appointments);
                patients.add(patientData);
            }
    
            // Add patients to doctor data
            doctorData.put("patients", patients);
    
            result.add(doctorData); // Add doctor to result
        }
    
        return result;
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

        // Iterate through the doctor's patients and sum their paid amounts
        for (PatientModel patient : doctor.getPatients()) {
            totalEarnings += patient.getPaid();
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
