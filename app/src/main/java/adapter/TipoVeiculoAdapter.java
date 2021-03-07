package adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import model.TipoVeiculo;

public class TipoVeiculoAdapter extends ArrayAdapter<TipoVeiculo> {

    private Context context;

    private TipoVeiculo[] values;

    public TipoVeiculoAdapter(@NonNull Context context, int resource, @NonNull TipoVeiculo[] tipoVeiculos) {
        super(context, resource, tipoVeiculos);
    }

    @Override
    public int getCount(){
        return values.length;
    }

    @Override
    public TipoVeiculo getItem(int position){
        return values[position];
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);

        label.setText(values[position].getTipoVeiculo());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
        //return super.getView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }
}
