package DoctorAdminBackend.AdminBackend.ServiceIMPL;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import DoctorAdminBackend.AdminBackend.Model.Appointment;
import DoctorAdminBackend.AdminBackend.Model.Doctor;
import DoctorAdminBackend.AdminBackend.Model.PatientModel;
import DoctorAdminBackend.AdminBackend.Reposotiry.DoctorRepository;

public class DoctorWithPatientsAndAppointments {

    private final DoctorRepository doctorRepository;

    public DoctorWithPatientsAndAppointments(DoctorRepository doctorRepository) {
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
            doctorData.put("experience_years", doctor.getExperienceYears());
            doctorData.put("ImageUrl", doctor.getImageURL());
            doctorData.put("isFeature", doctor.isFeature());
    
            // Ensure patients are properly fetched
            List<PatientModel> patientsList = doctor.getPatients(); 
            if (patientsList == null || patientsList.isEmpty()) {
                doctorData.put("patients", new ArrayList<>()); // Empty patients list
                result.add(doctorData);
                continue;
            }
    
            // Map patients associated with the doctor
            List<Map<String, Object>> patients = new ArrayList<>();
            for (PatientModel patient : patientsList) {
                Map<String, Object> patientData = new LinkedHashMap<>();
                patientData.put("patientId", patient.getId().toString());
                patientData.put("patientName", patient.getPatientName());
                patientData.put("age", patient.getAge());
                patientData.put("address", patient.getAddress());
                patientData.put("phone", patient.getPhone());
                patientData.put("lastVisit", patient.getLastVisit());
                patientData.put("paid", patient.getPaid());
                patientData.put("ImageUrl", patient.getImageURL());
    
                // Ensure appointments are properly fetched
                List<Appointment> appointmentList = patient.getAppointments();
                List<Map<String, Object>> appointments = new ArrayList<>();
                if (appointmentList != null) {
                    for (Appointment appointment : appointmentList) {
                        Map<String, Object> appointmentData = new LinkedHashMap<>();
                        appointmentData.put("appointmentId", appointment.getId().toString());
                        appointmentData.put("appointmentSlot", appointment.getFormattedAppointmentDate());
                        appointments.add(appointmentData);
                    }
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
    
}
