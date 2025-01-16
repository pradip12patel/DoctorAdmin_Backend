package DoctorAdminBackend.AdminBackend.Reposotiry;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import DoctorAdminBackend.AdminBackend.Model.Doctor;


@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

   @Query(value = "SELECT " +
        "d.id AS doctorId, " +
        "d.doctor_name AS doctorName, " +
        "d.specialization AS specialization, " +
        "d.member_since AS memberSince, " +
        "d.earnings AS earnings, " +
        "d.status AS doctorStatus, " +
        "d.about AS About, " +
        "d.certification AS certification, " +
        "d.imageurl AS imageurlDoctor, " +
        "d.experience_years AS experience_years, " +
        "a.appointment_start_time AS AppointmentStart, " +
        "a.appointment_end_time AS AppointmentEnd, " +
        "a.id AS appointmentID, " +
        "p.id AS patientId, " +
        "p.patient_name AS patientName, " +
        "p.age AS patientAge, " +
        "p.address AS patientAddress, " +
        "p.phone AS patientPhone, " +
        "p.last_visit AS lastVisit, " +
        "p.paid AS patientPaid, " +
        "p.imageurl AS imageurlPatient, " +
        "r.id AS reviewId, " +
        "r.description AS reviewContent, " +
        "r.rating AS reviewRating, " +
        "r.review_date_time AS ReviewDateTime " +
        "FROM doctors d " +
        "LEFT JOIN appointments a ON d.id = a.doctor_id " +
        "LEFT JOIN patients p ON a.patient_id = p.id " +
        "LEFT JOIN reviews r ON r.doctor_id = d.id AND r.patient_id = p.id",
        nativeQuery = true)

   List<Object[]> findAllDoctorsWithPatients();
   Optional<Doctor> findById(UUID id);
   List<Doctor> findByIsFeature(boolean isFeature);

}
