package DoctorAdminBackend.AdminBackend.Service;

import java.util.List;

import DoctorAdminBackend.AdminBackend.Model.Review;

public interface ReviewService {

    
   Review savereview(Review review);

   List<Review> getAllReview();
    
}
