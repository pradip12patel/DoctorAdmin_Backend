package DoctorAdminBackend.AdminBackend.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.GenericGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Getter
@Setter
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @Column(name = "doctor_name")
    private String doctorName;

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "certification")
    private String certification;

    @Column(name = "about")
    private String about;

    @Column(name = "member_since")
    private LocalDateTime memberSince;

    @Column(name = "earnings")
    private Double earnings;

    @Column(name = "status")
    private boolean status;

    @Column(name = "imageurl")
    private String imageurl;

    // **Address as JSON**
    @Column(name = "address")
    private String address;

    @Column(name = "emailId")
    private String email;

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }
    // @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch =
    // FetchType.EAGER)
    // @JsonManagedReference
    // private List<PatientModel> patients;

    @Column(name = "experience_years", nullable = false)
    private int experienceYears;

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Appointment> appointments;

    // Getter method for appointments
    public List<Appointment> getAppointments() {
        return appointments;
    }

    // Setter method for appointments
    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Review> reviews;

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    @Column(name = "is_feature", nullable = false)
    private boolean isFeature;

    public boolean isFeature() {
        return isFeature;
    }

    public void setFeature(boolean isFeature) {
        this.isFeature = isFeature;
    }

    // Getter for id
    public UUID getId() {
        return id;
    }

    // Setter for id
    public void setId(UUID id) {
        this.id = id;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public LocalDateTime getMemberSince() {
        return memberSince;
    }

    public void setMemberSince(LocalDateTime memberSince) {
        this.memberSince = memberSince;
    }

    public Double getEarnings() {
        return earnings;
    }

    public void setEarnings(Double earnings) {
        this.earnings = earnings;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getImageURL() {
        return imageurl;
    }

    public void setImageURL(String imageurl) {
        this.imageurl = imageurl;
    }

    // public List<PatientModel> getPatients() {
    // return patients;
    // }

    // public void setPatients(List<PatientModel> patients) {
    // this.patients = patients;
    // }

    public String getcertification() {

        return certification;

    }

    public void setcertification(String certification) {
        this.certification = certification;
    }

    public String getabout() {
        return about;
    }

    public void setabout(String about) {
        this.about = about;
    }

}
