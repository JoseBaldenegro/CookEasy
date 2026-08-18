package com.josea.versionsimplecookeasy;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoPasosActivity extends Activity {

    private CookEasyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private int recetaId;
    private String nombreReceta;

    //Vistas
    private TextView txtNombreRecetaVideo;
    private VideoView videoViewReceta;
    private FrameLayout videoContainer;
    private LinearLayout layoutPasos;
    private MediaController mediaController;
    private ProgressBar progressBar;
    private TextView txtMensajeVideo;

    //Handler para reintentos para cargar el video
    private Handler retryHandler = new Handler();
    private static final int MAX_RETRIES = 3;
    private int retryCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_pasos);

        recetaId = getIntent().getIntExtra("RECETA_ID", -1);
        nombreReceta = getIntent().getStringExtra("NOMBRE_RECETA");

        dbHelper = new CookEasyDatabaseHelper(this);
        db = dbHelper.getReadableDatabase();

        inicializarVistas();
        cargarDatosReceta();
        configurarListeners();
    }

    private void inicializarVistas() {
        txtNombreRecetaVideo = findViewById(R.id.txtNombreRecetaVideo);
        videoViewReceta = findViewById(R.id.videoViewReceta);
        videoContainer = findViewById(R.id.videoContainer);
        layoutPasos = findViewById(R.id.layoutPasos);
        txtMensajeVideo = findViewById(R.id.txtMensajeVideo);

        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        params.gravity = android.view.Gravity.CENTER;
        progressBar.setLayoutParams(params);
        videoContainer.addView(progressBar);
        progressBar.setVisibility(View.GONE);

        //Configurar MediaController
        mediaController = new MediaController(this) {
            @Override
            public void show(int timeout) {
                super.show(0); // Mostrar siempre sin timeout automático
            }
        };
        mediaController.setAnchorView(videoContainer);
        videoViewReceta.setMediaController(mediaController);

        //VideoView
        videoViewReceta.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(View.GONE);
                txtMensajeVideo.setText("Video listo para reproducir");

                mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        // Ajustar según el tamaño del video
                        mediaController.setAnchorView(videoContainer);
                    }
                });

                videoViewReceta.start();
            }
        });

        videoViewReceta.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                progressBar.setVisibility(View.GONE);
                txtMensajeVideo.setText("Error al cargar el video. Reintentando...");

                if (retryCount < MAX_RETRIES) {
                    retryCount++;
                    retryHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            reintentarReproduccion();
                        }
                    }, 2000); // Reintentar después de 2 segundos
                } else {
                    manejarErrorPermanente();
                }
                return true;
            }
        });

        videoViewReceta.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        progressBar.setVisibility(View.VISIBLE);
                        txtMensajeVideo.setText("Cargando video...");
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        progressBar.setVisibility(View.GONE);
                        txtMensajeVideo.setText("Video cargado");
                        break;
                    case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                        progressBar.setVisibility(View.GONE);
                        txtMensajeVideo.setText("Reproduciendo...");
                        break;
                }
                return false;
            }
        });

        videoViewReceta.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                txtMensajeVideo.setText("Video finalizado");
            }
        });
    }

    private void cargarDatosReceta() {
        txtNombreRecetaVideo.setText(nombreReceta);
        cargarVideo();
        cargarPasos();
    }

    private void cargarVideo() {
        Cursor recetaCursor = db.rawQuery("SELECT VIDEO_URL FROM RECETAS WHERE _id = ?",
                new String[]{String.valueOf(recetaId)});

        if (recetaCursor.moveToFirst()) {
            int videoUrlIndex = recetaCursor.getColumnIndex("VIDEO_URL");
            if (videoUrlIndex != -1) {
                String videoUrl = recetaCursor.getString(videoUrlIndex);
                if (videoUrl != null && !videoUrl.isEmpty()) {
                    configurarVideo(videoUrl);
                } else {
                    manejarSinVideo();
                }
            } else {
                manejarSinVideo();
            }
        } else {
            manejarSinVideo();
        }
        recetaCursor.close();
    }

    private void configurarVideo(String videoUrl) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            txtMensajeVideo.setText("Preparando video...");

            Uri uri = Uri.parse(videoUrl);

            videoViewReceta.setVideoURI(uri);


        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(VideoPasosActivity.this, "Error: URL de video inválida - " + e.getMessage(), Toast.LENGTH_LONG).show();
            videoContainer.setVisibility(View.GONE);
        }
    }

    private void reintentarReproduccion() {
        Cursor recetaCursor = db.rawQuery("SELECT VIDEO_URL FROM RECETAS WHERE _id = ?",
                new String[]{String.valueOf(recetaId)});

        if (recetaCursor.moveToFirst()) {
            int videoUrlIndex = recetaCursor.getColumnIndex("VIDEO_URL");
            if (videoUrlIndex != -1) {
                String videoUrl = recetaCursor.getString(videoUrlIndex);
                if (videoUrl != null && !videoUrl.isEmpty()) {
                    txtMensajeVideo.setText("Reintentando reproducción (" + retryCount + "/" + MAX_RETRIES + ")");
                    configurarVideo(videoUrl);
                }
            }
        }
        recetaCursor.close();
    }

    private void manejarErrorPermanente() {
        txtMensajeVideo.setText("No se pudo cargar el video después de " + MAX_RETRIES + " intentos");
        Toast.makeText(this,
                "Error de reproducción. Verifica tu conexión o intenta más tarde.",
                Toast.LENGTH_LONG).show();
    }

    private void manejarSinVideo() {
        videoContainer.setVisibility(View.GONE);
        txtMensajeVideo.setVisibility(View.GONE);
    }

    private void cargarPasos() {
        layoutPasos.removeAllViews();

        Cursor pasosCursor = db.rawQuery("SELECT * FROM PASOS WHERE RECETA_ID = ? ORDER BY ORDEN",
                new String[]{String.valueOf(recetaId)});

        if (pasosCursor.getCount() == 0) {
            TextView textView = new TextView(this);
            textView.setText("No hay pasos disponibles");
            textView.setTextSize(16);
            textView.setTextColor(getResources().getColor(android.R.color.black));
            textView.setPadding(0, 8, 0, 8);
            layoutPasos.addView(textView);
        } else {
            while (pasosCursor.moveToNext()) {
                int ordenIndex = pasosCursor.getColumnIndex("ORDEN");
                int descripcionIndex = pasosCursor.getColumnIndex("DESCRIPCION");

                if (ordenIndex != -1 && descripcionIndex != -1) {
                    int orden = pasosCursor.getInt(ordenIndex);
                    String descripcion = pasosCursor.getString(descripcionIndex);

                    TextView textView = new TextView(this);
                    textView.setText(String.format("%d. %s", orden, descripcion));
                    textView.setTextSize(16);
                    textView.setTextColor(getResources().getColor(android.R.color.black));
                    textView.setPadding(0, 12, 0, 12);
                    textView.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));

                    layoutPasos.addView(textView);
                }
            }
        }
        pasosCursor.close();
    }

    private void configurarListeners() {
        Button btnVolverAbajo = findViewById(R.id.btnVolverAbajo);
        btnVolverAbajo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Volver a la actividad anterior
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoViewReceta != null && videoViewReceta.isPlaying()) {
            videoViewReceta.pause();
        }
        retryHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (videoViewReceta != null && !videoViewReceta.isPlaying()) {
            //No reiniciar automáticamente, dejar que el usuario decida
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoViewReceta != null) {
            videoViewReceta.stopPlayback();
        }
        retryHandler.removeCallbacksAndMessages(null);
        if (db != null) {
            db.close();
        }
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}