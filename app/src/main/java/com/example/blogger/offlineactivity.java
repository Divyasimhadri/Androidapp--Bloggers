package com.example.blogger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class offlineactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offlineactivity);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference("scores");
        scoresRef.keepSynced(true);

        scoresRef.keepSynced(false);

    }
}