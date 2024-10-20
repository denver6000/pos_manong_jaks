package com.denproj.posmanongjaks.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.storage.FirebaseStorage;

public class ImagePathBinder {

    static FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();


    public static void bindImagePathToImageView(String imagePath, ImageView imageView, Context context, OnImageBound onImageBound) {
        firebaseStorage.getReference(imagePath)
                .getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    Glide
                            .with(context)
                            .load(uri)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imageView);
                    onImageBound.onBound();
                }).addOnCanceledListener(() -> {
                    onImageBound.onError(new Exception("Something went wrong in fetching image."));
                });
    }

    public interface OnImageBound {
        void onBound();

        void onError(Exception e);
    }

}
