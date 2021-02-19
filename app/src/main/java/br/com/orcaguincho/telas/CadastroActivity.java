package br.com.orcaguincho.telas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.HashMap;
import java.util.Map;

import br.com.orcaguincho.R;
import util.Preferencias;

public class CadastroActivity extends AppCompatActivity {

    private EditText etNome, etEmail, etTelefone;
    private Button btnCadastrar;
    private Preferencias preferencias;

    private RequestQueue requestQueue;
    private JsonArrayRequest jsonArrayRequest;
    private String url = "https://172.24.0.3/api/v1/usuario";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        Preferencias preferencias = new Preferencias(CadastroActivity.this);

       /* if (!preferencias.getIdentificador().isEmpty()) { *//* Já possui cadastro *//*
            Intent it = new Intent(CadastroActivity.this, HomeActivity.class);
            startActivity(it);
        }
*/
        validaCampo();
    }

    private void validaCampo() {

        etNome = (EditText) findViewById(R.id.idNome);
        etEmail = (EditText) findViewById(R.id.idEmail);
        etTelefone = (EditText) findViewById(R.id.idTelefone);
        btnCadastrar = (Button) findViewById(R.id.btnCadastro);

        mascara("(NN) NNNNN-NNNN" , etTelefone);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = etNome.getText().toString();
                String email = etEmail.getText().toString();
                String telefone = etTelefone.getText().toString();
                submit(nome, email, telefone);
            }
        });
    }

    private void submit(String nome, String email, String telefone) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent it = new Intent(CadastroActivity.this, HomeActivity.class);
                startActivity(it);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CadastroActivity.this, "Não foi possível salvar dados!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nome", nome);
                params.put("email", email);
                params.put("telefone", telefone);

                return super.getParams();
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    private void mascara(String mascara, EditText campo) {
        SimpleMaskFormatter smf = new SimpleMaskFormatter(mascara);
        MaskTextWatcher mtw = new MaskTextWatcher(campo, smf);
        campo.addTextChangedListener(mtw);
    }
}