package com.juego.alvaros.vistas;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.juego.alvaros.BaseDatos.BDRanking;
import com.juego.alvaros.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alvaro del Rio, Alvaro Santillana, Alvaro Velasco
 * @version 1.0 23/12/2019
 */
public class FragmentRanking extends Fragment {
    //Atributos
    private static Context mContext;
    private static ListView listaRanking;
    private static SQLiteDatabase db;

    public FragmentRanking() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);
        listaRanking = view.findViewById(R.id.idRanking);

        abrirBD();
        return view;
    }


    private void abrirBD(){
        //Abrimos la base de datos 'BDRanking' en modo escritura
        BDRanking ranking = new BDRanking(mContext, "BDRanking", null, 12);
        db = ranking.getWritableDatabase();
        borrarExcesoDatos();
        listar();
    }

    public static void addRegistro(String nom, int puntos, int niv){
        db.execSQL("INSERT INTO Ranking (nombre, puntuacion, nivel) VALUES ('"+ nom +"',"+puntos+","+niv+")");
        listar();
    }

    public static boolean mejorPuntuacion(int puntos){
        Cursor c=db.rawQuery("SELECT * FROM Ranking WHERE puntuacion > ?", new String[]{String.valueOf(puntos)});
        if (c.getCount() < 10){
            c.close();
            return true;
        } else {
            c.close();
            return true;
        }
    }

    public static int obtenerNiveles(){
        Cursor c=db.rawQuery("SELECT MAX(nivel) FROM Ranking", null);
        if (c.getCount() == 0){
            c.close();
            return 1;
        } else {
            c.moveToFirst();
            int nivel = c.getInt(0);
            c.close();
            return nivel;
        }
    }

    private void borrarExcesoDatos(){
        Cursor c=db.rawQuery("SELECT * FROM Ranking ORDER BY puntuacion ASC", null);
        if(c.getCount()> 10){
            c.moveToFirst();
            int cod = c.getInt(0);
            db.execSQL("DELETE FROM Ranking WHERE codigo=?", new String[]{String.valueOf(cod)});
            borrarExcesoDatos();
            c.close();
        }
    }

    private static void listar(){
        ArrayAdapter<String> adaptador;
        List<String> lista = new ArrayList<String>();

        Cursor c=db.rawQuery("SELECT * FROM Ranking ORDER BY puntuacion DESC", null);
        if(c.getCount()==0)
            lista.add("No hay registros");
        else{
            while(c.moveToNext() && lista.size() <= 10) {
                lista.add(c.getString(1)+" - "+c.getInt(2)+" - Nivel: "+c.getInt(3));
            }
        }
        adaptador=new ArrayAdapter<>(mContext,R.layout.fila_ranking,lista);
        listaRanking.setAdapter(adaptador);

        c.close();
    }

    //Para obtener el contexto
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }
}
