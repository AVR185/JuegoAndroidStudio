package com.juego.alvaros.vistas;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import com.google.android.material.tabs.TabLayout;
import com.juego.alvaros.MainActivity;
import com.juego.alvaros.R;

/**
 * @author Alvaro del Rio, Alvaro Santillana, Alvaro Velasco
 * @version 1.0 23/12/2019
 */
public class FragmentAjustes extends Fragment implements SeekBar.OnSeekBarChangeListener{
    //Atributos
    private ImageButton boton;
    private View view;
    private TabLayout tabLayout;
    private TextView valor;
    private static SeekBar seekBarFps, seekBarMusica, seekBarFx;

    public FragmentAjustes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ajustes, container, false);
        boton = view.findViewById(R.id.idButonHomeAjustes);
        tabLayout = MainActivity.getTabLayout();

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabLayout.getTabAt(2).select();
            }
        });

        seekBarFps = view.findViewById(R.id.idSeekBarFps);
        seekBarMusica = view.findViewById(R.id.idSeekBarMusica);
        seekBarFx = view.findViewById(R.id.idSeekBarFx);

        seekBarFps.setOnSeekBarChangeListener(this);
        seekBarMusica.setOnSeekBarChangeListener(this);
        seekBarFx.setOnSeekBarChangeListener(this);

        return view;
    }

    //======== Métodos a implementar de la interfaz =============
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        switch (seekBar.getId()){
            case R.id.idSeekBarFps:
                valor = view.findViewById(R.id.idTextViewFps);
                valor.setText(String.format("%d FPS", progress));
                break;
            case R.id.idSeekBarMusica:
                valor = view.findViewById(R.id.idTextViewMusica);
                valor.setText(String.format("%d%%", progress));
                break;
            case R.id.idSeekBarFx:
                valor = view.findViewById(R.id.idTextViewFx);
                valor.setText(String.format("%d%%", progress));
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    //========= Getters de las Seekbar =============
    public static SeekBar getSeekBarFps(){
        return seekBarFps;
    }

    public static SeekBar getSeekBarMusica(){
        return seekBarMusica;
    }

    public static SeekBar getSeekBarFx(){
        return seekBarFx;
    }
}
