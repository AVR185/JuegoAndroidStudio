package com.juego.alvaros;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.doctoror.particlesdrawable.ParticlesDrawable;
import com.google.android.material.tabs.TabLayout;
import com.juego.alvaros.vistas.FragmentAjustes;
import com.juego.alvaros.vistas.FragmentCreditos;
import com.juego.alvaros.vistas.FragmentMenuPrincipal;
import com.juego.alvaros.vistas.FragmentRanking;
import com.juego.alvaros.vistas.FragmentSeleccionNivel;
import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE;

/**
 * @author Alvaro del Rio, Alvaro Santillana, Alvaro Velasco
 * @version 1.0 18/12/2019
 */
public class MainActivity extends AppCompatActivity {
    //Atributos
    private final ParticlesDrawable mDrawable = new ParticlesDrawable(); //objeto del tipo ParticleDrawable para el fondo
    // para gestionar cada uno de los fragmentos
    private SectionsPagerAdapter mSectionsPagerAdapter;
    // Para alojar el contenido de cada fragment
    private ViewPager mViewPager;
    private static TabLayout tabLayout;
    //MusicaIntro
    private static MediaPlayer mPlayer;

    //Medidas de la pantalla
    private static int ancho;
    private static int alto;

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDrawable.start();
        introMusica();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDrawable.stop();
        mPlayer.stop();
    }

    //============ Eventos =============
    /**
     * Metodo para iniciar una nueva partida
     * @param view
     */
    public void inicioJuego(View view) {
        setContentView(R.layout.juego_layout);
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
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(2).select();
    }

    /**
     * Método que implementa la musica del menu
     */

    public void introMusica(){
        mPlayer = MediaPlayer.create(this,R.raw.intromenu);

        mPlayer.start();
    }

    /**
     * Método que responde al evento de hacer click en las ImageButton del menu principal
     * @param view
     */
    public void seleccionarMenu(View view) {
        switch (view.getId()){
            case R.id.idBotonAjustes:
                tabLayout.getTabAt(4).select();
                break;
            case R.id.idBotonCreditos:
                tabLayout.getTabAt(0).select();
                break;
            default:
                Toast.makeText(getApplicationContext(), "Vuelva a intentarlo", Toast.LENGTH_LONG).show();
        }
    }

    //Getter
    public static TabLayout getTabLayout(){
        return tabLayout;
    }

    //Getter para la el elemento de musica de la intro..
    public static MediaPlayer getmPlayer() {
        return mPlayer;
    }

    /**
     * Clase que devolvera un fragment segun la seccion donde se encuentre
     * */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
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
         * @param position
         * @return
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

    public static int getAlto(){
        return alto;
    }

    public static int getAncho(){
        return ancho;
    }
}
