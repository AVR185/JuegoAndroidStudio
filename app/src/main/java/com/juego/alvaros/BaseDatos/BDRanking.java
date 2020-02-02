package com.juego.alvaros.BaseDatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Button;

public class BDRanking extends SQLiteOpenHelper {
    //Atributo
    String sqlCreate = "CREATE TABLE Ranking (codigo INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, puntuacion INTEGER, nivel INTEGER)";

    public BDRanking(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Si no existe la base de datos la crea y ejecuta los siguientes comandos
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Ranking");

        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
    }
}
