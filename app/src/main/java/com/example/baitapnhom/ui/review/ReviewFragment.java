package com.example.baitapnhom.ui.review;

import android.os.Bundle;
import android.view.*;
import android.widget.Toast;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.baitapnhom.ShoppingApplication;
import com.example.baitapnhom.data.local.entity.Review;
import com.example.baitapnhom.databinding.FragmentReviewBinding;
import com.example.baitapnhom.utils.AppExecutors;
import com.example.baitapnhom.utils.Constants;

public class ReviewFragment extends Fragment {

    private FragmentReviewBinding b;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inf, ViewGroup c, Bundle s) {
        b = FragmentReviewBinding.inflate(inf, c, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ShoppingApplication app = (ShoppingApplication) requireActivity().getApplication();
        int productId = getArguments() != null ? getArguments().getInt(Constants.ARG_PRODUCT_ID, 0) : 0;

        b.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(view).navigateUp());

        b.btnSubmit.setOnClickListener(v -> {
            float  rating  = b.ratingBar.getRating();
            String comment = b.etComment.getText().toString().trim();

            if (rating == 0f) {
                Toast.makeText(getContext(), "Vui lòng chọn số sao", Toast.LENGTH_SHORT).show(); return;
            }

            int userId = app.getPrefs().getLoggedInUserId();
            AppExecutors.getInstance().diskIO().execute(() -> {
                com.example.baitapnhom.data.local.entity.User user =
                        app.getDatabase().userDao().getUserByIdSync(userId);
                String username = user != null ? user.username : "Người dùng";

                Review review = new Review(productId, userId, rating, comment, username);
                app.getDatabase().reviewDao().insert(review);

                // Update product rating
                float avg   = app.getDatabase().reviewDao().getAverageRating(productId);
                int   count = app.getDatabase().reviewDao().getReviewCount(productId);
                app.getDatabase().productDao().updateRating(productId, avg, count);

                AppExecutors.getInstance().mainThread().execute(() -> {
                    Toast.makeText(getContext(), "Cảm ơn bạn đã đánh giá! ⭐", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(requireView()).navigateUp();
                });
            });
        });
    }

    @Override public void onDestroyView() { super.onDestroyView(); b = null; }
}