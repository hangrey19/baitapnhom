package com.example.baitapnhom.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.baitapnhom.R;

public class ImageLoader {

    /**
     * Load ảnh thông minh:
     * - Nếu imageUrl là tên drawable (vd: "fruit_tao") → load từ drawable local
     * - Nếu imageUrl là URL (bắt đầu bằng http) → load từ internet
     */
    public static void load(ImageView imageView, String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageView.setImageResource(R.drawable.bg_placeholder);
            return;
        }

        Context ctx = imageView.getContext();

        if (imageUrl.startsWith("http")) {
            // Load từ URL
            Glide.with(ctx)
                    .load(imageUrl)
                    .placeholder(R.drawable.bg_placeholder)
                    .error(R.drawable.bg_placeholder)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView);
        } else {
            // Load từ drawable theo tên
            int resId = ctx.getResources().getIdentifier(
                    imageUrl, "drawable", ctx.getPackageName());

            if (resId != 0) {
                Glide.with(ctx)
                        .load(resId)
                        .placeholder(R.drawable.bg_placeholder)
                        .error(R.drawable.bg_placeholder)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(imageView);
            } else {
                imageView.setImageResource(R.drawable.bg_placeholder);
            }
        }
    }
}