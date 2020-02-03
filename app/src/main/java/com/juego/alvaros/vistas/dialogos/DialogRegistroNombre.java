package com.juego.alvaros.vistas.dialogos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.juego.alvaros.Juego.Juego;
import com.juego.alvaros.R;
import com.juego.alvaros.vistas.FragmentRanking;

public class DialogRegistroNombre extends DialogFragment {
    //Atributos
    private EditText texto;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_registro_nombre, null);
        builder.setView(v);

        texto = v.findViewById(R.id.idTextNick);
        Button aceptar = v.findViewById(R.id.idBotonRegistro);

        aceptar.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nick = texto.getText().toString();
                    int numero = Juego.getPuntos();
                    FragmentRanking.addRegistro(nick, numero, Juego.getNivel());
                    dismiss();
                }
            }
        );
        return builder.create();
    }
}
