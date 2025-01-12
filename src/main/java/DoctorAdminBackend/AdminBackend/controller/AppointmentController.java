package DoctorAdminBackend.AdminBackend.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import DoctorAdminBackend.AdminBackend.Model.Appointment;
import DoctorAdminBackend.AdminBackend.Model.Doctor;
import DoctorAdminBackend.AdminBackend.Model.PatientModel;
import DoctorAdminBackend.AdminBackend.ServiceIMPL.AppointmentService;
import DoctorAdminBackend.AdminBackend.Reposotiry.DoctorRepository;
import DoctorAdminBackend.AdminBackend.Reposotiry.PatientRepository;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @PostMapping("/book")
    public ResponseEntity<Object> bookAppointment(@RequestBody Appointment request) {

        // Ensure patient, doctor, appointment date, and appointment end time are not null
        if (request.getPatient() == null || request.getDoctor() == null || request.getAppointmentDate() == null || request.getAppointmentEndTime() == null) {
            throw new IllegalArgumentException("Patient, Doctor, Appointment Date, and Appointment End Time must not be null");
        }

        // Fetch the patient and doctor from the repository
        PatientModel patient = fetchPatientById(request.getPatient().getId());
        Doctor doctor = fetchDoctorById(request.getDoctor().getId());

        if (patient == null || doctor == null) {
            throw new IllegalArgumentException("Patient or Doctor not found");
        }

        // Call the service to book the appointment
        Appointment appointment = appointmentService.bookAppointment(patient, doctor, request.getAppointmentDate(), request.getAppointmentEndTime());

        // Custom message indicating success
        String message = "Appointment booked successfully";
        String status = "200";

        // Prepare the response map with the message at the top using LinkedHashMap
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status",status);
        response.put("message", message);

        // Add doctor details in a serial format
        List<Map<String, Object>> doctorDetails = new ArrayList<>();
        Map<String, Object> doctorInfo = new LinkedHashMap<>();
        doctorInfo.put("doctorId", doctor.getId());
        doctorInfo.put("doctorName", doctor.getDoctorName()); // Assuming Doctor has getDoctorName() method
        doctorDetails.add(doctorInfo);
        response.put("doctor", doctorDetails);

        // Add patient details with their appointments inside the patient array
        List<Map<String, Object>> patientAppointments = new ArrayList<>();
        Map<String, Object> patientInfo = new LinkedHashMap<>();
        patientInfo.put("patientId", patient.getId()); // Assuming Patient has getId() method
        patientInfo.put("patientName", patient.getPatientName()); // Assuming Patient has getPatientName() method
        
        // Collect appointments for the patient
        List<Map<String, Object>> appointments = new ArrayList<>();
        for (Appointment app : doctor.getAppointments()) {
            Map<String, Object> appointmentDetails = new LinkedHashMap<>();
            appointmentDetails.put("appointmentId", app.getId()); // Appointment ID
            appointmentDetails.put("appointmentDate", app.getFormattedAppointmentDate()); // Formatted Appointment Date
            appointments.add(appointmentDetails);
        }
        patientInfo.put("appointments", appointments);
        patientAppointments.add(patientInfo);

        // Add patient details to the response
        response.put("patients", patientAppointments);

        // Return the response with status 201 Created and the full details
        return ResponseEntity.status(201).body(response);
    }

   
    private PatientModel fetchPatientById(UUID id) {
        return patientRepository.findById(id).orElse(null); // Fetch patient from the database
    }

    
    private Doctor fetchDoctorById(UUID id) {
        return doctorRepository.findById(id).orElse(null); // Fetch doctor from the database
    }
}


