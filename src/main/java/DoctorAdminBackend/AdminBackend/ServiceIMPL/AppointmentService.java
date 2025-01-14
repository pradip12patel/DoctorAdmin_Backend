package DoctorAdminBackend.AdminBackend.ServiceIMPL;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import DoctorAdminBackend.AdminBackend.Model.Appointment;
import DoctorAdminBackend.AdminBackend.Model.Doctor;
import DoctorAdminBackend.AdminBackend.Model.PatientModel;
import DoctorAdminBackend.AdminBackend.Reposotiry.AppointmentRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository)  {

        this.appointmentRepository = appointmentRepository;
    }

    /**
     * @return The created and saved Appointment object.
     */
    public Appointment bookAppointment(
            PatientModel patient, 
            Doctor doctor, 
            LocalDateTime appointmentDate, 
            LocalDateTime appointmentEndTime) {
        
        // Validate inputs
        if (patient == null || doctor == null) {
            throw new IllegalArgumentException("Patient or Doctor cannot be null");
        }

        if (appointmentDate == null || appointmentEndTime == null) {
            throw new IllegalArgumentException("Appointment start time and end time cannot be null");
        }

        if (appointmentEndTime.isBefore(appointmentDate) || appointmentEndTime.isEqual(appointmentDate)) {
            throw new IllegalArgumentException("Appointment end time must be after start time");
        }

        // Create a new Appointment instance
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(appointmentDate); // Set start time
        appointment.setAppointmentEndTime(appointmentEndTime); // Set end time

        appointment.setDoctorName(doctor.getDoctorName());
        appointment.setPatientName(patient.getPatientName());

        // Save the appointment to the database
        return appointmentRepository.save(appointment);
    }

    
    public Appointment getAppointmentById(UUID appointmentId) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        return appointment.orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + appointmentId));
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }


    public Appointment updateAppointment(UUID appointmentId, Appointment updatedAppointment) {
        Appointment existingAppointment = getAppointmentById(appointmentId);

        // Update the fields of the existing appointment
        existingAppointment.setPatient(updatedAppointment.getPatient());
        existingAppointment.setDoctor(updatedAppointment.getDoctor());
        existingAppointment.setAppointmentDate(updatedAppointment.getAppointmentDate());
        existingAppointment.setAppointmentEndTime(updatedAppointment.getAppointmentEndTime());

        existingAppointment.setDoctorName(updatedAppointment.getDoctor().getDoctorName());
        existingAppointment.setPatientName(updatedAppointment.getPatient().getPatientName());

        // Save the updated appointment
        return appointmentRepository.save(existingAppointment);
    }

    
    public void deleteAppointment(UUID appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);
        appointmentRepository.delete(appointment);
    }

   
    public String getFormattedAppointmentDetails(Appointment appointment) {
        if (appointment.getAppointmentDate() == null || appointment.getAppointmentEndTime() == null) {
            throw new IllegalArgumentException("Appointment start time or end time cannot be null");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy, hh:mm a");
        return appointment.getAppointmentDate().format(formatter) 
                + " - " 
                + appointment.getAppointmentEndTime().format(formatter);
    }

    
}
