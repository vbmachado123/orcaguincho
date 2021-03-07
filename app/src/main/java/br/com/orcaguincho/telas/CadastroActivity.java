package br.com.orcaguincho.telas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.HashMap;
import java.util.Map;

import br.com.orcaguincho.R;
import model.Usuario;
import retrofit.Connection;
import retrofit.service.UsuarioService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Preferencias;

public class CadastroActivity extends AppCompatActivity {

    private EditText etNome, etEmail, etTelefone;
    private Button btnCadastrar;
    private Preferencias preferencias;
    private CheckBox cbAutoriza;

    private UsuarioService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        preferencias = new Preferencias(CadastroActivity.this);
        service = new Connection().getUsuarioService();

        if (preferencias == null)
            validaCampo();
        else
            acessarHome();
    }

    private void acessarHome() {
        Intent it = new Intent(CadastroActivity.this, HomeActivity.class);
        startActivity(it);
        finish();
    }

    private void validaCampo() {

        etNome = (EditText) findViewById(R.id.idNome);
        etEmail = (EditText) findViewById(R.id.idEmail);
        etTelefone = (EditText) findViewById(R.id.idTelefone);
        cbAutoriza = (CheckBox) findViewById(R.id.cbAutoriza);
        btnCadastrar = (Button) findViewById(R.id.btnCadastro);

        mascara("(NN) NNNNN-NNNN", etTelefone);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbAutoriza.isChecked()) {
                    String nome = etNome.getText().toString();
                    String email = etEmail.getText().toString();
                    String telefone = etTelefone.getText().toString();

                    salvaNaApi(new Usuario(email, telefone, nome));
                } else
                    Toast.makeText(CadastroActivity.this, "Autorize o recebimento de atualizações para prosseguir", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void salvaNaApi(Usuario usuario) {
        Call<Usuario> call = service.salva(usuario);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        preferencias.salvarDados(usuario.getNome(), usuario.getEmail());
                        acessarHome();
                    }
                } else {
                    Toast.makeText(CadastroActivity.this, "Não foi possível salvar, tente novamente!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(CadastroActivity.this, "Não foi possível salvar, tente novamente!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void mascara(String mascara, EditText campo) {
        SimpleMaskFormatter smf = new SimpleMaskFormatter(mascara);
        MaskTextWatcher mtw = new MaskTextWatcher(campo, smf);
        campo.addTextChangedListener(mtw);
    }
}