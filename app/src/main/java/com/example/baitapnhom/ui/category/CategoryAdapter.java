package com.example.baitapnhom.ui.category;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitapnhom.data.local.entity.Category;
import com.example.baitapnhom.databinding.ItemCategoryBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    public interface OnCategoryClickListener {
        void onClick(Category category);
    }

    private final List<Category> items = new ArrayList<>();
    private final OnCategoryClickListener listener;

    public CategoryAdapter(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<Category> list) {
        items.clear();
        if (list != null) items.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(ItemCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final ItemCategoryBinding binding;

        public CategoryViewHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Category category) {
            binding.tvIcon.setText(resolveCategoryIcon(category.icon));
            binding.tvName.setText(category.name);
            binding.tvDesc.setText(category.description);
            binding.getRoot().setOnClickListener(v -> listener.onClick(category));
        }

        private String resolveCategoryIcon(String icon) {
            if (icon == null) {
                return "\uD83C\uDF4E";
            }

            String normalized = icon.trim().toLowerCase(Locale.ROOT);
            if (normalized.contains("apple") || normalized.contains("noi")) {
                return "\uD83C\uDF4A";
            }
            if (normalized.contains("grape") || normalized.contains("nhap")) {
                return "\uD83C\uDF47";
            }
            if (normalized.contains("juice")) {
                return "\uD83E\uDD64";
            }
            if (normalized.contains("gift") || normalized.contains("combo")) {
                return "\uD83C\uDF81";
            }
            return icon;
        }
    }
}
