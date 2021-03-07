package br.com.orcaguincho.telas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import adapter.TarifaAdapter;
import asynctask.BaseAsyncTask;
import br.com.orcaguincho.R;
import database.ValoraDatabase;
import database.dao.DiaDAO;
import database.dao.HorarioDAO;
import database.dao.TarifaDAO;
import model.Dia;
import model.Horario;
import model.ListaTarifa;
import model.Tarifa;

public class ListaTarifasActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private ListView lvTarifas;

    private ListaTarifa lt;
    private List<ListaTarifa> tarifas;
    private List<ListaTarifa> tarifasFiltradas = new ArrayList<>();

    private List<Tarifa> tarifaList;
    private TarifaDAO tarifaDAO;

    private List<Dia> dias;
    private DiaDAO diaDAO;

    private List<Horario> horarios;
    private HorarioDAO horarioDAO;
    private Horario horario;
    private ValoraDatabase db;

    private Context context = this;
    private TarifaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_tarifas);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        db = ValoraDatabase.getInstance(context);

        tarifaList = new ArrayList<>();

        validaCampos();

    }

    private void validaCampos() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Tarifas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fab = (FloatingActionButton) findViewById(R.id.fabAdd);
        lvTarifas = (ListView) findViewById(R.id.lvTarifas);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ListaTarifasActivity.this, ConfiguracaoActivity.class);
                startActivity(it);
            }
        });

        tarifas = new ArrayList<>();

        new BaseAsyncTask<>(() -> {

            tarifaDAO = db.gerTarifaDAO();
            diaDAO = db.getDiaDAO();
            horarioDAO = db.getHorarioDAO();
            tarifaList = tarifaDAO.buscaTodos();

            for (Tarifa t : tarifaList) {
                dias = diaDAO.buscaPorTarifa(t.getId());
                lt = new ListaTarifa();
                horario = new Horario();
                horario = horarioDAO.buscaPorTarifa(t.getId());

                String dataConvertida = "";
                for (Dia d : dias)
                    dataConvertida += " " + converteDia(d.getDia()) + "; ";

                lt.setDiasSemana(dataConvertida);

                lt.setTipoVeiculo(t.getTipoVeiculo());
                lt.setHorario(formataHora(String.valueOf(horario.getHoraInicial())) +
                        " - " + formataHora(String.valueOf(horario.getHoraFinal())));
                lt.setValor15(formataValor(String.valueOf(t.getValor15())));
                lt.setValor30(formataValor(String.valueOf(t.getValor30())));
                lt.setKmAdicional(formataValorAdicional(String.valueOf(t.getValorKmAdicional())));

                tarifas.add(lt);
                dias.clear();

                lt.setIdTarifa(Long.valueOf(t.getId()));

            }

            return tarifas;
        }, (t) -> tarifasFiltradas.addAll(t)).execute();

        adapter = new TarifaAdapter(ListaTarifasActivity.this, tarifasFiltradas);
        lvTarifas.setAdapter(adapter);
        registerForContextMenu(lvTarifas);
    }

    private String formataValor(String valor) {
        String s = valor.replace("00.0", ",0");
        return s;
    }

    private String formataValorAdicional(String valor) {

        String v = valor.replace(".0", "");
        StringBuilder builder = new StringBuilder(v);

        builder = builder.insert(1, ",");

        return builder.toString();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.contexto_menu, menu);
    }

    public void excluir(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final ListaTarifa excluirTarifa = tarifasFiltradas.get(menuInfo.position);
        AlertDialog dialog = new AlertDialog.Builder(this, R.style.DialogStyle)
                .setTitle("Atenção")
                .setMessage("Realmente deseja excluir a tarifa?")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /* EXCLUIR AS NAS TRÊS TABELAS */
                        new BaseAsyncTask<>(() -> {
                            //Excluir nas tres tabelas
                            tarifaDAO = db.gerTarifaDAO();
                            diaDAO = db.getDiaDAO();
                            horarioDAO = db.getHorarioDAO();

//                            List<Dia> diaExclui = diaDAO.buscaPorTarifa(excluirTarifa.getIdTarifa());
//                            for(Dia d : diaExclui) diaDAO.remove(d);
//
//                            Horario horarioExclui = horarioDAO.buscaPorTarifa(excluirTarifa.getIdTarifa());
//                            horarioDAO.remove(horarioExclui);

                            Tarifa tarifaExclui = tarifaDAO.buscaPorId(excluirTarifa.getIdTarifa());
                            tarifaDAO.remove(tarifaExclui);

                            return null;
                        }, (item) -> {adapter.notifyDataSetChanged();}).execute();

                    }
                }).create();
        dialog.show();
    }

    public void visualizar(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final ListaTarifa tarifaVisualizar = tarifasFiltradas.get(menuInfo.position);
        Intent it = new Intent(ListaTarifasActivity.this, ConfiguracaoActivity.class);
        it.putExtra("tarifa", tarifaVisualizar);
        startActivity(it);

    }

    /* RECUPERANDO A HORA */
    private String formataHora(String hora) {

        StringBuilder builder = new StringBuilder(hora);

        if (builder.length() <= 3)
            builder = builder.insert(0, "0");

        builder = builder.insert(2, ":");

        return builder.toString();
    }

    private String converteDia(int data) {
        String day = "";
        switch (data) {
            case 0:
                day = "DOM";
                break;
            case 1:
                day = "SEG";
                break;
            case 2:
                day = "TER";
                break;
            case 3:
                day = "QUA";
                break;
            case 4:
                day = "QUI";
                break;
            case 5:
                day = "SEX";
                break;
            case 6:
                day = "SAB";
                break;
            default:
                day = "DOM";
                break;
        }

        return day;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tarifas_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.home:
                Intent it = new Intent(ListaTarifasActivity.this, HomeActivity.class);
                startActivity(it);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}