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
import android.widget.TextView;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import asynctask.BaseAsyncTask;
import br.com.orcaguincho.R;
import database.ValoraDatabase;
import database.dao.DiaDAO;
import database.dao.HorarioDAO;
import database.dao.TarifaDAO;
import model.Dia;
import model.Horario;
import model.Tarifa;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView dataHora, txtAviso, txtValorCorrida;
    private CheckBox cbImediato, cbDomFer;
    private EditText etDistancia;
    private Button btnCalcular;

    private Tarifa tarifa;
    private TarifaDAO tarifaDAO;

    private Dia dia;
    private DiaDAO diaDAO;

    private Horario horario;
    private HorarioDAO horarioDAO;
    private Context context;
    private List<Horario> horarios;
    private List<Dia> dias;

    private String valorFormatado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        context = this;
        ValoraDatabase db = ValoraDatabase.getInstance(context);

        tarifa = new Tarifa();
        tarifaDAO = db.gerTarifaDAO();

        dia = new Dia();
        diaDAO = db.getDiaDAO();

        horario = new Horario();
        horarioDAO = db.getHorarioDAO();

        horarios = new ArrayList<>();
        dias = new ArrayList<>();

        valorFormatado = "";

        new BaseAsyncTask<>(() -> {
            tarifa = tarifaDAO.buscaPorId(1); //Verifica se já possui alguma tarifa
            return tarifa;
        }, (tarifa) -> {
            if (tarifa == null) {
                Toast.makeText(context, "Insira algumas tarifas antes de prosseguir", Toast.LENGTH_SHORT).show();
                acessaActivity(ConfiguracaoActivity.class);
            }
        }).execute();

        validaCampos();
    }

    private void mascara(String mascara, EditText campo) {
        SimpleMaskFormatter smf = new SimpleMaskFormatter(mascara);
        MaskTextWatcher mtw = new MaskTextWatcher(campo, smf);
        campo.addTextChangedListener(mtw);
    }

    private void validaCampos() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Home");
        dataHora = (TextView) findViewById(R.id.dataHora);
        txtAviso = (TextView) findViewById(R.id.txtAviso);
        txtValorCorrida = (TextView) findViewById(R.id.txtValorCorrida);
       /* cbImediato = (CheckBox) findViewById(R.id.cbImediato);
        cbDomFer = (CheckBox) findViewById(R.id.cbDomFer);*/
        etDistancia = (EditText) findViewById(R.id.etDistancia);
        btnCalcular = (Button) findViewById(R.id.btnCalcular);
        dataHora.setText(formataHora("dd/MM/yyyy - HH:mm"));

        mascara("KM NNNNN", etDistancia);

        String data = formataHora("dd/MM/yyyy");
        String hora = formataHora("HHmm");
        //  txtValorCorrida.setText(getWeek(data));

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtValorCorrida.setText("R$ 00,00");
                if (!etDistancia.getText().toString().equals("")) {
                    double distancia = Double.parseDouble(etDistancia.getText().toString().replaceAll("KM", ""));
                    calcularValor(data, hora, distancia);
                } else
                    Toast.makeText(context, "Preencha a distância!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void calcularValor(String data, String hora, double distancia) {

        new BaseAsyncTask<>(() -> {
            String valor = "";
            int day = converteDia(data);
            dia = diaDAO.buscaPorDia(day);

            dias = diaDAO.buscaPorTarifa(dia.getIdTarifa());
            horario = horarioDAO.buscaPorTarifa(dia.getIdTarifa());

            for (Dia d : dias) Log.i("Tarifa", "Dia Tarifa: " + d.getDia());
            for (Horario h : horarios)
                Log.i("Tarifa", "Horário Tarifa: " + h.getHoraInicial() + " - " + h.getHoraFinal());

            int horaCompara = Integer.parseInt(hora);

            return calculaTarifa(distancia, valor, horaCompara);
        }, (valor) -> {
            Log.i("Tarifa", "Valor: " + valor);
            txtValorCorrida.setText("R$" + valor);
        }).execute();

    }

    @NotNull
    private String calculaTarifa(double distancia, String valor, int horaCompara) {
        for (Horario h : horarios) { //Tentando encontrar o horário
            if (h.getHoraInicial() <= horaCompara && h.getHoraFinal() >= horaCompara) {
                tarifa = tarifaDAO.buscaPorId(h.getIdTarifa());
                Log.i("Tarifa", "Tarifa Buscada: " + tarifa.getValor15());
            }
        }

        if (distancia <= 15) valor += String.valueOf(tarifa.getValor15());
        if (distancia <= 30 && distancia > 15) valor = String.valueOf(tarifa.getValor30());
        if (distancia > 30) {
            int diferenca = (int) (distancia - 30);
            int valorFinal = (int) (tarifa.getValor30() + ((int) (diferenca * tarifa.getValorKmAdicional())));
            valor += String.valueOf(valorFinal);
        }
        DecimalFormat df = new DecimalFormat(",##0,00");

        return df.format(Double.parseDouble(valor));
    }

    private int converteDia(String data) {
        int day = 0;
        switch (data) {
            case "DOM":
                day = 0;
                break;
            case "SEG":
                day = 1;
                break;
            case "TER":
                day = 2;
                break;
            case "QUA":
                day = 3;
                break;
            case "QUI":
                day = 4;
                break;
            case "SEX":
                day = 5;
                break;
            case "SAB":
                day = 6;
                break;
            default:
                day = 1;
                break;
        }

        return day;
    }

    /* RECUPERANDO A DATA */
    private String formataHora(String formato) {
        SimpleDateFormat dataFormatada = new SimpleDateFormat(formato);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Date dataAtual = calendar.getTime();
        return dataFormatada.format(dataAtual);
    }

    public static String getWeek(String date) {
        String dayWeek = "---";
        GregorianCalendar gc = new GregorianCalendar();
        try {
            gc.setTime(new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR")).parse(date));
            return new SimpleDateFormat("EEE", new Locale("pt", "BR")).format(gc.getTime()).toUpperCase();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dayWeek;
    }

    private void acessaActivity(Class c) {
        Intent it = new Intent(HomeActivity.this, c);
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
        switch (item.getItemId()) {
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