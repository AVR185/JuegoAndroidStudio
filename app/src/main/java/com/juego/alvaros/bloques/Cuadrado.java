package com.juego.alvaros.bloques;

import android.graphics.Canvas;
import android.graphics.Paint;
import com.juego.alvaros.Juego;

public class Cuadrado extends Rectangulo {
    /**Rectangulo que aparece en la parte superior de la pantalla en uan posicion aleatora en x
     *  y se mueve hacia la parte inferior de la pantalla
     *  a velocidad constante sin desplazarse en el eje x
     */

     public Cuadrado(Juego juego){
         super(juego);
         this.setDireccion_x(0);
         this.setVelocidad(30);
     }

    //Metodo necesita el primer argumento
    @Override
    public void Dibujar(Canvas c, Paint p){
        c.drawBitmap(Juego.getCuadrado(),coordenada_x,coordenada_y,p);
    }

    @Override
    public void CalcularCoordenadas() {
        double x;
        x = Math.random();
        coordenada_x =(int) Math.floor((anchoPantalla-80)*x);
        coordenada_y = 0;
    }
}
