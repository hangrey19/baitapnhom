package com.example.baitapnhom.ui.notification;

import android.os.Bundle;
import android.view.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.baitapnhom.ShoppingApplication;
import com.example.baitapnhom.databinding.FragmentNotificationBinding;

public class NotificationFragment extends Fragment {

    private FragmentNotificationBinding b;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inf, ViewGroup c, Bundle s) {
        b = FragmentNotificationBinding.inflate(inf, c, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ShoppingApplication app = (ShoppingApplication) requireActivity().getApplication();
        int userId = app.getPrefs().getLoggedInUserId();

        NotificationAdapter adapter = new NotificationAdapter(n ->
                app.getNotificationRepository().markAsRead(n.id));

        b.rvNotifications.setAdapter(adapter);
        b.rvNotifications.setLayoutManager(new LinearLayoutManager(getContext()));

        b.btnMarkAll.setOnClickListener(v -> app.getNotificationRepository().markAllAsRead(userId));

        app.getNotificationRepository().getNotifications(userId)
                .observe(getViewLifecycleOwner(), list -> {
                    adapter.submitList(list);
                    b.tvEmpty.setVisibility(list == null || list.isEmpty() ? View.VISIBLE : View.GONE);
                });
    }

    @Override public void onDestroyView() { super.onDestroyView(); b = null; }
}