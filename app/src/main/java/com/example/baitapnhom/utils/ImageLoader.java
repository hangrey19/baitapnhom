package com.example.baitapnhom.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.baitapnhom.R;

public class ImageLoader {
    public static void load(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.bg_placeholder)
                .error(R.drawable.bg_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }
}
