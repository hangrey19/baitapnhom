package com.example.baitapnhom.ui.order;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitapnhom.data.local.model.OrderItemDisplay;
import com.example.baitapnhom.databinding.ItemOrderDetailBinding;
import com.example.baitapnhom.utils.CurrencyUtils;
import com.example.baitapnhom.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {
    private final List<OrderItemDisplay> items = new ArrayList<>();

    public void submitList(List<OrderItemDisplay> list) {
        items.clear();
        if (list != null) items.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderItemViewHolder(ItemOrderDetailBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder {
        private final ItemOrderDetailBinding binding;

        public OrderItemViewHolder(ItemOrderDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(OrderItemDisplay item) {
            binding.tvName.setText(item.productName);
            binding.tvQuantity.setText("SL: " + item.quantity);
            binding.tvPrice.setText(CurrencyUtils.format(item.unitPrice * item.quantity));
            binding.tvUnit.setText(item.unit);
            ImageLoader.load(binding.ivProduct, item.imageUrl);
        }
    }
}
