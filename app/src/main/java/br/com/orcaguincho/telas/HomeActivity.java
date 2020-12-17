package br.com.orcaguincho.telas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import br.com.orcaguincho.R;
import dao.DiaDAO;
import dao.HorarioDAO;
import dao.TarifaDAO;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        context = this;
        tarifa = new Tarifa();
        tarifaDAO = new TarifaDAO(context);
        dia = new Dia();
        diaDAO = new DiaDAO(context);
        horario = new Horario();
        horarioDAO = new HorarioDAO(context);

        tarifa = tarifaDAO.recupera();
        if(tarifa == null) {
            Toast.makeText(context, "Insira algumas tarifas antes de prosseguir", Toast.LENGTH_SHORT).show();
            acessaActivity(ConfiguracaoActivity.class);
        }

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
        toolbar.setTitle("Configurações");
        dataHora = (TextView) findViewById(R.id.dataHora);
        txtAviso = (TextView) findViewById(R.id.txtAviso);
        txtValorCorrida = (TextView) findViewById(R.id.txtValorCorrida);
        cbImediato = (CheckBox) findViewById(R.id.cbImediato);
        cbDomFer = (CheckBox) findViewById(R.id.cbDomFer);
        etDistancia = (EditText) findViewById(R.id.etDistancia);
        btnCalcular = (Button) findViewById(R.id.btnCalcular);
        dataHora.setText(formataHora("dd/MM/yyyy - HH:mm"));

        mascara("KM NNNNN", etDistancia);

        String data = formataHora("dd/MM/yyyy");
        txtValorCorrida.setText(getWeek(data));

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double distancia = Double.parseDouble(etDistancia.getText().toString().replaceAll("KM", ""));
            }
        });

    }

    /* RECUPERANDO A DATA */
    private String formataHora(String formato){
        SimpleDateFormat dataFormatada = new SimpleDateFormat(formato);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Date dataAtual = calendar.getTime();
        return dataFormatada.format(dataAtual);
    }

    public static String getWeek(String date){
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
        switch (item.getItemId()){
            case R.id.item_valores:
                acessaActivity(ConfiguracaoActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}