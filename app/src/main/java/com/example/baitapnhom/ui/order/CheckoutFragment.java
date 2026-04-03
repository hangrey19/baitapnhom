package com.example.baitapnhom.ui.order;

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
import com.example.baitapnhom.databinding.FragmentCheckoutBinding;
import com.example.baitapnhom.ui.cart.CartViewModel;
import com.example.baitapnhom.ui.cart.CartViewModelFactory;
import com.example.baitapnhom.utils.CurrencyFormatter;

import java.util.List;

public class CheckoutFragment extends Fragment {

    private FragmentCheckoutBinding b;
    private OrderViewModel orderVm;
    private List<CartViewModel.CartItemWithProduct> selectedItems;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inf, ViewGroup c, Bundle s) {
        b = FragmentCheckoutBinding.inflate(inf, c, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ShoppingApplication app = (ShoppingApplication) requireActivity().getApplication();

        orderVm = new ViewModelProvider(requireActivity(),
                new OrderViewModelFactory(app.getOrderRepository(), app.getCartRepository(),
                        app.getProductRepository(), app.getNotificationRepository(), app.getPrefs())
        ).get(OrderViewModel.class);

        CartViewModel cartVm = new ViewModelProvider(requireActivity(),
                new CartViewModelFactory(app.getCartRepository(), app.getProductRepository(), app.getPrefs())
        ).get(CartViewModel.class);

        selectedItems = cartVm.getSelectedItems();

        // Show items summary
        CheckoutItemAdapter adapter = new CheckoutItemAdapter();
        b.rvItems.setAdapter(adapter);
        b.rvItems.setLayoutManager(new LinearLayoutManager(getContext()));
        b.rvItems.setNestedScrollingEnabled(false);
        adapter.submitList(selectedItems);

        double total = 0;
        int    qty   = 0;
        for (CartViewModel.CartItemWithProduct i : selectedItems) {
            total += i.cartItem.quantity * i.productPrice;
            qty   += i.cartItem.quantity;
        }
        b.tvTotal.setText(CurrencyFormatter.format(total));
        b.tvItemCount.setText(qty + " sản phẩm");

        b.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(view).navigateUp());

        b.btnPlaceOrder.setOnClickListener(v -> {
            String address = b.etAddress.getText().toString().trim();
            String note    = b.etNote.getText().toString().trim();
            String payment = getSelectedPayment();
            orderVm.placeOrder(selectedItems, address, payment, note);
        });

        orderVm.orderState.observe(getViewLifecycleOwner(), state -> {
            switch (state.state) {
                case OrderViewModel.ORDER_LOADING:
                    b.progressBar.setVisibility(View.VISIBLE);
                    b.btnPlaceOrder.setEnabled(false);
                    break;
                case OrderViewModel.ORDER_SUCCESS:
                    b.progressBar.setVisibility(View.GONE);
                    orderVm.resetState();
                    Bundle args = new Bundle();
                    args.putInt("order_id", state.orderId);
                    Navigation.findNavController(requireView())
                            .navigate(R.id.action_checkoutFragment_to_paymentFragment, args);
                    break;
                case OrderViewModel.ORDER_ERROR:
                    b.progressBar.setVisibility(View.GONE);
                    b.btnPlaceOrder.setEnabled(true);
                    Toast.makeText(getContext(), state.errorMsg, Toast.LENGTH_SHORT).show();
                    orderVm.resetState();
                    break;
                default:
                    b.progressBar.setVisibility(View.GONE);
                    b.btnPlaceOrder.setEnabled(true);
            }
        });
    }

    private String getSelectedPayment() {
        int id = b.rgPayment.getCheckedRadioButtonId();
        if (id == R.id.rbBank)    return "BANK";
        if (id == R.id.rbMomo)    return "MOMO";
        if (id == R.id.rbZaloPay) return "ZALOPAY";
        return "COD";
    }

    @Override public void onDestroyView() { super.onDestroyView(); b = null; }
}