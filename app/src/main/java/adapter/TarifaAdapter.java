package adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.orcaguincho.R;
import model.ListaTarifa;

public class TarifaAdapter extends BaseAdapter {

    private List<ListaTarifa> tarifas;
    private Activity activity;

    public TarifaAdapter(Activity activity, List<ListaTarifa> tarifas) {
        this.activity = activity;
        this.tarifas = tarifas;
    }

    @Override
    public int getCount() {
        return tarifas.size();
    }

    @Override
    public Object getItem(int i) {
        return tarifas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return tarifas.get(i).getId();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v = activity.getLayoutInflater().inflate(R.layout.list_tarifa, parent, false);

        TextView txtDias = v.findViewById(R.id.txtDias);
        TextView txtHorario = v.findViewById(R.id.txtHorario);
        TextView txtValor15 = v.findViewById(R.id.txtValor15);
        TextView txtValor30 = v.findViewById(R.id.txtValor30);
        TextView txtKmAdicional = v.findViewById(R.id.txtKmAdicional);
        TextView txtTipoVeiculo = v.findViewById(R.id.txtTipoVeiculo);

        ListaTarifa lt = tarifas.get(i);

        txtTipoVeiculo.setText(lt.getTipoVeiculo());
        txtDias.setText(lt.getDiasSemana());
        txtHorario.setText(lt.getHorario());
        txtValor15.setText("R$: " + lt.getValor15());
        txtValor30.setText("R$: " +lt.getValor30());
        txtKmAdicional.setText("R$: " + lt.getKmAdicional());

        return v;
    }


}
