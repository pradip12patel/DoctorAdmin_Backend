package DoctorAdminBackend.AdminBackend.ServiceIMPL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import DoctorAdminBackend.AdminBackend.Model.Doctor;
import DoctorAdminBackend.AdminBackend.Model.PatientModel;
import DoctorAdminBackend.AdminBackend.Reposotiry.DoctorRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class DoctorService implements DoctorAdminBackend.AdminBackend.Service.DoctorService{


    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
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
        System.out.println("Doctor Image URL: " + doctor.getImageURL());
        doctorData.put("ImageUrl", doctor.getImageURL());

        // Map associated patients
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
            patientData.put("ApointmentSlot", patient.getApointmentSlot());
            System.out.println("Patient Image URL: " + patient.getImageURL());
            patientData.put("ImageUrl", patient.getImageURL());
            patients.add(patientData);
        }

        doctorData.put("patients", patients); // Add patients to doctor
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


    // Method to update earnings for a specific doctor
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




}
