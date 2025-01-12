package DoctorAdminBackend.AdminBackend.Reposotiry;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import DoctorAdminBackend.AdminBackend.Model.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

  

}



