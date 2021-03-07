package br.com.orcaguincho.telas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import br.com.orcaguincho.R;

public class SplashScreen extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        View decorView = getWindow().getDecorView();
        // Esconde tanto a barra de navegação e a barra de status .
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility( progressBar.VISIBLE );

        //Aguardando um tempo para redirecionar
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SplashScreen.this.startActivity(new Intent(SplashScreen.this, CadastroActivity.class));
                SplashScreen.this.finish();
            }
        }, 3000);

    }
}