package DoctorAdminBackend.AdminBackend.Service;

import java.util.List;
import java.util.UUID;

import DoctorAdminBackend.AdminBackend.Model.Doctor;
import DoctorAdminBackend.AdminBackend.Model.PatientModel;

public interface DoctorService {

 //  List<Doctor> getAllDoctorsWithEarnings(); 

  Doctor savedoctor(Doctor doctor);

	Doctor getDoctorbyId(UUID id);
	  	

    
}
