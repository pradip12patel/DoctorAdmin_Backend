package DoctorAdminBackend.AdminBackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import DoctorAdminBackend.AdminBackend.Model.Doctor;
import DoctorAdminBackend.AdminBackend.ServiceIMPL.DoctorService;

@RestController
@RequestMapping("/api")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping("/doctors-with-patients")
    public ResponseEntity<List<Doctor>> getAllDoctorsWithPatients() {
        List<Doctor> doctors = doctorService.getAllDoctorsWithPatients();
        return ResponseEntity.ok(doctors);
    }
}

