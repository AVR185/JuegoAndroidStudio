package com.juego.alvaros.bloques;

import android.graphics.Canvas;
import com.juego.alvaros.Juego.Juego;

/**Rectangulo que aparece en la parte superior de la pantalla en uan posicion aleatora en x
 *  y se mueve hacia la parte inferior de la pantalla
 *  a velocidad constante sin desplazarse en el eje x
 */
public class Cuadrado extends Rectangulo {
    //Atributos
    private int velocidad = 20 * altoPantalla/1920;

    public Cuadrado(Juego juego, int nivel){
        super(juego, nivel);
        this.figura = Juego.getCuadrado();
        this.alto = this.figura.getHeight();
        this.ancho = this.figura.getWidth();
        this.setDireccion_x(0);
        this.setVelocidad(this.velocidad + (nivel*2));
    }

    //Metodo necesita el primer argumento
    @Override
    public void Dibujar(Canvas c){
        c.drawBitmap(this.figura, this.coordenada_x, this.coordenada_y, null);
    }

    @Override
    public void CalcularCoordenadas() {
        double x;
        x = Math.random();
        coordenada_x =(int) Math.floor((anchoPantalla-ancho)*x);
        coordenada_y = 0;
    }
}
