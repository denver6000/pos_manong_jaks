package com.denproj.posmanongjaks.repository.imp;

import com.denproj.posmanongjaks.repository.base.ImageRepository;
import com.denproj.posmanongjaks.util.OnDataReceived;

public class ImageRepositoryImpl implements ImageRepository {
//    @Override
//    public void insertImage(Uri uri, String imageFileName, OnDataReceived<String> onDataReceived) {
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        storage.getReference(PRODUCT_IMAGE_PATH).child(imageFileName).putFile(uri).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                onDataReceived.onSuccess(task.getResult().getMetadata().getName());
//            } else {
//                onDataReceived.onFail(task.getException());
//            }
//        });
//    }

    @Override
    public void getImage(String path, OnDataReceived<String> onDataReceived) {

    }
}
