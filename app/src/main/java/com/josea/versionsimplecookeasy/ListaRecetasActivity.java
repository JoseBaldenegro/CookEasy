package com.josea.versionsimplecookeasy;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ListaRecetasActivity extends Activity {

    private ListView listViewRecetas;
    private TextView txtTituloCategoria;
    private CookEasyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private String categoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_recetas);

        //Obtener categoría del intent
        categoria = getIntent().getStringExtra("CATEGORIA");

        //Inicializar base de datos
        dbHelper = new CookEasyDatabaseHelper(this);
        db = dbHelper.getReadableDatabase();

        listViewRecetas = findViewById(R.id.listViewRecetas);
        txtTituloCategoria = findViewById(R.id.txtTituloCategoria);

        // Configurar título
        txtTituloCategoria.setText(categoria);

        cargarRecetas();
    }

    private void cargarRecetas() {
        Cursor cursor;
        if (categoria.equals("Favoritos")) {
            cursor = db.rawQuery("SELECT _id, NOMBRE, DESCRIPCION, FAVORITA FROM RECETAS WHERE FAVORITA = 1 ORDER BY NOMBRE", null);
        } else {
            cursor = db.rawQuery("SELECT _id, NOMBRE, DESCRIPCION, FAVORITA FROM RECETAS WHERE CATEGORIA = ? ORDER BY NOMBRE",
                    new String[]{categoria});
        }

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No hay recetas disponibles", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] from = new String[]{"NOMBRE", "DESCRIPCION"};
        int[] to = new int[]{R.id.txtNombreReceta, R.id.txtDescripcion};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.item_receta_lista,
                cursor,
                from,
                to,
                0
        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                Cursor cursor = (Cursor) getItem(position);

                //Obtener nombre de la receta para la imagen
                int nombreIndex = cursor.getColumnIndex("NOMBRE");
                String nombreReceta = cursor.getString(nombreIndex);

                //Configurar imagen según nombre
                ImageView imgReceta = view.findViewById(R.id.imgReceta);
                configurarImagenPorNombre(imgReceta, nombreReceta);

                //Verificar que las columnas existen antes de acceder a ellas
                int favoritaIndex = cursor.getColumnIndex("FAVORITA");
                int idIndex = cursor.getColumnIndex("_id");

                if (favoritaIndex != -1 && idIndex != -1) {
                    CheckBox chkFavorita = view.findViewById(R.id.chkFavorita);
                    int favorita = cursor.getInt(favoritaIndex);
                    chkFavorita.setChecked(favorita == 1);

                    final int recetaId = cursor.getInt(idIndex);
                    chkFavorita.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean isChecked = ((CheckBox) v).isChecked();
                            actualizarFavorito(recetaId, isChecked);
                            if (isChecked) {
                                Toast.makeText(ListaRecetasActivity.this, "Agregado a favoritos", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ListaRecetasActivity.this, "Removido de favoritos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                return view;
            }
        };

        listViewRecetas.setAdapter(adapter);

        listViewRecetas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("ListaRecetas", "Item clickeado en posición: " + position);

                Cursor cursor = (Cursor) adapter.getItem(position);
                if (cursor != null) {
                    int idIndex = cursor.getColumnIndex("_id");
                    if (idIndex != -1) {
                        int recetaId = cursor.getInt(idIndex);
                        Log.d("ListaRecetas", "ID de receta: " + recetaId);

                        Intent intent = new Intent(ListaRecetasActivity.this, DetalleRecetaActivity.class);
                        intent.putExtra("RECETA_ID", recetaId);
                        startActivity(intent);
                    } else {
                        Log.e("ListaRecetas", "No se encontró la columna _id");
                    }
                } else {
                    Log.e("ListaRecetas", "Cursor es null");
                }
            }
        });

        listViewRecetas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ListaRecetasActivity.this, "Long click en posición: " + position, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void configurarImagenPorNombre(ImageView imageView, String nombreReceta) {
        int resourceId;
        switch (nombreReceta) {
            case "Helado de Frutas":
                resourceId = R.drawable.helado_frutas;
                break;
            case "Postre Navideño":
                resourceId = R.drawable.postre_navideno;
                break;
            case "Postre de Maracuyá con Cereza":
                resourceId = R.drawable.postre_maracuya;
                break;
            case "Sopa de Verduras":
                resourceId = R.drawable.sopa_verduras;
                break;
            case "Sopa de Calabaza":
                resourceId = R.drawable.sopa_calabaza;
                break;
            case "Sopa de Calabacín":
                resourceId = R.drawable.sopa_calabacin;
                break;
            default:
                resourceId = R.drawable.placeholder;
        }
        imageView.setImageResource(resourceId);
    }

    private void actualizarFavorito(int recetaId, boolean esFavorito) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("UPDATE RECETAS SET FAVORITA = ? WHERE _id = ?",
                new Object[]{esFavorito ? 1 : 0, recetaId});
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}