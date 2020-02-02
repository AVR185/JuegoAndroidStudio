package com.juego.alvaros.vistas;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.juego.alvaros.R;

/**
 * @author Alvaro del Rio, Alvaro Santillana, Alvaro Velasco
 * @version 1.0 23/12/2019
 */
public class FragmentSeleccionNivel extends Fragment {
    //Atributos
    private static int nivel;
    private Context mContext;

    public FragmentSeleccionNivel() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seleccion_nivel, container, false);
        ListView listaNiveles = view.findViewById(R.id.idSelecNivel);

        //Declaremos el array de elementos elegibles
        String[] elementos = obtenerNiveles();

        //Declaremos un adaptador de texto (String)
        ArrayAdapter<String> adaptador;
        //Instanciamos el adaptador
        adaptador = new ArrayAdapter<>(mContext,android.R.layout.simple_list_item_single_choice, elementos);
        //Referenciamos al listView
        listaNiveles.setAdapter(adaptador);
        //Pasamos el adaptador al listView
        listaNiveles.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //Ponemos la lista a la escucha
        listaNiveles.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String texto = ((TextView)view).getText().toString();
                String[] txtSeparado = texto.split(" ");
                nivel = Integer.parseInt(txtSeparado[1]);
            }
        });

        return view;
    }

    private String[] obtenerNiveles(){
        String[] elementos;
        int max = FragmentRanking.obtenerNiveles();
        if (max != 1){
            elementos = new String[max];
            for (int i = 1; i<=max; i++){
                elementos[i-1]="Nivel "+i;
            }
        } else {
            elementos = new String []{"Nivel 1"};
        }
        return elementos;
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

    public static int getNivel() {
        return nivel;
    }
}
