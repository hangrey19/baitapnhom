package com.example.baitapnhom.ui.order;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.baitapnhom.data.local.prefs.PreferencesManager;
import com.example.baitapnhom.data.repository.*;

public class OrderViewModelFactory implements ViewModelProvider.Factory {
    private final OrderRepository        orderRepo;
    private final CartRepository         cartRepo;
    private final ProductRepository      productRepo;
    private final NotificationRepository notifRepo;
    private final PreferencesManager     prefs;

    public OrderViewModelFactory(OrderRepository o, CartRepository c,
                                 ProductRepository p, NotificationRepository n,
                                 PreferencesManager prefs) {
        orderRepo   = o; cartRepo = c; productRepo = p; notifRepo = n; this.prefs = prefs;
    }

    @NonNull @Override
    public <T extends ViewModel> T create(@NonNull Class<T> cls) {
        return (T) new OrderViewModel(orderRepo, cartRepo, productRepo, notifRepo, prefs);
    }
}