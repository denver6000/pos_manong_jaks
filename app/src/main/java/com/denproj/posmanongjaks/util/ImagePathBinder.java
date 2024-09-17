package com.denproj.posmanongjaks.util;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
