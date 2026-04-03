package com.example.baitapnhom.ui.product;

import android.graphics.Paint;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.baitapnhom.data.local.entity.Product;
import com.example.baitapnhom.databinding.ItemProductGridBinding;
import com.example.baitapnhom.databinding.ItemProductHorizontalBinding;
import com.example.baitapnhom.utils.CurrencyFormatter;
import com.example.baitapnhom.utils.ImageLoader;

public class ProductAdapter extends ListAdapter<Product, RecyclerView.ViewHolder> {

    private static final int TYPE_GRID  = 0;
    private static final int TYPE_HORIZ = 1;

    public interface OnProductClickListener { void onClick(Product product); }
    public interface OnAddCartListener      { void onAdd(Product product); }

    private final boolean                isHorizontal;
    private final OnProductClickListener clickListener;
    private final OnAddCartListener      cartListener;

    private static final DiffUtil.ItemCallback<Product> DIFF =
            new DiffUtil.ItemCallback<Product>() {
                @Override public boolean areItemsTheSame(@NonNull Product a, @NonNull Product b)    { return a.id == b.id; }
                @Override public boolean areContentsTheSame(@NonNull Product a, @NonNull Product b) { return a.price == b.price && a.rating == b.rating && a.stock == b.stock; }
            };

    public ProductAdapter(boolean isHorizontal, OnProductClickListener click, OnAddCartListener cart) {
        super(DIFF);
        this.isHorizontal  = isHorizontal;
        this.clickListener = click;
        this.cartListener  = cart;
    }

    @Override public int getItemViewType(int pos) { return isHorizontal ? TYPE_HORIZ : TYPE_GRID; }

    @NonNull @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_HORIZ)
            return new HorizHolder(ItemProductHorizontalBinding.inflate(inf, parent, false));
        return new GridHolder(ItemProductGridBinding.inflate(inf, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {
        Product p = getItem(pos);
        if (holder instanceof GridHolder)  ((GridHolder)  holder).bind(p);
        if (holder instanceof HorizHolder) ((HorizHolder) holder).bind(p);
    }

    class GridHolder extends RecyclerView.ViewHolder {
        final ItemProductGridBinding b;
        GridHolder(ItemProductGridBinding b) { super(b.getRoot()); this.b = b; }

        void bind(Product p) {
            ImageLoader.load(b.ivProduct, p.imageUrl);
            b.tvName.setText(p.name);
            b.tvPrice.setText(CurrencyFormatter.format(p.price));
            b.tvRating.setText(String.valueOf(p.rating));
            b.tvSold.setText("Đã bán " + p.soldCount);

            if (p.getDiscountPercent() > 0) {
                b.tvDiscount.setVisibility(View.VISIBLE);
                b.tvDiscount.setText("-" + p.getDiscountPercent() + "%");

                b.tvOriginalPrice.setVisibility(View.VISIBLE);
                b.tvOriginalPrice.setText(CurrencyFormatter.format(p.originalPrice));
                // Strikethrough bằng code — XML paintFlags không hoạt động
                b.tvOriginalPrice.setPaintFlags(
                        b.tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                b.tvDiscount.setVisibility(View.GONE);
                b.tvOriginalPrice.setVisibility(View.GONE);
                // Xóa strikethrough nếu không có giảm giá (tránh lỗi recycle)
                b.tvOriginalPrice.setPaintFlags(
                        b.tvOriginalPrice.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            }

            b.tvFlash.setVisibility(p.isFlashSale ? View.VISIBLE : View.GONE);
            b.getRoot().setOnClickListener(v -> { if (clickListener != null) clickListener.onClick(p); });
            b.btnAddCart.setOnClickListener(v -> { if (cartListener != null) cartListener.onAdd(p); });
        }
    }

    class HorizHolder extends RecyclerView.ViewHolder {
        final ItemProductHorizontalBinding b;
        HorizHolder(ItemProductHorizontalBinding b) { super(b.getRoot()); this.b = b; }

        void bind(Product p) {
            ImageLoader.load(b.ivProduct, p.imageUrl);
            b.tvName.setText(p.name);
            b.tvPrice.setText(CurrencyFormatter.format(p.price));
            b.tvRating.setText("⭐ " + p.rating);

            if (p.getDiscountPercent() > 0) {
                b.tvDiscount.setVisibility(View.VISIBLE);
                b.tvDiscount.setText("-" + p.getDiscountPercent() + "%");
            } else {
                b.tvDiscount.setVisibility(View.GONE);
            }

            b.getRoot().setOnClickListener(v -> { if (clickListener != null) clickListener.onClick(p); });
        }
    }
}