package com.josea.versionsimplecookeasy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    private Button btnPostres, btnSopas, btnFavoritos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPostres = findViewById(R.id.btnPostres);
        btnSopas = findViewById(R.id.btnSopas);
        btnFavoritos = findViewById(R.id.btnFavoritos);

        btnPostres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCategoria("Postres");
            }
        });

        btnSopas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCategoria("Sopas");
            }
        });

        btnFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCategoria("Favoritos");
            }
        });
    }

    private void abrirCategoria(String categoria) {
        Intent intent = new Intent(MainActivity.this, ListaRecetasActivity.class);
        intent.putExtra("CATEGORIA", categoria);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "Bienvenido a CookEasy", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}