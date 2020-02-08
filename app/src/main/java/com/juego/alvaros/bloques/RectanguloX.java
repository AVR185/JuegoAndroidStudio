package com.juego.alvaros.bloques;

import android.graphics.Canvas;

import com.juego.alvaros.Juego.Juego;

/**Rectangulo que aparece en la parte superior de la pantalla en uan posicion aleatora en x
 * y se mueve hacia la parte inferior de la pantalla a velocidad constante
 * desplazandose por el eje x y rebotando al llegar a un borde de la pantalla
 */
public class RectanguloX extends Rectangulo {

    public RectanguloX(Juego j, int nivel){
        super(j, nivel);
        this.setDireccion_x((Math.random()>=0.5)? 1:-1);
    }

    @Override
    public void Dibujar(Canvas c){
        c.drawBitmap(this.figura, this.coordenada_x, this.coordenada_y, null);
    }

    //Determina la posicion x en la que aparece
    @Override
    public void CalcularCoordenadas(){
        double x;
        x = Math.random();
        coordenada_x =(int) Math.floor((anchoPantalla-ancho)*x);
        coordenada_y = 0;
    }

    @Override
    public void ActualizarCoordenadas(){
        super.ActualizarCoordenadas();
        if (coordenada_x <=0 ||coordenada_x >= anchoPantalla-ancho){
            direccion_x = -direccion_x;
        }
    }
}