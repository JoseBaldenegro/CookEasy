package com.josea.versionsimplecookeasy;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CookEasyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "CookEasy";
    private static final int DB_VERSION = 3;
    public static final String TABLE_RECETAS = "RECETAS";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NOMBRE = "NOMBRE";
    public static final String COLUMN_DESCRIPCION = "DESCRIPCION";
    public static final String COLUMN_CATEGORIA = "CATEGORIA";
    public static final String COLUMN_VIDEO_URL = "VIDEO_URL";
    public static final String COLUMN_FAVORITA = "FAVORITA";

    public static final String TABLE_INGREDIENTES = "INGREDIENTES";
    public static final String COLUMN_RECETA_ID = "RECETA_ID";
    public static final String COLUMN_INGREDIENTE_NOMBRE = "NOMBRE";
    public static final String COLUMN_CANTIDAD = "CANTIDAD";
    public static final String COLUMN_UNIDAD = "UNIDAD";

    public static final String TABLE_PASOS = "PASOS";
    public static final String COLUMN_ORDEN = "ORDEN";
    public static final String COLUMN_DESCRIPCION_PASO = "DESCRIPCION";

    CookEasyDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private static void insertReceta(SQLiteDatabase db, String nombre, String descripcion,
                                     String categoria, String videoUrl) {
        ContentValues recetaValues = new ContentValues();
        recetaValues.put(COLUMN_NOMBRE, nombre);
        recetaValues.put(COLUMN_DESCRIPCION, descripcion);
        recetaValues.put(COLUMN_CATEGORIA, categoria);
        recetaValues.put(COLUMN_VIDEO_URL, videoUrl);
        recetaValues.put(COLUMN_FAVORITA, 0);
        long recetaId = db.insert(TABLE_RECETAS, null, recetaValues);


        // Insertar ingredientes y pasos según la receta
        if (nombre.equals("Helado de Frutas")) {
            insertIngrediente(db, recetaId, "Fresas", "9", "");
            insertIngrediente(db, recetaId, "Plátano", "1", "");
            insertIngrediente(db, recetaId, "Crema de leche", "200", "ml");
            insertIngrediente(db, recetaId, "Leche Condensada", "6", "cucharadas");

            insertPaso(db, recetaId, 1, "Corta 9 fresas maduras: retira el tallo, pártelas por la mitad, y luego otra vez a la mitad. Reserva.");
            insertPaso(db, recetaId, 2, "Corta 1 banana madura: retira la cáscara y córtala en rodajas.");
            insertPaso(db, recetaId, 3, "En un bowl grande, agrega 200 ml de crema de leche bien fría.");
            insertPaso(db, recetaId, 4, "Bate con batidora hasta que espese y tenga consistencia firme.");
            insertPaso(db, recetaId, 5, "Agrega 6 cucharadas de leche condensada y bate 1 minuto más.");
            insertPaso(db, recetaId, 6, "En una licuadora, agrega las fresas y la banana cortadas. Licúa hasta obtener una mezcla homogénea.");
            insertPaso(db, recetaId, 7, "Añade la mezcla de frutas a la crema batida y mezcla para integrar bien.");
            insertPaso(db, recetaId, 8, "Pasa toda la mezcla a un recipiente apto para congelador.");
            insertPaso(db, recetaId, 9, "Decora con fresas al gusto.");
            insertPaso(db, recetaId, 10, "Lleva al congelador por al menos 8 horas, hasta que se convierta en helado.");
            insertPaso(db, recetaId, 11, "Sirve y disfruta este postre.");

        } else if (nombre.equals("Postre Navideño")) {
            insertIngrediente(db, recetaId, "Fresas", "700", "g");
            insertIngrediente(db, recetaId, "Miel", "80", "g");
            insertIngrediente(db, recetaId, "Agua", "120", "ml");
            insertIngrediente(db, recetaId, "Maizena (fécula de maiz)", "2", "cucharadas");
            insertIngrediente(db, recetaId, "Café", "200", "ml");
            insertIngrediente(db, recetaId, "Galletas de champagne", "200", "ml");
            insertIngrediente(db, recetaId, "Masa de hojaldre", "600", "g");
            insertIngrediente(db, recetaId, "Crema de leche", "100", "g");
            insertIngrediente(db, recetaId, "Canela en polvo", "", "al gusto");
            insertIngrediente(db, recetaId, "Yema de huevo", "1", "");

            insertPaso(db, recetaId, 1, "Corta 700 g de fresas maduras: retira el tallo, pártelas por la mitad, luego otra vez a la mitad y finalmente en cubitos.");
            insertPaso(db, recetaId, 2, "Pásalas a una olla mediana y agrega 80 g de miel y 120 ml de agua.");
            insertPaso(db, recetaId, 3, "Cocina a fuego medio durante 10 minutos, mezclando constantemente.");
            insertPaso(db, recetaId, 4, "Agrega 2 cucharadas de fécula de maíz y mezcla hasta que espese ligeramente.");
            insertPaso(db, recetaId, 5, "Pasa la mezcla a un recipiente y deja enfriar a temperatura ambiente.");
            insertPaso(db, recetaId, 6, "En un recipiente aparte, coloca 200 ml de café y moja 250 g de galletas tipo champagne. Reserva.");
            insertPaso(db, recetaId, 7, "Extiende 300 g de masa de hojaldre y coloca encima 50 g de crema de leche, dejando un borde libre.");
            insertPaso(db, recetaId, 8, "Espolvorea canela al gusto sobre la crema de leche.");
            insertPaso(db, recetaId, 9, "Agrega la mezcla de fresas (mermelada casera) y extiéndela bien.");
            insertPaso(db, recetaId, 10, "Coloca encima las galletas humedecidas en café.");
            insertPaso(db, recetaId, 11, "Cubre con otros 300 g de masa de hojaldre, sella los bordes con un tenedor y haz agujeros en la superficie.");
            insertPaso(db, recetaId, 12, "Coloca en una bandeja para hornear y pincela con una mezcla de 1 yema de huevo + 1 cucharada de crema de leche.");
            insertPaso(db, recetaId, 13, "Hornea en horno precalentado a 180 °C durante aproximadamente 25 minutos (el tiempo puede variar según tu horno).");
            insertPaso(db, recetaId, 14, "Retira, deja reposar un momento y sirve este postre navideño delicioso.");

        } else if (nombre.equals("Postre de Maracuyá con Cereza")) {
            insertIngrediente(db, recetaId, "Fresas", "700", "g");
            insertIngrediente(db, recetaId, "Miel", "80", "g");
            insertIngrediente(db, recetaId, "Agua", "120", "ml");
            insertIngrediente(db, recetaId, "Maizena (fécula de maiz)", "2", "cucharadas");
            insertIngrediente(db, recetaId, "Café", "200", "ml");
            insertIngrediente(db, recetaId, "Galletas de champagne", "200", "ml");
            insertIngrediente(db, recetaId, "Masa de hojaldre", "600", "g");
            insertIngrediente(db, recetaId, "Crema de leche", "100", "g");
            insertIngrediente(db, recetaId, "Canela en polvo", "", "al gusto");
            insertIngrediente(db, recetaId, "Yema de huevo", "1", "");

            insertPaso(db, recetaId, 1, "Corta 700 g de fresas maduras: retira el tallo, pártelas por la mitad, luego otra vez a la mitad y finalmente en cubitos.");
            insertPaso(db, recetaId, 2, "Pásalas a una olla mediana y agrega 80 g de miel y 120 ml de agua.");
            insertPaso(db, recetaId, 3, "Cocina a fuego medio durante 10 minutos, mezclando constantemente.");
            insertPaso(db, recetaId, 4, "Agrega 2 cucharadas de fécula de maíz y mezcla hasta que espese ligeramente.");
            insertPaso(db, recetaId, 5, "Pasa la mezcla a un recipiente y deja enfriar a temperatura ambiente.");
            insertPaso(db, recetaId, 6, "En un recipiente aparte, coloca 200 ml de café y moja 250 g de galletas tipo champagne. Reserva.");
            insertPaso(db, recetaId, 7, "Extiende 300 g de masa de hojaldre y coloca encima 50 g de crema de leche, dejando un borde libre.");
            insertPaso(db, recetaId, 8, "Espolvorea canela al gusto sobre la crema de leche.");
            insertPaso(db, recetaId, 9, "Agrega la mezcla de fresas (mermelada casera) y extiéndela bien.");
            insertPaso(db, recetaId, 10, "Coloca encima las galletas humedecidas en café.");
            insertPaso(db, recetaId, 11, "Cubre con otros 300 g de masa de hojaldre, sella los bordes con un tenedor y haz agujeros en la superficie.");
            insertPaso(db, recetaId, 12, "Coloca en una bandeja para hornear y pincela con una mezcla de 1 yema de huevo + 1 cucharada de crema de leche.");
            insertPaso(db, recetaId, 13, "Hornea en horno precalentado a 180 °C durante aproximadamente 25 minutos (el tiempo puede variar según tu horno).");
            insertPaso(db, recetaId, 14, "Retira, deja reposar un momento y sirve este postre navideño delicioso.");

        } else if (nombre.equals("Sopa de Verduras")) {
            insertIngrediente(db, recetaId, "Cebolla", "0.5", "unidad");
            insertIngrediente(db, recetaId, "Zanahoria", "1", "unidad");
            insertIngrediente(db, recetaId, "Apio", "200", "g");
            insertIngrediente(db, recetaId, "Pimiento rojo", "1", "unidad");
            insertIngrediente(db, recetaId, "Pimiento amarillo", "1", "unidad");
            insertIngrediente(db, recetaId, "Col", "200", "g");
            insertIngrediente(db, recetaId, "Tomate", "1", "unidad");
            insertIngrediente(db, recetaId, "Aceite vegetal", "40", "ml");
            insertIngrediente(db, recetaId, "Agua tibia", "1", "litro");
            insertIngrediente(db, recetaId, "Jugo de tomate", "400", "ml");
            insertIngrediente(db, recetaId, "Ajo", "1", "diente");
            insertIngrediente(db, recetaId, "Sal", "1", "cucharadita");
            insertIngrediente(db, recetaId, "Pimienta negra", "1", "pizca");
            insertIngrediente(db, recetaId, "Perejil", "al", "gusto");

            insertPaso(db, recetaId, 1, "Cortar una cebolla mediana en cubos pequeños y reservar");
            insertPaso(db, recetaId, 2, "Cortar una zanahoria pelada en medias lunas");
            insertPaso(db, recetaId, 3, "Cortar 200 g de apio en trozos gruesos");
            insertPaso(db, recetaId, 4, "Cortar un pimiento rojo en tiras finas");
            insertPaso(db, recetaId, 5, "Repetir el corte con un pimiento amarillo");
            insertPaso(db, recetaId, 6, "Cortar 200 g de repollo en tiras y luego en trozos pequeños");
            insertPaso(db, recetaId, 7, "Rallar un tomate entero usando la parte gruesa, sin piel");
            insertPaso(db, recetaId, 8, "En una olla a fuego medio-bajo, calentar 40 ml de aceite vegetal");
            insertPaso(db, recetaId, 9, "Agregar la cebolla y sofreír hasta dorar ligeramente");
            insertPaso(db, recetaId, 10, "Agregar la zanahoria y el apio, mezclar y cocinar 1 minuto");
            insertPaso(db, recetaId, 11, "Añadir el pimiento rojo y amarillo y cocinar hasta ablandar");
            insertPaso(db, recetaId, 12, "Agregar el repollo y 1 litro de agua tibia, tapar y cocinar 10 minutos");
            insertPaso(db, recetaId, 13, "Añadir el tomate rallado, 400 ml de zumo de tomate, un diente de ajo machacado, sal y pimienta");
            insertPaso(db, recetaId, 14, "Mezclar bien y cocinar tapado por 20 minutos");
            insertPaso(db, recetaId, 15, "Apagar el fuego y procesar la sopa hasta obtener un caldo espeso");
            insertPaso(db, recetaId, 16, "Servir y decorar con perejil al gusto");


        } else if (nombre.equals("Sopa de Calabaza")) {
            insertIngrediente(db, recetaId, "Calabaza", "1", "unidad (pequeña)");
            insertIngrediente(db, recetaId, "Tomates cherry", "100", "gramos");
            insertIngrediente(db, recetaId, "Cebolla morada", "1", "unidad");
            insertIngrediente(db, recetaId, "Zanahorias", "3", "unidades");
            insertIngrediente(db, recetaId, "Ajo", "1", "cabeza");
            insertIngrediente(db, recetaId, "Sal", "al", "gusto");
            insertIngrediente(db, recetaId, "Pimienta negra", "1", "pizca");
            insertIngrediente(db, recetaId, "Hierbas italianas", "al", "gusto");
            insertIngrediente(db, recetaId, "Aceite", "al", "gusto");
            insertIngrediente(db, recetaId, "Caldo de verduras", "500", "ml");
            insertIngrediente(db, recetaId, "Leche de coco", "200", "ml");
            insertIngrediente(db, recetaId, "Sal", "1", "cucharadita");
            insertIngrediente(db, recetaId, "Hojuelas de pimienta", "al", "gusto");
            insertIngrediente(db, recetaId, "Perejil", "al", "gusto");

            insertPaso(db, recetaId, 1, "Cortar una calabaza pequeña por la mitad y retirar semillas");
            insertPaso(db, recetaId, 2, "Colocar la calabaza en un refractario junto con 100 g de tomates cherry y media cebolla morada");
            insertPaso(db, recetaId, 3, "Añadir 3 zanahorias peladas y una cabeza de ajo cortada por arriba");
            insertPaso(db, recetaId, 4, "Sazonar con sal, pimienta negra y hierbas italianas al gusto");
            insertPaso(db, recetaId, 5, "Agregar un chorrito de aceite de oliva y llevar al horno a 180°C por 40 minutos");
            insertPaso(db, recetaId, 6, "Retirar del horno y quitar la cáscara de la calabaza y del ajo");
            insertPaso(db, recetaId, 7, "Licuar todo junto con 500 ml de caldo de verduras hasta homogeneizar");
            insertPaso(db, recetaId, 8, "Pasar la mezcla licuada a una olla a fuego medio-bajo");
            insertPaso(db, recetaId, 9, "Agregar 200 ml de leche de coco y 1 cucharadita de sal");
            insertPaso(db, recetaId, 10, "Cocinar por 5 minutos, mezclando bien");
            insertPaso(db, recetaId, 11, "Servir y decorar con leche de coco, copos de pimienta y perejil al gusto");

        } else if (nombre.equals("Sopa de Calabacín")) {
            insertIngrediente(db, recetaId, "Calabacín", "1", "unidad (grande)");
            insertIngrediente(db, recetaId, "Papas", "2", "unidades");
            insertIngrediente(db, recetaId, "Zanahorias", "2", "unidades");
            insertIngrediente(db, recetaId, "Mantequilla sin sal", "1", "cucharada");
            insertIngrediente(db, recetaId, "Cebolla", "1", "unidad");
            insertIngrediente(db, recetaId, "Ajo", "3", "dientes (en rodajas)");
            insertIngrediente(db, recetaId, "Caldo de vegetales", "24", "gramos (2 paquetes)");
            insertIngrediente(db, recetaId, "Agua caliente", "1", "litro");
            insertIngrediente(db, recetaId, "Pimienta dedo de niña", "1", "unidad");
            insertIngrediente(db, recetaId, "Crema agria", "200", "gramos");
            insertIngrediente(db, recetaId, "Sal", "2", "cucharaditas");
            insertIngrediente(db, recetaId, "Pimienta negra", "1", "pizca generosa");
            insertIngrediente(db, recetaId, "Perejil", "al", "gusto");

            insertPaso(db, recetaId, 1, "Cortar un calabacín grande en trozos pequeños y reservar");
            insertPaso(db, recetaId, 2, "Pelar y cortar dos papas medianas en trozos pequeños");
            insertPaso(db, recetaId, 3, "Cortar dos zanahorias en tiras finas y reservar");
            insertPaso(db, recetaId, 4, "En sartén grande a fuego medio-bajo, derretir 1 cucharada de mantequilla con un chorrito de aceite");
            insertPaso(db, recetaId, 5, "Agregar una cebolla picada en cubos y dorar ligeramente");
            insertPaso(db, recetaId, 6, "Añadir 3 dientes de ajo en rodajas y dorar");
            insertPaso(db, recetaId, 7, "Agregar las zanahorias, papas y calabacines, mezclar y cocinar 3 minutos");
            insertPaso(db, recetaId, 8, "Añadir 2 sobres de caldo de verduras (12 g c/u) disueltos en 1 litro de agua caliente");
            insertPaso(db, recetaId, 9, "Agregar un pimiento cortado en trocitos y cocinar 20 minutos");
            insertPaso(db, recetaId, 10, "Apagar el fuego y procesar todo hasta obtener una mezcla homogénea");
            insertPaso(db, recetaId, 11, "Volver la mezcla a la olla y calentar a fuego medio");
            insertPaso(db, recetaId, 12, "Añadir 200 g de crema de leche, 2 cucharaditas de sal y pimienta negra al gusto");
            insertPaso(db, recetaId, 13, "Cocinar 2 minutos más mezclando bien");
            insertPaso(db, recetaId, 14, "Agregar perejil al gusto y servir");

        }
    }

    private static void insertIngrediente(SQLiteDatabase db, long recetaId, String nombre,
                                          String cantidad, String unidad) {
        ContentValues ingredienteValues = new ContentValues();
        ingredienteValues.put(COLUMN_RECETA_ID, recetaId);
        ingredienteValues.put(COLUMN_INGREDIENTE_NOMBRE, nombre);
        ingredienteValues.put(COLUMN_CANTIDAD, cantidad);
        ingredienteValues.put(COLUMN_UNIDAD, unidad);
        db.insert(TABLE_INGREDIENTES, null, ingredienteValues);
    }

    private static void insertPaso(SQLiteDatabase db, long recetaId, int orden, String descripcion) {
        ContentValues pasoValues = new ContentValues();
        pasoValues.put(COLUMN_RECETA_ID, recetaId);
        pasoValues.put(COLUMN_ORDEN, orden);
        pasoValues.put(COLUMN_DESCRIPCION_PASO, descripcion);
        db.insert(TABLE_PASOS, null, pasoValues);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1){
            db.execSQL("CREATE TABLE " + TABLE_RECETAS + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_NOMBRE + " TEXT, "
                    + COLUMN_DESCRIPCION + " TEXT, "
                    + COLUMN_CATEGORIA + " TEXT, "
                    + COLUMN_VIDEO_URL + " TEXT, "
                    + COLUMN_FAVORITA + " INTEGER);");

            db.execSQL("CREATE TABLE " + TABLE_INGREDIENTES + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_RECETA_ID + " INTEGER, "
                    + COLUMN_INGREDIENTE_NOMBRE + " TEXT, "
                    + COLUMN_CANTIDAD + " TEXT, "
                    + COLUMN_UNIDAD + " TEXT);");

            db.execSQL("CREATE TABLE " + TABLE_PASOS + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_RECETA_ID + " INTEGER, "
                    + COLUMN_ORDEN + " INTEGER, "
                    + COLUMN_DESCRIPCION_PASO + " TEXT);");

            insertReceta(db, "Helado de Frutas",
                    "Delicioso helado natural con frutas frescas de temporada",
                    "Postres",
                    "https://github.com/JoseBaldenegro/recetario/raw/main/Postres/Postre%20Helado%20de%20Frutas.mp4");

            insertReceta(db, "Postre Navideño",
                    "Postre especial para celebraciones navideñas",
                    "Postres",
                    "https://github.com/JoseBaldenegro/recetario/raw/main/Postres/Postre%20Navide%C3%B1o.mp4");

            insertReceta(db, "Postre de Maracuyá con Cereza",
                    "Postre tropical refrescante con frutas exóticas",
                    "Postres",
                    "https://github.com/JoseBaldenegro/recetario/raw/main/Postres/Postre%20de%20Maracuya%20con%20Cereza.mp4");

            insertReceta(db, "Sopa de Verduras",
                    "Sopa nutritiva y saludable con verduras frescas",
                    "Sopas",
                    "https://github.com/JoseBaldenegro/recetario/raw/main/Sopas/Sopa%20De%20Verduras.mp4");

            insertReceta(db, "Sopa de Calabaza",
                    "Sopa cremosa de calabaza con especias",
                    "Sopas",
                    "https://github.com/JoseBaldenegro/recetario/raw/main/Sopas/Sopa%20De%20Calabaza.mp4");

            insertReceta(db, "Sopa de Calabacín",
                    "Sopa ligera y refrescante de calabacín",
                    "Sopas",
                    "https://github.com/JoseBaldenegro/recetario/raw/main/Sopas/Sopa%20De%20Calabacin.mp4");
        }
        if (oldVersion < 2){
            // Foto del usuario
            db.execSQL("ALTER TABLE RECETAS ADD COLUMN FOTO_PERSONAL TEXT;");
        }
        if (oldVersion < 3){
            // Actualizaciones futuras (?)
        }
    }
}