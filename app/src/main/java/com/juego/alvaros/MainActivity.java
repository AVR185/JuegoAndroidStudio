package com.juego.alvaros;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.graphics.PointF;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.doctoror.particlesdrawable.ParticlesDrawable;
import com.google.android.material.tabs.TabLayout;
import com.juego.alvaros.Juego.BucleJuego;
import com.juego.alvaros.vistas.dialogos.DialogoReinicio;
import com.juego.alvaros.vistas.FragmentAjustes;
import com.juego.alvaros.vistas.FragmentCreditos;
import com.juego.alvaros.vistas.FragmentMenuPrincipal;
import com.juego.alvaros.vistas.FragmentRanking;
import com.juego.alvaros.vistas.FragmentSeleccionNivel;
import java.util.Objects;
import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE;

/**
 * @author Alvaro del Rio, Alvaro Santillana, Alvaro Velasco
 * @version 1.0 18/12/2019
 */
public class MainActivity extends AppCompatActivity implements DialogoReinicio.OnSimpleDialogListener {
    //Atributos
    private final ParticlesDrawable mDrawable = new ParticlesDrawable(); //objeto del tipo ParticleDrawable para el fondo
    private static TabLayout tabLayout;
    //MusicaIntro
    private static MediaPlayer mPlayer;
    private static SoundPool soundPool; //Efectos de sonido
    private static int[] sonidos;
    //Bolas del juego
    private static ImageView Blue_Ball;
    private static ImageView Red_Ball;

    //Medidas de la pantalla
    private static int ancho;
    private static int alto;

    //Cargamos preferencias
    private SharedPreferences misPreferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_IMMERSIVE);

        //Comienza la animacion de las bolas
        movimientoBolas();
        //Si la version es la adecuada el usuario vera el fondo animado
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mDrawable.setNumDots(10);
            mDrawable.setDotRadiusRange(1f,3f);
            findViewById(R.id.idFondo).setForeground(mDrawable);
        }

        //Construimos el TabLayout
        inicializarTab();
        //Música del Menu
        introMusica();
        //Cargaos los efectos de sonido
        sonidosFx();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDrawable.start();
        introMusica();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDrawable.stop();
        mPlayer.stop();
    }

    //============ Eventos =============
    @Override
    public void onBackPressed(){
        recreate();
    }

    //Control botones dialogo reinicio de juego
    @Override
    public void onPossitiveButtonClick() {
        iniciarJuego();
    }

    @Override
    public void onNegativeButtonClick() {
        recreate();
    }

    /**
     * Metodo que responde al evento del textview para iniciar juego
     * @param view view que llama a dicha acción
     */
    public void botonNuevoJuego(View view) {
        iniciarJuego();
    }

    /**
     * Método con el que iniciamos un nuevo juego
     */
    @SuppressLint("ClickableViewAccessibility")
    public void iniciarJuego(){
        setContentView(R.layout.juego_layout);
        //Establecemos los FPS en el juego guardados en las preferencias
        int valor = misPreferencias.getInt("fps", 45);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            BucleJuego.setMaxFps(valor);
        } else {
            if (valor < 15 || valor > 75)
                BucleJuego.setMaxFps(45); //En versiones antiguas de Android limitamos el max y el min
            else
                BucleJuego.setMaxFps(valor);
        }

        final ImageView Controller_Blue = findViewById(R.id.Blue_Control);
        final ImageView Controller_Red = findViewById(R.id.Red_Control);
        Blue_Ball = findViewById(R.id.Blue_Ball);
        Red_Ball = findViewById(R.id.Red_Ball);

        Controller_Blue.setOnTouchListener(new View.OnTouchListener()
        {
            PointF DownPT = new PointF(); // Guarda la posición del ratón cuando se presiona
            PointF StartPT = new PointF(); // Guarda la posición inicial del controlador
            PointF StartPT_Ball = new PointF(); // Guarda la posición inicial de la bola

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_MOVE :
                        Controller_Blue.setX((int)(StartPT.x + event.getX() - DownPT.x));
                        Controller_Blue.setY((int)(StartPT.y + event.getY() - DownPT.y));
                        Blue_Ball.setX((int)(StartPT_Ball.x + event.getX() - DownPT.x));
                        Blue_Ball.setY((int)(StartPT_Ball.y + event.getY() - DownPT.y));
                        StartPT.set(Controller_Blue.getX(), Controller_Blue.getY());
                        StartPT_Ball.set(Blue_Ball.getX(), Blue_Ball.getY());
                        break;
                    case MotionEvent.ACTION_DOWN :
                        Controller_Blue.setImageAlpha(50); //hacemos el control semitransparente
                        DownPT.set( event.getX(), event.getY());
                        StartPT.set( Controller_Blue.getX(), Controller_Blue.getY());
                        StartPT_Ball.set(Blue_Ball.getX(), Blue_Ball.getY());
                        break;
                    case MotionEvent.ACTION_UP :
                        Controller_Blue.setImageAlpha(255); //recupera su opacidad
                        v.performClick();
                        break;
                    default :
                        break;
                }
                return true;
            }
        });

        Controller_Red.setOnTouchListener(new View.OnTouchListener()
        {
            PointF DownPT = new PointF(); // Guarda la posición del ratón cuando se presiona
            PointF StartPT = new PointF(); // Guarda la posición inicial del controlador
            PointF StartPT_Ball = new PointF(); // Guarda la posición inicial de la bola

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_MOVE :
                        Controller_Red.setX((int)(StartPT.x + event.getX() - DownPT.x));
                        Controller_Red.setY((int)(StartPT.y + event.getY() - DownPT.y));
                        Red_Ball.setX((int)(StartPT_Ball.x + event.getX() - DownPT.x));
                        Red_Ball.setY((int)(StartPT_Ball.y + event.getY() - DownPT.y));
                        StartPT.set(Controller_Red.getX(), Controller_Red.getY());
                        StartPT_Ball.set(Red_Ball.getX(), Red_Ball.getY());
                        break;
                    case MotionEvent.ACTION_DOWN :
                        Controller_Red.setImageAlpha(50); //hacemos el control semitransparente
                        DownPT.set( event.getX(), event.getY() );
                        StartPT.set( Controller_Red.getX(), Controller_Red.getY() );
                        StartPT_Ball.set(Red_Ball.getX(), Red_Ball.getY());
                        break;
                    case MotionEvent.ACTION_UP :
                        Controller_Red.setImageAlpha(255); //recupera su opacidad
                        v.performClick();
                        break;
                    default :
                        break;
                }
                return true;
            }
        });
    }

    //================= OCULTAMOS LA UI =================
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
/*
    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

 */

    //======= Link: https://developer.android.com/training/system-ui/immersive =========

    /**
     * Metodo con el cual animamos las bolas roja y azul, en el menu principal.
     */
    private void movimientoBolas(){
        ImageView circuloAzul = findViewById(R.id.circuloAzul);
        ImageView circuloRojo = findViewById(R.id.circuloRojo);

        //Medidas del dispositivo
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        ancho = metrics.widthPixels; // ancho absoluto en pixels
        alto = metrics.heightPixels; // alto absoluto en pixels

        //Animacion circulo azul
        Path path = new Path();
        path.arcTo(100f,250f, ancho-150f, alto-250f, 70f, -360f, true);

        ObjectAnimator animator = ObjectAnimator.ofFloat(circuloAzul, View.X, View.Y, path);
        animator.setDuration(3000);
        animator.setRepeatCount(-1);
        animator.start();

        //Animacion circulo Rojo
        Path path2 = new Path();
        path2.arcTo(50f,150f, ancho-150f, alto-400f, -115f, -360f, true); //Los angulos los recorre en el sentido de las agujas del reloj, 0 son las 3 en punto
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(circuloRojo, View.X, View.Y, path2);
        animator2.setDuration(3000);
        animator2.setRepeatCount(-1);
        animator2.start();

        //https://www.youtube.com/watch?v=Uteyf-THpp4
        //https://www.adictosaltrabajo.com/2019/02/21/animaciones-en-android/
    }

    //================== Tab Layout ======================
    private void inicializarTab() {
        // para gestionar cada uno de los fragmentos
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Para alojar el contenido de cada fragment
        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        Objects.requireNonNull(tabLayout.getTabAt(2)).select();
    }

    /**
     * Método que implementa la musica del menu
     */
    public void introMusica(){
        if(mPlayer == null || !mPlayer.isPlaying()){
            mPlayer = MediaPlayer.create(this,R.raw.intromenuf);
            misPreferencias = getSharedPreferences("prefs",MODE_PRIVATE);
            int volumen = misPreferencias.getInt("musica", 99);
            float conversor = (float) (1 - (Math.log(100 - volumen) / Math.log(100)));
            mPlayer.setVolume(conversor, conversor);
            mPlayer.start();
        }
    }

    //Los efectos de sonido los cargamos aqui para que le de tiempo a SoundPool a cargar, si no puede dar problemas con los primeros sonidos
    public void sonidosFx(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(15)
                    .build();
        }else{
            soundPool = new SoundPool(15, AudioManager.STREAM_MUSIC, 0);
        }
        int comienzo = soundPool.load(this, R.raw.comienzo, 3);
        int impacto = soundPool.load(this, R.raw.colision, 3);
        int enemigo = soundPool.load(this, R.raw.enemigos, 1);

        sonidos = new int[]{comienzo, enemigo, impacto};
    }

    /**
     * Método que responde al evento de hacer click en las ImageButton del menu principal
     * @param view view que llama al evento
     */
    public void seleccionarMenu(View view) {
        switch (view.getId()){
            case R.id.idBotonAjustes:
                Objects.requireNonNull(tabLayout.getTabAt(4)).select();
                break;
            case R.id.idBotonCreditos:
                Objects.requireNonNull(tabLayout.getTabAt(0)).select();
                break;
            default:
                Toast.makeText(getApplicationContext(), "Vuelva a intentarlo", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Clase que devolvera un fragment segun la seccion donde se encuentre
     * */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch(position){
                case 0:
                    fragment = new FragmentCreditos();
                    break;
                case 1:
                    fragment = new FragmentRanking();
                    break;
                case 2:
                    fragment = new FragmentMenuPrincipal();
                    break;
                case 3:
                    fragment = new FragmentSeleccionNivel();
                    break;
                case 4:
                    fragment = new FragmentAjustes();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 5;//tenemos cinco fragment
        }

        /**
         * Método para poner el titulo de cada TabLayout
         * @param position posición de cada página
         * @return CharSequence con el titulo de cada páguina
         */
        @Override
        public CharSequence getPageTitle(int position) {
            String titulo = "";
            switch (position){
                case 0: titulo = getString(R.string.creditos); break;
                case 1: titulo = getString(R.string.ranking); break;
                case 2: titulo = getString(R.string.juego); break;
                case 3: titulo = getString(R.string.niveles); break;
                case 4: titulo = getString(R.string.ajustes); break;
            }
            return titulo;
        }
    }

    //================== Getters ==================
    public static int getAlto(){
        return alto;
    }

    public static int getAncho(){
        return ancho;
    }

    public static ImageView getBlue_Ball() {
        return Blue_Ball;
    }

    public static ImageView getRed_Ball() {
        return Red_Ball;
    }

    public static TabLayout getTabLayout(){
        return tabLayout;
    }

    //Getter para la el elemento de musica de la intro..
    public static MediaPlayer getmPlayer() {
        return mPlayer;
    }

    public static SoundPool getSoundPool(){ return soundPool;}

    public static int[] getEfectosSonido(){ return  sonidos; }
}
