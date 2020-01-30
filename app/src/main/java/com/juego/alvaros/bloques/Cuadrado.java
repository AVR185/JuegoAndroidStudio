package com.juego.alvaros.bloques;

import android.graphics.Canvas;
import com.juego.alvaros.Juego;

/**Rectangulo que aparece en la parte superior de la pantalla en uan posicion aleatora en x
 *  y se mueve hacia la parte inferior de la pantalla
 *  a velocidad constante sin desplazarse en el eje x
 */
public class Cuadrado extends Rectangulo {
    //Atributos
    private int velocidad = 30;

    public Cuadrado(Juego juego){
        super(juego);
        this.setDireccion_x(0);
        this.setVelocidad(velocidad + (Juego.getNivel()*2));
    }

    //Metodo necesita el primer argumento
    @Override
    public void Dibujar(Canvas c){
        c.drawBitmap(Juego.getCuadrado(), this.coordenada_x, this.coordenada_y, null);
    }

    @Override
    public void CalcularCoordenadas() {
        double x;
        x = Math.random();
        coordenada_x =(int) Math.floor((anchoPantalla-80)*x);
        coordenada_y = 0;
    }
}
