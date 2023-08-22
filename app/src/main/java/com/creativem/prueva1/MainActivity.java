package com.creativem.prueva1;

import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.media.AudioManager;
import android.content.Context;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    //web inicio
    WebView miVisorWeb;
    String url = "https://corario-infantil-ipu-7wtz.glide.page";
    //web final
    MediaPlayer mp1;
    FloatingActionButton fabButton;
    AudioManager audioManager;
    AudioManager.OnAudioFocusChangeListener audioFocusChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //web inicio
        miVisorWeb = (WebView) findViewById(R.id.visorWeb);
        final WebSettings ajustesVisorWeb = miVisorWeb.getSettings();
        ajustesVisorWeb.setJavaScriptEnabled(true);
        miVisorWeb.loadUrl(url);
        //web final

        // ID del botón flotante
        fabButton = findViewById(R.id.inicio);
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

        // Inicializar el AudioManager
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // Definir el listener de cambio de enfoque de audio
        audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                    // Detener la reproducción si se pierde el enfoque de audio
                    detenerReproduccion();
                }
            }
        };

        // Solicitar el enfoque de audio
        int result = audioManager.requestAudioFocus(audioFocusChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // Si se obtiene el enfoque de audio, reproducir el audio
            reproducir();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        detenerReproduccion();
        audioManager.abandonAudioFocus(audioFocusChangeListener);
    }

    public void reproducir() {
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

