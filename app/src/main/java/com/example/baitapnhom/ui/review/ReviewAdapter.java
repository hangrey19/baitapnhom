package com.example.baitapnhom.ui.review;

import android.view.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.baitapnhom.data.local.entity.Review;
import com.example.baitapnhom.databinding.ItemReviewBinding;
import com.example.baitapnhom.utils.DateFormatter;

public class ReviewAdapter extends ListAdapter<Review, ReviewAdapter.VH> {

    private static final DiffUtil.ItemCallback<Review> DIFF = new DiffUtil.ItemCallback<Review>() {
        @Override public boolean areItemsTheSame(@NonNull Review a, @NonNull Review b)    { return a.id == b.id; }
        @Override public boolean areContentsTheSame(@NonNull Review a, @NonNull Review b) { return a.rating == b.rating && a.comment.equals(b.comment); }
    };

    public ReviewAdapter() { super(DIFF); }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ItemReviewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override public void onBindViewHolder(@NonNull VH holder, int pos) { holder.bind(getItem(pos)); }

    static class VH extends RecyclerView.ViewHolder {
        final ItemReviewBinding b;
        VH(ItemReviewBinding b) { super(b.getRoot()); this.b = b; }
        void bind(Review r) {
            b.tvUsername.setText(r.username.isEmpty() ? "Ẩn danh" : r.username);
            b.tvComment.setText(r.comment.isEmpty() ? "Không có bình luận" : r.comment);
            b.tvDate.setText(DateFormatter.formatDate(r.createdAt));
            b.ratingBar.setRating(r.rating);
        }
    }
}