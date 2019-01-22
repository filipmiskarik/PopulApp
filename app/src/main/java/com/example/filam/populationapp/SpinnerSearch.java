package com.example.filam.populationapp;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.gson.JsonParser;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SpinnerSearch extends AppCompatActivity {
Spinner spinner;
String tmp;
Button button;
String responseStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner_search);
        spinner = findViewById(R.id.spinner);
        final TextView textView1 = findViewById(R.id.textView4);
        button = findViewById(R.id.button4);
        responseStr = getIntent().getStringExtra("response");
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();
        final String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        try{
            JSONObject jsonObject = new JSONObject(responseStr);
            System.out.println(jsonObject.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("countries");
            ArrayList<String> arrayList = new ArrayList<String>();
            for(int i = 0; i < jsonArray.length(); i++){
                System.out.println(jsonArray.get(i).toString());
                arrayList.add(jsonArray.get(i).toString());
            }
            String[] array = new String[arrayList.size()];

            array = arrayList.toArray(array);
            loadSpinner(array, spinner);
            System.out.println(jsonArray.toString());
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
        String position = preferences.getString("SpinnerCountryByCountry","");
        ArrayAdapter arrayAdapter = (ArrayAdapter) spinner.getAdapter();

        int spinnerPosition = arrayAdapter.getPosition(position);
        spinner.setSelection(spinnerPosition);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String country = spinner.getSelectedItem().toString();
                tmp = country;
                editor.putString("SpinnerCountryByCountry", country);
                System.out.println(country);
                editor.commit();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
     button.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             AsyncTask asyncTask = new AsyncTask() {
                 @Override
                 protected Object doInBackground(Object[] objects) {

                     OkHttpClient client = new OkHttpClient();
                     Request request = new Request.Builder()
                             .url("http://api.population.io:80/1.0/population/"+ tmp +"/"+ date +"/")
                             .build();
                     Response response = null;

                     try{
                         response = client.newCall(request).execute();
                         return response.body().string();
                     }
                     catch (IOException e)
                     {
                         e.printStackTrace();
                     }
                     return null;
                 }

                 @Override
                 protected void onPostExecute(Object o){
                     String json = o.toString();
                     JsonParser jsonParser = new JsonParser();
                     textView1.setText("Population in " + tmp.replace("%20", " ") +" is ");
                     try{
                         JSONObject jsonObject = new JSONObject(json);
                         JSONObject jsonObject1 = jsonObject.getJSONObject("total_population");
                         System.out.println(jsonObject1.toString());
                         long population = jsonObject1.getInt("population");
                         String pattern = "###,###.###";
                         DecimalFormat decimalFormat = new DecimalFormat(pattern);
                         String format = decimalFormat.format(population);
                         textView1.append(format);
                     }
                     catch(JSONException e) {
                         e.printStackTrace();
                     }
                     System.out.println("OK");
                 }
             }.execute();
         }
     });
    }
    public void loadSpinner(String[] array, Spinner spinner){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, array);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }
}
