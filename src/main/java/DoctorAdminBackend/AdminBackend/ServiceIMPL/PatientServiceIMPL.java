package DoctorAdminBackend.AdminBackend.ServiceIMPL;

import java.util.Arrays;
import java.util.List;


import org.springframework.stereotype.Service;

import DoctorAdminBackend.AdminBackend.Model.PatientModel;
import DoctorAdminBackend.AdminBackend.Reposotiry.PatientReposotiry;
import DoctorAdminBackend.AdminBackend.Service.PatientService;

@Service
public class PatientServiceIMPL implements PatientService {

    // private final PatientReposotiry patientrepo;

    // // Constructor injection
    // public PatientServiceIMPL(PatientReposotiry patientrepo) {
    //     super();
    //     this.patientrepo = patientrepo;
    // }

    // @Override
    // public PatientModel savePatient(PatientModel patient) {
    //     return patientrepo.save(patient);
    // }

    // @Override
    // public List<PatientModel> savesPatient(PatientModel[] newPatients) {
    //     // Convert array to a list correctly
    //     List<PatientModel> savedPatients = Arrays.asList(newPatients);
    //     return patientrepo.saveAll(savedPatients);
    // }

    // @Override
    // public List<PatientModel> getAllPatients() {

    //     return patientrepo.findAll();
    // }

    // // @Override
    // // public PatientModel getPatientById(long id) {
    // //     // Use Optional's orElseThrow to simplify null check
    // //     Optional<PatientModel> pro = Optional.of(patientrepo.findById(id));

    // //     return patientrepo.findById(id);
    // // } 

    // @Override
    // public PatientModel updatePatient(PatientModel patient, long id) {
    //     PatientModel existingPatient = patientrepo.findById(id);
    //     // Update fields of the existing patient
    //     existingPatient.setPatientName(patient.getPatientName());
    //     existingPatient.setAge(patient.getAge());
    //     existingPatient.setAddress(patient.getAddress());
    //     existingPatient.setPhone(patient.getPhone());
    //     existingPatient.setLastVisit(patient.getLastVisit());
    //     existingPatient.setPaid(patient.getPaid());
    //     return patientrepo.save(existingPatient);
    // }

    // @Override
    // public void deletePatient(long id) {
    //     if (patientrepo.existsById(id)) {
    //         patientrepo.deleteById(id);
    //     } else {
    //         throw new RuntimeException("Patient not found with ID: " + id);
    //     }
    // }
}
