package com.example.compteur;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    TextView nbtraivaill, nbtotal;
    Button rez,add;
    DatabaseReference refTotal, refTravaille;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nbtraivaill = findViewById(R.id.nbtravaille);
        nbtotal = findViewById(R.id.nbtotal);
        rez = findViewById(R.id.rez);
        add = findViewById(R.id.add);

        // Initialize Firebase Realtime Database references
        refTotal = FirebaseDatabase.getInstance().getReference("arduino/MACH-001/nbpiecetotale");
        refTravaille = FirebaseDatabase.getInstance().getReference("arduino/MACH-001/nbpiecetravaille");

        // Listen for changes to total
        refTotal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Long val = snapshot.getValue(Long.class);
                nbtotal.setText(String.valueOf(val));
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        // Listen for changes to travaille
        refTravaille.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Long val = snapshot.getValue(Long.class);
                nbtraivaill.setText(String.valueOf(val));
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        // Reset button
        rez.setOnClickListener(v -> {
            refTravaille.setValue(0); // Reset travaille to 0
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, addMachine.class);
                startActivity(intent);
            }
        });
    }
}
