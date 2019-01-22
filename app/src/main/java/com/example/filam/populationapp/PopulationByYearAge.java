package com.example.filam.populationapp;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class PopulationByYearAge extends AppCompatActivity {
EditText yearEditText;
EditText ageEditText;
Button button;
String age;
String year;
String responseStr;
String country;
Spinner countrySpinner;
TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_population_by_year_age);
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();
        String agePref = preferences.getString("EditTextAge","");
        String yearPref = preferences.getString("EditTextYear", "");
        yearEditText = findViewById(R.id.editTextYear);
        ageEditText = findViewById(R.id.editTextAge);
        button = findViewById(R.id.button6);
        countrySpinner = findViewById(R.id.spinner4);
        textView = findViewById(R.id.textView9);
        responseStr = getIntent().getStringExtra("response");
        ageEditText.setText(agePref);
        yearEditText.setText(yearPref);



        try{
            JSONObject jsonObject = new JSONObject(responseStr);
            System.out.println(jsonObject.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("countries");
            ArrayList<String> arrayList = new ArrayList<>();
            for(int i = 0; i < jsonArray.length(); i++){
                System.out.println(jsonArray.get(i).toString());
                arrayList.add(jsonArray.get(i).toString());
            }
            String[] array = new String[arrayList.size()];

            array = arrayList.toArray(array);

            loadSpinner(array, countrySpinner);
            System.out.println(jsonArray.toString());
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
        String position = preferences.getString("SpinnerCountryByAgeYear","");
        ArrayAdapter arrayAdapter = (ArrayAdapter) countrySpinner.getAdapter();

        int spinnerPosition = arrayAdapter.getPosition(position);
        countrySpinner.setSelection(spinnerPosition);
        //ArrayAdapter arrayAdapter = (ArrayAdapter) ageEditText.getAdapter();
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                country = countrySpinner.getSelectedItem().toString();
                editor.putString("SpinnerCountryByAgeYear", country);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                age = ageEditText.getText().toString();
                year = yearEditText.getText().toString();

                editor.putString("EditTextAge", age);
                editor.putString("EditTextYear", year);

                ageEditText.onEditorAction(EditorInfo.IME_ACTION_DONE);
                yearEditText.onEditorAction(EditorInfo.IME_ACTION_DONE);

                AsyncTask asyncTask = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] objects) {

                        OkHttpClient client = new OkHttpClient();
                        com.squareup.okhttp.Request request = new Request.Builder()
                                .url("http://api.population.io:80/1.0/population/"+ year +"/"+ country +"/" + age + "/")
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
                        try{
                            JSONArray array = new JSONArray(json);
                            JSONObject object = array.getJSONObject(0);
                            long males = object.getInt("males");
                            long females = object.getInt("females");
                            long total = object.getInt("total");
                            String pattern = "###,###.###";
                            DecimalFormat decimalFormat = new DecimalFormat(pattern);
                            String formatMales = decimalFormat.format(males);
                            String formatFemales = decimalFormat.format(females);
                            String formatTotal = decimalFormat.format(total);

                            textView.setText("In " + year + " was in " + country + "\n" + formatMales + " males\n" + formatFemales + " females\n" + "= " + formatTotal + " population " + age +
                                    " years old");

                        } catch (JSONException e){
                            e.printStackTrace();
                        }


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
