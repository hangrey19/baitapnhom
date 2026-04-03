package com.example.baitapnhom.ui.order;

import android.view.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.baitapnhom.data.local.entity.Order;
import com.example.baitapnhom.databinding.ItemOrderBinding;
import com.example.baitapnhom.utils.CurrencyFormatter;
import com.example.baitapnhom.utils.DateFormatter;

public class OrderAdapter extends ListAdapter<Order, OrderAdapter.VH> {

    public interface OnOrderClick  { void onClick(Order order); }
    public interface OnOrderCancel { void onCancel(Order order); }

    private final OnOrderClick  clickListener;
    private final OnOrderCancel cancelListener;

    private static final DiffUtil.ItemCallback<Order> DIFF = new DiffUtil.ItemCallback<Order>() {
        @Override public boolean areItemsTheSame(@NonNull Order a, @NonNull Order b)    { return a.id == b.id; }
        @Override public boolean areContentsTheSame(@NonNull Order a, @NonNull Order b) { return a.status.equals(b.status) && a.totalAmount == b.totalAmount; }
    };

    public OrderAdapter(OnOrderClick click, OnOrderCancel cancel) {
        super(DIFF);
        this.clickListener  = click;
        this.cancelListener = cancel;
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ItemOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override public void onBindViewHolder(@NonNull VH holder, int pos) { holder.bind(getItem(pos)); }

    class VH extends RecyclerView.ViewHolder {
        final ItemOrderBinding b;
        VH(ItemOrderBinding b) { super(b.getRoot()); this.b = b; }

        void bind(Order order) {
            b.tvOrderId.setText("Đơn hàng #" + order.id);
            b.tvDate.setText(DateFormatter.format(order.createdAt));
            b.tvTotal.setText(CurrencyFormatter.format(order.totalAmount));

            switch (order.status) {
                case Order.STATUS_PENDING:   b.tvStatus.setText("⏳ Chờ xử lý");     break;
                case Order.STATUS_PAID:      b.tvStatus.setText("✅ Đã thanh toán"); break;
                case Order.STATUS_SHIPPING:  b.tvStatus.setText("🚚 Đang giao");     break;
                case Order.STATUS_DELIVERED: b.tvStatus.setText("📦 Đã giao");       break;
                case Order.STATUS_CANCELLED: b.tvStatus.setText("❌ Đã hủy");        break;
                default:                     b.tvStatus.setText(order.status);
            }

            boolean canCancel = order.status.equals(Order.STATUS_PAID) || order.status.equals(Order.STATUS_PENDING);
            b.btnCancel.setVisibility(canCancel ? View.VISIBLE : View.GONE);
            b.btnCancel.setOnClickListener(v -> { if (cancelListener != null) cancelListener.onCancel(order); });
            b.getRoot().setOnClickListener(v -> { if (clickListener != null) clickListener.onClick(order); });
        }
    }
}