package com.example.baitapnhom.ui.order;

import android.view.LayoutInflater;
import android.view.View;
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
    public interface CartActionListener {
        void onIncrease(OrderItemDisplay item);
        void onDecrease(OrderItemDisplay item);
        void onRemove(OrderItemDisplay item);
    }

    private final List<OrderItemDisplay> items = new ArrayList<>();
    private final boolean editable;
    private final CartActionListener actionListener;

    public OrderItemAdapter() {
        this(false, null);
    }

    public OrderItemAdapter(boolean editable, CartActionListener actionListener) {
        this.editable = editable;
        this.actionListener = actionListener;
    }

    public void submitList(List<OrderItemDisplay> list) {
        items.clear();
        if (list != null) {
            items.addAll(list);
        }
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

        OrderItemViewHolder(ItemOrderDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(OrderItemDisplay item) {
            binding.tvName.setText(item.productName);
            binding.tvQuantity.setText(editable ? "So luong trong gio: " + item.quantity : "SL: " + item.quantity);
            binding.tvPrice.setText(CurrencyUtils.format(item.unitPrice * item.quantity));
            binding.tvUnit.setText("Don vi: " + item.unit + " | Gia: " + CurrencyUtils.format(item.unitPrice));
            binding.layoutActions.setVisibility(editable ? View.VISIBLE : View.GONE);
            ImageLoader.load(binding.ivProduct, item.imageUrl);

            if (editable && actionListener != null) {
                binding.btnIncrease.setOnClickListener(v -> actionListener.onIncrease(item));
                binding.btnDecrease.setOnClickListener(v -> actionListener.onDecrease(item));
                binding.btnRemove.setOnClickListener(v -> actionListener.onRemove(item));
            } else {
                binding.btnIncrease.setOnClickListener(null);
                binding.btnDecrease.setOnClickListener(null);
                binding.btnRemove.setOnClickListener(null);
            }
        }
    }
}
