package com.juego.alvaros.vistas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.juego.alvaros.R;

/**
 * @author Alvaro del Rio, Alvaro Santillana, Alvaro Velasco
 * @version 1.0 23/12/2019
 */
public class DialogInfoAutor extends DialogFragment {
    //Atributos
    private String[] nombres = {"Álvaro del Río", "Álvaro Santillana", "Álvaro Velasco"};
    private String[] linkedin = {"url del rio", "url Santillana", "https://www.linkedin.com/in/alvaro-velasco/"};
    private String[] github = {"https://github.com/alvarorio97", "https://github.com/AlvaroSanCer", "https://github.com/AVR185"};

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final AlertDialog dialog = builder.setView(inflater.inflate(R.layout.dialog_info_autores, null))
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss(); //Cerramos el dialogo
                    }
                }).create();

        //2. now setup to change color of the button
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            }
        });
        // Create the AlertDialog object and return it
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        int posicion = FragmentCreditos.getPosicion();
        TextView textView = (TextView) getDialog().findViewById(R.id.titulo);
        textView.setText(nombres[posicion]);

        textView = (TextView) getDialog().findViewById(R.id.idLinkedin);
        textView.setText(linkedin[posicion]);

        textView = (TextView) getDialog().findViewById(R.id.idGithub);
        textView.setText(github[posicion]);
    }
}
