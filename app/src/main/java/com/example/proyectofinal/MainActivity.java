package com.example.proyectofinal;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView FondoimageView;
    private ImageView hombreImageView;
    private ImageView mujerImageView;
    private EditText IngresarNombre;
    private TextView GeneroTexto;
    private MediaPlayer mediaPlayer; // Objeto para reproducir música de fondo
    private TextView VerNombre;
    private int contador = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Crear imagen de fondo
        FondoimageView = new ImageView(this);
        FondoimageView.setBackgroundResource(R.drawable.fondoprincipio);

        mediaPlayer = MediaPlayer.create(this, R.raw.intro);
        mediaPlayer.setLooping(true); // Reproduce la música en un loop
        mediaPlayer.start(); // Inicia la reproducción de la música

        // Crear ImageView para la imagen de hombre y establecer su imagen
        hombreImageView = new ImageView(this);
        hombreImageView.setImageResource(R.drawable.huntermen);
        hombreImageView.setX(50);
        hombreImageView.setY(1100);

        // Crear ImageView para la imagen de mujer y establecer su imagen
        mujerImageView = new ImageView(this);
        mujerImageView.setImageResource(R.drawable.huntergirl);
        mujerImageView.setX(580);
        mujerImageView.setY(1100);

        // Crear EditText para el nombre del jugador
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
        // Mover el EditText a la posición (100, 200)
        IngresarNombre.setX(0);
        IngresarNombre.setY(-500);

        // Crear TextView para mostrar el nombre del jugador
        VerNombre = new TextView(this);
        VerNombre.setTextSize(16);  // Tamaño de fuente en sp
        VerNombre.setTextColor(Color.WHITE);  // Color del texto en blanco
        VerNombre.setVisibility(View.GONE);
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        lp2.addRule(RelativeLayout.CENTER_IN_PARENT);
        VerNombre.setLayoutParams(lp2);
        // Mover el TextView a la posición (200, 300)
        VerNombre.setX(0);
        VerNombre.setY(-500);

        GeneroTexto = new TextView(this);
        GeneroTexto.setText("TOCA EL GÉNERO A ELEGIR");
        GeneroTexto.setTextSize(14);
        GeneroTexto.setTextColor(Color.WHITE);
        GeneroTexto.setX(278);
        GeneroTexto.setY(695);

        // Crear botón "Siguiente"
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
        RelativeLayout layout = new RelativeLayout(this);
        layout.addView(FondoimageView);
        layout.addView(hombreImageView);
        layout.addView(mujerImageView);
        layout.addView(IngresarNombre);
        layout.addView(VerNombre);
        layout.addView(button);
        layout.addView(GeneroTexto);
        setContentView(layout);

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
                } else if ((int)FondoimageView.getTag() == R.drawable.fondofuncionamiento) {
                    backgroundImage = R.drawable.fondohistoria;
                } else {
                    // Loop back to the first background image
                    backgroundImage = R.drawable.fondoprincipio;
                }
                FondoimageView.setBackgroundResource(backgroundImage);
                FondoimageView.setTag(backgroundImage);
                VerNombre.setVisibility(View.GONE);
                GeneroTexto.setVisibility(View.GONE);
                IngresarNombre.setVisibility(View.GONE);
                hombreImageView.setVisibility(View.GONE);
                mujerImageView.setVisibility(View.GONE);
                contador++;
                if (contador == 2) {
                    button.setVisibility(View.GONE);
                }
            }
        });

    }
}



