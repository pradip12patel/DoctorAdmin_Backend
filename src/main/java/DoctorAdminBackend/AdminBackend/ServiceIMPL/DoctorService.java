package DoctorAdminBackend.AdminBackend.ServiceIMPL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import DoctorAdminBackend.AdminBackend.Model.Doctor;
import DoctorAdminBackend.AdminBackend.Model.PatientModel;
import DoctorAdminBackend.AdminBackend.Reposotiry.DoctorRepository;

@Service
public class DoctorService {


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



}
