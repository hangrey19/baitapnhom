package com.example.baitapnhom.ui.order;

import android.view.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.baitapnhom.databinding.ItemCheckoutProductBinding;
import com.example.baitapnhom.ui.cart.CartViewModel.CartItemWithProduct;
import com.example.baitapnhom.utils.CurrencyFormatter;
import com.example.baitapnhom.utils.ImageLoader;

public class CheckoutItemAdapter extends ListAdapter<CartItemWithProduct, CheckoutItemAdapter.VH> {

    private static final DiffUtil.ItemCallback<CartItemWithProduct> DIFF =
            new DiffUtil.ItemCallback<CartItemWithProduct>() {
                @Override public boolean areItemsTheSame(@NonNull CartItemWithProduct a, @NonNull CartItemWithProduct b) {
                    return a.cartItem.id == b.cartItem.id;
                }
                @Override public boolean areContentsTheSame(@NonNull CartItemWithProduct a, @NonNull CartItemWithProduct b) {
                    return a.cartItem.quantity == b.cartItem.quantity;
                }
            };

    public CheckoutItemAdapter() { super(DIFF); }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ItemCheckoutProductBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override public void onBindViewHolder(@NonNull VH holder, int pos) { holder.bind(getItem(pos)); }

    static class VH extends RecyclerView.ViewHolder {
        final ItemCheckoutProductBinding b;
        VH(ItemCheckoutProductBinding b) { super(b.getRoot()); this.b = b; }
        void bind(CartItemWithProduct item) {
            ImageLoader.load(b.ivProduct, item.productImage);
            b.tvName.setText(item.productName);
            b.tvPrice.setText(CurrencyFormatter.format(item.productPrice));
            b.tvQuantity.setText("x" + item.cartItem.quantity);
            b.tvSubtotal.setText(CurrencyFormatter.format(item.productPrice * item.cartItem.quantity));
        }
    }
}