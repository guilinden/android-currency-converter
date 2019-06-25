package br.feevale.conversordemoedas;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    String originalCurrency = "BRL";
    String convertToCurrency;
    EditText valueInput;
    Boolean hasInternetPermission = false;
    TextView txtConvertedValue;
    DataBaseStructure db;
    public final int CODE_MAIN = 989;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner2 = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.moedas,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        Button btnConverter = findViewById(R.id.btnConverter);
        valueInput = (EditText) findViewById(R.id.valueInput);
        txtConvertedValue = (TextView) findViewById(R.id.txtConvertedValue);
        askPermissions();

        db = new DataBaseStructure(this);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                convertToCurrency = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        }

    public boolean hasPermission(){
        return hasInternetPermission;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Toast.makeText(getBaseContext(), "Permission Result: " + permissions[0], Toast.LENGTH_LONG).show();
        if(grantResults[0] == 0) {
            hasInternetPermission = true;
        }
        else {
            hasInternetPermission = false;
        }
        Toast.makeText(getBaseContext(), "Permission Result: " + grantResults[0], Toast.LENGTH_LONG).show();
    }

    public void askPermissions(){
        if(ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getBaseContext(), "Dont't have permission!", Toast.LENGTH_LONG).show();

            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)){
                showDialogOK("Try accept permission again?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                Toast.makeText(getBaseContext(), "Yes, I want!", Toast.LENGTH_LONG).show();
                                String permissions[] = {Manifest.permission.INTERNET};
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        permissions,
                                        1010);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                Toast.makeText(getBaseContext(), "No, I do not!", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
            }else{
                String permissions[] = {Manifest.permission.INTERNET};
                ActivityCompat.requestPermissions(this,
                        permissions,
                        1010);
            }


        }else{

        }

        if(ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getBaseContext(), "Dont't have permission!", Toast.LENGTH_LONG).show();

            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)){
                showDialogOK("Try accept permission again?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                Toast.makeText(getBaseContext(), "Yes, I want!", Toast.LENGTH_LONG).show();
                                String permissions[] = {Manifest.permission.INTERNET};
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        permissions,
                                        1010);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                Toast.makeText(getBaseContext(), "No, I do not!", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
            }else{
                String permissions[] = {Manifest.permission.INTERNET};
                ActivityCompat.requestPermissions(this,
                        permissions,
                        1010);
            }


        }else{
            Toast.makeText(getBaseContext(), "You already have permission!", Toast.LENGTH_LONG).show();
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    class DownloadFileFromURL extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
    }

        public void convertCurrency(View v){
            final double value = Double.parseDouble(valueInput.getText().toString());



            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            String url ="https://api.exchangerate-api.com/v4/latest/";
// Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url+originalCurrency,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            JSONObject obj = null;

                            try {
                                obj = new JSONObject(response);
                                JSONObject firstItem = obj.getJSONObject("rates");
                                String timestamp = obj.getString("date");
                                Log.d("JSON",firstItem.getString(convertToCurrency));
                                double currencyRate = Double.parseDouble(firstItem.getString(convertToCurrency));
                                double converted = value * currencyRate;
                                txtConvertedValue.setText(String.valueOf(converted));
                                String[] splittedData = timestamp.split("-");
                                String finalTimestamp = splittedData[2] + "/" + splittedData[1] + "/" + splittedData[0];
                                Rate r = new Rate();
                                r.setDate(finalTimestamp);
                                r.setToCurrency(convertToCurrency);
                                r.setBaseCurrency(originalCurrency);
                                r.setRateValue(currencyRate);
                                Long sId = db.addRate(r);
                                Toast.makeText(getBaseContext(), "Permission Result: " + finalTimestamp, Toast.LENGTH_LONG).show();
                                Log.d("RATE INSERT","ID " + sId);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Log.d("Response is: ", response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("That didn't work!","");
                }
            });

// Add the request to the RequestQueue.
            queue.add(stringRequest);
            Log.d("Value", String.valueOf(value));
            Log.d("From", String.valueOf(originalCurrency));
            Log.d("To", String.valueOf(convertToCurrency));

        }

    public void viewHistory(View v){
        Intent it  = new Intent(getBaseContext(), ListRates.class);
        startActivityForResult(it,CODE_MAIN);
    }

    public void viewCredits(View v){
        Intent it  = new Intent(getBaseContext(), telaCreditos.class);
        startActivityForResult(it,CODE_MAIN);
    }
}
