package com.paszlelab.dcroarapp.Utilities;

import android.content.Context;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.module.AppGlideModule;
import com.paszlelab.dcroarapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

@GlideModule
public class RetrieveImage extends AppGlideModule {

    static String prefix = "https://firebasestorage.googleapis.com/v0/b/dcroarchat.appspot.com/o/profileImages%2F";
    static String suffix = ".jpeg?alt=media";

    public static void getImg(Context context, String userId, ImageView imgView) {

        String uri = prefix+userId+suffix;

        Glide.with(context)
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.profile)
                .into(imgView);

    }

    public static void getImg(Fragment fragment, String userId, ImageView imgView) {

        String uri = prefix+userId+suffix;

        Glide.with(fragment)
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.profile)
                .into(imgView);

    }


    public static void getImg(Context context, String userId, CircleImageView imgView) {

        String uri = prefix+userId+suffix;

        Glide.with(context)
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.profile)
                .into(imgView);

    }

    public static void getImg(Fragment fragment, String userId, CircleImageView imgView) {

        String uri = prefix+userId+suffix;

        Glide.with(fragment)
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.profile)
                .into(imgView);

    }
}
