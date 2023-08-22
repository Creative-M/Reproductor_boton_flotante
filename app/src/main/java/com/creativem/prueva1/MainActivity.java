package com.creativem.prueva1;

import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
     MediaPlayer mp1;
     FloatingActionButton fabButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fabButton = findViewById(R.id.inicio); // ID del botón flotante
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mp1 != null && mp1.isPlaying()) {
                    detenerReproduccion();
                } else {
                    reproducir();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mp1 != null) {
            mp1.release();
            mp1 = null;
        }
    }

    private void reproducir() {
        if (mp1 == null) {
            mp1 = new MediaPlayer();
            try {
                mp1.setDataSource("https://play14.tikast.com:22038/stream?type=http&nocache=438");
                mp1.prepareAsync();
                mp1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                        fabButton.setImageResource(R.drawable.music_off); // Cambiar la imagen del botón flotante a "Detener"
                    }
                });
                Toast.makeText(this, "Radio IPUC En Línea", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(this, "Lo siento, no estamos en línea", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void detenerReproduccion() {
        if (mp1 != null) {
            mp1.stop();
            mp1.release();
            mp1 = null;
            fabButton.setImageResource(R.drawable.play); // Cambiar la imagen del botón flotante a "Reproducir"
        }

    }


}
