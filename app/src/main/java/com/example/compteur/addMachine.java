package com.example.compteur;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.compteur.config.RetrofitClient;
import com.example.compteur.machine.Machine;
import com.example.compteur.machine.MachineApi;
import com.google.firebase.database.*;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class addMachine extends AppCompatActivity {

    EditText nom;
    Button ajout;
    ListView machineList;
    DatabaseReference refMachines;
    ArrayList<String> machineData;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.compteur.R.layout.activity_add_machine);

        nom = findViewById(com.example.compteur.R.id.editTextText);
        ajout = findViewById(com.example.compteur.R.id.button);
        machineList = findViewById(R.id.machineList);

        refMachines = FirebaseDatabase.getInstance().getReference("arduino");
        machineData = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, machineData);
        machineList.setAdapter(adapter);

        // Ajouter machine
        ajout.setOnClickListener(v -> {
            String nomMachine = nom.getText().toString().trim();

            if (TextUtils.isEmpty(nomMachine)) {
                Toast.makeText(addMachine.this, "Entrer un nom de machine", Toast.LENGTH_SHORT).show();
            } else {
                MachineApi api = RetrofitClient.getRetrofitInstance().create(MachineApi.class);
                Machine machine = new Machine();
                machine.setReference(nomMachine);
                machine.setType("default"); // ou un champ que tu ajoutes dans ton UI
                machine.setMarque("default"); // ou un champ que tu ajoutes dans ton UI
                Call<Machine> call = api.createMachine(machine);
                call.enqueue(new Callback<Machine>() {
                    @Override
                    public void onResponse(Call<Machine> call, Response<Machine> response) {
                        if (response.isSuccessful()) {
                            // ✅ Si insertion SQL réussie, on ajoute dans Firebase
                            refMachines.child(nomMachine).child("nbpiecetotale").setValue(0);
                            refMachines.child(nomMachine).child("nbpiecetravaille").setValue(0);
                            Toast.makeText(addMachine.this, "Machine ajoutée avec succès", Toast.LENGTH_SHORT).show();
                            nom.setText("");
                        } else {
                            Toast.makeText(addMachine.this, "Erreur API SQL: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Machine> call, Throwable t) {
                        Toast.makeText(addMachine.this, "Erreur de connexion API", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });

        // Charger les machines existantes
        refMachines.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                machineData.clear();
                for (DataSnapshot machineSnap : snapshot.getChildren()) {
                    String nomMachine = machineSnap.getKey();
                    Machine m = machineSnap.getValue(Machine.class);
                    machineData.add(nomMachine + " ➤ " + m.nbpiecetotale + " total / " + m.nbpiecetravaille + " travaillées");
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(addMachine.this, "Erreur de chargement", Toast.LENGTH_SHORT).show();
            }
        });

        // Écouteur de clic pour ouvrir la page MachineDetailActivity
        machineList.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = machineData.get(position);
            String nomMachine = selectedItem.split("➤")[0].trim();  // Extraire le nom de la machine

            Intent intent = new Intent(addMachine.this, MachineDetailActivity.class);
            intent.putExtra("nom_machine", nomMachine);
            startActivity(intent);
        });
    }
}
