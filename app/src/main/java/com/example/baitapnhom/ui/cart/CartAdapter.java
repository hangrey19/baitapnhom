package com.example.baitapnhom.ui.cart;

import android.view.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.baitapnhom.databinding.ItemCartBinding;
import com.example.baitapnhom.ui.cart.CartViewModel.CartItemWithProduct;
import com.example.baitapnhom.utils.CurrencyFormatter;
import com.example.baitapnhom.utils.ImageLoader;

public class CartAdapter extends ListAdapter<CartItemWithProduct, CartAdapter.VH> {

    public interface OnQtyChange    { void onChange(CartItemWithProduct item, int newQty); }
    public interface OnRemove       { void onRemove(CartItemWithProduct item); }
    public interface OnSelectChange { void onSelect(CartItemWithProduct item, boolean checked); }

    private final OnQtyChange    qtyChange;
    private final OnRemove       remove;
    private final OnSelectChange selectChange;

    private static final DiffUtil.ItemCallback<CartItemWithProduct> DIFF =
            new DiffUtil.ItemCallback<CartItemWithProduct>() {
                @Override public boolean areItemsTheSame(@NonNull CartItemWithProduct a, @NonNull CartItemWithProduct b) {
                    return a.cartItem.id == b.cartItem.id;
                }
                @Override public boolean areContentsTheSame(@NonNull CartItemWithProduct a, @NonNull CartItemWithProduct b) {
                    return a.cartItem.quantity == b.cartItem.quantity
                            && a.cartItem.isSelected == b.cartItem.isSelected
                            && a.productPrice == b.productPrice;
                }
            };

    public CartAdapter(OnQtyChange qtyChange, OnRemove remove, OnSelectChange selectChange) {
        super(DIFF);
        this.qtyChange    = qtyChange;
        this.remove       = remove;
        this.selectChange = selectChange;
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ItemCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override public void onBindViewHolder(@NonNull VH holder, int pos) { holder.bind(getItem(pos)); }

    class VH extends RecyclerView.ViewHolder {
        final ItemCartBinding b;
        VH(ItemCartBinding b) { super(b.getRoot()); this.b = b; }

        void bind(CartItemWithProduct item) {
            ImageLoader.load(b.ivProduct, item.productImage);
            b.tvName.setText(item.productName);
            b.tvPrice.setText(CurrencyFormatter.format(item.productPrice));
            b.tvQuantity.setText(String.valueOf(item.cartItem.quantity));
            b.tvSubtotal.setText(CurrencyFormatter.format(item.cartItem.quantity * item.productPrice));

            // Prevent listener firing during bind
            b.cbSelect.setOnCheckedChangeListener(null);
            b.cbSelect.setChecked(item.cartItem.isSelected);
            b.cbSelect.setOnCheckedChangeListener((btn, checked) -> selectChange.onSelect(item, checked));

            b.btnMinus.setOnClickListener(v -> qtyChange.onChange(item, item.cartItem.quantity - 1));
            b.btnPlus.setOnClickListener(v  -> qtyChange.onChange(item, item.cartItem.quantity + 1));
            b.btnRemove.setOnClickListener(v -> remove.onRemove(item));
        }
    }
}