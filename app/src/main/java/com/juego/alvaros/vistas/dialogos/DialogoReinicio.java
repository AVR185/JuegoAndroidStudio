package com.juego.alvaros.vistas.dialogos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import com.juego.alvaros.R;

public class DialogoReinicio extends DialogFragment {
    // Interfaz de comunicación
    private OnSimpleDialogListener listener;

    //Este método es llamado al hacer el show() de la clase DialogFragment()
    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState){
        //Usamos la clase Builder para construir el diálogo
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //Escribimos el título
        builder.setTitle(R.string.titulo2);
        //Añadimos la lista y la respuesta de cada elemento a través de una función de callback de la interfaz DialogInterface
        builder.setMessage(R.string.mensaje);
        builder.setPositiveButton(R.string.jugar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onPossitiveButtonClick();
                dismiss();
            }
        });
        builder.setNegativeButton(R.string.volver_menu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onNegativeButtonClick();
                dismiss();
            }
        });

        //Crear el AlertDialog y devolverlo
        return builder.create();
    }

    public interface OnSimpleDialogListener {
        void onPossitiveButtonClick();// Eventos Botón Positivo
        void onNegativeButtonClick();// Eventos Botón Negativo
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnSimpleDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " no implementó OnSimpleDialogListener");
        }
    }
}
