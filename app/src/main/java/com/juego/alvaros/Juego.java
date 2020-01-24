package com.juego.alvaros;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import com.juego.alvaros.bloques.Hexagono;
import com.juego.alvaros.bloques.Rectangulo;
import com.juego.alvaros.bloques.Cuadrado;
import com.juego.alvaros.bloques.RectanguloX;
import java.util.ArrayList;

/**
 * @author Alvaro del Rio, Alvaro Santillana, Alvaro Velasco
 * @version 1.0 18/12/2019
 */
public class Juego extends SurfaceView implements SurfaceHolder.Callback {
    //Atributos
    private SurfaceHolder holder;
    private BucleJuego bucle;
    private static final String TAG = Juego.class.getSimpleName();
    private Paint myPaint;

    //Bloques
    private static Bitmap rectangulo, cuadrado, hexagono;
    //numero enemigos por minuto
    private int rectangulo_minuto =50;
    //frames que restan hasta generar nuevo enemigo
    private int frames_new_rectangulo = 0;
    //lista de enemigos
    private ArrayList<Rectangulo> listaBloques = new ArrayList<>();

    //Constructor de la clase
    public Juego(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        cargaRectangulos();
    }

    //Metodos que hay que implementar de la interfaz

    /**
     *
     * @param surfaceHolder parametro necesario para acceder al canvas de la SurfaceView
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //Se crea la superficie, creamos el bucle del juego

        //Para interceptar los eventos de la SurfaceView
        getHolder().addCallback(this);

        //Creamos el bucle del juego
        bucle = new BucleJuego(getHolder(), this);

        //Hacer la Vista focusable para que pueda capturar eventos
        setFocusable(true);

        //Comenzar el bucle
        bucle.start();
    }

    /**
     * Mtodo actualiza el estado del juego. Contiene la logica del videojuego generando los
     * nuevos estados y dejando listo el sistema para un repintado.
     */
    public void actualizar(){
        if(frames_new_rectangulo==0){
            crearRectangulo();
            frames_new_rectangulo = BucleJuego.getFPS()*60/rectangulo_minuto;
        }
        frames_new_rectangulo--;

        for(Rectangulo r : listaBloques){
            r.ActualizarCoordenadas();
        }
    }

    /**
     * Metodo que dibuja el siguiente paso de la animacion correspondiente
     */
    public void renderizar(Canvas canvas){
        if(canvas!=null){
            //canvas.drawColor(255255000);
            myPaint = new Paint();
            //myPaint.setStyle(Paint.Style.STROKE);
            for(Rectangulo r : listaBloques){
                r.Dibujar(canvas, myPaint);
            }
        }
    }

    //No lo implementa el libro pero Android Studio te obliga
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }


    /**
     *
     * @param surfaceHolder parametro necesario para acceder al canvas de la SurfaceView
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "Juego destruido!");
        //Cerramos el thread y esperamos a que termine
        boolean retry = true;
        while(retry){
            try{
                bucle.fin(); //finaliza el bucle del juego
                bucle.join();
                retry = false;
            } catch (InterruptedException e){
                //tratar posible fallo al finalizar el bucle
            }
        }
    }

    public void cargaRectangulos(){
        frames_new_rectangulo = BucleJuego.getFPS() *60/rectangulo_minuto;
        rectangulo = BitmapFactory.decodeResource(
                getResources(), R.drawable.rectangulo);
        cuadrado = BitmapFactory.decodeResource(
                getResources(), R.drawable.cuadrado);
        hexagono = BitmapFactory.decodeResource(
                getResources(), R.drawable.hexagono);
    }

    public void crearRectangulo(){
        //probabilidad de rectangulo 80% y cuadrado 20%
        double numero = Math.random();

        if (numero >= 0.5) {
            listaBloques.add(new RectanguloX(this));
        } else if(numero > 0.2 && numero < 0.5) {
            listaBloques.add(new Cuadrado(this));
        }else {
            listaBloques.add(new Hexagono(this));
        }
    }

    public static Bitmap getRectangulo(){
        return rectangulo;
    }

    public static Bitmap getCuadrado(){
        return cuadrado;
    }

    public static Bitmap getHexagono(){
        return hexagono;
    }
}
