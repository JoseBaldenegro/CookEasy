package com.josea.versionsimplecookeasy;

import android.app.Activity;
import android.content.*;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.*;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetalleRecetaActivity extends Activity {

    private CookEasyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private int recetaId;

    // Vistas
    private TextView txtNombreRecetaDetalle;
    private ImageView imgRecetaDetalle, imgResultadoUsuario;
    private CheckBox chkFavoritaDetalle;
    private LinearLayout layoutIngredientes;
    private Button btnTomarFoto, btnSeleccionarFoto, btnCompartir, btnVerVideoPasos;

    // Constantes y variables
    private static final int REQUEST_IMAGE_CAPTURE = 1, REQUEST_PICK_IMAGE = 2, REQUEST_CAMERA_PERMISSION = 100;
    private String currentPhotoPath;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_receta);

        recetaId = getIntent().getIntExtra("RECETA_ID", -1);
        dbHelper = new CookEasyDatabaseHelper(this);
        db = dbHelper.getReadableDatabase();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        inicializarVistas();
        cargarDatosReceta();
        cargarFotoGuardada();
        configurarListeners();
    }

    private void inicializarVistas() {
        txtNombreRecetaDetalle = findViewById(R.id.txtNombreRecetaDetalle);
        imgRecetaDetalle = findViewById(R.id.imgRecetaDetalle);
        imgResultadoUsuario = findViewById(R.id.imgResultadoUsuario);
        chkFavoritaDetalle = findViewById(R.id.chkFavoritaDetalle);
        layoutIngredientes = findViewById(R.id.layoutIngredientes);
        btnTomarFoto = findViewById(R.id.btnTomarFoto);
        btnSeleccionarFoto = findViewById(R.id.btnSeleccionarFoto);
        btnCompartir = findViewById(R.id.btnCompartir);
        btnVerVideoPasos = findViewById(R.id.btnVerVideoPasos);

        //Escalado automático
        imgResultadoUsuario.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imgResultadoUsuario.setAdjustViewBounds(true);
    }

    private void cargarDatosReceta() {
        Cursor cursor = db.rawQuery("SELECT NOMBRE, FAVORITA FROM RECETAS WHERE _id = ?",
                new String[]{String.valueOf(recetaId)});

        if (cursor.moveToFirst()) {
            txtNombreRecetaDetalle.setText(cursor.getString(0));
            chkFavoritaDetalle.setChecked(cursor.getInt(1) == 1);
            configurarImagenPorNombre(imgRecetaDetalle, cursor.getString(0));
            cargarIngredientes();
        } else {
            Toast.makeText(this, "Receta no encontrada", Toast.LENGTH_SHORT).show();
            finish();
        }
        cursor.close();
    }

    private void cargarIngredientes() {
        layoutIngredientes.removeAllViews();
        Cursor cursor = db.rawQuery("SELECT NOMBRE, CANTIDAD, UNIDAD FROM INGREDIENTES WHERE RECETA_ID = ? ORDER BY _id",
                new String[]{String.valueOf(recetaId)});

        if (cursor.getCount() == 0) {
            agregarTextoALayout("No hay ingredientes disponibles", layoutIngredientes);
        } else {
            while (cursor.moveToNext()) {
                String texto = String.format("• %s: %s %s", cursor.getString(0), cursor.getString(1), cursor.getString(2));
                agregarTextoALayout(texto, layoutIngredientes);
            }
        }
        cursor.close();
    }

    private void agregarTextoALayout(String texto, LinearLayout layout) {
        TextView tv = new TextView(this);
        tv.setText(texto);
        tv.setTextSize(16);
        tv.setTextColor(getResources().getColor(android.R.color.black));
        tv.setPadding(0, 8, 0, 8);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(tv);
    }

    private void configurarImagenPorNombre(ImageView imageView, String nombreReceta) {
        int[] imagenes = {R.drawable.helado_frutas, R.drawable.postre_navideno, R.drawable.postre_maracuya,
                R.drawable.sopa_verduras, R.drawable.sopa_calabaza, R.drawable.sopa_calabacin};
        String[] nombres = {"Helado de Frutas", "Postre Navideño", "Postre de Maracuyá con Cereza",
                "Sopa de Verduras", "Sopa de Calabaza", "Sopa de Calabacín"};

        for (int i = 0; i < nombres.length; i++) {
            if (nombres[i].equals(nombreReceta)) {
                imageView.setImageResource(imagenes[i]);
                return;
            }
        }
        imageView.setImageResource(R.drawable.placeholder);
    }

    private void cargarFotoGuardada() {
        String fotoPath = sharedPreferences.getString("foto_receta_" + recetaId, null);
        if (fotoPath != null && new File(fotoPath).exists()) {
            imgResultadoUsuario.setImageURI(Uri.fromFile(new File(fotoPath)));
        }
    }

    private void configurarListeners() {
        //Favoritos
        chkFavoritaDetalle.setOnClickListener(v -> {
            boolean isChecked = ((CheckBox) v).isChecked();
            db.execSQL("UPDATE RECETAS SET FAVORITA = ? WHERE _id = ?", new Object[]{isChecked ? 1 : 0, recetaId});
            Toast.makeText(this, isChecked ? "Agregado a favoritos" : "Removido de favoritos", Toast.LENGTH_SHORT).show();
        });

        //Fotos
        btnTomarFoto.setOnClickListener(v -> tomarFoto());
        btnSeleccionarFoto.setOnClickListener(v -> seleccionarFoto());

        //Compartir y navegación
        btnCompartir.setOnClickListener(v -> compartirReceta());
        btnVerVideoPasos.setOnClickListener(v -> {
            Intent intent = new Intent(this, VideoPasosActivity.class);
            intent.putExtra("RECETA_ID", recetaId);
            intent.putExtra("NOMBRE_RECETA", txtNombreRecetaDetalle.getText().toString());
            startActivity(intent);
        });
    }

    private void tomarFoto() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            iniciarCamara();
        }
    }

    private void iniciarCamara() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                File photoFile = crearArchivoImagen();
                Uri photoURI = FileProvider.getUriForFile(this, "com.josea.versionsimplecookeasy.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } catch (IOException e) {
                Toast.makeText(this, "Error creando archivo", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No hay aplicación de cámara", Toast.LENGTH_SHORT).show();
        }
    }

    private File crearArchivoImagen() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "RECETA_" + recetaId + "_" + timeStamp + ".jpg";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) storageDir.mkdirs();

        File image = new File(storageDir, imageFileName);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void seleccionarFoto() {
        startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), REQUEST_PICK_IMAGE);
    }

    private void compartirReceta() {
        Cursor cursor = db.rawQuery("SELECT NOMBRE FROM RECETAS WHERE _id = ?",
                new String[]{String.valueOf(recetaId)});

        if (cursor.moveToFirst()) {
            String nombreReceta = cursor.getString(0);
            String shareText = "¡Mira cómo me quedó mi " + nombreReceta + "! #CookEasy";

            //Verificar si hay una foto guardada
            String fotoPath = sharedPreferences.getString("foto_receta_" + recetaId, null);

            if (fotoPath != null && new File(fotoPath).exists()) {
                //Compartir con foto y texto
                File fotoFile = new File(fotoPath);
                Uri fotoUri = FileProvider.getUriForFile(this,
                        "com.josea.versionsimplecookeasy.fileprovider", fotoFile);

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                shareIntent.putExtra(Intent.EXTRA_STREAM, fotoUri);

                // Otorgar permisos temporales
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                startActivity(Intent.createChooser(shareIntent, "Compartir mi resultado"));
            } else {
                // Si no hay foto, mostrar mensaje al usuario
                Toast.makeText(this, "Primero toma o selecciona una foto para compartir", Toast.LENGTH_LONG).show();
            }
        }
        cursor.close();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            iniciarCamara();
        } else {
            Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                //Foto de cámara
                imgResultadoUsuario.setImageURI(Uri.fromFile(new File(currentPhotoPath)));
                guardarRutaFoto(currentPhotoPath);
                Toast.makeText(this, "Foto tomada y guardada", Toast.LENGTH_SHORT).show();

            } else if (requestCode == REQUEST_PICK_IMAGE && data != null && data.getData() != null) {
                //Foto de galería
                Uri selectedImage = data.getData();
                imgResultadoUsuario.setImageURI(selectedImage);

                //Guardar copia localmente
                guardarCopiaImagen(selectedImage);
                Toast.makeText(this, "Foto seleccionada", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void guardarCopiaImagen(Uri imageUri) {
        try {
            //Crear archivo destino
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String imageFileName = "RECETA_" + recetaId + "_GALERIA_" + timeStamp + ".jpg";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if (!storageDir.exists()) storageDir.mkdirs();

            File destFile = new File(storageDir, imageFileName);

            //Copiar archivo
            InputStream in = getContentResolver().openInputStream(imageUri);
            OutputStream out = new FileOutputStream(destFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

            in.close();
            out.flush();
            out.close();

            //Guardar ruta
            guardarRutaFoto(destFile.getAbsolutePath());

        } catch (Exception e) {
            Log.e("GuardarCopia", "Error: " + e.getMessage());
            Toast.makeText(this, "Error al guardar imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarRutaFoto(String filePath) {
        sharedPreferences.edit().putString("foto_receta_" + recetaId, filePath).apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) db.close();
        if (dbHelper != null) dbHelper.close();
    }
}