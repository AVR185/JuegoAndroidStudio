package com.juego.alvaros.vistas;

import android.content.SharedPreferences;
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
import java.util.Locale;
import java.util.Objects;
import static android.content.Context.MODE_PRIVATE;

/**
 * @author Alvaro del Rio, Alvaro Santillana, Alvaro Velasco
 * @version 1.0 23/12/2019
 */
public class FragmentAjustes extends Fragment implements SeekBar.OnSeekBarChangeListener{
    private TabLayout tabLayout;
    private TextView textViewFps, textViewMusica, textViewFx;
    private SeekBar seekBarFps, seekBarMusica, seekBarFx;
    private SharedPreferences misPreferencias;

    public FragmentAjustes() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ajustes, container, false);
        //Atributos
        ImageButton boton = view.findViewById(R.id.idButonHomeAjustes);
        tabLayout = MainActivity.getTabLayout();

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(tabLayout.getTabAt(2)).select();
            }
        });

        seekBarFps = view.findViewById(R.id.idSeekBarFps);
        seekBarMusica = view.findViewById(R.id.idSeekBarMusica);
        seekBarFx = view.findViewById(R.id.idSeekBarFx);

        //Cargamos los valores guardados
        misPreferencias = Objects.requireNonNull(this.getActivity()).getSharedPreferences("prefs",MODE_PRIVATE);
        //Cargamos el valor en las seekBar
        seekBarFps.setProgress(misPreferencias.getInt("fps", 45));
        seekBarMusica.setProgress(misPreferencias.getInt("musica", 100));
        seekBarFx.setProgress(misPreferencias.getInt("fx", 100));
        //Cargamos los valores en los textViews correspondientes
        textViewFps = view.findViewById(R.id.idTextViewFps);
        textViewFps.setText(String.format(Locale.FRENCH,"%d FPS" , misPreferencias.getInt("fps", 45)));
        textViewMusica = view.findViewById(R.id.idTextViewMusica);
        textViewMusica.setText(String.format(Locale.FRENCH,"%d%%" , misPreferencias.getInt("fx", 100)==99?100:misPreferencias.getInt("musica", 100)));
        textViewFx = view.findViewById(R.id.idTextViewFx);
        textViewFx.setText(String.format(Locale.FRENCH,"%d%%" , misPreferencias.getInt("fx", 100)==99?100:misPreferencias.getInt("fx", 100)));

        seekBarFps.setOnSeekBarChangeListener(this);
        seekBarMusica.setOnSeekBarChangeListener(this);
        seekBarFx.setOnSeekBarChangeListener(this);

        return view;
    }

    @Override
    public void onStop(){
        super.onStop();

        //Se crea el objeto editor que permite modificar los valores del objeto misPreferencias
        SharedPreferences.Editor editor = misPreferencias.edit();

        //Para que no salga infinito en la conversion del valor 100 a un volumen entre 1 y 0 con log, si el volumen es 100 lo transformamos en 99
        int fps = seekBarFps.getProgress();
        int volumenMusica = seekBarMusica.getProgress()==100?99:seekBarMusica.getProgress();
        int volumenFx = seekBarFx.getProgress()==100?99:seekBarFx.getProgress();

        //Se crea o modifican los pares nombre/valor para las preferencias
        editor.putInt("fps", fps);
        editor.putInt("musica", volumenMusica);
        editor.putInt("fx" , volumenFx);

        //Ahora es cuando realmente se guardan las preferencias creadas
        editor.apply();
    }

    //======== Métodos a implementar de la interfaz =============
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        switch (seekBar.getId()){
            case R.id.idSeekBarFps:
                textViewFps.setText(String.format(Locale.FRENCH,"%d FPS", progress));
                break;
            case R.id.idSeekBarMusica:
                textViewMusica.setText(String.format(Locale.FRENCH,"%d%%", progress));
                //Controlamos el volumen del menu principal
                int conversor = progress==100?99:progress;
                float volumen = (float) (1 - (Math.log(100 - conversor) / Math.log(100)));
                MainActivity.getmPlayer().setVolume(volumen,volumen);
                break;
            case R.id.idSeekBarFx:
                textViewFx.setText(String.format(Locale.FRENCH,"%d%%", progress));
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) { }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) { }
}
