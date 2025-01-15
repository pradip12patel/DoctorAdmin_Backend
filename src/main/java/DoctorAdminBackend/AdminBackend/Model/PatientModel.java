package DoctorAdminBackend.AdminBackend.Model;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Patients")
public class PatientModel {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @Column(name = "patient_name", nullable = false) // Ensure column names match the database
    private String patientName;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "last_visit")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate lastVisit;

    @Column(name = "paid", nullable = false)
    private double paid;

    @Column(name = "imageurl", nullable = false)
    private String imageurl;

    // private String date;       // Format: "5 Nov 2019"
    // private String startTime;  // Format: "11:00 AM"
    // private String endTime;

    // @Column(name = "apointmentslot", nullable = false)
    // private String apointmentslot;

    // @ManyToOne(fetch = FetchType.LAZY) // Use LAZY fetching for better performance
    // @JoinColumn(name = "doctor_id", referencedColumnName = "id", nullable = false) // Maps to Doctor's primary key
    // @JsonBackReference
    // private Doctor doctor;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Appointment> appointments;

    // Getter method for appointments
   public List<Appointment> getAppointments() {
    return appointments;
   }

// Setter method for appointments
   public void setAppointments(List<Appointment> appointments) {
    this.appointments = appointments;
  }

  @OneToMany(mappedBy =  "patient",fetch = FetchType.EAGER)
   private List<Review> reviews;

    public List<Review> getReviews() {
        return reviews;
       }
    
       public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
      }



    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(LocalDate lastVisit) {
        this.lastVisit = lastVisit;
    }

    
    // public String getApointmentSlot() {
    //     return apointmentslot;
    // }

    // public void setApointmentSlot(String apoitmentslot) {
    //     this.apointmentslot = apoitmentslot;
    // }

    // // Method to set the slot using date and time
    // public void setFormattedapointmentslot(String date, String startTime, String endTime) {
    //     this.apointmentslot = String.format("%s, %s - %s", date, startTime, endTime);
    // }

    public double getPaid() {
        return paid;
    }

    public void setPaid(double paid) {
        this.paid = paid;
    }

    public String getImageURL() {
        return  imageurl;
    }

    public void setImageURL(String imageurl) {
        this.imageurl = imageurl;
    }

    // public Doctor getDoctor() {
    //     return doctor;
    // }

    // public void setDoctor(Doctor doctor) {
    //     this.doctor = doctor;
    // }

//     // Getter and Setter for 'date'
// public String getDate() {
//     return date;
// }

// public void setDate(String date) {
//     this.date = date;
// }



}
