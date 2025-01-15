package DoctorAdminBackend.AdminBackend.Reposotiry;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import DoctorAdminBackend.AdminBackend.Model.Appointment;
import DoctorAdminBackend.AdminBackend.Model.Doctor;
import DoctorAdminBackend.AdminBackend.Model.PatientModel;
import DoctorAdminBackend.AdminBackend.Model.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID>{

     List<Review> findByDoctorId(UUID doctorId);
     List<Review> findByPatientId(UUID patientId);

     List<Review> findByDoctorAndPatient(Doctor doctor, PatientModel patient);

     
    
}
