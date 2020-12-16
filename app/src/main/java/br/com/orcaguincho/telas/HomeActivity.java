package br.com.orcaguincho.telas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import br.com.orcaguincho.R;

public class HomeActivity extends AppCompatActivity {

    private EditText horaInicial, horaFinal;
    private CheckBox cbSeg, cbTer, cbQua, cbQui, cbSex, cbSab, cbDom;
    private TextView txt15, txt30, txt31;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        validaCampo();
    }

    private void validaCampo() {

        horaFinal = (EditText) findViewById(R.id.idHoraInicial);
        horaFinal = (EditText) findViewById(R.id.idHoraFinal);
        cbSeg = (CheckBox) findViewById(R.id.idCBSegunda);
        cbTer = (CheckBox) findViewById(R.id.idCBTerca);
        cbQua = (CheckBox) findViewById(R.id.idCBQuarta);
        cbQui = (CheckBox) findViewById(R.id.idCBQuinta);
        cbSex = (CheckBox) findViewById(R.id.idCBSexta);
        cbSab = (CheckBox) findViewById(R.id.idCBSabado);
        cbDom = (CheckBox) findViewById(R.id.idCBDomingo);
        txt15 = (TextView) findViewById(R.id.idtxtValor15);
        txt30 = (TextView) findViewById(R.id.idtxtValor30);
        txt31 = (TextView) findViewById(R.id.idtxtValor31);


    }
}