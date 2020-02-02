package com.juego.alvaros.vistas.dialogos;

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

public class DialogRegistroNombre extends DialogFragment {
    //Atributos
    private EditText nick;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_registro_nombre, null);
        builder.setView(v);

        Button aceptar = v.findViewById(R.id.idBotonRegistro);
        nick = v.findViewById(R.id.idTextNick);

        aceptar.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Juego.setNick(nick.getText().toString());
                    dismiss();
                }
            }
        );
        return builder.create();
    }
}
