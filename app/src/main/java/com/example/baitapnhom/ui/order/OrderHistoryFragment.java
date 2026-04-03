package com.example.baitapnhom.ui.order;

import android.os.Bundle;
import android.view.*;
import android.widget.Toast;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.tabs.TabLayout;
import com.example.baitapnhom.R;
import com.example.baitapnhom.ShoppingApplication;
import com.example.baitapnhom.data.local.entity.Order;
import com.example.baitapnhom.databinding.FragmentOrderHistoryBinding;
import com.example.baitapnhom.utils.Constants;
import java.util.ArrayList;
import java.util.List;

public class OrderHistoryFragment extends Fragment {

    private FragmentOrderHistoryBinding b;
    private OrderViewModel vm;
    private OrderAdapter   adapter;

    private static final String[] TAB_LABELS   = {"Tất cả", "Đã TT", "Đang giao", "Đã giao", "Đã hủy"};
    private static final String[] TAB_STATUSES = {"ALL", Order.STATUS_PAID, Order.STATUS_SHIPPING, Order.STATUS_DELIVERED, Order.STATUS_CANCELLED};

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inf, ViewGroup c, Bundle s) {
        b = FragmentOrderHistoryBinding.inflate(inf, c, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ShoppingApplication app = (ShoppingApplication) requireActivity().getApplication();
        vm = new ViewModelProvider(requireActivity(),
                new OrderViewModelFactory(app.getOrderRepository(), app.getCartRepository(),
                        app.getProductRepository(), app.getNotificationRepository(), app.getPrefs())
        ).get(OrderViewModel.class);

        adapter = new OrderAdapter(
                order -> {
                    Bundle args = new Bundle();
                    args.putInt(Constants.ARG_ORDER_ID, order.id);
                    Navigation.findNavController(requireView())
                            .navigate(R.id.action_orderHistoryFragment_to_orderDetailFragment, args);
                },
                order -> {
                    vm.cancelOrder(order.id);
                    Toast.makeText(getContext(), "Đã hủy đơn #" + order.id, Toast.LENGTH_SHORT).show();
                }
        );

        b.rvOrders.setAdapter(adapter);
        b.rvOrders.setLayoutManager(new LinearLayoutManager(getContext()));

        for (String label : TAB_LABELS) b.tabLayout.addTab(b.tabLayout.newTab().setText(label));

        b.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab)   { filterTab(tab.getPosition()); }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        vm.orders.observe(getViewLifecycleOwner(), orders -> filterTab(b.tabLayout.getSelectedTabPosition()));

        b.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(view).navigateUp());
    }

    private void filterTab(int pos) {
        List<Order> all = vm.orders.getValue();
        if (all == null) all = new ArrayList<>();
        String status = TAB_STATUSES[pos];
        List<Order> filtered = new ArrayList<>();
        for (Order o : all) {
            if (status.equals("ALL") || o.status.equals(status)) filtered.add(o);
        }
        adapter.submitList(filtered);
        b.tvEmpty.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override public void onDestroyView() { super.onDestroyView(); b = null; }
}
