package com.example.mijuego2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private Handler handler=new Handler();
    private Timer t; // declaramos la variable t como una variable de instancia
    private Button botonReiniciar;
    private MediaPlayer mediaPlayer; // Objeto para reproducir música de fondo
    private MediaPlayer mediaPlayer2; // Objeto para reproducir música de victoria
    private MediaPlayer mediaPlayer3; // Objeto para reproducir música de derrota
    private MediaPlayer Corte; // Objeto para reproducir música de efecto de corte
    private MediaPlayer Impacto; // Objeto para reproducir música de efecto de impacto
    private int contadorColisiones = 3;
    private int contadorRayos = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Juego galaxy = new Juego(this);
        setContentView(galaxy);

        // Carga la música del archivo mp3
        mediaPlayer = MediaPlayer.create(this, R.raw.rajangmusic);
        mediaPlayer2 = MediaPlayer.create(this, R.raw.huntwin);
        mediaPlayer3 = MediaPlayer.create(this, R.raw.huntloss);
        Corte = MediaPlayer.create(this, R.raw.corte);
        Impacto = MediaPlayer.create(this, R.raw.golpe);
        mediaPlayer.setLooping(true); // Reproduce la música en un loop
        mediaPlayer.start(); // Inicia la reproducción de la música

        t = new Timer(); // inicializamos la variable t en el método onCreate
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        galaxy.invalidate();
                    }
                });
            }
        },0,50);
    }

    class Juego extends View {

        int posiciorajangX;
        int posiciorajangY;
        int posicionhunterX;
        int posicionhunterY;
        int posicioSNSX;
        int posicioSNSY;
        int movimientohunter = 1; // Inicializamos la dirección del "hunter" a 1, comenzara hacia la izquierda
        boolean gameOver = false; // variable para indicar si el juego ha terminado
        boolean gameWin = false;
        int posicionRayoX;
        int posicionRayoY;


        public Juego(Context context) {
            super(context);
            //posiciones del "rajang"
            posiciorajangX = 200;
            posiciorajangY = 1700;
            //posiciones de la espada y escudo "SNS"
            posicioSNSX = 600;
            posicioSNSY = 100;
            //posiciones del "hunter"
            posicionhunterX = 800;
            posicionhunterY = 130;
            // posición del rayo
            posicionRayoX = posiciorajangX;
            posicionRayoY = posiciorajangY;

        }
        @Override
        protected void onDraw(Canvas canvas) {
            pintaFondo(canvas);
            //Imagen del "rajang" y del "hunter"
            Bitmap rajang= BitmapFactory.decodeResource(getResources(), R.drawable.rajang);
            canvas.drawBitmap(rajang, posiciorajangX, posiciorajangY, null);
            Bitmap hunter = BitmapFactory.decodeResource(getResources(), R.drawable.hunter);
            canvas.drawBitmap(hunter, posicionhunterX, posicionhunterY, null);

            Bitmap cabezahunter = BitmapFactory.decodeResource(getResources(), R.drawable.headhunter);
            canvas.drawBitmap(cabezahunter, 10, 15, null);
            //TEXTO DEL CAZADOR
            Paint life = new Paint();
            life.setColor(Color.WHITE);
            life.setTextSize(50);
            canvas.drawText("CAZADOR: "+ contadorRayos,   80, 60, life);

            //BOTON REINICIAR EL JUEGO SI SE PIERDE
            if (gameOver && contadorColisiones == 0 && botonReiniciar == null) {
                botonReiniciar = new Button(getContext());
                Bitmap perdido = BitmapFactory.decodeResource(getResources(), R.drawable.questfailed);
                canvas.drawBitmap(perdido, 130, 430, null);
                botonReiniciar.setText("Reiniciar Juego");
                botonReiniciar.setTextSize(20);
                botonReiniciar.setX(310);
                botonReiniciar.setY(1100);
                botonReiniciar.setLayoutParams(new ViewGroup.LayoutParams(400, 150));
                ((ViewGroup) getParent()).addView(botonReiniciar);

                botonReiniciar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gameOver = false;
                        contadorColisiones = 3;
                        gameWin = false;
                        contadorRayos = 3;
                        mediaPlayer3.pause();
                        mediaPlayer3.seekTo(0);
                        mediaPlayer.start();
                        ((ViewGroup) getParent()).removeView(botonReiniciar);
                        botonReiniciar = null;
                        // Crear una nueva instancia de la clase Juego
                        Juego galaxy = new Juego(getContext());
                        setContentView(galaxy);
                        // Reiniciar el temporizador
                        t.cancel();
                        t = new Timer();
                        t.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                handler.post(new Runnable() {
                                    public void run() {
                                        galaxy.invalidate();
                                    }
                                });
                            }
                        }, 0, 50);
                    }
                });
            } else if (botonReiniciar != null) {
                botonReiniciar.setVisibility(View.VISIBLE);
            }
            //BOTON REINICIAR SI SE GANA
            if (gameWin && contadorRayos == 0 && botonReiniciar == null) {
                botonReiniciar = new Button(getContext());
                Bitmap completado = BitmapFactory.decodeResource(getResources(), R.drawable.questcomplete);
                canvas.drawBitmap(completado, 160, 430, null);
                botonReiniciar.setText("Reiniciar");
                botonReiniciar.setTextSize(20);
                botonReiniciar.setX(340);
                botonReiniciar.setY(1100);
                botonReiniciar.setLayoutParams(new ViewGroup.LayoutParams(400, 150));
                ((ViewGroup) getParent()).addView(botonReiniciar);

                botonReiniciar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gameWin = false;
                        contadorRayos = 3;
                        gameOver = false;
                        contadorColisiones = 3;
                        mediaPlayer2.pause();
                        mediaPlayer2.seekTo(0);
                        mediaPlayer.start();
                        ((ViewGroup) getParent()).removeView(botonReiniciar);
                        botonReiniciar = null;
                        // Crear una nueva instancia de la clase Juego
                        Juego galaxy = new Juego(getContext());
                        setContentView(galaxy);
                        // Reiniciar el temporizador
                        t.cancel();
                        t = new Timer();
                        t.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                handler.post(new Runnable() {
                                    public void run() {
                                        galaxy.invalidate();
                                    }
                                });
                            }
                        }, 0, 50);
                    }
                });
            } else if (botonReiniciar != null) {
                botonReiniciar.setVisibility(View.VISIBLE);
            }
            //Genera la SNS y el movimiento descendiente
            if(posicioSNSY >= posiciorajangY) { //Verifica que "SNS" llega a la altura del objeto "Rajang"
                posicioSNSX = posicionhunterX; // Se establace en la misma posicion del "hunter"
                posicioSNSY = posicionhunterY + 280; // Se ubica la posicion vertical del hunter + 280 por debajo
            }
            else {
                Bitmap sns = BitmapFactory.decodeResource(getResources(), R.drawable.sns);
                canvas.drawBitmap(sns, posicioSNSX, posicioSNSY, null);
                posicioSNSY += 40; // la imagen "sns" desciende verticalmente
                if (seIntersectan(canvas)) { // verifica si la espada SNS colisiona con el objeto "Rajang"
                    contadorColisiones--; // reduce el contador de colisiones de 3 a 0
                    if (contadorColisiones == 0) { // si el contador de colisiones llega a 0
                        gameOver = true; // establecer el valor de gameOver en true
                        mediaPlayer.pause(); // Pausa la musica de fondo
                        mediaPlayer3.start(); //Reproduce la musica de derrota
                        t.cancel(); // detener el temporizador
                    }
                }
            }

            //GENERA LA IMAGEN RAYO movimiento ascendente
            if (posicionRayoY <= posicionhunterY) { // Verifica que "rayo" llego a la altura del "hunter"
                posicionRayoX = posiciorajangX; // Se establece desde la posición horizontal del "rajang"
                posicionRayoY = posiciorajangY; // Se establece desde la posicion vertical del "rajang"
            }
            else {
                Bitmap rayo = BitmapFactory.decodeResource(getResources(), R.drawable.rayo);
                canvas.drawBitmap(rayo, posicionRayoX, posicionRayoY, null);
                posicionRayoY -= 40; // movemos la imagen del rayo hacia arriba
                if (seAtaca(canvas)) { // verifica si el rayo colisiona con el objeto "hunter"
                    contadorRayos--; // reduce el contador de colisiones de rayos de 3 a 0
                    if (contadorRayos == 0) { // si el contador de colisiones llega a 0
                        gameWin = true; // establecer el valor de gameOver en true
                        mediaPlayer.pause(); //Pausa la musica de fondo
                        mediaPlayer2.start(); //Reproduce la musica de victoria
                        t.cancel(); // detener el temporizador
                    }
                }
            }

            Bitmap cabezarajang = BitmapFactory.decodeResource(getResources(), R.drawable.headrajang);
            canvas.drawBitmap(cabezarajang, 730, 15, null);
            //VIDA DEL RAJANG
            Paint muerte = new Paint();
            muerte.setColor(Color.WHITE);
            muerte.setTextSize(50);
            canvas.drawText("RAJANG: "+ contadorColisiones,   800, 60, muerte);

            //Movimiento de derecha a izquierda y viceversa del "hunter"

            //int movimientohunter = 1; Inicializamos la dirección del "hunter" a 1
            int movimiento = 10; //Indica cuanto se movera el hunter
            if (posicionhunterX >= getWidth() - hunter.getWidth()) { // Verifica que alcanzo un borde lateral
                movimientohunter = -1; // Movimiento hacia la izquierda
            } else if (posicionhunterX <= 0) {
                movimientohunter = 1; //Movimiento hacia la derecha
            }
            posicionhunterX += movimiento * movimientohunter; //Actualiza la posicion horizontal del hunter

            //SI EL JUEGO TERMINA APARECE EL GAME OVER o GAME WIN Y EL JUEGO SE DETIENE
            if (gameOver) {
                Paint paintTexto = new Paint();
                paintTexto.setColor(Color.WHITE);
                paintTexto.setTextSize(100);
                canvas.drawText("GAME OVER", getWidth() / 2 - 300, getHeight() / 2, paintTexto);
            }
            if (gameWin) {
                Paint paintTexto = new Paint();
                paintTexto.setColor(Color.WHITE);
                paintTexto.setTextSize(100);
                canvas.drawText("YOU WIN", getWidth() / 2 - 200, getHeight() / 2, paintTexto);
            }

            //La imagen de colision se crea en la ubicacion del rajang
            if(seIntersectan(canvas)){
                Bitmap critico = BitmapFactory.decodeResource(getResources(), R.drawable.critico);
                canvas.drawBitmap(critico, posiciorajangX, posiciorajangY, null);
                Corte.start(); // inicia la reproducción
            }
            //La imagen de colision se crea en la ubicacion del hunter
            if(seAtaca(canvas)){
                Bitmap golpe = BitmapFactory.decodeResource(getResources(), R.drawable.golpe);
                canvas.drawBitmap(golpe, posicionhunterX, posicionhunterY, null);
                Impacto.seekTo(2000); // establece el tiempo de inicio en 2 segundos
                Impacto.start(); // inicia la reproducción
            }
        }

        //función "seIntersectan" devuelve verdadero si se están intersectando y falso si no se están intersectando.
        private boolean seIntersectan(Canvas canvas) {
            //Recuadro de la espada
            Rect recibeSNS = new Rect(posicioSNSX, posicioSNSY, posicioSNSX + 130, posicioSNSY + 1);
            //Recuadro del Rajang
            Rect recibeRajang = new Rect(posiciorajangX, posiciorajangY, posiciorajangX + 340, posiciorajangY + 300);
            //Los dos objetos se están intersectando, por lo que la función devuelve verdadero (true)
            if(Rect.intersects(recibeSNS, recibeRajang)){
                return true;
            }else{
                return false;
            }
        }

        //funciona "SeAtaca" devuelve verdadero o falso si el rayo y el hunter se estan o no, intersectando
        private boolean seAtaca(Canvas canvas){
            //Recuadro del rayo
            Rect recibeRayo = new Rect(posicionRayoX, posicionRayoY, posicionRayoX + 130, posicionRayoY + 1);
            //Recuadro del Hunter
            Rect recibeHunter = new Rect(posicionhunterX, posicionhunterY, posicionhunterX + 210, posicionhunterY + 50);
            //Los dos objetos se están intersectando, por lo que la función devuelve verdadero (true)
            if(Rect.intersects(recibeRayo, recibeHunter)){
                return true;
            }else{
                return false;
            }
        }

        @Override // Detectar cuando tocamos la pantalla
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                float x = event.getX(); // coordenada x: ¿derecha o izquierda?
                if (x > posiciorajangX) { //Donde se ubique x en la pantalla se dezplaza
                    //DERECHA!
                    posiciorajangX = posiciorajangX + 100;
                } else {
                    //IZQUIERDA!
                    posiciorajangX = posiciorajangX - 100;
                }
                invalidate();
            }
            return true;
        }
        public void pintaFondo(Canvas canvas) {
            //Carga la imagen de fondo y la aplica en su maximo de pantalla
            Bitmap fondo = BitmapFactory.decodeResource(getResources(), R.drawable.fondo);
            fondo = Bitmap.createScaledBitmap(fondo, getWidth(), getHeight(), true);
            canvas.drawBitmap(fondo,0, 0, null);
        }
    }
}
