package DoctorAdminBackend.AdminBackend.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import DoctorAdminBackend.AdminBackend.Service.PatientService;
import DoctorAdminBackend.AdminBackend.Model.PatientModel;
import org.springframework.beans.factory.annotation.Autowired;


@RequestMapping("/patient")
@RestController
@CrossOrigin(origins = "http://localhost:8084")
public class PatientController {
    
   private PatientService patientservice;


    @Autowired
    public PatientController(PatientService patientservice)  {

        this.patientservice = patientservice;
    }

    @PostMapping("/create")
		public ResponseEntity<PatientModel> savepatient(@RequestBody  PatientModel pro) throws IOException{
			
		   System.out.println("id: " + pro.getId());
		   System.out.println("Patient_name: " + pro.getPatientName());
	       System.out.println("address: " + pro.getAddress());
	       System.out.println("age: " + pro.getAge());
	       System.out.println("phone: " + pro.getPhone());
           System.out.println("last_visit" + pro.getLastVisit());
	       
	       System.out.println("-------------------------------------------------");
			
			return new ResponseEntity<PatientModel>(patientservice.savePatient(pro), HttpStatus.CREATED);
			
			  
		}

        @GetMapping("/allpatients")
        public List<Map<String, Object>> getAllPatients() {
        List<PatientModel> patients = patientservice.getAllPatients();

    // Transform patient data into a simplified format
    return patients.stream().map(patient -> {
        Map<String, Object> patientData = new LinkedHashMap<>();
        patientData.put("id", patient.getId());
        patientData.put("patientName", patient.getPatientName());
        patientData.put("age", patient.getAge());
        patientData.put("address", patient.getAddress());
        patientData.put("phone", patient.getPhone());
        patientData.put("ImageUrl", patient.getImageURL());
        patientData.put("lastVisit", patient.getLastVisit());
        return patientData;
    }).collect(Collectors.toList());

}

@GetMapping("/get/{id}")
public ResponseEntity<PatientModel> getPatientById(@PathVariable("id") UUID id) {
    
    PatientModel patient = patientservice.getPatientbyID(id);

    if (patient != null) {
        return new ResponseEntity<>(patient, HttpStatus.OK);
    } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}




        @PutMapping("/update/{id}")
        public ResponseEntity<PatientModel> updateatient(@PathVariable("id") UUID id, @RequestBody PatientModel patient) {

        return new ResponseEntity<PatientModel>(patientservice.updatePatient(patient, id), HttpStatus.OK);
       }


     @DeleteMapping("/delete/{id}")
     public ResponseEntity<String> deletePatient(@PathVariable("id") UUID id) {
    
    patientservice.deletePatient(id);
    return new ResponseEntity<>("Patient deleted successfully.", HttpStatus.OK);
}












}
