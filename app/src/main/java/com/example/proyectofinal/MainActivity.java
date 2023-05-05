package com.example.proyectofinal;

import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextPaint;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //String currentMethod = Thread.currentThread().getStackTrace()[1].getMethodName();
    // Variable para el Fondo Principal, FondoFuncionamiento y Fondos de cada Historia (5 + advertencia + final)
    private ImageView FondoimageView,FondoSegundaHistoria,FondoTerceraHistoria,FondoCuartaHistoria
            ,FondoQuintaHistoria,FondoAtencion,FondoFinHistoria;
    //Variables para cargar el fondo de cada Boss Enemigo
    private ImageView FondoPrimerBoss,FondoSegundoBoss,FondoTercerBoss,FondoCuartoBoss,FondoFinalBoss;
    //Variables para carga el fondo de Victoria o Derrota de cada combate
    private ImageView FondoVictoria,FondoDerrota;
    //Variables para Cargar la imagen del cazador o cazadora y para guardar la imagen del genero elegido
    private ImageView hombreImageView,mujerImageView,generoImageView;
    //BOTONES DEL PRIMER COMBATE
    private ImageView botonLigero;      //Imagen del Boton Ligero es Papel
    private ImageView botonTactico;     //Imagen del Boton Tactico es Tijera
    private ImageView botonPotente;     //Imagen del Boton Potente es Piedra
    //BOTONES DEL SEGUNDO COMBATE
    private ImageView botonLigeroSegundo,botonTacticoSegundo,botonPotenteSegundo;
    //BOTONES DEL TERCER COMBATE
    private ImageView botonLigeroTercero,botonTacticoTercero,botonPotenteTercero;
    //BOTONES DEL CUARTO COMBATE
    private ImageView botonLigeroCuarto,botonTacticoCuarto,botonPotenteCuarto;
    //BOTONES DEL QUINTO COMBATE
    private ImageView botonLigeroQuinto,botonTacticoQuinto,botonPotenteQuinto;

    //Variables para cargar la imagen de cada enemigo
    private ImageView primerBossImageView,segundoBossImageView,terceroBossImageView
            ,cuartoBossImageView,quintoBossImageView;
    //EditText para solicitar el nombre del usuario y Texto para indicar el genero elegido
    private EditText IngresarNombre;
    private TextView GeneroTexto,VerNombre,NombreUsuario; //Se ve el nombre del usuario en cada combate

    //TextViews para mostrar el nombre de cada boss enemigo
    private TextView NombrePrimerBoss,NombreSegundoBoss,NombreTercerBoss,NombreCuartoBoss,NombreQuintoBoss;
    //TextView para mostrar el texto que cuenta cada historia segun el nivel
    private TextView historiaTextView;
    private TextView historiaSegundaTextView,historiaTerceraTextView,historiaCuartaTextView
            ,historiaQuintaTextView,textoAtencion,historiaFinalTextView;
    //TextView para ver la vida del Jugador y del Boss Enemigo ( cada nivel varia la vida)
    private TextView tvVidaUsuario,tvVidaEnemigo,tvVidaUsuario2,tvVidaEnemigo2,tvVidaUsuario3,tvVidaEnemigo3
            ,tvVidaUsuario4,tvVidaEnemigo4,tvVidaUsuario5,tvVidaEnemigo5;
    /*Button para su ocasion, los botones "comenzar" son aquellos que llevan al evento para comenzar
    un combate, mientras que los botones "continuar" son aquellos que se usan despues de una victoria
    para el evento que muestra el fondo con su historia respectiva*/
    private Button comenzarButton,continuarButton2,continuarButton3,continuarButton4,continuarButton5
            ,continuarButton6,continuarButton7,comenzar2Button,comenzar3Button,comenzar4Button,comenzar5Button;
    //Boton Reiniciar como indica su nombre al perder un combate se muestra en el Fondo Derrota
    private Button botonReiniciar;
    /*MediaPlayers para cargar los audios de musica, esta el Inicio que es la musica del Menu y de cada
    historia, estan los audios de cada boss, los audios de victoria o derrota y los audios de efecto especial*/
    private MediaPlayer musicaInicio,musicaPrimerBoss,musicaSegundoBoss,musicaTercerBoss,musicaCuartoBoss,musicaQuintoBoss
            ,musicaVictoria,musicaDerrota,musicaFinal,corte,impacto,papel,dolor,empate;
    /*variable de instancia para indicar el género seleccionado, string para guardar el nombre
    del jugador del IngresarNombre y mostrarlo en cada combate */
    private boolean esHombre = true;
    private RelativeLayout layout;
    private String nombrejugador;
    /*Variables para marcar, cada uno establece la vida de cada Usuario y de Cada Enemigo, por ultimo
    dos variables que uso para el ataque Cargado y Curacion que se realizan en el ultimo Boss Enemigo */
    private int contador = 0;
    private int vidaPrimerBoss = 20,vidaSegundoBoss = 20,vidaTercerBoss = 20,vidaCuartoBoss = 20,vidaQuintoBoss = 20;
    private int vidaUsuario = 50,vidaUsuario2 = 80,vidaUsuario3 = 100,vidaUsuario4 = 120,vidaUsuario5 = 150;
    private int ContarAtaqueCargado = 0;
    private int ContarCuracion = 0;
    private static final String TAG = "MiActividad";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cazadoresRef = db.collection("cazadores");

        //Inicializa la imagen de fondo
        FondoimageView = new ImageView(this);
        FondoimageView.setBackgroundResource(R.drawable.fondoprincipio);

        //Carga el fondo del  primer boss
        FondoPrimerBoss = new ImageView(this);
        FondoPrimerBoss.setBackgroundResource(R.drawable.fondoprimerboss);

        //Carga el fondo de victoria
        FondoVictoria = new ImageView(this);
        FondoVictoria.setBackgroundResource(R.drawable.fondovictoria);

        //Carga el fondo de derrota
        FondoDerrota = new ImageView(this);
        FondoDerrota.setBackgroundResource(R.drawable.fondoderrota);

        //Inicializar las vista de mujer,hombre y genero
        hombreImageView = new ImageView(this);
        mujerImageView = new ImageView(this);
        GeneroTexto = new TextView(this);

        //Boton que se usa para llamar al primer combate NIVEL 1
        comenzarButton = new Button(this);
        comenzarButton.setText("Comenzar");
        RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        lp4.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lp4.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp4.setMargins(0, 0, 0, 100);
        comenzarButton.setLayoutParams(lp4);

        //Cargar todas las MUSICAS DEL VIDEOJUEGO
        musicaInicio = MediaPlayer.create(this, R.raw.intro);
        musicaPrimerBoss = MediaPlayer.create(this, R.raw.primerboss);
        musicaVictoria = MediaPlayer.create(this,R.raw.victoria);
        musicaDerrota = MediaPlayer.create(this, R.raw.derrota);
        musicaSegundoBoss = MediaPlayer.create(this,R.raw.segundoboss);
        musicaTercerBoss = MediaPlayer.create(this,R.raw.tercerboss);
        musicaCuartoBoss = MediaPlayer.create(this,R.raw.cuartoboss);
        musicaQuintoBoss = MediaPlayer.create(this,R.raw.finalboss);
        musicaFinal = MediaPlayer.create(this,R.raw.finalhistoria);
        corte = MediaPlayer.create(this,R.raw.cut);
        impacto = MediaPlayer.create(this,R.raw.impact);
        papel = MediaPlayer.create(this,R.raw.papelhit);
        dolor = MediaPlayer.create(this,R.raw.dolorcazador);
        empate = MediaPlayer.create(this,R.raw.error);
        musicaInicio.setLooping(true); // Reproduce la música en un loop
        musicaInicio.start(); // Inicia la reproducción de la música

        //ImageView para la imagen de HOMBRE y establecer su imagen en el FondoPrincipal
        hombreImageView = new ImageView(this);
        hombreImageView.setImageResource(R.drawable.huntermen);
        hombreImageView.setX(50);
        hombreImageView.setY(1100);

        //ImageView para la imagen de MUJER y establecer su imagen en el FondoPrincipal
        mujerImageView = new ImageView(this);
        mujerImageView.setImageResource(R.drawable.huntergirl);
        mujerImageView.setX(580);
        mujerImageView.setY(1100);

        //BOTONES DEL PRIMER COMBATE
        //Boton Ligero
        botonLigero = new ImageView(this);
        botonLigero.setImageResource(R.drawable.botonesligero);
        botonLigero.setX(600);
        botonLigero.setY(1328);
        //Boton Tactico
        botonTactico = new ImageView(this);
        botonTactico.setImageResource(R.drawable.botonestactico);
        botonTactico.setX(600);
        botonTactico.setY(1530);
        //Boton Potente
        botonPotente = new ImageView(this);
        botonPotente.setImageResource(R.drawable.botonespotente);
        botonPotente.setX(600);
        botonPotente.setY(1740);

        //ImageView para la imagen del PRIMER BOSS IFRIT y establecer su imagen
        primerBossImageView = new ImageView(this);
        primerBossImageView.setImageResource(R.drawable.ifritprimero);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        primerBossImageView.setLayoutParams(params);
        primerBossImageView.setY(50);

        //ImageView para la imagen del SEGUNDO BOSS GOBLIN y establecer su imagen
        segundoBossImageView = new ImageView(this);
        segundoBossImageView.setImageResource(R.drawable.globinsegundo);
        RelativeLayout.LayoutParams paramos = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        paramos.addRule(RelativeLayout.CENTER_HORIZONTAL);
        segundoBossImageView.setLayoutParams(paramos);
        segundoBossImageView.setY(50);

        //ImageView para la imagen del TERCERO BOSS UKANLOS y establecer su imagen
        terceroBossImageView = new ImageView(this);
        terceroBossImageView.setImageResource(R.drawable.ukanlostercero);
        RelativeLayout.LayoutParams paramos2 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        paramos2.addRule(RelativeLayout.CENTER_HORIZONTAL);
        terceroBossImageView.setLayoutParams(paramos2);
        terceroBossImageView.setY(50);

        //ImageView para la imagen del CUARTO BOSS GRAND MINOTAURO y establecer su imagen
        cuartoBossImageView = new ImageView(this);
        cuartoBossImageView.setImageResource(R.drawable.minotaurocuarto);
        RelativeLayout.LayoutParams paramos3 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        paramos3.addRule(RelativeLayout.CENTER_HORIZONTAL);
        cuartoBossImageView.setLayoutParams(paramos3);
        cuartoBossImageView.setY(50);

        //ImageView para la imagen del QUINTO BOSS BEHEMOTH y establecer su imagen
        quintoBossImageView = new ImageView(this);
        quintoBossImageView.setImageResource(R.drawable.behemontquinto);
        RelativeLayout.LayoutParams paramos4 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        quintoBossImageView.setLayoutParams(paramos4);
        quintoBossImageView.setX(320);
        quintoBossImageView.setY(50);

        //EditText para solicitar el nombre del usuario
        IngresarNombre = new EditText(this);
        IngresarNombre.setHint("Ingrese su nombre");
        IngresarNombre.setInputType(InputType.TYPE_CLASS_TEXT);
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        lp1.addRule(RelativeLayout.CENTER_IN_PARENT);
        lp1.setMargins(50, 50, 50, 50);
        IngresarNombre.setLayoutParams(lp1);
        IngresarNombre.setX(0);
        IngresarNombre.setY(-500);


        //Boton para ir cambiando el genero del cazador que se va a usar durante el resto del juego
        Button cambiarGeneroButton = new Button(this);
        cambiarGeneroButton.setText("Cambiar género");
        RelativeLayout.LayoutParams lp6 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        lp6.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lp6.addRule(RelativeLayout.BELOW, IngresarNombre.getId()); // coloca el botón debajo del EditText
        lp6.setMargins(0, 50, 0, 0);
        cambiarGeneroButton.setLayoutParams(lp6);
        cambiarGeneroButton.setY(750);

        //ImageView para mostrar la imagen de género que se va a elegir durante el resto del juego
        generoImageView = new ImageView(this);
        RelativeLayout.LayoutParams lp7 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        lp7.setMargins(0, 50, 0, 0);
        generoImageView.setLayoutParams(lp7);
        generoImageView.setX(40);
        generoImageView.setY(1280);

        // Evento clic para cambiar el género
        cambiarGeneroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (esHombre) {
                    hombreImageView.setVisibility(View.GONE); // ocultar la imagen de hombre
                    mujerImageView.setVisibility(View.VISIBLE); // mostrar la imagen de mujer
                    GeneroTexto.setText("Tu personaje es: MUJER"); // actualizar el texto
                    esHombre = false; // establecer el género como mujer
                } else {
                    mujerImageView.setVisibility(View.GONE); // ocultar la imagen de mujer
                    hombreImageView.setVisibility(View.VISIBLE); // mostrar la imagen de hombre
                    GeneroTexto.setText("Tu personaje es: HOMBRE"); // actualizar el texto
                    esHombre = true; // establecer el género como hombre
                }
                // Verificar el género seleccionado y establecer la imagen correspondiente
                if (esHombre) {
                    generoImageView.setImageResource(R.drawable.huntermen); // establece la imagen del hombre
                } else {
                    generoImageView.setImageResource(R.drawable.huntergirl); // establece la imagen de la mujer
                }
            }
        });

        //TextView para mostrar el nombre del jugador
        VerNombre = new TextView(this);
        VerNombre.setTextSize(16);
        VerNombre.setTextColor(Color.WHITE);
        VerNombre.setVisibility(View.GONE);
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        lp2.addRule(RelativeLayout.CENTER_IN_PARENT);
        VerNombre.setLayoutParams(lp2);
        VerNombre.setX(0);
        VerNombre.setY(-500);

        //TextView para mostrar el nombre del jugador en cada combate
        NombreUsuario = new TextView(this);
        NombreUsuario.setTextSize(16);  // Tamaño de fuente del texto
        NombreUsuario.setTextColor(Color.WHITE);  // Color del texto en blanco
        NombreUsuario.setVisibility(View.GONE);
        RelativeLayout.LayoutParams lp10 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        NombreUsuario.setLayoutParams(lp10);
        NombreUsuario.setX(170);
        NombreUsuario.setY(1260);

        //Texto para ver la opcion de elegir el genero sobre el boton Genero
        GeneroTexto = new TextView(this);
        GeneroTexto.setText("TOCA EL GÉNERO A ELEGIR");
        GeneroTexto.setTextSize(14);
        GeneroTexto.setTextColor(Color.WHITE);
        GeneroTexto.setX(278);
        GeneroTexto.setY(695);

        //Mostrar el nombre del primer boss IFRIT
        NombrePrimerBoss = new TextView(this);
        NombrePrimerBoss.setText("DEMONIO IFRIT");
        NombrePrimerBoss.setTextSize(14);
        NombrePrimerBoss.setTextColor(Color.WHITE);
        NombrePrimerBoss.setX(390);
        NombrePrimerBoss.setY(20);
        //Trazo Negro Primer Boss
        TextPaint primerenemigo = NombrePrimerBoss.getPaint();
        primerenemigo.setStrokeWidth(0); // establecer el ancho
        primerenemigo.setStyle(Paint.Style.FILL_AND_STROKE);
        primerenemigo.setColor(Color.WHITE); // Establece el color del texto en blanco
        primerenemigo.setShadowLayer(10, 0, 0, Color.BLACK);

        //Mostrara el nombre del segundo boss GOBLIN
        NombreSegundoBoss = new TextView(this);
        NombreSegundoBoss.setText("REY GOBLIN");
        NombreSegundoBoss.setTextSize(14);
        NombreSegundoBoss.setTextColor(Color.WHITE);
        NombreSegundoBoss.setX(400);
        NombreSegundoBoss.setY(20);
        //Trazo Negro Segunda Boss
        TextPaint paintsegundo = NombreSegundoBoss.getPaint();
        paintsegundo.setStrokeWidth(0);
        paintsegundo.setStyle(Paint.Style.FILL_AND_STROKE);
        paintsegundo.setColor(Color.WHITE);
        paintsegundo.setShadowLayer(10, 0, 0, Color.BLACK);

        //Mostrar el nombre del tercer boss UKANLOS
        NombreTercerBoss = new TextView(this);
        NombreTercerBoss.setText("GRAND UKANLOS");
        NombreTercerBoss.setTextSize(14);
        NombreTercerBoss.setTextColor(Color.WHITE);
        NombreTercerBoss.setX(386);
        NombreTercerBoss.setY(20);
        //Trazo Negro Tercer Boss
        TextPaint painttercero = NombreTercerBoss.getPaint();
        painttercero.setStrokeWidth(0);
        painttercero.setStyle(Paint.Style.FILL_AND_STROKE);
        painttercero.setColor(Color.WHITE);
        painttercero.setShadowLayer(10, 0, 0, Color.BLACK);

        //Mostrar el nombre del cuarto boss
        NombreCuartoBoss = new TextView(this);
        NombreCuartoBoss.setText("GOLD MINOTAURO");
        NombreCuartoBoss.setTextSize(14);
        NombreCuartoBoss.setTextColor(Color.WHITE);
        NombreCuartoBoss.setX(390);
        NombreCuartoBoss.setY(20);
        //Trazo Negro Cuarto Boss
        TextPaint paintcuarto = NombreCuartoBoss.getPaint();
        paintcuarto.setStrokeWidth(0);
        paintcuarto.setStyle(Paint.Style.FILL_AND_STROKE);
        paintcuarto.setColor(Color.WHITE);
        paintcuarto.setShadowLayer(10, 0, 0, Color.BLACK);

        //Mostrar el nombre del quinto boss
        NombreQuintoBoss = new TextView(this);
        NombreQuintoBoss.setText("BEHEMOTH");
        NombreQuintoBoss.setTextSize(14);
        NombreQuintoBoss.setTextColor(Color.WHITE);
        NombreQuintoBoss.setX(390);
        NombreQuintoBoss.setY(20);
        //Trazo Negro Quinto Boss
        TextPaint paintquinto = NombreQuintoBoss.getPaint();
        paintquinto.setStrokeWidth(0);
        paintquinto.setStyle(Paint.Style.FILL_AND_STROKE);
        paintquinto.setColor(Color.WHITE);
        paintquinto.setShadowLayer(10, 0, 0, Color.BLACK);

        //TextView para la historia principal del cazador, trazo y establecer posicion
        historiaTextView = new TextView(this);
        historiaTextView.setTextColor(Color.WHITE);
        historiaTextView.setTextSize(21);
        historiaTextView.setY(-650);
        historiaTextView.setText("Eres un cazador de una tribu y tu misión es derrotar a los enemigos que amenazan tu territorio, de esta forma traer la paz a tu tribu. Te adentras en las zonas volcánicas y encuentras a un pequeño Ifrit, un demonio de fuego. ¡Te preparas para el combate!");
        //Trazo Negro Historia Principal
        TextPaint paint = historiaTextView.getPaint();
        paint.setStrokeWidth(0);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.WHITE);
        paint.setShadowLayer(10, 0, 0, Color.BLACK);
        RelativeLayout.LayoutParams lp5 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        lp5.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lp5.addRule(RelativeLayout.CENTER_VERTICAL);
        lp5.setMargins(50, 50, 50, 50);
        historiaTextView.setLayoutParams(lp5);

        //Botón "Siguiente" ( este boton se usa para el FondoPrincipal, FondoFuncionamiento y FondoPrimeraHistoria
        //Uso el int contador para usarlo 2 veces para cambiar entre esos 3 fondos
        Button button = new Button(this);
        button.setText("Siguiente");
        RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        lp3.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lp3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp3.setMargins(0, 0, 0, 50);
        button.setLayoutParams(lp3);

        // Agregar EditText, TextView, botón y imagen a la vista
        layout = new RelativeLayout(this);
        layout.addView(FondoimageView);
        layout.addView(hombreImageView);
        layout.addView(mujerImageView);
        layout.addView(IngresarNombre);
        layout.addView(VerNombre);
        layout.addView(button);
        layout.addView(GeneroTexto);
        setContentView(layout);
        layout.addView(cambiarGeneroButton);

        // Agregar acción al EditText para mostrar el nombre del jugador
        IngresarNombre.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String nombre = IngresarNombre.getText().toString();
                    if (!nombre.isEmpty()) {
                        VerNombre.setText("¡Bienvenido " + nombre + ", preparate para la caza!");
                        VerNombre.setVisibility(View.VISIBLE);
                        IngresarNombre.setVisibility(View.GONE);
                        button.setVisibility(View.VISIBLE);
                    }
                    return true;
                }
                return false;
            }
        });

        // Agregar acción al botón "Siguiente" para cambiar el fondo de la imagen
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int backgroundImage = 0;
                if (FondoimageView.getTag() == null || (int)FondoimageView.getTag() == R.drawable.fondoprincipio) {
                    backgroundImage = R.drawable.fondofuncionamiento;
                    String nombreCazador = IngresarNombre.getText().toString();
                    String gen = GeneroTexto.getText().toString();
                    Map<String, Object> data = new HashMap<>();
                    data.put("Nombre", nombreCazador);
                    data.put("Genero", gen);
                    cazadoresRef.document().set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "Nombre guardado en Firestore Database");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error al guardar nombre en Firestore Database", e);
                                }
                            });
                } else if ((int)FondoimageView.getTag() == R.drawable.fondofuncionamiento) {
                    backgroundImage = R.drawable.fondohistoria;
                    if(backgroundImage == R.drawable.fondohistoria) {
                        layout.addView(historiaTextView); //Muestra el texto de historia principal
                        layout.addView(comenzarButton); //Muestra el boton Comenzar
                        //En este String se guardara el nombre del jugador ingresado
                        nombrejugador = IngresarNombre.getText().toString();
                        //Evento del Boton Comenzar para iniciar el primer combate NIVEL 1
                        comenzarButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //llama al metodo que ejecuta el nivel 1
                                comenzarprimercombate();
                            }
                        });

                    }
                }
                //oculta fondo, imagenes , texto y boton seguiente
                cambiarGeneroButton.setVisibility(View.GONE);
                FondoimageView.setBackgroundResource(backgroundImage);
                FondoimageView.setTag(backgroundImage);
                VerNombre.setVisibility(View.GONE);
                GeneroTexto.setVisibility(View.GONE);
                IngresarNombre.setVisibility(View.GONE);
                hombreImageView.setVisibility(View.GONE);
                mujerImageView.setVisibility(View.GONE);
                contador++;
                if (contador == 2) {
                    //Al usar dos veces el boton en FondoFunc. y FondoHist. este se oculte
                    button.setVisibility(View.GONE);
                }
            }
        });

        // Crear botón de Reinicio y establecer su posicion
        botonReiniciar = new Button(this);
        botonReiniciar.setText("Reiniciar Nivel");
        botonReiniciar.setX(375);
        botonReiniciar.setY(1300);
        //Evento Click para llamar al metodo reiniciarJuego()
        botonReiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reiniciarJuego();
            }
        });

        //Boton Continuar2 para mostrar la segundo historia despues del nivel 1
        continuarButton2 = new Button(this);
        continuarButton2.setText("Continuar");
        continuarButton2.setX(410);
        continuarButton2.setY(1300);
        continuarButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                segundaHistoria();
            }
        });

        //Boton Continuar3 para mostrar la tercera historia despues del nivel 2
        continuarButton3 = new Button(this);
        continuarButton3.setText("Continuar");
        continuarButton3.setX(410);
        continuarButton3.setY(1300);
        continuarButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                terceraHistoria();
            }
        });

        //Boton Continuar4 para mostrar la cuarta historia despues del nivel 3
        continuarButton4 = new Button(this);
        continuarButton4.setText("Continuar");
        continuarButton4.setX(410);
        continuarButton4.setY(1300);
        continuarButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cuartaHistoria();
            }
        });

        //Boton Continuar5 para mostrar la quinta historia despues del nivel 4
        continuarButton5 = new Button(this);
        continuarButton5.setText("Continuar");
        continuarButton5.setX(410);
        continuarButton5.setY(1300);
        continuarButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quintaHistoria();
            }
        });
        //Boton Continuar para mostrar la historia final despues del nivel 5
        continuarButton6 = new Button(this);
        continuarButton6.setText("Continuar");
        continuarButton6.setX(410);
        continuarButton6.setY(1300);
        continuarButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalHistoria();
            }
        });
    }
    public void comenzarprimercombate() {
        // Eliminar cualquier vista anterior ( se usa al reiniciar)
        layout.removeView(FondoSegundoBoss);
        layout.removeView(NombreSegundoBoss);
        layout.removeView(segundoBossImageView);
        layout.removeView(tvVidaUsuario);
        layout.removeView(tvVidaEnemigo);

        layout.addView(FondoPrimerBoss); //Carga el Fondo
        musicaInicio.pause();   //Pausar musica
        musicaPrimerBoss.start();   //Iniciar musica del primer Boss
        musicaPrimerBoss.setLooping(true);  //Que se repita en bucle la musica
        historiaTextView.setVisibility(View.GONE);  //Ocultar Texto Historia Principal
        comenzarButton.setVisibility(View.GONE);    //Ocultar Boton Comenzar
        NombreUsuario.setText(nombrejugador);
        NombreUsuario.setVisibility(View.VISIBLE);  //Mostrar nombre del jugador
        //Trazo Negro Nombre Jugador
        TextPaint paint = NombreUsuario.getPaint();
        paint.setStrokeWidth(0);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.WHITE);
        paint.setShadowLayer(15, 5, 0, Color.BLACK);

        layout.addView(botonLigero);    //Muestra el Boton Ligero (papel)
        layout.addView(botonTactico);   //Muestra el Boton Tactico (tijera)
        layout.addView(botonPotente);   //Muestra el Boton Potente (piedra)
        layout.addView(NombreUsuario);
        layout.addView(generoImageView); //Muestra la imagen del genero elegido
        layout.addView(NombrePrimerBoss);
        layout.addView(primerBossImageView); //Muestra la imagen del primer boss
        funcionataqueLigero();      //Se aplica la metodologia del Boton Ligero
        funcionataqueTactico();     //Se aplica la metodologia del Boton Tactico
        funcionataquePotente();     //Se aplica la metodologia del Boton Potente
        // Agregar TextViews que muestran las vidas del usuario y del enemigo
        tvVidaUsuario = new TextView(this);
        tvVidaUsuario.setText("Vida Jugador: " + vidaUsuario);
        tvVidaUsuario.setTextSize(24);
        tvVidaUsuario.setY(1100);
        tvVidaUsuario.setTextColor(Color.GREEN);
        tvVidaUsuario.setGravity(Gravity.CENTER_HORIZONTAL);
        tvVidaUsuario.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(tvVidaUsuario);

        tvVidaEnemigo = new TextView(this);
        tvVidaEnemigo.setText("Vida Enemigo: " + vidaPrimerBoss);
        tvVidaEnemigo.setTextSize(24);
        tvVidaEnemigo.setY(710);
        tvVidaEnemigo.setTextColor(Color.WHITE);
        tvVidaEnemigo.setGravity(Gravity.CENTER_HORIZONTAL);
        tvVidaEnemigo.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(tvVidaEnemigo);

    }
    public void comenzarsegundocombate() {
        //Pausa musica de la segunda historia, oculta boton comenzar2 y inicia musica del segundo boss en ciclo
        musicaInicio.pause();
        comenzar2Button.setVisibility(View.GONE);
        musicaSegundoBoss.start();
        musicaSegundoBoss.setLooping(true);
        //Cargar el fondo, el nombre y imagen del segundo BOSS
        FondoSegundoBoss = new ImageView(this);
        FondoSegundoBoss.setBackgroundResource(R.drawable.fondosegundoboss);
        layout.addView(FondoSegundoBoss);
        layout.addView(NombreSegundoBoss);
        layout.addView(segundoBossImageView);
        //Carga la imagen de genero y su nombre
        layout.addView(generoImageView);
        generoImageView.setVisibility(View.VISIBLE);
        layout.addView(NombreUsuario);
        //BOTONES DEL SEGUNDO COMBATE
        //Boton Ligero
        botonLigeroSegundo = new ImageView(this);
        botonLigeroSegundo.setImageResource(R.drawable.botonesligero);
        botonLigeroSegundo.setX(600);
        botonLigeroSegundo.setY(1328);
        layout.addView(botonLigeroSegundo);
        //Boton Tactico
        botonTacticoSegundo = new ImageView(this);
        botonTacticoSegundo.setImageResource(R.drawable.botonestactico);
        botonTacticoSegundo.setX(600);
        botonTacticoSegundo.setY(1530);
        layout.addView(botonTacticoSegundo);
        //Boton Potente
        botonPotenteSegundo = new ImageView(this);
        botonPotenteSegundo.setImageResource(R.drawable.botonespotente);
        botonPotenteSegundo.setX(600);
        botonPotenteSegundo.setY(1740);
        layout.addView(botonPotenteSegundo);
        funcionataqueLigero2();
        funcionataqueTactico2();
        funcionataquePotente2();
        // Agregar TextViews que muestran las vidas del usuario y del enemigo
        tvVidaUsuario2 = new TextView(this);
        tvVidaUsuario2.setText("Vida Jugador: " + vidaUsuario2);
        tvVidaUsuario2.setTextSize(24);
        tvVidaUsuario2.setY(1100);
        tvVidaUsuario2.setTextColor(Color.GREEN);
        tvVidaUsuario2.setGravity(Gravity.CENTER_HORIZONTAL);
        tvVidaUsuario2.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(tvVidaUsuario2);

        tvVidaEnemigo2 = new TextView(this);
        tvVidaEnemigo2.setText("Vida Enemigo: " + vidaSegundoBoss);
        tvVidaEnemigo2.setTextSize(24);
        tvVidaEnemigo2.setY(710);
        tvVidaEnemigo2.setTextColor(Color.WHITE);
        tvVidaEnemigo2.setGravity(Gravity.CENTER_HORIZONTAL);
        tvVidaEnemigo2.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(tvVidaEnemigo2);
    }
    public void comenzartercercombate(){
        //Pausa musica de la tercera historia, oculta boton comenzar3 y inicia musica del tercer boss en ciclo
        musicaInicio.pause();
        comenzar3Button.setVisibility(View.GONE);
        musicaTercerBoss.start();
        musicaTercerBoss.setLooping(true);
        //Cargar el fondo, su nombre y imagen del tercer BOSS
        FondoTercerBoss = new ImageView(this);
        FondoTercerBoss.setBackgroundResource(R.drawable.fondotercerboss);
        layout.addView(FondoTercerBoss);
        layout.addView(NombreTercerBoss);
        layout.addView(terceroBossImageView);
        //Carga la imagen de genero y su nombre
        layout.addView(generoImageView);
        generoImageView.setVisibility(View.VISIBLE);
        layout.addView(NombreUsuario);
        //BOTONES DEL TERCER COMBATE
        //Boton Ligero
        botonLigeroTercero = new ImageView(this);
        botonLigeroTercero.setImageResource(R.drawable.botonesligero);
        botonLigeroTercero.setX(600);
        botonLigeroTercero.setY(1328);
        layout.addView(botonLigeroTercero);
        //Boton Tactico
        botonTacticoTercero = new ImageView(this);
        botonTacticoTercero.setImageResource(R.drawable.botonestactico);
        botonTacticoTercero.setX(600);
        botonTacticoTercero.setY(1530);
        layout.addView(botonTacticoTercero);
        //Boton Potente
        botonPotenteTercero = new ImageView(this);
        botonPotenteTercero.setImageResource(R.drawable.botonespotente);
        botonPotenteTercero.setX(600);
        botonPotenteTercero.setY(1740);
        layout.addView(botonPotenteTercero);
        funcionataqueLigero3();
        funcionataqueTactico3();
        funcionataquePotente3();
        // Agregar TextViews que muestran las vidas del usuario y del enemigo
        tvVidaUsuario3 = new TextView(this);
        tvVidaUsuario3.setText("Vida Jugador: " + vidaUsuario3);
        tvVidaUsuario3.setTextSize(24);
        tvVidaUsuario3.setY(1100);
        tvVidaUsuario3.setTextColor(Color.GREEN);
        tvVidaUsuario3.setGravity(Gravity.CENTER_HORIZONTAL);
        tvVidaUsuario3.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(tvVidaUsuario3);

        tvVidaEnemigo3 = new TextView(this);
        tvVidaEnemigo3.setText("Vida Enemigo: " + vidaTercerBoss);
        tvVidaEnemigo3.setTextSize(24);
        tvVidaEnemigo3.setY(710);
        tvVidaEnemigo3.setTextColor(Color.WHITE);
        tvVidaEnemigo3.setGravity(Gravity.CENTER_HORIZONTAL);
        tvVidaEnemigo3.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(tvVidaEnemigo3);
    }
    public void comenzarcuartocombate(){
        //Pausa musica de la cuarta historia, oculta boton comenzar4 y inicia musica del cuarto boss en ciclo
        musicaInicio.pause();
        comenzar4Button.setVisibility(View.GONE);
        musicaCuartoBoss.start();
        musicaCuartoBoss.setLooping(true);
        //Cargar el fondo, su nombre y imagen del cuarto BOSS
        FondoCuartoBoss = new ImageView(this);
        FondoCuartoBoss.setBackgroundResource(R.drawable.fondocuartoboss);
        layout.addView(FondoCuartoBoss);
        layout.addView(NombreCuartoBoss);
        layout.addView(cuartoBossImageView);
        //Carga la imagen de genero y su nombre
        layout.addView(generoImageView);
        generoImageView.setVisibility(View.VISIBLE);
        layout.addView(NombreUsuario);
        //BOTONES DEL CUARTO COMBATE
        //Boton Ligero
        botonLigeroCuarto = new ImageView(this);
        botonLigeroCuarto.setImageResource(R.drawable.botonesligero);
        botonLigeroCuarto.setX(600);
        botonLigeroCuarto.setY(1328);
        layout.addView(botonLigeroCuarto);
        //Boton Tactico
        botonTacticoCuarto = new ImageView(this);
        botonTacticoCuarto.setImageResource(R.drawable.botonestactico);
        botonTacticoCuarto.setX(600);
        botonTacticoCuarto.setY(1530);
        layout.addView(botonTacticoCuarto);
        //Boton Potente
        botonPotenteCuarto = new ImageView(this);
        botonPotenteCuarto.setImageResource(R.drawable.botonespotente);
        botonPotenteCuarto.setX(600);
        botonPotenteCuarto.setY(1740);
        layout.addView(botonPotenteCuarto);
        funcionataqueLigero4();
        funcionataqueTactico4();
        funcionataquePotente4();
        // Agregar TextViews que muestran las vidas del usuario y del enemigo
        tvVidaUsuario4 = new TextView(this);
        tvVidaUsuario4.setText("Vida Jugador: " + vidaUsuario4);
        tvVidaUsuario4.setTextSize(24);
        tvVidaUsuario4.setY(1100);
        tvVidaUsuario4.setTextColor(Color.GREEN);
        tvVidaUsuario4.setGravity(Gravity.CENTER_HORIZONTAL);
        tvVidaUsuario4.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(tvVidaUsuario4);

        tvVidaEnemigo4 = new TextView(this);
        tvVidaEnemigo4.setText("Vida Enemigo: " + vidaCuartoBoss);
        tvVidaEnemigo4.setTextSize(24);
        tvVidaEnemigo4.setY(710);
        tvVidaEnemigo4.setTextColor(Color.WHITE);
        tvVidaEnemigo4.setGravity(Gravity.CENTER_HORIZONTAL);
        tvVidaEnemigo4.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(tvVidaEnemigo4);
    }
    public void comenzarquintocombate(){
        //Pausa musica de la quinta historia, oculta boton comenzar5 y inicia musica del quinto boss en ciclo
        musicaInicio.pause();
        comenzar5Button.setVisibility(View.GONE);
        musicaQuintoBoss.start();
        musicaQuintoBoss.setLooping(true);
        //Cargar el fondo, su nombre y imagen del segundo BOSS
        FondoFinalBoss = new ImageView(this);
        FondoFinalBoss.setBackgroundResource(R.drawable.fondofinalboss);
        layout.addView(FondoFinalBoss);
        layout.addView(quintoBossImageView);
        layout.addView(NombreQuintoBoss);
        //Carga la imagen de genero y su nombre
        layout.addView(generoImageView);
        generoImageView.setVisibility(View.VISIBLE);
        layout.addView(NombreUsuario);
        //BOTONES DEL QUINTO COMBATE
        //Boton Ligero
        botonLigeroQuinto = new ImageView(this);
        botonLigeroQuinto.setImageResource(R.drawable.botonesligero);
        botonLigeroQuinto.setX(600);
        botonLigeroQuinto.setY(1328);
        layout.addView(botonLigeroQuinto);
        //Boton Tactico
        botonTacticoQuinto = new ImageView(this);
        botonTacticoQuinto.setImageResource(R.drawable.botonestactico);
        botonTacticoQuinto.setX(600);
        botonTacticoQuinto.setY(1530);
        layout.addView(botonTacticoQuinto);
        //Boton Potente
        botonPotenteQuinto = new ImageView(this);
        botonPotenteQuinto.setImageResource(R.drawable.botonespotente);
        botonPotenteQuinto.setX(600);
        botonPotenteQuinto.setY(1740);
        layout.addView(botonPotenteQuinto);
        funcionataqueLigero5();
        funcionataqueTactico5();
        funcionataquePotente5();
        // Agregar TextViews que muestran las vidas del usuario y del enemigo
        tvVidaUsuario5 = new TextView(this);
        tvVidaUsuario5.setText("Vida Jugador: " + vidaUsuario5);
        tvVidaUsuario5.setTextSize(24);
        tvVidaUsuario5.setY(1100);
        tvVidaUsuario5.setTextColor(Color.GREEN);
        tvVidaUsuario5.setGravity(Gravity.CENTER_HORIZONTAL);
        tvVidaUsuario5.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(tvVidaUsuario5);

        tvVidaEnemigo5 = new TextView(this);
        tvVidaEnemigo5.setText("Vida Enemigo: " + vidaQuintoBoss);
        tvVidaEnemigo5.setTextSize(24);
        tvVidaEnemigo5.setY(710);
        tvVidaEnemigo5.setTextColor(Color.WHITE);
        tvVidaEnemigo5.setGravity(Gravity.CENTER_HORIZONTAL);
        tvVidaEnemigo5.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(tvVidaEnemigo5);
    }
    //Actualizamos las vidas del jugador y del primer boss
    public void actualizarVidaUsuario() {
        tvVidaUsuario.setText("Vida Jugador: " + vidaUsuario);
    }
    public void actualizarVidaEnemigo() {
        tvVidaEnemigo.setText("Vida Enemigo: " + vidaPrimerBoss);
    }
    //Actualizamos las vidas del jugador y del segundo boss
    public void actualizarVidaUsuario2() {
        tvVidaUsuario2.setText("Vida Jugador: " + vidaUsuario2);
    }
    public void actualizarVidaEnemigo2() {
        tvVidaEnemigo2.setText("Vida Enemigo: " + vidaSegundoBoss);
    }
    //Actualizamos las vidas del jugador y del tercer boss
    public void actualizarVidaUsuario3() {
        tvVidaUsuario3.setText("Vida Jugador: " + vidaUsuario3);
    }
    public void actualizarVidaEnemigo3() {
        tvVidaEnemigo3.setText("Vida Enemigo: " + vidaTercerBoss);
    }
    //Actualizamos las vidas del jugador y del cuarto boss
    public void actualizarVidaUsuario4() {
        tvVidaUsuario4.setText("Vida Jugador: " + vidaUsuario4);
    }
    public void actualizarVidaEnemigo4() {
        tvVidaEnemigo4.setText("Vida Enemigo: " + vidaCuartoBoss);
    }
    //Actualizamos las vidas del jugador y del quinto boss
    public void actualizarVidaUsuario5() {
        tvVidaUsuario5.setText("Vida Jugador: " + vidaUsuario5);
    }
    public void actualizarVidaEnemigo5() {
        tvVidaEnemigo5.setText("Vida Enemigo: " + vidaQuintoBoss);
    }
    //Este metodo se encarga de verificar si se a ganado o perdido el combate, dependiendo de ello mostrara un resultado diferente
    private void verificarFinJuego() {
        //Si la vida del Primer Boss es 0
        if (vidaPrimerBoss <= 0) {
            //Gana el jugador, se pausa la musica de combate y se muestra fondo y musica de victoria
            musicaPrimerBoss.pause();
            musicaVictoria.start();
            Toast.makeText(this, "GAME WIN", Toast.LENGTH_SHORT).show();
            layout.addView(FondoVictoria);
            //Mostrar botón de Continuar para la Segunda historia y ocultar los botones de ataque y las vistas de vida
            layout.addView(continuarButton2);
            continuarButton2.setVisibility(View.VISIBLE);
            botonLigero.setVisibility(View.GONE);
            botonTactico.setVisibility(View.GONE);
            botonPotente.setVisibility(View.GONE);
            tvVidaUsuario.setVisibility(View.GONE);
            tvVidaEnemigo.setVisibility(View.GONE);
            generoImageView.setVisibility(View.GONE);
            //Desactivar layouts de nivel 1
            layout.removeView(botonLigero);
            layout.removeView(botonTactico);
            layout.removeView(botonPotente);
            layout.removeView(generoImageView); // Eliminar imagen del genero al ganar
            layout.removeView(NombreUsuario);   //Eliminar nombre usuario al ganar
            layout.removeView(tvVidaUsuario);
            layout.removeView(tvVidaEnemigo);
        } else if (vidaUsuario <= 0) {
            //Si por el contrario el jugador lleva a 0 de vida
            //Se pausa la musica de combate y se muestra el fondo y musica de derrota
            Toast.makeText(this, "GAME OVER", Toast.LENGTH_SHORT).show();
            layout.addView(FondoDerrota);
            // Mostrar botón de reinicio y ocultar los botones de ataque y las vistas de vida
            layout.addView(botonReiniciar);
            botonReiniciar.setVisibility(View.VISIBLE);
            botonLigero.setVisibility(View.GONE);
            botonTactico.setVisibility(View.GONE);
            botonPotente.setVisibility(View.GONE);
            tvVidaUsuario.setVisibility(View.GONE);
            tvVidaEnemigo.setVisibility(View.GONE);
            musicaPrimerBoss.pause();
            musicaDerrota.start();
        }
    }
    //Verificacion de victoria o derrota del Segundo Boss
    private void verificarFinJuego2() {
        if (vidaSegundoBoss <= 0) {
            // El USUARIO CAZADOR GANO, fondo y musica de Victoria
            musicaSegundoBoss.pause();
            musicaVictoria.start();
            Toast.makeText(this, "GAME WIN", Toast.LENGTH_SHORT).show();
            layout.addView(FondoVictoria);
            // Mostrar botón de Continuar para la Tercera historia y ocultar los botones de ataque y las vistas de vida
            layout.addView(continuarButton3);
            continuarButton3.setVisibility(View.VISIBLE);
            botonLigeroSegundo.setVisibility(View.GONE);
            botonTacticoSegundo.setVisibility(View.GONE);
            botonPotenteSegundo.setVisibility(View.GONE);
            tvVidaUsuario2.setVisibility(View.GONE);
            tvVidaEnemigo2.setVisibility(View.GONE);
            generoImageView.setVisibility(View.GONE);
            //Desactivar layouts de nivel 2
            layout.removeView(botonLigeroSegundo);
            layout.removeView(botonTacticoSegundo);
            layout.removeView(botonPotenteSegundo);
            layout.removeView(generoImageView); // Eliminar imagen del genero al ganar
            layout.removeView(NombreUsuario);   //Eliminar nombre usuario al ganar
            layout.removeView(tvVidaUsuario2);
            layout.removeView(tvVidaEnemigo2);
        } else if (vidaUsuario2 <= 0) {
            // EL ENEMIGO GOBLIN GANO, fondo y musica de Derrota
            Toast.makeText(this, "GAME OVER", Toast.LENGTH_SHORT).show();
            layout.addView(FondoDerrota);
            // Mostrar botón de reinicio y ocultar los botones de ataque y las vistas de vida
            layout.addView(botonReiniciar);
            botonReiniciar.setVisibility(View.VISIBLE);
            botonLigeroSegundo.setVisibility(View.GONE);
            botonTacticoSegundo.setVisibility(View.GONE);
            botonPotenteSegundo.setVisibility(View.GONE);
            tvVidaUsuario2.setVisibility(View.GONE);
            tvVidaEnemigo2.setVisibility(View.GONE);
            musicaSegundoBoss.pause();
            musicaDerrota.start();
        }
    }
    //Verificacion de victoria o derrota del Tercer Boss
    private void verificarFinJuego3() {
        if (vidaTercerBoss <= 0) {
            // El USUARIO CAZADOR GANO, fondo y musica de Victoria
            musicaTercerBoss.pause();
            musicaVictoria.start();
            Toast.makeText(this, "GAME WIN", Toast.LENGTH_SHORT).show();
            layout.addView(FondoVictoria);
            // Mostrar botón de Continuar para la Cuarta historia y ocultar los botones de ataque y las vistas de vida
            layout.addView(continuarButton4);
            continuarButton4.setVisibility(View.VISIBLE);
            botonLigeroTercero.setVisibility(View.GONE);
            botonTacticoTercero.setVisibility(View.GONE);
            botonPotenteTercero.setVisibility(View.GONE);
            tvVidaUsuario3.setVisibility(View.GONE);
            tvVidaEnemigo3.setVisibility(View.GONE);
            generoImageView.setVisibility(View.GONE);
            //Desactivar layouts de nivel 3
            layout.removeView(botonLigeroTercero);
            layout.removeView(botonTacticoTercero);
            layout.removeView(botonPotenteTercero);
            layout.removeView(generoImageView); // Eliminar imagen del genero al ganar
            layout.removeView(NombreUsuario);   //Eliminar nombre usuario al ganar
            layout.removeView(tvVidaUsuario3);
            layout.removeView(tvVidaEnemigo3);
        } else if (vidaUsuario3 <= 0) {
            // EL ENEMIGO UKANLOS GANO, fondo y musica de Derrota
            Toast.makeText(this, "GAME OVER", Toast.LENGTH_SHORT).show();
            layout.addView(FondoDerrota);
            // Mostrar botón de reinicio y ocultar los botones de ataque y las vistas de vida
            layout.addView(botonReiniciar);
            botonReiniciar.setVisibility(View.VISIBLE);
            botonLigeroTercero.setVisibility(View.GONE);
            botonTacticoTercero.setVisibility(View.GONE);
            botonPotenteTercero.setVisibility(View.GONE);
            tvVidaUsuario3.setVisibility(View.GONE);
            tvVidaEnemigo3.setVisibility(View.GONE);
            musicaTercerBoss.pause();
            musicaDerrota.start();
        }
    }
    //Verificacion de victoria o derrota del Cuarto Boss
    private void verificarFinJuego4() {
        if (vidaCuartoBoss <= 0) {
            // El USUARIO CAZADOR GANO, fondo y musica de Victoria
            musicaCuartoBoss.pause();
            musicaVictoria.start();
            Toast.makeText(this, "GAME WIN", Toast.LENGTH_SHORT).show();
            layout.addView(FondoVictoria);
            // Mostrar botón de Continuar para la Quinta historia y ocultar los botones de ataque y las vistas de vida
            layout.addView(continuarButton5);
            continuarButton5.setVisibility(View.VISIBLE);
            botonLigeroCuarto.setVisibility(View.GONE);
            botonTacticoCuarto.setVisibility(View.GONE);
            botonPotenteCuarto.setVisibility(View.GONE);
            tvVidaUsuario4.setVisibility(View.GONE);
            tvVidaEnemigo4.setVisibility(View.GONE);
            generoImageView.setVisibility(View.GONE);
            //Desactivar layouts de nivel 4
            layout.removeView(botonLigeroCuarto);
            layout.removeView(botonTacticoCuarto);
            layout.removeView(botonPotenteCuarto);
            layout.removeView(generoImageView); // Eliminar imagen del genero al ganar
            layout.removeView(NombreUsuario);   //Eliminar nombre usuario al ganar
            layout.removeView(tvVidaUsuario4);
            layout.removeView(tvVidaEnemigo4);
        } else if (vidaUsuario4 <= 0) {
            // EL ENEMIGO MINOTAURO GANO, fondo y musica de Derrota
            Toast.makeText(this, "GAME OVER", Toast.LENGTH_SHORT).show();
            layout.addView(FondoDerrota);
            // Mostrar botón de reinicio y ocultar los botones de ataque y las vistas de vida
            layout.addView(botonReiniciar);
            botonReiniciar.setVisibility(View.VISIBLE);
            botonLigeroCuarto.setVisibility(View.GONE);
            botonTacticoCuarto.setVisibility(View.GONE);
            botonPotenteCuarto.setVisibility(View.GONE);
            tvVidaUsuario4.setVisibility(View.GONE);
            tvVidaEnemigo4.setVisibility(View.GONE);
            musicaCuartoBoss.pause();
            musicaDerrota.start();
        }
    }
    //Verificacion de victoria o derrota del Quinto Boss
    private void verificarFinJuego5() {
        if (vidaQuintoBoss <= 0) {
            // El USUARIO CAZADOR GANO
            musicaQuintoBoss.pause();
            musicaVictoria.start();
            Toast.makeText(this, "LO AS LOGRADO!", Toast.LENGTH_SHORT).show();
            layout.addView(FondoVictoria);
            // Mostrar botón de Continuar Quinta historia y ocultar los botones de ataque y las vistas de vida
            layout.addView(continuarButton6);
            continuarButton6.setVisibility(View.VISIBLE);
            botonLigeroQuinto.setVisibility(View.GONE);
            botonTacticoQuinto.setVisibility(View.GONE);
            botonPotenteQuinto.setVisibility(View.GONE);
            tvVidaUsuario5.setVisibility(View.GONE);
            tvVidaEnemigo5.setVisibility(View.GONE);
            generoImageView.setVisibility(View.GONE);
            //Desactivar layouts de nivel 5
            layout.removeView(botonLigeroQuinto);
            layout.removeView(botonTacticoQuinto);
            layout.removeView(botonPotenteQuinto);
            layout.removeView(generoImageView); // Eliminar imagen del genero al ganar
            layout.removeView(NombreUsuario);   //Eliminar nombre usuario al ganar
            layout.removeView(tvVidaUsuario5);
            layout.removeView(tvVidaEnemigo5);
        } else if (vidaUsuario5 <= 0) {
            // EL ENEMIGO BEHEMOTH GANO
            Toast.makeText(this, "GAME OVER", Toast.LENGTH_SHORT).show();
            layout.addView(FondoDerrota);
            // Mostrar botón de reinicio y ocultar los botones de ataque y las vistas de vida
            layout.addView(botonReiniciar);
            botonReiniciar.setVisibility(View.VISIBLE);
            botonLigeroQuinto.setVisibility(View.GONE);
            botonTacticoQuinto.setVisibility(View.GONE);
            botonPotenteQuinto.setVisibility(View.GONE);
            tvVidaUsuario5.setVisibility(View.GONE);
            tvVidaEnemigo5.setVisibility(View.GONE);
            musicaQuintoBoss.pause();
            musicaDerrota.start();
        }
    }
    // A PARTIR DE AQUI LOS METODOS DE PIEDRA PAPEL Y TIJERA DE LOS COMBATES
    // Metodos de Ataque Potente, Piedra
    private void funcionataquePotente(){
        botonPotente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Generar sonido de impacto al presionar la imagen boton Potente
                impacto.start();
                // Generar elección aleatoria para el enemigo
                int eleccionEnemigo = (int) (Math.random() * 3);
                //Dependiendo del numero se le aplicar un valor
                String eleccionEnemigoString = "";
                if (eleccionEnemigo == 0) {
                    eleccionEnemigoString = "papel";
                } else if (eleccionEnemigo == 1) {
                    eleccionEnemigoString = "tijera";
                } else if (eleccionEnemigo == 2) {
                    eleccionEnemigoString = "piedra";
                }

                // Comparar elecciones y reducir vida del enemigo si el usuario gana
                if (eleccionEnemigoString.equals("papel")) {
                    //Pierde el usuario piedra vs papel, se reduce vida, audio de dolor
                    vidaUsuario -= 10;
                    dolor.seekTo(500);
                    dolor.start();
                } else if (eleccionEnemigoString.equals("tijera")) {
                    // Gana usuario piedra vs tijera, se reduce vida al enemigo
                    vidaPrimerBoss -= 10;
                } else if (eleccionEnemigoString.equals("piedra")) {
                    // Empate, musica de efecto de empate
                    empate.start();
                }
                // Actualizar vidas en sus metodos
                actualizarVidaUsuario();
                actualizarVidaEnemigo();
                // Verificar si el juego ha terminado
                verificarFinJuego();
            }
        });
    }
    private void funcionataquePotente2(){
        botonPotenteSegundo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Generar sonido de impacto al presionar la imagen boton Potente
                impacto.start();
                // Generar elección aleatoria para el enemigo
                int eleccionEnemigo = (int) (Math.random() * 3);
                //Dependiendo del numero se le aplicar un valor
                String eleccionEnemigoString = "";
                if (eleccionEnemigo == 0) {
                    eleccionEnemigoString = "papel";
                } else if (eleccionEnemigo == 1) {
                    eleccionEnemigoString = "tijera";
                } else if (eleccionEnemigo == 2) {
                    eleccionEnemigoString = "piedra";
                }

                // Comparar elecciones y reducir vida del enemigo si el usuario gana
                if (eleccionEnemigoString.equals("papel")) {
                    // Pierde usuario
                    vidaUsuario2 -= 10;
                    dolor.seekTo(500);
                    dolor.start();
                } else if (eleccionEnemigoString.equals("tijera")) {
                    // Gana usuario
                    vidaSegundoBoss -= 10;
                } else if (eleccionEnemigoString.equals("piedra")) {
                    // Empate
                    empate.start();
                }
                // Actualizar vidas en sus metodos
                actualizarVidaUsuario2();
                actualizarVidaEnemigo2();
                // Verificar si el juego ha terminado
                verificarFinJuego2();
            }
        });
    }
    private void funcionataquePotente3(){
        botonPotenteTercero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Generar sonido de impacto
                impacto.start();
                // Generar elección aleatoria para el enemigo
                int eleccionEnemigo = (int) (Math.random() * 3);
                String eleccionEnemigoString = "";
                if (eleccionEnemigo == 0) {
                    eleccionEnemigoString = "papel";
                } else if (eleccionEnemigo == 1) {
                    eleccionEnemigoString = "tijera";
                } else if (eleccionEnemigo == 2) {
                    eleccionEnemigoString = "piedra";
                }

                // Comparar elecciones y reducir vida del enemigo si el usuario gana
                if (eleccionEnemigoString.equals("papel")) {
                    // Pierde usuario
                    vidaUsuario3 -= 10;
                    dolor.seekTo(500);
                    dolor.start();
                } else if (eleccionEnemigoString.equals("tijera")) {
                    // Gana usuario
                    vidaTercerBoss -= 10;
                } else if (eleccionEnemigoString.equals("piedra")) {
                    // Empate
                    empate.start();
                }
                // Actualizar vidas en sus metodos
                actualizarVidaUsuario3();
                actualizarVidaEnemigo3();
                // Verificar si el juego ha terminado
                verificarFinJuego3();
            }
        });
    }
    private void funcionataquePotente4(){
        botonPotenteCuarto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Generar sonido de impacto
                impacto.start();
                // Generar elección aleatoria para el enemigo
                int eleccionEnemigo = (int) (Math.random() * 3);
                String eleccionEnemigoString = "";
                if (eleccionEnemigo == 0) {
                    eleccionEnemigoString = "papel";
                } else if (eleccionEnemigo == 1) {
                    eleccionEnemigoString = "tijera";
                } else if (eleccionEnemigo == 2) {
                    eleccionEnemigoString = "piedra";
                }

                // Comparar elecciones y reducir vida del enemigo si el usuario gana
                if (eleccionEnemigoString.equals("papel")) {
                    // Pierde usuario
                    vidaUsuario4 -= 10;
                    dolor.seekTo(500);
                    dolor.start();
                } else if (eleccionEnemigoString.equals("tijera")) {
                    // Gana usuario
                    vidaCuartoBoss -= 10;
                } else if (eleccionEnemigoString.equals("piedra")) {
                    // Empate
                    empate.start();
                }
                // Actualizar vidas en sus metodos
                actualizarVidaUsuario4();
                actualizarVidaEnemigo4();
                // Verificar si el juego ha terminado
                verificarFinJuego4();
            }
        });
    }
    //Los metodos referentes al combate5 tienen dos cambios
    private void funcionataquePotente5(){
        botonPotenteQuinto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Generar sonido de impacto
                impacto.start();
                // Generar elección aleatoria para el enemigo
                int eleccionEnemigo = (int) (Math.random() * 3);
                String eleccionEnemigoString = "";
                if (eleccionEnemigo == 0) {
                    eleccionEnemigoString = "papel";
                } else if (eleccionEnemigo == 1) {
                    eleccionEnemigoString = "tijera";
                } else if (eleccionEnemigo == 2) {
                    eleccionEnemigoString = "piedra";
                }

                // Comparar elecciones y reducir vida del enemigo si el usuario gana
                if (eleccionEnemigoString.equals("papel")) {
                    // Pierde usuario
                    vidaUsuario5 -= 10;
                    dolor.seekTo(500);
                    dolor.start();
                } else if (eleccionEnemigoString.equals("tijera")) {
                    // Gana usuario
                    vidaQuintoBoss -= 10;
                    //1r Cambio: Si el usuario gana se suma 1 a la variable Ataque Cargado
                    ContarAtaqueCargado++;
                    if(ContarAtaqueCargado == 3){
                        //Si consigues acertar 3 Ataques Potentes, le restan mas vida al enemigo y la variable se reinicia
                        vidaQuintoBoss -= 40;
                        ContarAtaqueCargado = 0;
                    }
                    //2n Cambio: Cuando el usuario acierte 3 ataques (Potente, Tactico, Ligero) se curara
                    ContarCuracion++;
                    if(ContarCuracion == 3){
                        //Llama al metodo para curar
                        CurarJugador();
                    }
                } else if (eleccionEnemigoString.equals("piedra")) {
                    // Empate
                    empate.start();
                }
                // Actualizar vidas en sus metodos
                actualizarVidaUsuario5();
                actualizarVidaEnemigo5();
                // Verificar si el juego ha terminado
                verificarFinJuego5();
            }
        });
    }
    //Metodos de Ataque Tactico, Tijera
    private void funcionataqueTactico(){
        botonTactico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Generar sonido de tijera al presionar la imagen del boton Tactico
                corte.seekTo(3000);
                corte.start();
                // Generar elección aleatoria para el enemigo
                int eleccionEnemigo = (int) (Math.random() * 3);
                //Dependiendo del numero se le aplicar un valor
                String eleccionEnemigoString = "";
                if (eleccionEnemigo == 0) {
                    eleccionEnemigoString = "papel";
                } else if (eleccionEnemigo == 1) {
                    eleccionEnemigoString = "tijera";
                } else if (eleccionEnemigo == 2) {
                    eleccionEnemigoString = "piedra";
                }

                // Comparar elecciones y reducir vida del enemigo si el usuario gana
                if (eleccionEnemigoString.equals("papel")) {
                    // Gana usuario tijera vs papel, se reduce vida al enemigo
                    vidaPrimerBoss -= 10;
                } else if (eleccionEnemigoString.equals("tijera")) {
                    // Empate
                    empate.start();
                } else if (eleccionEnemigoString.equals("piedra")) {
                    // Pierde usuario, se le reduce vida y sonido de dolor
                    vidaUsuario -= 10;
                    dolor.seekTo(500);
                    dolor.start();
                }
                // Actualizar vidas en sus metodos
                actualizarVidaUsuario();
                actualizarVidaEnemigo();
                // Verificar si el juego ha terminado
                verificarFinJuego();
            }
        });
    }
    private void funcionataqueTactico2(){
        botonTacticoSegundo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Generar sonido de tijera
                corte.seekTo(3000);
                corte.start();
                // Generar elección aleatoria para el enemigo
                int eleccionEnemigo = (int) (Math.random() * 3);
                String eleccionEnemigoString = "";
                if (eleccionEnemigo == 0) {
                    eleccionEnemigoString = "papel";
                } else if (eleccionEnemigo == 1) {
                    eleccionEnemigoString = "tijera";
                } else if (eleccionEnemigo == 2) {
                    eleccionEnemigoString = "piedra";
                }

                // Comparar elecciones y reducir vida del enemigo si el usuario gana
                if (eleccionEnemigoString.equals("papel")) {
                    // Gana usuario
                    vidaSegundoBoss -= 10;
                } else if (eleccionEnemigoString.equals("tijera")) {
                    // Empate
                    empate.start();
                } else if (eleccionEnemigoString.equals("piedra")) {
                    // Pierde usuario
                    vidaUsuario2 -= 10;
                    dolor.seekTo(500);
                    dolor.start();
                }
                // Actualizar vidas en sus metodos
                actualizarVidaUsuario2();
                actualizarVidaEnemigo2();
                // Verificar si el juego ha terminado
                verificarFinJuego2();
            }
        });
    }
    private void funcionataqueTactico3(){
        botonTacticoTercero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Generar sonido de tijera
                corte.seekTo(3000);
                corte.start();
                // Generar elección aleatoria para el enemigo
                int eleccionEnemigo = (int) (Math.random() * 3);
                String eleccionEnemigoString = "";
                if (eleccionEnemigo == 0) {
                    eleccionEnemigoString = "papel";
                } else if (eleccionEnemigo == 1) {
                    eleccionEnemigoString = "tijera";
                } else if (eleccionEnemigo == 2) {
                    eleccionEnemigoString = "piedra";
                }

                // Comparar elecciones y reducir vida del enemigo si el usuario gana
                if (eleccionEnemigoString.equals("papel")) {
                    // Gana usuario
                    vidaTercerBoss -= 10;
                } else if (eleccionEnemigoString.equals("tijera")) {
                    // Empate
                    empate.start();
                } else if (eleccionEnemigoString.equals("piedra")) {
                    // Pierde usuario
                    vidaUsuario3 -= 10;
                    dolor.seekTo(500);
                    dolor.start();
                }
                // Actualizar vidas en sus metodos
                actualizarVidaUsuario3();
                actualizarVidaEnemigo3();
                // Verificar si el juego ha terminado
                verificarFinJuego3();
            }
        });
    }
    private void funcionataqueTactico4(){
        botonTacticoCuarto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Generar sonido de tijera
                corte.seekTo(3000);
                corte.start();
                // Generar elección aleatoria para el enemigo
                int eleccionEnemigo = (int) (Math.random() * 3);
                String eleccionEnemigoString = "";
                if (eleccionEnemigo == 0) {
                    eleccionEnemigoString = "papel";
                } else if (eleccionEnemigo == 1) {
                    eleccionEnemigoString = "tijera";
                } else if (eleccionEnemigo == 2) {
                    eleccionEnemigoString = "piedra";
                }

                // Comparar elecciones y reducir vida del enemigo si el usuario gana
                if (eleccionEnemigoString.equals("papel")) {
                    // Gana usuario
                    vidaCuartoBoss -= 10;
                } else if (eleccionEnemigoString.equals("tijera")) {
                    // Empate
                    empate.start();
                } else if (eleccionEnemigoString.equals("piedra")) {
                    // Pierde usuario
                    vidaUsuario4 -= 10;
                    dolor.seekTo(500);
                    dolor.start();
                }
                // Actualizar vidas en sus metodos
                actualizarVidaUsuario4();
                actualizarVidaEnemigo4();
                // Verificar si el juego ha terminado
                verificarFinJuego4();
            }
        });
    }
    private void funcionataqueTactico5(){
        botonTacticoQuinto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Generar sonido de tijera
                corte.seekTo(3000);
                corte.start();
                // Generar elección aleatoria para el enemigo
                int eleccionEnemigo = (int) (Math.random() * 3);
                String eleccionEnemigoString = "";
                if (eleccionEnemigo == 0) {
                    eleccionEnemigoString = "papel";
                } else if (eleccionEnemigo == 1) {
                    eleccionEnemigoString = "tijera";
                } else if (eleccionEnemigo == 2) {
                    eleccionEnemigoString = "piedra";
                }

                // Comparar elecciones y reducir vida del enemigo si el usuario gana
                if (eleccionEnemigoString.equals("papel")) {
                    // Gana usuario
                    vidaQuintoBoss -= 10;
                    //Se suma el conteo para la Curacion
                    ContarCuracion++;
                    if(ContarCuracion == 3){
                        CurarJugador();
                    }
                } else if (eleccionEnemigoString.equals("tijera")) {
                    // Empate
                    empate.start();
                } else if (eleccionEnemigoString.equals("piedra")) {
                    // Pierde usuario
                    vidaUsuario5 -= 10;
                    dolor.seekTo(500);
                    dolor.start();
                }
                // Actualizar vidas en sus metodos
                actualizarVidaUsuario5();
                actualizarVidaEnemigo5();
                // Verificar si el juego ha terminado
                verificarFinJuego5();
            }
        });
    }
    //Metodos de Ataque Ligero, Papel
    private void funcionataqueLigero(){
        botonLigero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Generar sonido de papel al presionar la imagen boton Ligero, sonido de papel
                papel.seekTo(1000);
                papel.start();
                // Generar elección aleatoria para el enemigo
                int eleccionEnemigo = (int) (Math.random() * 3);
                //Dependiendo del numero se le aplicar un valor
                String eleccionEnemigoString = "";
                if (eleccionEnemigo == 0) {
                    eleccionEnemigoString = "papel";
                } else if (eleccionEnemigo == 1) {
                    eleccionEnemigoString = "tijera";
                } else if (eleccionEnemigo == 2) {
                    eleccionEnemigoString = "piedra";
                }

                // Comparar elecciones y reducir vida del enemigo si el usuario gana
                if (eleccionEnemigoString.equals("papel")) {
                    // Empate
                    empate.start();
                } else if (eleccionEnemigoString.equals("tijera")) {
                    // Pierde usuario papel vs tijera, se le reduce vida y audio de dolor
                    vidaUsuario -= 10;
                    dolor.seekTo(500);
                    dolor.start();
                } else if (eleccionEnemigoString.equals("piedra")) {
                    // Gana usuario, se le reduce vida al enemigo
                    vidaPrimerBoss -= 10;
                }
                // Actualizar vidas en sus metodos
                actualizarVidaUsuario();
                actualizarVidaEnemigo();
                // Verificar si el juego ha terminado
                verificarFinJuego();
            }
        });
    }
    private void funcionataqueLigero2(){
        botonLigeroSegundo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Generar sonido de papel
                papel.seekTo(1000);
                papel.start();
                // Generar elección aleatoria para el enemigo
                int eleccionEnemigo = (int) (Math.random() * 3);
                String eleccionEnemigoString = "";
                if (eleccionEnemigo == 0) {
                    eleccionEnemigoString = "papel";
                } else if (eleccionEnemigo == 1) {
                    eleccionEnemigoString = "tijera";
                } else if (eleccionEnemigo == 2) {
                    eleccionEnemigoString = "piedra";
                }

                // Comparar elecciones y reducir vida del enemigo si el usuario gana
                if (eleccionEnemigoString.equals("papel")) {
                    // Empate
                    empate.start();
                } else if (eleccionEnemigoString.equals("tijera")) {
                    // Pierde usuario
                    vidaUsuario2 -= 10;
                    dolor.seekTo(500);
                    dolor.start();
                } else if (eleccionEnemigoString.equals("piedra")) {
                    // Gana usuario
                    vidaSegundoBoss -= 10;
                }
                // Actualizar vidas en sus metodos
                actualizarVidaUsuario2();
                actualizarVidaEnemigo2();
                // Verificar si el juego ha terminado
                verificarFinJuego2();
            }
        });
    }
    private void funcionataqueLigero3(){
        botonLigeroTercero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Generar sonido de papel
                papel.seekTo(1000);
                papel.start();
                // Generar elección aleatoria para el enemigo
                int eleccionEnemigo = (int) (Math.random() * 3);
                String eleccionEnemigoString = "";
                if (eleccionEnemigo == 0) {
                    eleccionEnemigoString = "papel";
                } else if (eleccionEnemigo == 1) {
                    eleccionEnemigoString = "tijera";
                } else if (eleccionEnemigo == 2) {
                    eleccionEnemigoString = "piedra";
                }

                // Comparar elecciones y reducir vida del enemigo si el usuario gana
                if (eleccionEnemigoString.equals("papel")) {
                    // Empate
                    empate.start();
                } else if (eleccionEnemigoString.equals("tijera")) {
                    // Pierde usuario
                    vidaUsuario3 -= 10;
                    dolor.seekTo(500);
                    dolor.start();
                } else if (eleccionEnemigoString.equals("piedra")) {
                    // Gana usuario
                    vidaTercerBoss -= 10;
                }
                // Actualizar vidas en sus metodos
                actualizarVidaUsuario3();
                actualizarVidaEnemigo3();
                // Verificar si el juego ha terminado
                verificarFinJuego3();
            }
        });
    }
    private void funcionataqueLigero4(){
        botonLigeroCuarto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Generar sonido de papel
                papel.seekTo(1000);
                papel.start();
                // Generar elección aleatoria para el enemigo
                int eleccionEnemigo = (int) (Math.random() * 3);
                String eleccionEnemigoString = "";
                if (eleccionEnemigo == 0) {
                    eleccionEnemigoString = "papel";
                } else if (eleccionEnemigo == 1) {
                    eleccionEnemigoString = "tijera";
                } else if (eleccionEnemigo == 2) {
                    eleccionEnemigoString = "piedra";
                }

                // Comparar elecciones y reducir vida del enemigo si el usuario gana
                if (eleccionEnemigoString.equals("papel")) {
                    // Empate
                    empate.start();
                } else if (eleccionEnemigoString.equals("tijera")) {
                    // Pierde usuario
                    vidaUsuario4 -= 10;
                    dolor.seekTo(500);
                    dolor.start();
                } else if (eleccionEnemigoString.equals("piedra")) {
                    // Gana usuario
                    vidaCuartoBoss -= 10;
                }
                // Actualizar vidas en sus metodos
                actualizarVidaUsuario4();
                actualizarVidaEnemigo4();
                // Verificar si el juego ha terminado
                verificarFinJuego4();
            }
        });
    }
    private void funcionataqueLigero5(){
        botonLigeroQuinto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Generar sonido de papel
                papel.seekTo(1000);
                papel.start();
                // Generar elección aleatoria para el enemigo
                int eleccionEnemigo = (int) (Math.random() * 3);
                String eleccionEnemigoString = "";
                if (eleccionEnemigo == 0) {
                    eleccionEnemigoString = "papel";
                } else if (eleccionEnemigo == 1) {
                    eleccionEnemigoString = "tijera";
                } else if (eleccionEnemigo == 2) {
                    eleccionEnemigoString = "piedra";
                }

                // Comparar elecciones y reducir vida del enemigo si el usuario gana
                if (eleccionEnemigoString.equals("papel")) {
                    // Empate
                    empate.start();
                } else if (eleccionEnemigoString.equals("tijera")) {
                    // Pierde usuario
                    vidaUsuario5 -= 10;
                    dolor.seekTo(500);
                    dolor.start();
                    //El Ultimo Boss se pude curar cada vez que el usuario pierde papel vs tijera
                    if(vidaQuintoBoss != 240){
                        vidaQuintoBoss+=10;
                    }
                } else if (eleccionEnemigoString.equals("piedra")) {
                    // Gana usuario
                    vidaQuintoBoss -= 10;
                    //Se suma el conteo para la Curacion
                    ContarCuracion++;
                    if(ContarCuracion == 3){
                        CurarJugador();
                    }
                }
                // Actualizar vidas en sus metodos
                actualizarVidaUsuario5();
                actualizarVidaEnemigo5();
                // Verificar si el juego ha terminado
                verificarFinJuego5();
            }
        });
    }
    private void reiniciarJuego() {
        // Reiniciar variables de vida, actualizamos las vidas del Nivel 1 donde reinicia, pausamos musica Derrota
        vidaUsuario = 50;
        vidaPrimerBoss = 50;
        vidaUsuario2 = 80;
        vidaSegundoBoss = 80;
        vidaUsuario3 = 100;
        vidaTercerBoss = 100;
        vidaUsuario4 = 120;
        vidaCuartoBoss = 120;
        vidaUsuario5 = 150;
        vidaQuintoBoss = 240;
        actualizarVidaUsuario();
        actualizarVidaEnemigo();
        musicaDerrota.pause();

        // Ocultar el botón de reinicio y mostrar los botones de ataque y las vistas de vida
        botonReiniciar.setVisibility(View.GONE);
        botonLigero.setVisibility(View.VISIBLE);
        botonTactico.setVisibility(View.VISIBLE);
        botonPotente.setVisibility(View.VISIBLE);
        tvVidaUsuario.setVisibility(View.VISIBLE);
        tvVidaEnemigo.setVisibility(View.VISIBLE);
        musicaPrimerBoss.seekTo(0); // Establecer la posición de reproducción actual en cero de todos los niveles
        musicaSegundoBoss.seekTo(0);
        musicaTercerBoss.seekTo(0);
        musicaCuartoBoss.seekTo(0);
        musicaQuintoBoss.seekTo(0);
        musicaDerrota.seekTo(0);

        // Remover vistas
        layout.removeAllViews();
        // Comenzar el primer combate de nuevo
        comenzarprimercombate();
    }
    //Metodo dentro del Boton Continuar2 para mostrar la segunda Historia
    private void segundaHistoria(){
        //Ocultar boton Reiniciar y Continuar2, remover fondo y musica Victoria, iniciar musica principal
        botonReiniciar.setVisibility(View.GONE);
        continuarButton2.setVisibility(View.GONE);
        layout.removeView(FondoVictoria);
        musicaVictoria.pause();
        musicaVictoria.seekTo(0);
        musicaInicio.start();
        //Carga el fondo de la segunda historia
        FondoSegundaHistoria = new ImageView(this);
        FondoSegundaHistoria.setBackgroundResource(R.drawable.fondosegundahistoria);
        layout.addView(FondoSegundaHistoria);
        // Crear TextView para la segunda historia del cazador
        historiaSegundaTextView = new TextView(this);
        historiaSegundaTextView.setTextColor(Color.WHITE);
        historiaSegundaTextView.setTextSize(21);
        historiaSegundaTextView.setY(50);
        historiaSegundaTextView.setText("Después de vencer al Ifrit consigues salir del volcan con leves heridas. Mientras te curas, descubres en el horizonte un bosque gigante, te adentras en él pero a medida que avanzas te das cuenta que no estas solo y aparece un Rey Goblin. Te preparas para el combate!");
        historiaSegundaTextView.setPadding(50,50,50,50);
        //Trazo Negro Segunda Historia
        TextPaint paint = historiaSegundaTextView.getPaint();
        paint.setStrokeWidth(0); // establecer el ancho del borde en 0 píxeles
        paint.setStyle(Paint.Style.FILL_AND_STROKE); // Establece el estilo de dibujo del borde como relleno y trazo
        paint.setColor(Color.WHITE); // Establece el color del texto en blanco
        paint.setShadowLayer(10, 0, 0, Color.BLACK); // Agrega un sombreado alrededor del texto con el desplazamiento (0,0) y el color negro.
        //Crear el boton Comenzar Segundo Combate
        comenzar2Button = new Button(this);
        comenzar2Button.setText("Continuar");
        comenzar2Button.setX(400);
        comenzar2Button.setY(1800);
        comenzar2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comenzarsegundocombate();
            }
        });
        layout.addView(comenzar2Button);
        layout.addView(historiaSegundaTextView);
    }
    //Metodo dentro del Boton Continuar3 para mostrar la tercera Historia
    private void terceraHistoria(){
        botonReiniciar.setVisibility(View.GONE);
        continuarButton3.setVisibility(View.GONE);
        layout.removeView(FondoVictoria);
        musicaVictoria.pause();
        musicaVictoria.seekTo(0);
        musicaInicio.start();
        //Carga el fondo de la tercera historia
        FondoTerceraHistoria = new ImageView(this);
        FondoTerceraHistoria.setBackgroundResource(R.drawable.fondotercerahistoria);
        layout.addView(FondoTerceraHistoria);
        // Crear TextView para la tercera historia del cazador
        historiaTerceraTextView = new TextView(this);
        historiaTerceraTextView.setTextColor(Color.WHITE);
        historiaTerceraTextView.setTextSize(21);
        historiaTerceraTextView.setY(50);
        historiaTerceraTextView.setText("Ganas la pelea y continuas un par de dias en el bosque, hasta que por fin terminas de cruzarlo. Notas un aire que te congela, levantas la mirada y delante de ti ves un páramo cubierto de hielo y nieve. Te diriges hacia delante pero un Ukanlos bloquea la entrada al bioma congelado.Te preparas para el combate! ");
        historiaTerceraTextView.setPadding(50,50,50,50);
        //Trazo Negro Historia
        TextPaint paint = historiaTerceraTextView.getPaint();
        paint.setStrokeWidth(0); // establecer el ancho del borde en 0 píxeles
        paint.setStyle(Paint.Style.FILL_AND_STROKE); // Establece el estilo de dibujo del borde como relleno y trazo
        paint.setColor(Color.WHITE); // Establece el color del texto en blanco
        paint.setShadowLayer(10, 0, 0, Color.BLACK); // Agrega un sombreado alrededor del texto con el desplazamiento (0,0) y el color negro.

        //Crear el boton Comenzar Tercer Combate
        comenzar3Button = new Button(this);
        comenzar3Button.setText("Continuar");
        comenzar3Button.setX(400);
        comenzar3Button.setY(1800);
        comenzar3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comenzartercercombate();
            }
        });
        layout.addView(comenzar3Button);
        layout.addView(historiaTerceraTextView);
    }
    //Metodo dentro del Boton Continuar4 para mostrar la cuarta Historia
    private void cuartaHistoria(){
        botonReiniciar.setVisibility(View.GONE);
        continuarButton4.setVisibility(View.GONE);
        layout.removeView(FondoVictoria);
        musicaVictoria.pause();
        musicaVictoria.seekTo(0);
        musicaInicio.start();
        //Carga el fondo de la cuarta historia
        FondoCuartaHistoria = new ImageView(this);
        FondoCuartaHistoria.setBackgroundResource(R.drawable.fondocuartahistoria);
        layout.addView(FondoCuartaHistoria);
        // Crear TextView para la tercera historia del cazador
        historiaCuartaTextView = new TextView(this);
        historiaCuartaTextView.setTextColor(Color.WHITE);
        historiaCuartaTextView.setTextSize(21);
        historiaCuartaTextView.setY(50);
        historiaCuartaTextView.setText("Ganas el combate, por desgracia mas enemigos se acercan y te ves obligado a huir y tener que cambiar tu ruta. Llegas al desierto, la calor y la sed te impiden darte cuenta que una sombra con cuernos esta detras de ti. Te giras y evades por milagro el ataque.Te preparas para el combate!");
        historiaCuartaTextView.setPadding(50,50,50,50);
        //Trazo Negro Historia
        TextPaint paint = historiaCuartaTextView.getPaint();
        paint.setStrokeWidth(0); // establecer el ancho del borde en 0 píxeles
        paint.setStyle(Paint.Style.FILL_AND_STROKE); // Establece el estilo de dibujo del borde como relleno y trazo
        paint.setColor(Color.WHITE); // Establece el color del texto en blanco
        paint.setShadowLayer(10, 0, 0, Color.BLACK); // Agrega un sombreado alrededor del texto con el desplazamiento (0,0) y el color negro.
        //Crear el boton Comenzar Cuarto Combate
        comenzar4Button = new Button(this);
        comenzar4Button.setText("Continuar");
        comenzar4Button.setX(400);
        comenzar4Button.setY(1800);
        comenzar4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comenzarcuartocombate();
            }
        });
        layout.addView(comenzar4Button);
        layout.addView(historiaCuartaTextView);
    }
    //Metodo dentro del Boton Continuar5 para mostrar la quinta Historia
    private void quintaHistoria(){
        botonReiniciar.setVisibility(View.GONE);
        continuarButton5.setVisibility(View.GONE);
        layout.removeView(FondoVictoria);
        musicaVictoria.pause();
        musicaVictoria.seekTo(0);
        musicaInicio.start();
        //Carga el fondo de la quinta historia
        FondoQuintaHistoria = new ImageView(this);
        FondoQuintaHistoria.setBackgroundResource(R.drawable.fondoquintahistoria);
        layout.addView(FondoQuintaHistoria);
        // Crear TextView para la quinta historia del cazador
        historiaQuintaTextView= new TextView(this);
        historiaQuintaTextView.setTextColor(Color.WHITE);
        historiaQuintaTextView.setTextSize(21);
        historiaQuintaTextView.setY(50);
        historiaQuintaTextView.setText("Victorioso de la pelea cruzas el desierto hasta llegar a tu objetivo, delante tuyo, una torre inmensa se alzaba hasta perderse entre las nubes. En lo alto de la torre se encontraba la mayor amenaza para tu tribu. Conseguiras vencerlo?");
        historiaQuintaTextView.setPadding(50,50,50,50);
        //Trazo Negro Quinta Historia
        TextPaint paint = historiaQuintaTextView.getPaint();
        paint.setStrokeWidth(0); // establecer el ancho del borde en 0 píxeles
        paint.setStyle(Paint.Style.FILL_AND_STROKE); // Establece el estilo de dibujo del borde como relleno y trazo
        paint.setColor(Color.WHITE); // Establece el color del texto en blanco
        paint.setShadowLayer(10, 0, 0, Color.BLACK); // Agrega un sombreado alrededor del texto con el desplazamiento (0,0) y el color negro.
        ///Crear el boton Continuar Atencion Historia
        continuarButton7 = new Button(this);
        continuarButton7.setText("Continuar");
        continuarButton7.setX(410);
        continuarButton7.setY(1800);
        continuarButton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comenzarAtencion();
            }
        });
        layout.addView(continuarButton7);
        layout.addView(historiaQuintaTextView);
    }
    //Metodo dentro del Boton Continuar6 para mostrar la Historia Final
    private void finalHistoria(){
        botonReiniciar.setVisibility(View.GONE);
        continuarButton6.setVisibility(View.GONE);
        layout.removeView(FondoVictoria);
        musicaVictoria.pause();
        musicaVictoria.seekTo(0);
        musicaFinal.seekTo(54000);
        musicaFinal.start();
        musicaFinal.setLooping(true);
        //Carga el fondo de la historia final
        FondoFinHistoria = new ImageView(this);
        FondoFinHistoria.setBackgroundResource(R.drawable.fondofinalhistoria);
        layout.addView(FondoFinHistoria);
        // Crear TextView para la quinta historia del cazador
        historiaFinalTextView= new TextView(this);
        historiaFinalTextView.setTextColor(Color.WHITE);
        historiaFinalTextView.setTextSize(21);
        historiaFinalTextView.setY(50);
        historiaFinalTextView.setText("Derrotaste al Behemoth! Exhausto despues de la intensa batalla , con tus ultimas fuerzas comienzas el viaje de regreso para llevar la noticia de tu victoria. Despues de un largo viaje llegas a la aldea  donde la tribu entera  te recibe como un heroe, entre aplausos y alegria. La amenaza habia sido eliminada y una vez mas tu tribu volvia a estar a salvo y en paz.\n \n Y te recordaran como un heroe por los años de los años. ");
        historiaFinalTextView.setPadding(50,50,50,50);
        //Trazo Negro Final Historia
        TextPaint paint = historiaFinalTextView.getPaint();
        paint.setStrokeWidth(0); // establecer el ancho del borde en 0 píxeles
        paint.setStyle(Paint.Style.FILL_AND_STROKE); // Establece el estilo de dibujo del borde como relleno y trazo
        paint.setColor(Color.WHITE); // Establece el color del texto en blanco
        paint.setShadowLayer(10, 0, 0, Color.BLACK); // Agrega un sombreado alrededor del texto con el desplazamiento (0,0) y el color negro.



        // Crear un botón para salir del juego
        Button botonCerrar = new Button(this);
        botonCerrar.setText("SALIR");
        botonCerrar.setX(410);
        botonCerrar.setY(1780);
        botonCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CerrarAplicacion();
            }
        });
        layout.addView(botonCerrar);
        layout.addView(historiaFinalTextView);
    }
    //Metodo dentro del Boton Continuar7 para mostrar el fondo de ATENCION, donde explica un poco sobre el quinto boss
    private void comenzarAtencion(){
        botonReiniciar.setVisibility(View.GONE);
        continuarButton7.setVisibility(View.GONE);
        //Carga el fondo de la atencion historia
        FondoAtencion = new ImageView(this);
        FondoAtencion.setBackgroundResource(R.drawable.fondoatencion);
        layout.addView(FondoAtencion);
        // Crear TextView para la Atencion historia del cazador
        textoAtencion = new TextView(this);
        textoAtencion.setTextColor(Color.WHITE);
        textoAtencion.setTextSize(21);
        textoAtencion.setY(370);
        textoAtencion.setText("CUIDADO! Este enemigo tiene la habilidad de curarse muchisimo. Pero no te preocupes! Acabas de aprender un Ataque Cargado, si consigues acertar 3 ataques potentes, realizaras una ataque cargado e infligiras mucho daño!\n \n Por tus esfuerzos de haber vencido a los anteriores enemigos los dioses te han bendecido, asi que te iran curando casualmente.\n \n Preparate y que la suerte te acompañe!");
        textoAtencion.setPadding(50,50,50,50);
        //Trazo Negro Atencion Historia
        TextPaint paint = textoAtencion.getPaint();
        paint.setStrokeWidth(1); // establecer el ancho del borde en 0 píxeles
        paint.setStyle(Paint.Style.FILL_AND_STROKE); // Establece el estilo de dibujo del borde como relleno y trazo
        paint.setColor(Color.WHITE); // Establece el color del texto en blanco
        paint.setShadowLayer(10, 0, 0, Color.BLACK); // Agrega un sombreado alrededor del texto con el desplazamiento (0,0) y el color negro.

        //Crear el boton Comenzar Quinto Combate
        comenzar5Button = new Button(this);
        comenzar5Button.setText("Continuar");
        comenzar5Button.setX(400);
        comenzar5Button.setY(1800);
        comenzar5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comenzarquintocombate();
            }
        });
        layout.addView(comenzar5Button);
        layout.addView(textoAtencion);
    }
    //Metodo dentro de los metodos de Ataque Potente5, Tactico5 y Ligero5 para curar al jugador cuando acierte 3 ataques diferentes
    public void CurarJugador(){
        if(vidaUsuario5 != 150){
            vidaUsuario5 += 10;
            //Se reinicia la variable
            ContarCuracion = 0;
        }
    }
    //Metodo dentro del boton Cerra para finalizar la aplicacion
    private void CerrarAplicacion() {
        // Cerrar la aplicación
        System.exit(0);
    }
}



