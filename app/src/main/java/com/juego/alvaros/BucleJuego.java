package com.juego.alvaros;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.SeekBar;

import com.juego.alvaros.vistas.FragmentAjustes;

/**
 * @author Alvaro del Rio, Alvaro Santillana, Alvaro Velasco
 * @version 1.0 18/12/2019
 */
public class BucleJuego extends Thread {
    //Atributos de la clase
    //private final static int MAX_FPS = 30; //Frames por segundo deseados
    private static int MAX_FPS;
    private final static int MAX_FRAMES_SALTATOS = 5; //Maximo numero de frames que saltaremos
    //private final static int TIEMPO_FRAME = 1000 / MAX_FPS; //El periodo de frames
    private static int TIEMPO_FRAME;
    private Juego juego;
    private boolean juegoEnEjecucion = true;
    private static final String TAG = Juego.class.getSimpleName();
    private SurfaceHolder surfaceHolder;

    //Constructor de la clase
    BucleJuego(SurfaceHolder surfaceHolder, Juego juego){
        this.juego = juego;
        this.surfaceHolder = surfaceHolder;

        //Comprobamos si el usuario ha modificado los FPS e inicializamos las variables
        SeekBar seekBar = FragmentAjustes.getSeekBarFps();
        MAX_FPS = (seekBar!=null)?seekBar.getProgress():30;
        TIEMPO_FRAME = 1000 / MAX_FPS;
    }

    /**
     * Método que contiene la lógica del bucle del juego
     */
    @Override
    public void run(){
        //Variables
        Log.d(TAG, "Comienza el bucle del juego");
        Log.d(TAG, "FPS = " + MAX_FPS);
        Canvas canvas;
        long tiempoComienzo; //Tiempo en el que la iteracion del bucle comenzó
        long tiempoDiferencia; //Tiempo que duró el ciclo
        int tiempoDormir; //Tiempo que el thread debe dormir (<0 si vamos mal de tiempo)
        int framesASaltar; //Número de frames que vamos a saltar

        //Lógica del bucle
        while(juegoEnEjecucion){
            canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas(); //bloqueamos el canvas para que nadie mas pueda escribir en el
                synchronized (surfaceHolder){
                    tiempoComienzo = System.currentTimeMillis(); //momento que comienza el bucle
                    framesASaltar = 0;
                    //Actualizamos el estado del juego
                    juego.actualizar();
                    //Renderizamos la imagen
                    juego.renderizar(canvas);

                    //Calculamos cuanto tardó el cicle
                    tiempoDiferencia = System.currentTimeMillis() - tiempoComienzo;

                    //¿Cuánto debe dormir el thread antes de la siguiente iteración?
                    tiempoDormir = (int) (TIEMPO_FRAME - tiempoDiferencia);
                    //Si es mayor que 0, vamos bien de tiempo
                    if(tiempoDormir > 0){
                        try {
                            //Enviamos el thread a dormir
                            Thread.sleep(tiempoDormir);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //Si vamos mal de tiempo debemos ponernos al dia
                    while (tiempoDormir < 0 && framesASaltar < MAX_FRAMES_SALTATOS){
                        juego.actualizar(); //actualizamos sin renderizar
                        tiempoDormir += TIEMPO_FRAME; //actualizamos el tiempo de dormir
                        framesASaltar++;
                    }
                }
            } finally {
                //Si ocurre alguna excepcion desbloqueamos el canvas
                if (canvas != null){
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

        }
        Log.d(TAG, "Nueva iteración del bucle del juego!");
    }

    /**
     * Método que finaliza el bucle del juego
     */
    public void fin(){
        juegoEnEjecucion = false;
    }

    public static int getFPS(){
        return MAX_FPS;
    }
}
