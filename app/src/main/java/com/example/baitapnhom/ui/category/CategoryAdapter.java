package com.example.baitapnhom.ui.category;

import android.view.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.baitapnhom.data.local.entity.Category;
import com.example.baitapnhom.databinding.ItemCategoryBinding;

public class CategoryAdapter extends ListAdapter<Category, CategoryAdapter.VH> {

    public interface OnCategoryClick { void onClick(Category category); }
    private final OnCategoryClick listener;

    private static final DiffUtil.ItemCallback<Category> DIFF = new DiffUtil.ItemCallback<Category>() {
        @Override public boolean areItemsTheSame(@NonNull Category a, @NonNull Category b)    { return a.id == b.id; }
        @Override public boolean areContentsTheSame(@NonNull Category a, @NonNull Category b) { return a.name.equals(b.name); }
    };

    public CategoryAdapter(OnCategoryClick listener) {
        super(DIFF);
        this.listener = listener;
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ItemCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override public void onBindViewHolder(@NonNull VH holder, int pos) { holder.bind(getItem(pos)); }

    class VH extends RecyclerView.ViewHolder {
        final ItemCategoryBinding b;
        VH(ItemCategoryBinding b) { super(b.getRoot()); this.b = b; }
        void bind(Category cat) {
            b.tvEmoji.setText(cat.iconEmoji);
            b.tvName.setText(cat.name);
            b.getRoot().setOnClickListener(v -> { if (listener != null) listener.onClick(cat); });
        }
    }
}