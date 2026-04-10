package com.example.baitapnhom.ui.order;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baitapnhom.data.local.entity.Order;
import com.example.baitapnhom.databinding.ItemOrderHistoryBinding;
import com.example.baitapnhom.utils.CurrencyUtils;
import com.example.baitapnhom.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder> {

    public interface OnOrderClickListener {
        void onClick(Order order);
    }

    public interface OnReorderClickListener {
        void onReorder(Order order);
    }

    private final List<Order> items = new ArrayList<>();
    private final OnOrderClickListener clickListener;
    private final OnReorderClickListener reorderListener;

    public OrderHistoryAdapter(OnOrderClickListener clickListener,
                               OnReorderClickListener reorderListener) {
        this.clickListener   = clickListener;
        this.reorderListener = reorderListener;
    }

    public void submitList(List<Order> list) {
        items.clear();
        if (list != null) items.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderHistoryViewHolder(ItemOrderHistoryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() { return items.size(); }

    class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
        private final ItemOrderHistoryBinding binding;

        OrderHistoryViewHolder(ItemOrderHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Order order) {
            binding.tvOrderCode.setText("Đơn hàng #" + order.id);
            binding.tvDate.setText("🕐 " + DateTimeUtils.formatTimestamp(order.createdAt));
            binding.tvStatus.setText(order.status.equals("PAID") ? "✓ Đã thanh toán" : order.status);
            binding.tvTotal.setText(CurrencyUtils.format(order.totalAmount));

            binding.getRoot().setOnClickListener(v -> clickListener.onClick(order));
            binding.btnReorder.setOnClickListener(v -> reorderListener.onReorder(order));
        }
    }
}