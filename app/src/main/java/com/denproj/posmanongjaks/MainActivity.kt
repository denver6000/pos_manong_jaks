package com.denproj.posmanongjaks

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.denproj.posmanongjaks.databinding.ActivityMainBinding
import com.denproj.posmanongjaks.viewModel.MainViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.PersistentCacheSettings
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    var viewModel: MainViewModel? = null
    var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setLocalCacheSettings(
                PersistentCacheSettings.newBuilder()
                    .setSizeBytes((200 * 1024).toLong())
                    .build()
            )
            .build()

        db.firestoreSettings = settings
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        FirebaseFirestore.getInstance().setFirestoreSettings(settings)
        setContentView(binding!!.root)
    }
}