package com.example.tp1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    EditText edmdp, ednom;
    Button quitter, valider;
    CheckBox stay_connected;
    Boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        // Check if the user is already connected
        isConnected = getSharedPreferences("isConnected", MODE_PRIVATE).getBoolean("isConnected", false);
        if (isConnected) {
            Intent i = new Intent(MainActivity.this, Accueil.class);
            startActivity(i);
        }
        edmdp = findViewById(R.id.auth_pwd);
        ednom = findViewById(R.id.auth_nom);
        quitter = findViewById(R.id.auth_quit_button);
        valider = findViewById(R.id.auth_validate_button);
        stay_connected = findViewById(R.id.stay_connected_btn);

        String sharedPreferences = getSharedPreferences("isConnected" , MODE_PRIVATE).toString();
        quitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finish();

            }
        });

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the connection status
                getSharedPreferences("isConnected", MODE_PRIVATE).edit().putBoolean("isConnected", stay_connected.isChecked()).apply();

                String nom = ednom.getText().toString();
                String pwd = edmdp.getText().toString();
                if (nom.isEmpty() || pwd.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (nom.equalsIgnoreCase("ouss") && pwd.equalsIgnoreCase("0000")) {
                    Intent i = new Intent(MainActivity.this, Accueil.class);
//                    i.putExtra("user","oussama");
                    startActivity(i);
                } else {
                    Toast.makeText(MainActivity.this, "Information Erroné", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
    }

    // Request permission CallBack function
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if(grantResults.length > 0){
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    // Permission granted
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
             else {
                // Permission denied
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }
}