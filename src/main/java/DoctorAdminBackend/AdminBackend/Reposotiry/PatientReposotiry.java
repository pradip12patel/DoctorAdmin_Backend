package DoctorAdminBackend.AdminBackend.Reposotiry;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import DoctorAdminBackend.AdminBackend.Model.PatientModel;

@Repository
 public interface PatientReposotiry {     //extends JpaRepository<PatientModel, Long>

//     List<PatientModel> findByPatientNameContainingIgnoreCase(String patientName);


//     PatientModel findById(long id);

    
}
