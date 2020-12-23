package br.com.orcaguincho.telas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import adapter.TarifaAdapter;
import br.com.orcaguincho.R;
import dao.DiaDAO;
import dao.HorarioDAO;
import dao.TarifaDAO;
import model.Dia;
import model.Horario;
import model.ListaTarifa;
import model.Tarifa;

public class ListaTarifasActivity extends AppCompatActivity {

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

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_tarifas);

        validaCampos();
    }

    private void validaCampos() {

        fab = (FloatingActionButton) findViewById(R.id.fabAdd);
        lvTarifas = (ListView) findViewById(R.id.lvTarifas);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ListaTarifasActivity.this, ConfiguracaoActivity.class);
                startActivity(it);
            }
        });

        tarifaDAO = new TarifaDAO(context);
        diaDAO = new DiaDAO(context);
        horarioDAO = new HorarioDAO(context);

        tarifas = new ArrayList<>();
        tarifaList = tarifaDAO.obterTodos();

        for(Tarifa t : tarifaList) {
            dias = diaDAO.getByTarifa(t.getId());
            lt = new ListaTarifa();
            horario = new Horario();
            horario = horarioDAO.getByIdTarifa(t.getId());

            String dataConvertida = "";
            for(Dia d : dias)
                dataConvertida += " " + converteDia(d.getDia()) + ";";

            lt.setDiasSemana(dataConvertida);

            lt.setHorario(horario.getHoraInicial() + " - " + horario.getHoraFinal());
            lt.setValor15(String.valueOf(t.getValor15()));
            lt.setValor30(String.valueOf(t.getValor30()));
            lt.setKmAdicional(String.valueOf(t.getValor31()));

            tarifas.add(lt);
            dias.clear();
        }

        tarifasFiltradas.addAll(tarifas);

        TarifaAdapter adapter = new TarifaAdapter(ListaTarifasActivity.this, tarifasFiltradas);
        lvTarifas.setAdapter(adapter);
        registerForContextMenu(lvTarifas);
    }

    private String converteDia(int data) {
        String day = "";
        switch (data){
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

}