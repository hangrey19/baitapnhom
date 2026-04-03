package com.example.baitapnhom.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.Toast;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.baitapnhom.R;
import com.example.baitapnhom.ShoppingApplication;
import com.example.baitapnhom.databinding.FragmentCartBinding;
import com.example.baitapnhom.ui.auth.LoginActivity;
import com.example.baitapnhom.utils.CurrencyFormatter;

public class CartFragment extends Fragment {

    private FragmentCartBinding b;
    private CartViewModel vm;
    private CartAdapter   adapter;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inf, ViewGroup c, Bundle s) {
        b = FragmentCartBinding.inflate(inf, c, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ShoppingApplication app = (ShoppingApplication) requireActivity().getApplication();

        // Chưa đăng nhập → hiện gợi ý login
        if (!app.getPrefs().isLoggedIn()) {
            b.layoutLoginPrompt.setVisibility(View.VISIBLE);
            b.layoutCart.setVisibility(View.GONE);
            b.btnGoLogin.setOnClickListener(v ->
                    startActivity(new Intent(getContext(), LoginActivity.class)));
            return;
        }

        b.layoutLoginPrompt.setVisibility(View.GONE);
        b.layoutCart.setVisibility(View.VISIBLE);

        vm = new ViewModelProvider(requireActivity(),
                new CartViewModelFactory(app.getCartRepository(), app.getProductRepository(), app.getPrefs())
        ).get(CartViewModel.class);

        setupAdapter();
        observeData();
        setupButtons();
    }

    private void setupAdapter() {
        adapter = new CartAdapter(
                (item, qty)     -> vm.updateQuantity(item.cartItem, qty),
                item            -> {
                    vm.removeItem(item.cartItem);
                    Toast.makeText(getContext(), "Đã xóa khỏi giỏ", Toast.LENGTH_SHORT).show();
                },
                (item, checked) -> vm.toggleSelected(item.cartItem.id, checked)
        );
        b.rvCart.setAdapter(adapter);
        b.rvCart.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void observeData() {
        vm.cartItemsWithProduct.observe(getViewLifecycleOwner(), items -> {
            adapter.submitList(items);
            boolean empty = items == null || items.isEmpty();
            b.layoutEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
            b.rvCart.setVisibility(empty ? View.GONE : View.VISIBLE);
            b.layoutBottom.setVisibility(empty ? View.GONE : View.VISIBLE);
        });

        vm.totalPrice.observe(getViewLifecycleOwner(), total ->
                b.tvTotal.setText(CurrencyFormatter.format(total != null ? total : 0)));

        vm.selectedCount.observe(getViewLifecycleOwner(), count -> {
            b.btnCheckout.setText("Mua hàng (" + (count != null ? count : 0) + ")");
            b.btnCheckout.setEnabled(count != null && count > 0);
        });

        vm.isAllSelected.observe(getViewLifecycleOwner(), allSel -> {
            b.cbSelectAll.setOnCheckedChangeListener(null);
            b.cbSelectAll.setChecked(allSel != null && allSel);
            b.cbSelectAll.setOnCheckedChangeListener((btn, checked) -> vm.toggleSelectAll(checked));
        });
    }

    private void setupButtons() {
        b.cbSelectAll.setOnCheckedChangeListener((btn, checked) -> vm.toggleSelectAll(checked));

        b.btnCheckout.setOnClickListener(v -> {
            if (vm.getSelectedItems().isEmpty()) {
                Toast.makeText(getContext(), "Chọn ít nhất 1 sản phẩm", Toast.LENGTH_SHORT).show();
                return;
            }
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_cartFragment_to_checkoutFragment);
        });
    }

    @Override public void onDestroyView() { super.onDestroyView(); b = null; }
}