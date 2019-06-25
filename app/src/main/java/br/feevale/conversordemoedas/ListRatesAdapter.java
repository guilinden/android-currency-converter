package br.feevale.conversordemoedas;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListRatesAdapter extends BaseAdapter {

    DataBaseStructure db;
    Context ctx;
    LayoutInflater inflater;
    private String baseCurrency;

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public ListRatesAdapter(Context ctx, DataBaseStructure db){
        inflater = LayoutInflater.from(ctx);
        this.db = db;
    }

    @Override
    public int getCount() {
        return db.getRateByCurrency(baseCurrency).size();
    }

    @Override
    public Object getItem(int position) {
        return db.getRates().get(position);
    }

    @Override
    public long getItemId(int position) {
        return db.getRateByCurrency(baseCurrency).get(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = inflater.inflate(R.layout.activity_list_rates_adapter, viewGroup, false);

        TextView txtConvertedValue = (TextView) v.findViewById(R.id.txtValorCotacao);
        //TextView txtMoedaBase = (TextView) v.findViewById(R.id.txtMoedaBase);
        TextView txtMoedaCotada = (TextView) v.findViewById(R.id.txtMoedaCotada);
        TextView txtDataCotacao = (TextView) v.findViewById(R.id.txtDataCotacao);

        Rate r = db.getRateByCurrency(baseCurrency).get(position);

        txtDataCotacao.setText(String.valueOf(r.getDate()));
        //txtMoedaBase.setText(String.valueOf(r.getBaseCurrency()));
        txtMoedaCotada.setText(String.valueOf(r.getToCurrency()));
        txtConvertedValue.setText(String.valueOf(r.getRateValue()));

        return v;
    }
}
