package com.example.testfirestoreapp;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
//어노테이션 필수임
@GlideModule
public class GlideMudule extends AppGlideModule {

    @Override
    public void registerComponents(Context context,  Glide glide,  Registry registry) {
        super.registerComponents(context, glide, registry);
        registry.append(StorageReference.class, InputStream.class,
                new FirebaseImageLoader.Factory());
    }
}
