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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.lang.reflect.Array;
import java.util.ArrayList;

import adapter.TipoVeiculoAdapter;
import asynctask.BaseAsyncTask;
import br.com.orcaguincho.R;
import database.dao.DiaDAO;
import database.dao.HorarioDAO;
import database.dao.TarifaDAO;
import database.ValoraDatabase;
import model.Dia;
import model.Horario;
import model.ListaTarifa;
import model.Tarifa;
import util.MoneyTextWatcher;

public class ConfiguracaoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText horaInicial, horaFinal;
    private CheckBox cbSeg, cbTer, cbQua, cbQui, cbSex, cbSab, cbDom/* cbPasseio, cbUtilitario*/;
    private EditText txt15, txt30, txt31;
    private Button btnAddValores;

    private Spinner tiposVeiculosS;

    private Tarifa tarifa;
    private TarifaDAO tarifaDAO;

    private Dia dia;
    private DiaDAO diaDAO;

    private Horario horario;
    private HorarioDAO horarioDAO;
    private ArrayList<CheckBox> checklist;
    private ArrayList<EditText> inputs;

    private Context context;
    private ListaTarifa listaTarifa;
    private String tipoSelecionado = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Recuperando o caminho do documento
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            listaTarifa = new ListaTarifa();
            listaTarifa = (ListaTarifa) extras.getSerializable("tarifa");
            Log.i("tarifa", listaTarifa.getDiasSemana());
        }

        context = this;
        ValoraDatabase db = ValoraDatabase.getInstance(context);

        tarifaDAO = db.gerTarifaDAO();
        diaDAO = db.getDiaDAO();
        horarioDAO = db.getHorarioDAO();
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
        tiposVeiculosS = (Spinner) findViewById(R.id.tiposVeiculosS);
       /* cbPasseio = (CheckBox) findViewById(R.id.idCBPasseio);
        cbUtilitario = (CheckBox) findViewById(R.id.idCBUtilitario);*/
        txt15 = (EditText) findViewById(R.id.idtxtValor15);
        txt30 = (EditText) findViewById(R.id.idtxtValor30);
        txt31 = (EditText) findViewById(R.id.idtxtValor31);
        btnAddValores = (Button) findViewById(R.id.btnAddValores);

        if (listaTarifa == null) {
            mascara("NN:NN", horaInicial);
            mascara("NN:NN", horaFinal);
            txt15.addTextChangedListener(new MoneyTextWatcher(txt15));
            txt30.addTextChangedListener(new MoneyTextWatcher(txt30));
            txt31.addTextChangedListener(new MoneyTextWatcher(txt31));
        }

        ArrayAdapter<CharSequence> tiposVeiculosAdapter =
                ArrayAdapter.createFromResource(this, R.array.tiposVeiculos, R.layout.spinner_item);

        tiposVeiculosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       // tiposVeiculosS.setOnItemSelectedListener(TipoVeiculoAdapter.class);

       // tiposVeiculosS.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) context);

        copulaListas();

        if (listaTarifa != null)
            recuperarInformacoes();

        btnAddValores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificaCampoVazio()) { /* Lógica para salvar todas as tarifas e limpar os campos */
/*
                    int hInicio, hFim;
                    hInicio = Integer.parseInt(removeCaracter(horaInicial, ":", ""));
                    hFim = Integer.parseInt(removeCaracter(horaFinal, ":", ""));

                    if(hInicio > hFim) {*/
                    tarifa = new Tarifa();

                    tarifa.setValor15(Double.parseDouble(removeCaracter(txt15, ",", "")));
                    tarifa.setValor30(Double.parseDouble(removeCaracter(txt30, ",", "")));
                    tarifa.setValorKmAdicional(Double.parseDouble(removeCaracter(txt31, ",", "")));


                    /*if (cbPasseio.isChecked()) tarifa.setPasseio(1);
                    else if (cbUtilitario.isChecked()) tarifa.setUtilitario(1);
                    else tarifa.setPasseio(1);*/
                    tarifa.setTipoVeiculo((String) tiposVeiculosS.getSelectedItem());

                    new BaseAsyncTask<>(() -> {

                        int idTarifa = (int) tarifaDAO.salva(tarifa);

                        Log.i("Retorno", "idTarifa - " + idTarifa);

                        int i = 0;
                        for (CheckBox cb : checklist) {
                            if (cb.isChecked()) {
                                dia = new Dia();
                                dia.setIdTarifa(idTarifa);
                                dia.setDia(i);
                                long inserido = diaDAO.salva(dia);
                                Log.i("Tarifa", "Dia inserido: " + inserido);
                            }

                            i++;
                        }

                        horario = new Horario();
                        horario.setIdTarifa(idTarifa);
                        horario.setHoraInicial(Integer.parseInt(removeCaracter(horaInicial, ":", "")));
                        horario.setHoraFinal(Integer.parseInt(removeCaracter(horaFinal, ":", "")));
                        horarioDAO.salva(horario);

                        return null;

                    }, (inserido) -> {
                        limparCampos();
                        toast("Tarifa adicionada com sucesso!");
                    }).execute();

                } else
                    toast("Preencha todos os campos!");
            }
        });
    }

    private void copulaListas() {
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
    }

    private void recuperarInformacoes() {

        horaInicial.setText(listaTarifa.getHorario().substring(0, 5));
        horaFinal.setText(listaTarifa.getHorario().substring(8, 13));

        preencheDias();

        txt15.setText(listaTarifa.getValor15() + "0");
        txt30.setText(listaTarifa.getValor30() + "0");
        txt31.setText(listaTarifa.getKmAdicional());

    }

    private void preencheDias() {
        if (listaTarifa.getDiasSemana().contains("DOM")) cbDom.setChecked(true);
        if (listaTarifa.getDiasSemana().contains("SEG")) cbSeg.setChecked(true);
        if (listaTarifa.getDiasSemana().contains("TER")) cbTer.setChecked(true);
        if (listaTarifa.getDiasSemana().contains("QUA")) cbQua.setChecked(true);
        if (listaTarifa.getDiasSemana().contains("QUI")) cbQui.setChecked(true);
        if (listaTarifa.getDiasSemana().contains("SEX")) cbSex.setChecked(true);
        if (listaTarifa.getDiasSemana().contains("SAB")) cbSab.setChecked(true);
    }

    private void mascara(String mascara, EditText campo) {
        SimpleMaskFormatter smf = new SimpleMaskFormatter(mascara);
        MaskTextWatcher mtw = new MaskTextWatcher(campo, smf);
        campo.addTextChangedListener(mtw);
    }

    private void toast(String mensagem) {
        Toast.makeText(context, mensagem, Toast.LENGTH_SHORT).show();
    }

    private void limparCampos() {

        for (EditText et : inputs)
            et.getText().clear();

        for (CheckBox cb : checklist)
            if (cb.isChecked()) cb.setChecked(false);

//        acessaActivity(ConfiguracaoActivity.class);

    }

    private String removeCaracter(EditText inicial, String reg, String rep) {
        return inicial.getText().toString().replace(reg, rep);
    }

    private boolean verificaCampoVazio() {
        boolean salvar = true;

        if (horaInicial.getText().toString().equals("")) salvar = false;
        if (horaFinal.getText().toString().equals("")) salvar = false;
        if (txt15.getText().toString().equals("")) salvar = false;
        if (txt30.getText().toString().equals("")) salvar = false;
        if (txt31.getText().toString().equals("")) salvar = false;

        for (CheckBox cb : checklist)
            if (cb.isChecked()) break;

        return salvar;
    }

    private void acessaActivity(Class c) {
        Intent it = new Intent(ConfiguracaoActivity.this, c);
        startActivity(it);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.principal_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
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