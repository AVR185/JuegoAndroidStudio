package com.juego.alvaros.vistas;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import com.google.android.material.tabs.TabLayout;
import com.juego.alvaros.MainActivity;
import com.juego.alvaros.R;
import com.juego.alvaros.vistas.dialogos.DialogInfoAutor;

import java.util.Objects;

/**
 * @author Alvaro del Rio, Alvaro Santillana, Alvaro Velasco
 * @version 1.0 23/12/2019
 */
public class FragmentCreditos extends Fragment implements ListView.OnItemClickListener{
    private TabLayout tabLayout;
    private static int posicion;


    public FragmentCreditos() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_creditos, container, false);
        //Atributos
        ImageButton boton = view.findViewById(R.id.idButonHomeCreditos);
        tabLayout = MainActivity.getTabLayout();

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(tabLayout.getTabAt(2)).select();
            }
        });

        ListView listView = view.findViewById(R.id.idListaAutores);
        listView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        DialogInfoAutor dialogoAutores = new DialogInfoAutor();
        dialogoAutores.show(Objects.requireNonNull(getFragmentManager()),"Datos de los autores");

        posicion = i;
    }


    //Getter para conocer la opcion de la lista pulsada
    public static int getPosicion() {
        return posicion;
    }
}
