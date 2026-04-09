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
        void onSelectChanged(OrderItemDisplay item, boolean selected);
    }

    private final List<OrderItemDisplay> items = new ArrayList<>();
    private final boolean editable;
    private final CartActionListener actionListener;

    public OrderItemAdapter() {
        this(false, null);
    }

    public OrderItemAdapter(boolean editable, CartActionListener actionListener) {
        this.editable       = editable;
        this.actionListener = actionListener;
    }

    public void submitList(List<OrderItemDisplay> list) {
        items.clear();
        if (list != null) items.addAll(list);
        notifyDataSetChanged();
    }

    /** Trả về tổng tiền các item đang được chọn */
    public double getSelectedTotal() {
        double total = 0;
        for (OrderItemDisplay item : items) {
            if (item.selected) total += item.unitPrice * item.quantity;
        }
        return total;
    }

    /** Trả về số lượng item đang được chọn */
    public int getSelectedCount() {
        int count = 0;
        for (OrderItemDisplay item : items) {
            if (item.selected) count++;
        }
        return count;
    }

    public boolean isAllSelected() {
        for (OrderItemDisplay item : items) {
            if (!item.selected) return false;
        }
        return !items.isEmpty();
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderItemViewHolder(ItemOrderDetailBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() { return items.size(); }

    class OrderItemViewHolder extends RecyclerView.ViewHolder {
        private final ItemOrderDetailBinding binding;

        OrderItemViewHolder(ItemOrderDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(OrderItemDisplay item) {
            binding.tvName.setText(item.productName);
            binding.tvUnit.setText(item.unit + " | " + CurrencyUtils.format(item.unitPrice) + "/đơn vị");
            binding.tvQuantity.setText("Số lượng: " + item.quantity);
            binding.tvPrice.setText(CurrencyUtils.format(item.unitPrice * item.quantity));
            ImageLoader.load(binding.ivProduct, item.imageUrl);

            if (editable) {
                // Hiện checkbox
                binding.cbSelected.setVisibility(View.VISIBLE);
                binding.layoutActions.setVisibility(View.VISIBLE);
                binding.tvQtyNumber.setText(String.valueOf(item.quantity));

                // Gán checkbox (tạm remove listener tránh vòng lặp)
                binding.cbSelected.setOnCheckedChangeListener(null);
                binding.cbSelected.setChecked(item.selected);
                binding.cbSelected.setOnCheckedChangeListener((btn, checked) -> {
                    item.selected = checked;
                    if (actionListener != null) actionListener.onSelectChanged(item, checked);
                });

                binding.btnIncrease.setOnClickListener(v -> {
                    if (actionListener != null) actionListener.onIncrease(item);
                });
                binding.btnDecrease.setOnClickListener(v -> {
                    if (actionListener != null) actionListener.onDecrease(item);
                });
                binding.btnRemove.setOnClickListener(v -> {
                    if (actionListener != null) actionListener.onRemove(item);
                });
            } else {
                binding.cbSelected.setVisibility(View.GONE);
                binding.layoutActions.setVisibility(View.GONE);
            }
        }
    }
}