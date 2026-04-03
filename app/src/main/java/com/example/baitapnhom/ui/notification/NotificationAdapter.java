package com.example.baitapnhom.ui.notification;

import android.view.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.baitapnhom.data.local.entity.Notification;
import com.example.baitapnhom.databinding.ItemNotificationBinding;
import com.example.baitapnhom.utils.DateFormatter;

public class NotificationAdapter extends ListAdapter<Notification, NotificationAdapter.VH> {

    public interface OnNotifClick { void onClick(Notification n); }
    private final OnNotifClick listener;

    private static final DiffUtil.ItemCallback<Notification> DIFF =
            new DiffUtil.ItemCallback<Notification>() {
                @Override public boolean areItemsTheSame(@NonNull Notification a, @NonNull Notification b)    { return a.id == b.id; }
                @Override public boolean areContentsTheSame(@NonNull Notification a, @NonNull Notification b) { return a.isRead == b.isRead; }
            };

    public NotificationAdapter(OnNotifClick listener) {
        super(DIFF);
        this.listener = listener;
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ItemNotificationBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override public void onBindViewHolder(@NonNull VH holder, int pos) { holder.bind(getItem(pos)); }

    class VH extends RecyclerView.ViewHolder {
        final ItemNotificationBinding b;
        VH(ItemNotificationBinding b) { super(b.getRoot()); this.b = b; }
        void bind(Notification n) {
            b.tvEmoji.setText(n.iconEmoji);
            b.tvTitle.setText(n.title);
            b.tvMessage.setText(n.message);
            b.tvDate.setText(DateFormatter.format(n.createdAt));
            b.getRoot().setAlpha(n.isRead ? 0.6f : 1f);
            b.getRoot().setOnClickListener(v -> { if (listener != null) listener.onClick(n); });
        }
    }
}