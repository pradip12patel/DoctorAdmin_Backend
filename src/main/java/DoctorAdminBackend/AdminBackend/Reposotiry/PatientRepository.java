package DoctorAdminBackend.AdminBackend.Reposotiry;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import DoctorAdminBackend.AdminBackend.Model.PatientModel;

@Repository
 public interface PatientRepository extends JpaRepository<PatientModel, UUID>{    

     //List<PatientModel> findByPatientNameContainingIgnoreCase(String patientName);

       Optional<PatientModel> findById(UUID id);

    
}
