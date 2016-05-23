package com.pippo.record;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.audiocapture.R;

import java.io.IOException;
import java.util.logging.LogRecord;


public class MainActivity extends Activity {
    private MediaRecorder registratore = null;
    private MediaPlayer mp = null;
    private double startTime = 0;

    private static String filename = null;

    private boolean ascoltando = false;
    private boolean registrando = false;

    @Override
    public void onCreate(Bundle args) {
        super.onCreate(args);
        setContentView(R.layout.activity_main);
        filename = Environment.getExternalStorageDirectory().getAbsolutePath();
        filename += "/registrazione.3gp";
    }

    @Override
    public void onPause() {
        super.onPause();
        if (registratore != null) {
            registratore.release();
            registratore = null;
        }

        if (mp != null) {
            mp.release();
            mp = null;
        }
    }


    public void registra(View v)
    {
        Button btn=(Button) v;
        if (registrando)
        {
            // serve ad interrompere
            fermaRegistrazione();
            btn.setText("Registra");
        }
        else
        {
            // serve ad iniziare la registrazione
            registra();
            btn.setText("Ferma registrazione");
        }
        registrando=!registrando;
    }

    public void ascolta(View v)
    {
        Button btn=(Button) v;
        if (ascoltando)
        {
            // serve ad interrompere
            fermaRiproduzione();
            btn.setText("Ascolta");
        }
        else
        {
            // serve ad iniziare la riproduzione dell'audio
            riproduci();
            btn.setText("Ferma");
        }
        ascoltando=!ascoltando;
    }



    private void riproduci() {
        mp = new MediaPlayer();
        try
        {
            mp.setDataSource(filename);
            mp.prepare();
            mp.start();
        }
        catch (IOException e)
        {
            Toast.makeText(getApplicationContext(), "Errore durante la riproduzione" , Toast.LENGTH_LONG).show();
        }
    }

    private void fermaRiproduzione() {
        mp.release();
        mp = null;
    }

    private void registra() {
        registratore = new MediaRecorder();
        registratore.setAudioSource(MediaRecorder.AudioSource.MIC);
        registratore.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        registratore.setOutputFile(filename);
        registratore.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try
        {
            registratore.prepare();
        }
        catch (IOException e)
        {
            // gestisci eccezione
            Toast.makeText(getApplicationContext(), "Errore durante la registrazione" , Toast.LENGTH_LONG).show();
        }

        registratore.start();
    }

    private void fermaRegistrazione() {
        registratore.stop();
        registratore.release();
        registratore = null;
    }




}