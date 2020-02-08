package com.juego.alvaros.bloques;

import android.graphics.Canvas;
import com.juego.alvaros.Juego.Juego;

public class Hexagono extends Rectangulo {
    //Atributos
    private int velocidad = 5 * altoPantalla/1920;

    public Hexagono(Juego j, int nivel) {
        super(j, nivel);
        this.figura = Juego.getHexagono();
        this.alto = this.figura.getHeight();
        this.ancho = this.figura.getWidth();
        this.setVelocidad(this.velocidad + (nivel*2));
        this.setDireccion_y((Math.random()>=0.5)? 1:-1);
    }

    //Metodo necesita el primer argumento
    @Override
    public void Dibujar(Canvas c){
        c.drawBitmap(this.figura,this.coordenada_x, this.coordenada_y, null);
    }

    @Override
    public void ActualizarCoordenadas(){
        super.ActualizarCoordenadas();
        if (coordenada_y <=0 || coordenada_y > altoPantalla-alto){
            direccion_y = -direccion_y;
        }
    }

    @Override
    public void CalcularCoordenadas(){
        double y;
        y = Math.random();
        if(y>=0.5){ //sale por la izquierda
            this.coordenada_x = 0;
            this.coordenada_y = (int) Math.floor((altoPantalla-30)*y);

        } else { //sale por la derecha
            this.coordenada_x = anchoPantalla-30;
            this.direccion_x = -1;
            this.coordenada_y = (int) Math.floor((altoPantalla-30)*y);
        }

    }
}
