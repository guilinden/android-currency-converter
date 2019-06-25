package br.feevale.conversordemoedas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class ListRates extends AppCompatActivity {

    DataBaseStructure db;
    ListRatesAdapter listRatesAdapter;
    ListView listRates;
    Spinner spinner;
    Intent it;
    String originalCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_rates);

        db = new DataBaseStructure(this);
        it = getIntent();
        originalCurrency = "USD";
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.moedas,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        listRates = (ListView) findViewById(R.id.listRates);



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                originalCurrency = parent.getItemAtPosition(position).toString();
                listRatesAdapter.setBaseCurrency(originalCurrency);
                listRatesAdapter.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listRatesAdapter = new ListRatesAdapter(getBaseContext(),db);
        listRatesAdapter.setBaseCurrency(originalCurrency);
        listRates.setAdapter(listRatesAdapter);


    }
}
