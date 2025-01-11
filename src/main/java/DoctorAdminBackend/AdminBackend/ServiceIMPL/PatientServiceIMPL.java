package DoctorAdminBackend.AdminBackend.ServiceIMPL;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import DoctorAdminBackend.AdminBackend.Model.PatientModel;
import DoctorAdminBackend.AdminBackend.Reposotiry.PatientRepository;
import DoctorAdminBackend.AdminBackend.Service.PatientService;

@Service
public class PatientServiceIMPL implements PatientService{

     @Autowired
    public PatientRepository patientRepository;

   // Constructor injection
    public PatientServiceIMPL(PatientRepository patientRepository) {
        
        this.patientRepository = patientRepository;
    }

    public PatientModel setAppointmentSlot(PatientModel request) {
        PatientModel patient = patientRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + request.getId()));

                patient.setFormattedapointmentslot(request.getDate(), request.getStartTime(), request.getEndTime());
        return patientRepository.save(patient);
    }






    

    @Override
    public PatientModel savePatient(PatientModel patient) {
        return patientRepository.save(patient);
    }

    @Override
    public List<PatientModel> savesPatient(PatientModel[] newPatients) {
        // Convert array to a list correctly
        List<PatientModel> savedPatients = Arrays.asList(newPatients);
        return patientRepository.saveAll(savedPatients);
    }

    @Override
    public List<PatientModel> getAllPatients() {

        return patientRepository.findAll();
    }

    @Override
public PatientModel getPatientbyID(UUID id) {
    // Use Optional's orElseThrow to simplify null check
    Optional<PatientModel> patientOpt = patientRepository.findById(id);
    return patientOpt.orElseThrow(() -> new RuntimeException("Patient not found with ID: " + id));
}

    @Override
    public PatientModel updatePatient(PatientModel patient, UUID id) {
        PatientModel existingPatient = patientRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + id));
        // Update fields of the existing patient
        existingPatient.setPatientName(patient.getPatientName());
        existingPatient.setAge(patient.getAge());
        existingPatient.setAddress(patient.getAddress());
        existingPatient.setPhone(patient.getPhone());
        existingPatient.setLastVisit(patient.getLastVisit());
        existingPatient.setPaid(patient.getPaid());
        existingPatient.setApointmentSlot(patient.getApointmentSlot()); // Ensure this field is set
        existingPatient.setImageURL(patient.getImageURL());  


        return patientRepository.save(existingPatient);
    }

    @Override
public void deletePatient(UUID id) {
    Optional<PatientModel> delete = patientRepository.findById(id);

    if (delete.isPresent()) { // Check if the Optional contains a value
        patientRepository.deleteById(id);
    } else {
        throw new RuntimeException("Patient not found with ID: " + id);
    }
}

}

    


