package DoctorAdminBackend.AdminBackend.ServiceIMPL;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    /**
     * Books an appointment with both start and end time.
     
     * @param patient              The patient associated with the appointment.
     * @param doctor               The doctor associated with the appointment.
     * @param appointmentDate      The start time of the appointment.
     * @param appointmentEndTime   The end time of the appointment.
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

        // Save the appointment to the database
        return appointmentRepository.save(appointment);
    }

    /**
     * Formats the appointment date and time for a given appointment.
     *
     * @param appointment The appointment object to format.
     * @return A formatted string of the appointment date and time.
     */
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
