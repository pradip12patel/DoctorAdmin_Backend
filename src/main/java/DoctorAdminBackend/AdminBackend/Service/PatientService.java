package DoctorAdminBackend.AdminBackend.Service;

import java.util.List;
import java.util.UUID;

import DoctorAdminBackend.AdminBackend.Model.PatientModel;

public interface PatientService {

    PatientModel savePatient(PatientModel Patient);
	  
	    List<PatientModel> savesPatient(PatientModel[] newPatient);
		
        List<PatientModel> getAllPatients();

		PatientModel getPatientbyID(UUID id);
		
		PatientModel updatePatient(PatientModel pro, UUID id);
		
		void deletePatient(UUID id);

        
		//  boolean existsById(long id);
	   
        //  PatientModel findById(long id);
 
        //  PatientModel save(PatientModel Patient);
    
}
