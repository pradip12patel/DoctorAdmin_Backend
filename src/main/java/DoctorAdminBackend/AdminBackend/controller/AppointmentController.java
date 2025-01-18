package DoctorAdminBackend.AdminBackend.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@CrossOrigin(origins = "http://localhost:8084")
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

        // Prepare the response map with the message at the top using LinkedHashMap
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "200");
        response.put("message", "Appointment booked successfully");

        // Add doctor details in a serial format
        List<Map<String, Object>> doctorDetails = new ArrayList<>();
        Map<String, Object> doctorInfo = new LinkedHashMap<>();
        doctorInfo.put("doctorId", doctor.getId());
        doctorInfo.put("doctorName", doctor.getDoctorName());
        doctorDetails.add(doctorInfo);
        response.put("doctor", doctorDetails);

        // Add patient details with their appointments inside the patient array
        List<Map<String, Object>> patientAppointments = new ArrayList<>();
        Map<String, Object> patientInfo = new LinkedHashMap<>();
        patientInfo.put("patientId", patient.getId());
        patientInfo.put("patientName", patient.getPatientName());

        // Collect appointments for the patient
        List<Map<String, Object>> appointments = new ArrayList<>();
        for (Appointment app : doctor.getAppointments()) {
            Map<String, Object> appointmentDetails = new LinkedHashMap<>();
            appointmentDetails.put("appointmentId", app.getId());
            appointmentDetails.put("appointmentDate", app.getFormattedAppointmentDate()); // Assuming you have a `getFormattedAppointmentDate` method
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

    
      //Get all appointments.
     
     @GetMapping("/getallappointments")
     public ResponseEntity<List<Map<String, Object>>> getAllAppointments() {
    
    List<PatientModel> patients = patientRepository.findAll();
    
    List<Map<String, Object>> response = new ArrayList<>();

    for (PatientModel patient : patients) {
        // Create a map for each patient
        Map<String, Object> patientInfo = new LinkedHashMap<>();
        patientInfo.put("patientId", patient.getId());
        patientInfo.put("patientName", patient.getPatientName());

         // Prepare the response map with the message at the top using LinkedHashMap
         Map<String, Object> responses = new LinkedHashMap<>();
         responses.put("status", "200");
         responses.put("message", "Appointments retrived successfully");
        
        // List to hold appointments of the current patient
        List<Map<String, Object>> patientAppointments = new ArrayList<>();

        // Add appointments to the patient details
        for (Appointment appointment : patient.getAppointments()) {
            Map<String, Object> appointmentDetails = new LinkedHashMap<>();
            appointmentDetails.put("appointmentId", appointment.getId());
            appointmentDetails.put("appointmentDate", appointment.getFormattedAppointmentDate());
            appointmentDetails.put("appointmentEndTime", appointment.getAppointmentEndTime());
            patientAppointments.add(appointmentDetails);
        }
        
        // Add the appointments list to the patient info
        patientInfo.put("appointments", patientAppointments);

        // Add the patient info to the response
        response.add(patientInfo);
    }

    // Return the response containing all patients and their appointments
    return ResponseEntity.ok(response);
}

@GetMapping("/{appointmentId}")
public ResponseEntity<Map<String, Object>> getAppointmentById(@PathVariable UUID appointmentId) {
    try {
        // Fetch the appointment by ID
        Appointment appointment = appointmentService.getAppointmentById(appointmentId);
        
        if (appointment == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        // Fetch the associated patient
        PatientModel patient = appointment.getPatient();


         // Prepare the response map with the message at the top using LinkedHashMap
         Map<String, Object> response1 = new LinkedHashMap<>();
         response1.put("status", "200");
         response1.put("message", "Appointment retrived successfully");

        // Prepare the response
        Map<String, Object> response = new LinkedHashMap<>();

        // Add appointment details to the response
        Map<String, Object> appointmentDetails = new LinkedHashMap<>();
        appointmentDetails.put("appointmentId", appointment.getId());
        appointmentDetails.put("appointmentDate", appointment.getFormattedAppointmentDate());
      //  appointmentDetails.put("appointmentEndTime", appointment.getAppointmentEndTime());

        // Add patient details to the response
        Map<String, Object> patientDetails = new LinkedHashMap<>();
        patientDetails.put("patientId", patient.getId());
        patientDetails.put("patientName", patient.getPatientName());

        // Combine the details into a single response
        response.put("patient", patientDetails);
        response.put("appointment", appointmentDetails);

        return new ResponseEntity<>(response, HttpStatus.OK);

    } catch (IllegalArgumentException ex) {
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}


   
@PutMapping("/{appointmentId}")
public ResponseEntity<Map<String, Object>> updateAppointment(
        @PathVariable UUID appointmentId,
        @RequestBody Appointment updatedAppointment) {
    try {
        // Call service to update the appointment
        Appointment appointment = appointmentService.updateAppointment(appointmentId, updatedAppointment);

        // Prepare the response map
        Map<String, Object> response = new LinkedHashMap<>();

        // Add success message at the top of the response body
        response.put("status", "200");
        response.put("message", "Appointment updated successfully");
        

        // Add the updated appointment details to the response
        Map<String, Object> appointmentDetails = new LinkedHashMap<>();
        appointmentDetails.put("appointmentId", appointment.getId());
        appointmentDetails.put("appointmentDate", appointment.getFormattedAppointmentDate());
        appointmentDetails.put("appointmentEndTime", appointment.getAppointmentEndTime());

        response.put("appointment", appointmentDetails);

        // Return response with status 200 OK
        return new ResponseEntity<>(response, HttpStatus.OK);

    } catch (IllegalArgumentException ex) {
        // Handle error case where appointment is not found or update fails
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "400");
        response.put("message", "Appointment not found or invalid data");
        

        // Return response with BAD_REQUEST status
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}


    
    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<Map<String, Object>> deleteAppointment(@PathVariable UUID appointmentId) {
    try {
        // Attempt to delete the appointment
        appointmentService.deleteAppointment(appointmentId);

        // Prepare the response map
        Map<String, Object> response = new LinkedHashMap<>();
        
        // Add success message at the top of the response body
        response.put("status", "200");
        response.put("message", "Deleted successfully");
        

        // Return response with a NO_CONTENT status and the custom message
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    } catch (IllegalArgumentException ex) {
        // Handle the case when appointment is not found
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "404");
        response.put("message", "Appointment not found");
       

        // Return response with NOT_FOUND status and the error message
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}

}
