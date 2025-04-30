package com.example.compteur;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.compteur.machine.Machine;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class MachineDetailActivity extends AppCompatActivity {

    TextView textMachineName, nbTravaille, nbTotal;
    Button btnResetTravaille;
    DatabaseReference refMachine;
    String nomMachine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_detail);

        // Récupérer les vues
        textMachineName = findViewById(R.id.textMachineName);
        nbTravaille = findViewById(R.id.nbtravaille);
        nbTotal = findViewById(R.id.nbtotal);
        btnResetTravaille = findViewById(R.id.btnResetTravaille);

        // Récupérer le nom de la machine passé par l'intent
        nomMachine = getIntent().getStringExtra("nom_machine");

        // Référence à la machine dans Firebase
        refMachine = FirebaseDatabase.getInstance().getReference("arduino").child(nomMachine);

        // Afficher les données de la machine
        refMachine.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Récupérer les valeurs
                    Machine machine = snapshot.getValue(Machine.class);
                    if (machine != null) {
                        textMachineName.setText("Machine : " + nomMachine);
                        nbTravaille.setText(String.valueOf(machine.nbpiecetravaille));
                        nbTotal.setText(String.valueOf(machine.nbpiecetotale));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MachineDetailActivity.this, "Erreur de chargement des données", Toast.LENGTH_SHORT).show();
            }
        });

        // Réinitialiser nbpiecetravaille à 0
        btnResetTravaille.setOnClickListener(v -> {
            refMachine.child("nbpiecetravaille").setValue(0, (error, ref) -> {
                if (error == null) {
                    Toast.makeText(MachineDetailActivity.this, "Nombre de pièces travaillées réinitialisé", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MachineDetailActivity.this, "Erreur lors de la réinitialisation", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
