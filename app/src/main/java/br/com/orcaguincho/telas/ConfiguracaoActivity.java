package br.com.orcaguincho.telas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.ArrayList;

import br.com.orcaguincho.R;
import dao.DiaDAO;
import dao.HorarioDAO;
import dao.TarifaDAO;
import model.Dia;
import model.Horario;
import model.Tarifa;
import util.MoneyTextWatcher;

public class ConfiguracaoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText horaInicial, horaFinal;
    private CheckBox cbSeg, cbTer, cbQua, cbQui, cbSex, cbSab, cbDom;
    private EditText txt15, txt30, txt31;
    private Button btnAddValores;

    private Tarifa tarifa;
    private TarifaDAO tarifaDAO;

    private Dia dia;
    private DiaDAO diaDAO;

    private Horario horario;
    private HorarioDAO horarioDAO;
    private  ArrayList<CheckBox> checklist;
    private  ArrayList<EditText> inputs;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        context = this;
        tarifaDAO = new TarifaDAO(context);
        diaDAO = new DiaDAO(context);
        horarioDAO = new HorarioDAO(context);
        checklist = new ArrayList<>();
        inputs = new ArrayList<>();
        validaCampo();
    }

    private void validaCampo() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Configurações");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        horaInicial = (EditText) findViewById(R.id.idHoraInicial);
        horaFinal = (EditText) findViewById(R.id.idHoraFinal);
        cbSeg = (CheckBox) findViewById(R.id.idCBSegunda);
        cbTer = (CheckBox) findViewById(R.id.idCBTerca);
        cbQua = (CheckBox) findViewById(R.id.idCBQuarta);
        cbQui = (CheckBox) findViewById(R.id.idCBQuinta);
        cbSex = (CheckBox) findViewById(R.id.idCBSexta);
        cbSab = (CheckBox) findViewById(R.id.idCBSabado);
        cbDom = (CheckBox) findViewById(R.id.idCBDomingo);
        txt15 = (EditText) findViewById(R.id.idtxtValor15);
        txt30 = (EditText) findViewById(R.id.idtxtValor30);
        txt31 = (EditText) findViewById(R.id.idtxtValor31);
        btnAddValores = (Button) findViewById(R.id.btnAddValores);

        mascara("NN:NN", horaInicial);
        mascara("NN:NN", horaFinal);
        txt15.addTextChangedListener(new MoneyTextWatcher(txt15));
        txt30.addTextChangedListener(new MoneyTextWatcher(txt30));
        txt31.addTextChangedListener(new MoneyTextWatcher(txt31));

        inputs.add(horaInicial);
        inputs.add(horaFinal);
        inputs.add(txt15);
        inputs.add(txt30);
        inputs.add(txt31);

        checklist.add(cbDom);
        checklist.add(cbSeg);
        checklist.add(cbTer);
        checklist.add(cbQua);
        checklist.add(cbQui);
        checklist.add(cbSex);
        checklist.add(cbSab);

        btnAddValores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verificaCampoVazio()) { /* Lógica para salvar todas as tarifas e limpar os campos */
/*
                    int hInicio, hFim;
                    hInicio = Integer.parseInt(removeCaracter(horaInicial, ":", ""));
                    hFim = Integer.parseInt(removeCaracter(horaFinal, ":", ""));

                    if(hInicio > hFim) {*/
                        tarifa = new Tarifa();

                        tarifa.setValor15(Double.parseDouble(txt15.getText().toString().replaceAll(",", "")));
                        tarifa.setValor30(Double.parseDouble(txt30.getText().toString().replaceAll(",", "")));
                        tarifa.setValor31(Double.parseDouble(txt31.getText().toString().replaceAll(",", "")));
                        int idTarifa = (int) tarifaDAO.inserir(tarifa);

                        Log.i("Retorno", "idTarifa - " + idTarifa);

                        salvarDias(idTarifa);
                        salvarHorarios(idTarifa);
                        limparCampos();
                        toast("Tarifa adicionada com sucesso!");
                   /*
                   } else
                        toast("A Hora INICIAL não pode ser menor do que a FINAL!");
                   * */
                } else
                    toast("Preencha todos os campos!");
            }
        });
    }

    private void mascara(String mascara, EditText campo) {
        SimpleMaskFormatter smf = new SimpleMaskFormatter(mascara);
        MaskTextWatcher mtw = new MaskTextWatcher(campo, smf);
        campo.addTextChangedListener(mtw);
    }

    private void toast(String mensagem){
        Toast.makeText(context, mensagem, Toast.LENGTH_SHORT).show();
    }

    private void limparCampos() {

       for(EditText et : inputs)
           et.setText("");

       for(CheckBox cb : checklist)
           if(cb.isChecked()) cb.setChecked(false);

    }

    private void salvarDias(int idTarifa) {

        int i = 0;
        for(CheckBox cb : checklist){
            if(cb.isChecked()){
                dia = new Dia();
                dia.setIdTarifa(idTarifa);
                dia.setDia(i);

               long id = diaDAO.inserir(dia);

               Log.i("Retorno", "idDia - " + id);
            }

            i++;
        }
    }

    private String removeCaracter(EditText inicial , String reg, String rep) {
        return inicial.getText().toString().replace(reg, rep);
    }

    private void salvarHorarios(int idTarifa) {
        horario = new Horario();
        horario.setIdTarifa(idTarifa);
        horario.setHoraInicial(Integer.parseInt(removeCaracter(horaInicial, ":", "")));
        horario.setHoraFinal(Integer.parseInt(removeCaracter(horaFinal, ":", "")));
        long id = horarioDAO.inserir(horario);

        Log.i("Retorno", "idHorario - " + id);
    }

    private boolean verificaCampoVazio() {
        boolean salvar = true;

        if(horaInicial.getText().toString().equals("")) salvar = false;
        if(horaFinal.getText().toString().equals("")) salvar = false;
        if(txt15.getText().toString().equals("")) salvar = false;
        if(txt30.getText().toString().equals("")) salvar = false;
        if(txt31.getText().toString().equals("")) salvar = false;


        for(CheckBox cb : checklist)
            if(cb.isChecked()) break;

        return salvar;
    }

    private void acessaActivity(Class c) {
        Intent it = new Intent(ConfiguracaoActivity.this, c);
        startActivity(it);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.principal_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.item_valores:
                acessaActivity(ConfiguracaoActivity.class);
                return true;
            case R.id.item_tarifas:
                acessaActivity(ListaTarifasActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}