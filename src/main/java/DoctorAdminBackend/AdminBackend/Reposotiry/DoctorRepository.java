package DoctorAdminBackend.AdminBackend.Reposotiry;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import DoctorAdminBackend.AdminBackend.Model.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Query(value = "SELECT d.*, p.* FROM doctors d LEFT JOIN patients p ON d.doctor_id = p.doctor_id", nativeQuery = true)
    List<Doctor> findAllDoctorsWithPatients();
}
