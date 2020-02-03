package com.juego.alvaros.Juego;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import com.juego.alvaros.MainActivity;
import com.juego.alvaros.R;
import com.juego.alvaros.bloques.Hexagono;
import com.juego.alvaros.bloques.Rectangulo;
import com.juego.alvaros.bloques.Cuadrado;
import com.juego.alvaros.bloques.RectanguloX;
import com.juego.alvaros.vistas.dialogos.DialogRegistroNombre;
import com.juego.alvaros.vistas.dialogos.DialogoReinicio;
import com.juego.alvaros.vistas.FragmentRanking;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Alvaro del Rio, Alvaro Santillana, Alvaro Velasco
 * @version 1.0 18/12/2019
 */
public class Juego extends SurfaceView implements SurfaceHolder.Callback {
    //Atributos
    private SurfaceHolder holder;
    private BucleJuego bucle;
    private static final String TAG = Juego.class.getSimpleName();

    //=============== Bloques =================
    private static Bitmap rectangulo, cuadrado, hexagono;
    //frames que restan hasta generar nuevo enemigo
    private int frames_new_bloque = 0;
    //lista de enemigos
    private ArrayList<Rectangulo> listaBloques = new ArrayList<>();

    //============== Dificultad ===============
    private final int PUNTOS_CAMBIOS_NIVEL = 1000;
    private static int nivel;
    private int puntos;
    private int bloquesMinuto;//numero enemigos por minuto

    TimerTask temporizador;
    Timer timer;

    private FragmentManager fragmentManager; //Variable necesaria para mostrar los DialogFragment
    private static int puntosActuales;   //Son los valores que guardaremos ya que con el juego parado el tiempo
    private static int nivelActual;      // que tardes en escribir nombre, los puntos se siguen sumando

    //Efectos de sonido
    private SoundPool soundPool;
    private int comienzo, impacto, enemigo;

    //Constructor de la clase
    public Juego(Context context, AttributeSet attrs) {
        super(context, attrs);
        //Activity activity = (Activity) context;
        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();

        puntos = (nivel==1)?0:nivel*PUNTOS_CAMBIOS_NIVEL;
        bloquesMinuto = 50 + 10*nivel;

        //Hacemos el fondo transparente para que se vea el fondo animado
        SurfaceView surfaceView = findViewById(R.id.idGameView);
        surfaceView.setZOrderOnTop(true);    // necessary
        SurfaceHolder sfhTrackHolder = surfaceView.getHolder();
        sfhTrackHolder.setFormat(PixelFormat.TRANSPARENT);

        holder = getHolder();
        holder.addCallback(this);
        cargarBloques(); //Cargamos las figuras

        //Cada 1 segundos sumamos 50 puntos a la puntuacion
        timer = new Timer();
        temporizador = new TimerTask() {
            @Override
            public void run() {
                puntos += 50;
            }
        };
        timer.scheduleAtFixedRate(temporizador, 0, 1000);

        //Cuando comienza el juego reproducimos el sonido correspondiente
        soundPool = MainActivity.getSoundPool();
        soundPool.play(MainActivity.getEfectosSonido()[0], 1, 1, 3, 0, 1);
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
        if(frames_new_bloque ==0){
            crearBloque();
            soundPool.play(MainActivity.getEfectosSonido()[1], 1, 1, 1, 0, 1); //sonido cuando se crea un enemigo
            frames_new_bloque = BucleJuego.getFPS()*60/ bloquesMinuto;
        }
        frames_new_bloque--;

        for(int i = 0;i<listaBloques.size(); i++){
            listaBloques.get(i).ActualizarCoordenadas();

            /*if(colisiones(listaBloques.get(i),MainActivity.getBlue_Ball())){
                Log.i("COL","Impacto de bola Azul");
            }
            if(colisiones(listaBloques.get(i),MainActivity.getRed_Ball())){
                Log.i("COL","Impacto de bola Roja");
            }*/
            if(colisiones(listaBloques.get(i), MainActivity.getRed_Ball()) || colisiones(listaBloques.get(i),MainActivity.getBlue_Ball())){
                soundPool.play(MainActivity.getEfectosSonido()[2], 1, 1, 3, 0, 1); //sonido cuando hay un impacto
                finalizarJuego();
            }

            //Si se sale de las coordenadas de la pantalla lo eliminamos de la lista
            if(listaBloques.get(i).coordenada_x<-20 || listaBloques.get(i).coordenada_y<-20 ||
                    listaBloques.get(i).coordenada_x>listaBloques.get(i).anchoPantalla ||
                    listaBloques.get(i).coordenada_y>(listaBloques.get(i).altoPantalla+10)){
                listaBloques.remove(i);
                i--;
            }
        }

        //Cada PUNTOS_CAMBIO_NIVEL se incremeneta la dificultad
        if(nivel != puntos/PUNTOS_CAMBIOS_NIVEL && puntos/PUNTOS_CAMBIOS_NIVEL != 0){
            nivel = puntos/PUNTOS_CAMBIOS_NIVEL;
            bloquesMinuto += 10;
        }
    }

    /**
     * Metodo que dibuja el siguiente paso de la animacion correspondiente
     */
    public void renderizar(Canvas canvas){
        if(canvas!=null){
            //Borrar canvas antes de dibujar
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            //Dibujamos la puntuación y el nivel por pantalla
            Paint myPaint = new Paint();
            myPaint.setTextSize(50);
            myPaint.setColor(Color.GREEN);
            canvas.drawText("Puntos: " + puntos + " - Nivel: " + nivel, 50, 50, myPaint);

            for(Rectangulo r : listaBloques){
                r.Dibujar(canvas);
            }
        }
    }

    public boolean colisiones (Rectangulo rect, ImageView bola){
        boolean cond1 = bola.getX()+10 < rect.getCoordenada_x() + rect.getAncho() && bola.getX()+bola.getWidth()-10 > rect.getCoordenada_x();
        boolean cond2 = bola.getY()+10 < rect.getCoordenada_y() + rect.getAlto() && bola.getY()+bola.getHeight()-10 > rect.getCoordenada_y();
        return cond1 && cond2;
    }

    public void finalizarJuego(){
        bucle.fin();
        puntosActuales = puntos;
        nivelActual= nivel;
        //Mostramos el dialogo de fin de juego
        DialogoReinicio reinicio = new DialogoReinicio();
        reinicio.show(Objects.requireNonNull(fragmentManager),"Reinicio");

        //Mostramos o no el dialogo para guardar el nick
        DialogRegistroNombre dialogo = new DialogRegistroNombre();;
        if(FragmentRanking.mejorPuntuacion(puntos))
            dialogo.show(Objects.requireNonNull(fragmentManager),"Registro nombre");
    }

    //No lo implementa el libro pero Android Studio te obliga
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) { }

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

    public void cargarBloques(){
        frames_new_bloque = BucleJuego.getFPS() *60/ bloquesMinuto;
        rectangulo = BitmapFactory.decodeResource(getResources(), R.drawable.rectangulo);
        cuadrado = BitmapFactory.decodeResource(getResources(), R.drawable.cuadrado);
        hexagono = BitmapFactory.decodeResource(getResources(), R.drawable.hexagono);
    }

    public void crearBloque(){
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

    //=============== Getters y Setters ========================
    public static Bitmap getRectangulo(){
        return rectangulo;
    }

    public static Bitmap getCuadrado(){
        return cuadrado;
    }

    public static Bitmap getHexagono(){
        return hexagono;
    }

    public static void setNivel(int nivel) { Juego.nivel = nivel; }

    public static int getNivel(){ return nivelActual; }

    public static int getPuntos() { return puntosActuales; }
}
